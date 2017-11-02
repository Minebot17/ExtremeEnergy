package ru.minebot.extreme_energy.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.modules.ChipArgs;
import ru.minebot.extreme_energy.modules.IInfo;
import ru.minebot.extreme_energy.other.ImplantData;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class LightningEvents {
    private static int soundCD;
    private static List<Lightning> lightnings = new ArrayList<>();
    final static Minecraft mc = Minecraft.getMinecraft();

    public static void spawnLightning(World world, Vec3d from, Vec3d to, Type type){
        if (world.isRemote){
            lightnings.add(new Lightning(from, to, type));
            if (soundCD <= 0) {
                world.playSound(from.x, from.y, from.z, type.sound, SoundCategory.WEATHER, type.volume, 1, true);
                if (from.distanceTo(to) > 50)
                    world.playSound(to.x, to.y, to.z, type.sound, SoundCategory.WEATHER, type.volume, 1, true);
                soundCD = 21;
            }
        }
    }

    @SubscribeEvent
    public void playerTicks(TickEvent.PlayerTickEvent e){
        if (e.player.world.isRemote) {
            try {
                for (Lightning lightning : lightnings)
                    lightning.time--;
                lightnings.removeIf(lightning -> lightning.time <= 0);
            } catch (ConcurrentModificationException a) {
                a.printStackTrace();
            } finally {
                if (mc.world != null && mc.world.provider.getDimension() == 0)
                    soundCD--;
            }
        }
    }

    @SubscribeEvent
    public void drawInWorld(RenderWorldLastEvent e){
        try {
            for (Lightning lightning : lightnings)
                drawLightning(Tessellator.getInstance(), lightning, e.getPartialTicks());
        }
        catch (Exception ignored){}

        EntityPlayer player = Minecraft.getMinecraft().player;
        ImplantData data = player.getCapability(ImplantProvider.IMPLANT, null).getImplant();
        if (data != null) {
            NBTTagCompound iTag = data.implant;
            boolean isOn = iTag.getBoolean("isOn");
            if (isOn) {
                float partialTicks = e.getPartialTicks();
                byte[] modulesActive = iTag.getByteArray("activesArray");
                int energy = iTag.getInteger("energy");
                int voltage = iTag.getInteger("voltage");
                List<ItemStack> modules = ModUtils.getModules(data);
                for (int i = 0; i < modules.size(); i++)
                    if (modules.get(i).getItem() instanceof IInfo && modulesActive[i] == 1) {
                        glPushMatrix();
                        double x_fix = -(mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * partialTicks);
                        double y_fix = -(mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * partialTicks);
                        double z_fix = -(mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * partialTicks);
                        glTranslated(x_fix, y_fix, z_fix);
                        ((IInfo) modules.get(i).getItem()).renderWorld(new ChipArgs(player, modulesActive[i] != 0, isOn, voltage != 0, voltage, energy, ModUtils.getNotNullCategory(modules.get(i)), ModUtils.getBlocksRay(), ModUtils.getEntityRay()), mc, Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), new ScaledResolution(mc));
                        glPopMatrix();
                    }
            }
        }
    }

    private void drawLightning(Tessellator tes, Lightning lightning, float partialTicks){
        Vec3d from = lightning.from;
        Vec3d to = lightning.to;
        int countLines = lightning.type.getCount((float)to.subtract(from).lengthVector());
        Vec3d[] points = lightning.points;

        if (points == null){
            points = new Vec3d[countLines+2];
            points[0] = from;
            Vec3d delta = to.subtract(from);
            Vec3d oneLine = new Vec3d(delta.x/(float)countLines, delta.y/(float)countLines, delta.z/(float)countLines);
            for (int i = 1; i < countLines+2; i++){
                Vec3d vec = oneLine.rotateYaw((float) Math.toRadians((ModUtils.random.nextFloat()-0.5f)*80f)).addVector(0, ModUtils.random.nextFloat()-0.5f, 0).add(oneLine.scale(i-1));
                points[i] = from.addVector(vec.x, vec.y, vec.z);
            }
            points[countLines+1] = to.addVector((ModUtils.random.nextFloat()-0.5f)/2.5f, (ModUtils.random.nextFloat()-0.5f)/2.5f, (ModUtils.random.nextFloat()-0.5f)/2.5f);
            lightning.points = points;
        }
        mc.getTextureManager().bindTexture(lightning.type.texture);
        for (int i = 0; i < points.length-1; i++)
            drawLine(tes, points[i], points[i+1], lightning.type.size, partialTicks);
    }

    private void drawLine(Tessellator tes, Vec3d from, Vec3d to, float size, float partialTicks){
        Minecraft mc = Minecraft.getMinecraft();
        double x_fix = -(mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * partialTicks);
        double y_fix = -(mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * partialTicks);
        double z_fix = -(mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * partialTicks);
        double length = to.distanceTo(from);
        double length2 = Math.sqrt(Math.pow(to.x - from.x, 2) + Math.pow(to.z - from.z, 2));
        Vec3d delta = to.subtract(from);

        glPushMatrix();
        glTranslated(x_fix + from.x, y_fix + from.y-size/2, z_fix + from.z+size/2);
        glRotatef((float)(-Math.atan2(delta.z, delta.x) / Math.PI * 180), 0, 1, 0);
        glRotatef((float)(Math.atan2(delta.y, length2) / Math.PI * 180), 0, 0, 1);
        glTranslated(0, size/2, -size/2);

        drawPolygon(tes, length, size, EnumFacing.UP);
        drawPolygon(tes, length, size, EnumFacing.EAST);
        drawPolygon(tes, length, size, EnumFacing.DOWN);
        drawPolygon(tes, length, size, EnumFacing.WEST);
        glPopMatrix();
    }

    private void drawPolygon(Tessellator tes, double length, float size, EnumFacing facing){
        BufferBuilder buf = tes.getBuffer();

        float offset = size/2;
        int angle = facing == EnumFacing.DOWN ? 180 : facing == EnumFacing.EAST ? 90 : facing == EnumFacing.WEST ? 270 : 0;
        GlStateManager.enableTexture2D();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glPushMatrix();
        glTranslatef(0, -offset, offset);
        glRotated(angle, 1, 0, 0);
        glTranslatef(0, offset, -offset);
        glTranslated(0, -offset, 0);

        buf.begin(GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
        buf.pos(0, 0, 0).tex(0,0).endVertex();
        buf.pos(0, 0, size).tex(0,1).endVertex();
        buf.pos(length, 0, size).tex(1,1).endVertex();
        buf.pos(length, 0, 0).tex(1,0).endVertex();
        tes.draw();

        glPopMatrix();
        glDisable(GL_BLEND);
    }

    public enum Type {
        TINY("meem:textures/particles/lightning_tiny.png", ModSoundHandler.ltiny, 0.05f, 0.8f, 0.5f, 5),
        SMALL("meem:textures/particles/lightning_small.png", ModSoundHandler.lsmall, 0.15f, 1f, 1f, 10),
        STANDART("meem:textures/particles/lightning_standart.png", ModSoundHandler.lstandart, 0.3f, 1.2f, 3f, 15),
        BIG("meem:textures/particles/lightning_big.png", ModSoundHandler.lbig, 0.6f, 2.5f, 10f, 20);

        public ResourceLocation texture;
        public float size;
        private float length;
        public int time;
        public SoundEvent sound;
        public float volume;

        Type(String texture, SoundEvent sound, float size, float length, float volume, int time) {
            this.texture = new ResourceLocation(texture);
            this.sound = sound;
            this.size = size;
            this.length = length;
            this.volume = volume;
            this.time = time;
        }

        public int getCount(float length){
            return Math.round(length/this.length);
        }

        public int getID(){
            switch (this){
                case TINY: return 0;
                case SMALL: return 1;
                case STANDART: return 2;
                case BIG: return 3;
            }
            return -1;
        }

        public static Type getType(int ID){
            switch (ID){
                case 0: return TINY;
                case 1: return SMALL;
                case 2: return STANDART;
                case 3: return BIG;
            }
            return null;
        }
    }

    private static class Lightning {
        public Vec3d from;
        public Vec3d to;
        public Vec3d[] points;
        public Type type;
        public int time;

        public Lightning(Vec3d from, Vec3d to, Type type) {
            this.from = from;
            this.to = to;
            this.type = type;
            this.time = type.time;
        }
    }
}
