package ru.minebot.extreme_energy.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import org.lwjgl.opengl.GL11;
import ru.minebot.extreme_energy.init.ModConfig;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.ItemChargeDetector;

import static org.lwjgl.opengl.GL11.*;

public class ImplantsHUD extends Gui{
    private int lastEnergy;
    private int state;
    private long lastTick;

    public void draw(Minecraft mc, FontRenderer font, float energy, float energyMax, int voltage){
        if (mc.world.getTotalWorldTime()%20==0 && lastTick != mc.world.getTotalWorldTime()) {
            state = lastEnergy > energy ? 0 : lastEnergy == energy ? 1 : 2;
            lastEnergy = (int) energy;
            lastTick = mc.world.getTotalWorldTime();
        }

        ScaledResolution res = new ScaledResolution(mc);
        GL11.glEnable(GL_BLEND);
        GL11.glColor4f(1,1 , 1, 0.75f);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/gui/implantshud/energybackground.png"));
        drawModalRectWithCustomSizedTexture(0, 0, 0,0,109,290,109,290);
        GL11.glScalef(2, 2, 1);
        mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/gui/implantshud/energybar.png"));
        drawTexturedModalRect(0, (int)((energyMax-energy)/energyMax*145f), 0, (int)((energyMax-energy)/energyMax*145f), 54, (int)(energy/energyMax*145f));
        GL11.glScalef(0.5f, 0.5f, 1);

        String topString = ((int)energy) + " RF";
        String bottomString = state == 0 ? "Discharging" : state == 1 ? "Not charging" : (voltage == 0 ? "Charging" : voltage + " Volt");
        font.drawString(topString, 60 - font.getStringWidth(topString)/2, -10, 1606345);
        font.drawString(bottomString, 47 - font.getStringWidth(bottomString)/2, 292, state == 0 ? 0xFF0000 : state == 1 ? 0xFFFF00 : 0x00FF00);

        GL11.glDisable(GL_BLEND);
        GL11.glColor4f(1,1 , 1, 1);
    }

    public void drawCharge(Minecraft mc, FontRenderer font, float charge){
        GL11.glEnable(GL_BLEND);
        GL11.glColor4f(1,1 , 1, 1f);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/gui/implantshud/chargebackground.png"));
        drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 30, 47, 30, 47);
        GL11.glScalef(0.25f, 0.25f, 1);
        mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/gui/implantshud/chargebar.png"));
        drawTexturedModalRect(16, 16 + (int)((ModConfig.maxCapOfChunk-charge)/ModConfig.maxCapOfChunk*156f), 0, (int)((ModConfig.maxCapOfChunk-charge)/ModConfig.maxCapOfChunk*156f), 88, (int)(charge/ModConfig.maxCapOfChunk*156f));
        GL11.glScalef(4, 4, 1);

        int index = ItemChargeDetector.getMessageIndexFromPos(mc.world, new ChunkPos(mc.player.getPosition()));
        String topString = "Chunk charge";
        String bottomString = charge + " RF";
        font.drawString(topString, 15 - font.getStringWidth(topString)/2, -10, 1606345);
        font.drawString(ItemChargeDetector.getMessageColor(index) + bottomString, 15 - font.getStringWidth(bottomString)/2, 50, 1606345);

        GL11.glDisable(GL_BLEND);
        GL11.glColor4f(1,1 , 1, 1);
    }
}
