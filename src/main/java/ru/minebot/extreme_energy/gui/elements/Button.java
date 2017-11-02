package ru.minebot.extreme_energy.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public abstract class Button extends Gui {
    protected ResourceLocation texture;
    protected ResourceLocation textureHover;
    protected ResourceLocation textureClick;
    protected int sizeX;
    protected int sizeY;
    protected int posX;
    protected int posY;
    protected boolean isHover;
    protected boolean isMouseDown;

    public Button(int sizeX, int sizeY, int posX, int posY, ResourceLocation texture, ResourceLocation textureHover, ResourceLocation textureClick) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.posX = posX;
        this.posY = posY;
        this.texture = texture;
        this.textureHover = textureHover;
        this.textureClick = textureClick;
    }

    public Button(int sizeX, int sizeY, int posX, int posY, ResourceLocation texture, ResourceLocation textureHover) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.posX = posX;
        this.posY = posY;
        this.texture = texture;
        this.textureHover = textureHover;
        this.textureClick = textureHover;
    }

    public void draw(Minecraft mc, int mouseX, int mouseY){
        isHover = posX < mouseX && posX + sizeX > mouseX && posY < mouseY && posY + sizeY > mouseY;
        ResourceLocation tex = isMouseDown && isHover ? textureClick : (isHover ? textureHover : texture);
        mc.getTextureManager().bindTexture(tex);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawModalRectWithCustomSizedTexture(posX, posY, 0, 0, sizeX, sizeY, sizeX, sizeY);
    }

    public void mouseDown(){
        if (isHover){
            isMouseDown = true;
            playPressSound(Minecraft.getMinecraft().getSoundHandler());
            onButtonClicked();
        }
    }

    public void mouseUp(){
        if (isHover){
            isMouseDown = false;
        }
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public abstract void onButtonClicked();
}
