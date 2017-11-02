package ru.minebot.extreme_energy.items.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import static org.lwjgl.opengl.GL11.*;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.gui.elements.moduleGui.BooleanModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.IModuleGui;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.ChipArgs;
import ru.minebot.extreme_energy.modules.IChip;
import ru.minebot.extreme_energy.modules.IInfo;
import ru.minebot.extreme_energy.modules.Module;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketEntityPotionEffects;

import java.util.ArrayList;
import java.util.List;

public class ItemEntityInfoModule extends Module implements IChip, IInfo {
    public static List<PotionEffect> effects = new ArrayList<>();
    private final int textColor = 0xf2f2f2;

    public ItemEntityInfoModule() {
        super(Reference.ExtremeEnergyItems.MODULEINFOENTITY.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULEINFOENTITY.getRegistryName(), 1, false);
    }

    @Override
    public void renderScreen(ChipArgs args, Minecraft mc, Tessellator tes, BufferBuilder buf, ScaledResolution res) {

    }

    @Override
    public void renderWorld(ChipArgs args, Minecraft mc, Tessellator tes, BufferBuilder buf, ScaledResolution res) {
        if (args.entityCollide != 0 && args.energy > 1000){
            Entity e = args.player.world.getEntityByID(args.entityCollide);
            if (e != null && e instanceof EntityLivingBase && e.getPositionVector().distanceTo(args.player.getPositionVector()) < 15){
                EntityLivingBase entity = (EntityLivingBase)e;

                glEnable(GL_BLEND);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                glDisable(GL_DEPTH_TEST);
                glPushMatrix();

                Vec3d from = entity.getPositionVector();
                Vec3d to = mc.player.getPositionVector();
                double length2 = Math.sqrt(Math.pow(to.x - from.x, 2) + Math.pow(to.z - from.z, 2));
                Vec3d delta = to.subtract(from);

                mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/gui/infomodules/entitypanel.png"));
                glTranslated(entity.getPositionVector().x, entity.getPositionVector().y + entity.height/2, entity.getPositionVector().z);
                glRotatef((float)(-Math.atan2(delta.z, delta.x) / Math.PI * 180), 0, 1, 0);
                glRotatef((float)(Math.atan2(delta.y, length2) / Math.PI * 180), 0, 0, 1);

                buf.begin(GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
                buf.pos(0, 1.2f, -entity.width-0.1f).tex(1, 0).endVertex();
                buf.pos(0, -1.2f, -entity.width-0.1f).tex(1, 1).endVertex();
                buf.pos(0, -1.2f, -2.1f-entity.width).tex(0, 1).endVertex();
                buf.pos(0, 1.2f, -2.1f-entity.width).tex(0, 0).endVertex();
                tes.draw();

                glTranslated(0, 1.15f, -entity.width-0.2f);
                glRotated(90, 0, 1, 0);

                List<IElement> elementList = new ArrayList<>();
                elementList.add(new Standart());
                elementList.add(new State());
                if (entity instanceof EntityPlayer)
                    elementList.add(new Player());
                if (effects.size() != 0)
                    elementList.add(new Effects());

                FontRenderer font = Minecraft.getMinecraft().fontRenderer;
                for (IElement element : elementList){
                    element.render(args, entity, font);
                    glTranslated(0, -element.getHeight(), 0);
                }

                glPopMatrix();
                glEnable(GL_DEPTH_TEST);
                glDisable(GL_BLEND);
            }
        }
    }

    @Override
    public int getTier() {
        return 0;
    }

    @Override
    public int onImplantWork(ChipArgs args) {
        if (args.player.world.getTotalWorldTime() % 5 == 0) {
            if (args.entityCollide != 0 && args.player.world.getEntityByID(args.entityCollide) instanceof EntityLivingBase)
                NetworkWrapper.instance.sendToServer(new PacketEntityPotionEffects(args.entityCollide));
        }
        return 2;
    }

    @Override
    public IModuleGui[] getGui() {
        return new IModuleGui[0];
    }

    private interface IElement{
        void render(ChipArgs args, EntityLivingBase entity, FontRenderer font);
        float getHeight();
    }

    private class Standart implements IElement {

        @Override
        public void render(ChipArgs args, EntityLivingBase entity, FontRenderer font) {
            String name = entity.getDisplayName().getUnformattedText();
            int color = entity instanceof EntityMob ? 0xff0000 : textColor;
            ModUtils.drawText(font, 0, 0, name, color, 0.04f);
            ModUtils.drawText(font, 0, 0.31f, (int)entity.getHealth() + "/" + (int)entity.getMaxHealth() + " HP", textColor, 0.02f);
            ModUtils.drawText(font, 0, 0.47f, "Armor: " + entity.getTotalArmorValue(), textColor, 0.02f);
        }

        @Override
        public float getHeight() {
            return 0.65f;
        }
    }

    private class State implements IElement {
        private float height;
        String[] massText = new String[]{
                "It is impossible to push",
                "Can breathe under water",
                "You can't hit this entity",
                "Does not see you",
                "Is burn",
                "Invisible",
                "Immune to fire",
                "Immune to explosions"
        };

        boolean[] conditions = new boolean[]{
                false,
                true,
                false,
                false,
                true,
                true,
                true,
                true
        };

        @Override
        public void render(ChipArgs args, EntityLivingBase entity, FontRenderer font) {
            boolean[] massBool = new boolean[]{
                    entity.canBePushed(),
                    entity.canBreatheUnderwater(),
                    entity.attackable(),
                    entity.canEntityBeSeen(Minecraft.getMinecraft().player),
                    entity.isBurning(),
                    entity.isInvisible(),
                    entity.isImmuneToFire(),
                    entity.isImmuneToExplosions()
            };

            height = 0;
            for (int i = 0; i < massBool.length; i++)
                if (massBool[i] == conditions[i]){
                    ModUtils.drawText(font, 0, height, massText[i], textColor, 0.02f);
                    height += 0.16f;
                }
        }

        @Override
        public float getHeight() {
            return height + 0.05f;
        }
    }

    private class Player implements IElement {
        private float height;

        @Override
        public void render(ChipArgs args, EntityLivingBase entity, FontRenderer font) {
            EntityPlayer player = (EntityPlayer)entity;
            int level = player.experienceLevel;
            ItemStack stack = player.getActiveItemStack();
            boolean sprint = player.isSprinting();
            boolean sit = player.isSneaking();

            height = 0;
            ModUtils.drawText(font, 0, height, "Level: " + level, textColor, 0.02f);
            height += 0.16f;
            if (!stack.isEmpty()) {
                ModUtils.drawText(font, 0, height, "Hold: " + stack.getDisplayName(), textColor, 0.02f);
                height += 0.16f;
            }
            if (sprint) {
                ModUtils.drawText(font, 0, height, "Is sprinting", textColor, 0.02f);
                height += 0.16f;
            }
            if (sit) {
                ModUtils.drawText(font, 0, height, "Is sneaking", textColor, 0.02f);
                height += 0.16f;
            }
        }

        @Override
        public float getHeight() {
            return height + 0.05f;
        }
    }

    private class Effects implements IElement {
        private static final float size = 0.5f;
        private static final float m = 0.00390625f;
        private int count;

        @Override
        public void render(ChipArgs args, EntityLivingBase entity, FontRenderer font) {
            count = 0;
            for (PotionEffect effect : effects){
                if (!effect.getPotion().shouldRender(effect))
                    continue;

                Tessellator tes = Tessellator.getInstance();
                BufferBuilder buf = tes.getBuffer();
                String duration = Potion.getPotionDurationString(effect, 1);
                Minecraft.getMinecraft().getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
                int index = effect.getPotion().getStatusIconIndex();
                int posX = count%3;
                int posY = -count/3;
                float u = index % 8 * 18;
                float v = index / 8 * 18 + 198;

                buf.begin(GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
                buf.pos(size * posX + 0.1f * posX, size * posY + 0.3f * posY, 0).tex(u*m, v*m).endVertex();
                buf.pos(size * posX + 0.1f * posX, size * posY + 0.3f * posY - size, 0).tex(u*m, (v+18f)*m).endVertex();
                buf.pos(size * posX + 0.1f * posX + size, size * posY + 0.3f * posY - size, 0).tex((u+18f)*m, (v+18f)*m).endVertex();
                buf.pos(size * posX + 0.1f * posX + size, size * posY + 0.3f * posY, 0).tex((u+18f)*m, v*m).endVertex();
                tes.draw();

                ModUtils.drawText(font, (size + 0.1f) * posX + size/2f - (float)font.getStringWidth(duration)/100f, (size + 0.275f)* -posY + 0.02f + size, duration, textColor, 0.02f);
                count++;
            }
        }

        @Override
        public float getHeight() {
            return (count/3 + 0.3f) * size + 0.1f;
        }
    }
}
