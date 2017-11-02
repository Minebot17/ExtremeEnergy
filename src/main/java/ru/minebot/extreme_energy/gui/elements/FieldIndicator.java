package ru.minebot.extreme_energy.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class FieldIndicator extends Gui {
    protected ResourceLocation texture;
    protected ResourceLocation textureActive;
    protected int posX;
    protected int posY;

    public FieldIndicator(int posX, int posY){
        this.posX = posX;
        this.posY = posY;
        texture = new ResourceLocation("meem:textures/gui/indicator/infield.png");
        textureActive = new ResourceLocation("meem:textures/gui/indicator/infield_active.png");
    }

    public void draw(Minecraft mc, boolean inField){
        mc.getTextureManager().bindTexture(inField ? textureActive : texture);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawModalRectWithCustomSizedTexture(posX, posY, 0, 0, 7, 7, 7, 7);
    }
}
