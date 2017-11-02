package ru.minebot.extreme_energy.items.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import static org.lwjgl.opengl.GL11.*;

import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.gui.elements.moduleGui.BooleanModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.IModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.KeyModuleGui;
import ru.minebot.extreme_energy.gui.tablet.Element;
import ru.minebot.extreme_energy.init.ModGuiHandler;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.*;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketSaveMarks;

import java.util.ArrayList;
import java.util.List;

public class ItemPathInfoModule extends Module implements IChip, IInfo, IKey {
    public List<Mark> markList = new ArrayList<>();
    public static Mark activeMark;

    public ItemPathInfoModule() {
        super(Reference.ExtremeEnergyItems.MODULEINFOPATH.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULEINFOPATH.getRegistryName(), 1, false);
    }

    @Override
    public void renderScreen(ChipArgs args, Minecraft mc, Tessellator tes, BufferBuilder buf, ScaledResolution res) {
    }

    @Override
    public void renderWorld(ChipArgs args, Minecraft mc, Tessellator tes, BufferBuilder buf, ScaledResolution res) {
        if (args.energy > 1000)

        glDisable(GL_DEPTH_TEST);

        for (Mark mark : markList)
            mark.render(tes, buf, mc.player.getPositionEyes(mc.getRenderPartialTicks()));

        glEnable(GL_DEPTH_TEST);
    }

    @Override
    public int getTier() {
        return 1;
    }

    @Override
    public int onImplantWork(ChipArgs args) {
        if (args.player.world.isRemote) {
            if (args.player.world.getTotalWorldTime()%5==0) {
                Minecraft mc = Minecraft.getMinecraft();
                activeMark = null;
                for (Mark mark : markList) {
                    Vec3d v1 = new Vec3d(mark.pos).subtract(mc.player.getPositionVector());
                    Vec3d v2 = mc.player.getLook(mc.getRenderPartialTicks());
                    float angle = (float) Math.toDegrees(Math.acos(
                            (v1.x * v2.x + v1.y * v2.y + v1.z * v2.z) /
                                    (Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z) *
                                            Math.sqrt(v2.x * v2.x + v2.y * v2.y + v2.z * v2.z))
                    ));

                    if (angle < 6) {
                        activeMark = mark;
                        break;
                    }
                }
            }
            if (args.player.world.getTotalWorldTime()%200==0) {
                List<ItemStack> modules = ModUtils.getModules(args.player.getCapability(ImplantProvider.IMPLANT, null).getImplant());
                ItemStack module = ItemStack.EMPTY;
                for (ItemStack item : modules)
                    if (item.getItem() instanceof ItemPathInfoModule)
                        module = item;

                load(module);
            }
        }
        return 10;
    }

    @Override
    public IModuleGui[] getGui() {
        return new IModuleGui[]{
                new KeyModuleGui("Create/Edit:", "interact")
        };
    }

    @Override
    public void onModuleActivated(ChipArgs args, int keyIndex) {
        if (args.player.world.isRemote){
            BlockPos pos = args.player.getPosition();
            args.player.openGui(ExtremeEnergy.instance, ModGuiHandler.MARKER_SCREEN, args.player.world, pos.getX(), pos.getY()+1, pos.getZ());
        }
    }

    @Override
    public int getEnergy(ChipArgs args, int power, int keyIndex) {
        return 0;
    }

    @Override
    public int[] getKeyCodes(NBTTagCompound data) {
        return new int[]{ data.getInteger("interact") };
    }

    public static void createMark(ItemStack stack, String name, BlockPos pos, int color){
        ((ItemPathInfoModule)stack.getItem()).markList.add(new Mark(name, pos, color));
        NetworkWrapper.instance.sendToServer(new PacketSaveMarks(stack));
    }

    public static void changeMark(ItemStack stack, String primaryName, String name, BlockPos pos, int color){
        int index = findIndex(stack, primaryName);
        if (index == -1)
            return;

        ((ItemPathInfoModule)stack.getItem()).markList.set(index, new Mark(name, pos, color));
        NetworkWrapper.instance.sendToServer(new PacketSaveMarks(stack));
    }

    public static void removeMark(ItemStack stack, String name){
        int index = findIndex(stack, name);
        if (index == -1)
            return;

        ((ItemPathInfoModule)stack.getItem()).markList.remove(index);
        NetworkWrapper.instance.sendToServer(new PacketSaveMarks(stack));
    }

    public static boolean existMark(ItemStack stack, String name){
        return findIndex(stack, name) != -1;
    }

    private static int findIndex(ItemStack stack, String name){
        List<Mark> markList = ((ItemPathInfoModule)stack.getItem()).markList;
        for (int i = 0; i < markList.size(); i++)
            if (markList.get(i).name.equals(name))
                return i;
        return -1;
    }

    public static void save(ItemStack stack, List<Mark> markList){
        NBTTagCompound nbt = ModUtils.getNotNullCategory(stack);
        NBTTagList list = new NBTTagList();
        for (Mark mark : markList){
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("name", mark.name);
            tag.setInteger("color", mark.color);
            tag.setInteger("x", mark.pos.getX());
            tag.setInteger("y", mark.pos.getY());
            tag.setInteger("z", mark.pos.getZ());
            list.appendTag(tag);
        }
        nbt.setTag("marks", list);
        stack.getTagCompound().setTag(ExtremeEnergy.NBT_CATEGORY, nbt);
    }

    private static void load(ItemStack stack){
        NBTTagCompound nbt = ModUtils.getNotNullCategory(stack);
        NBTTagList list = (NBTTagList) nbt.getTag("marks");
        if (list != null) {
            ((ItemPathInfoModule) stack.getItem()).markList = new ArrayList<>();
            for (NBTBase t : list) {
                NBTTagCompound tag = (NBTTagCompound) t;
                ((ItemPathInfoModule) stack.getItem()).markList.add(new Mark(tag.getString("name"), new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z")), tag.getInteger("color")));
            }
        }
    }

    public static class Mark {
        public String name;
        public BlockPos pos;
        public int color;

        public Mark(String name, BlockPos pos){
            this(name, pos, 0xf2f2f2);
        }

        public Mark(String name, BlockPos pos, int color){
            this.name = name;
            this.pos = pos;
            this.color = color;
        }

        public void render(Tessellator tes, BufferBuilder buf, Vec3d eyes){
            Minecraft mc = Minecraft.getMinecraft();
            Vec3d from = new Vec3d(pos).addVector(0.5f, 0.5f,0.5f);
            double dist = eyes.distanceTo(from)/50f;
            double length = Math.sqrt(Math.pow(eyes.x - from.x, 2) + Math.pow(eyes.z - from.z, 2));
            Vec3d delta = eyes.subtract(from);

            glPushMatrix();
            glTranslated(from.x + delta.x*0.8f, from.y + delta.y*0.8f, from.z + delta.z*0.8f);
            glScaled(dist, dist, dist);
            glRotatef((float)(-Math.atan2(delta.z, delta.x) / Math.PI * 180) + 90, 0, 1, 0);
            glRotatef((float)(-Math.atan2(delta.y, length) / Math.PI * 180), 1, 0, 0);

            mc.getTextureManager().bindTexture(new ResourceLocation(activeMark != null ? (activeMark.name.equals(name) ? "meem:textures/other/markactive.png" : "meem:textures/other/mark.png") : "meem:textures/other/mark.png"));
            buf.begin(GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
            buf.pos(-0.32f, 0.32f, 0).tex(0,0).endVertex();
            buf.pos(-0.32f, -0.32f, 0).tex(0,1).endVertex();
            buf.pos(0.32f, -0.32f, 0).tex(1,1).endVertex();
            buf.pos(0.32f, 0.32f, 0).tex(1,0).endVertex();
            tes.draw();

            ModUtils.drawText(mc.fontRenderer, 0, -0.8f, name, color, 0.06f, Element.Align.CENTER);
            ModUtils.drawText(mc.fontRenderer, 0, 0.4f, (int)Math.round(dist*10)+"", color, 0.04f, Element.Align.CENTER);
            glPopMatrix();
        }
    }
}
