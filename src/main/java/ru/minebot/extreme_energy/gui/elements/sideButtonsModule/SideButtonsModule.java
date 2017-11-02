package ru.minebot.extreme_energy.gui.elements.sideButtonsModule;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public abstract class SideButtonsModule extends Gui{
    protected ResourceLocation left;
    protected ResourceLocation left_hover;
    protected ResourceLocation left_click;
    protected ResourceLocation right;
    protected ResourceLocation right_hover;
    protected ResourceLocation right_click;

    protected int color;
    protected int value;

    protected int posX;
    protected int posY;
    protected int radius;

    protected boolean isHoverLeft;
    protected boolean isClickLeft;
    protected boolean isHoverRight;
    protected boolean isClickRight;

    public SideButtonsModule(ResourceLocation left, ResourceLocation left_hover, ResourceLocation left_click, ResourceLocation right, ResourceLocation right_hover, ResourceLocation right_click, int color, int startValue, int posX, int posY, int radius) {
        this.radius = radius;
        this.left = left;
        this.left_hover = left_hover;
        this.left_click = left_click;
        this.right = right;
        this.right_hover = right_hover;
        this.right_click = right_click;
        this.color = color;
        this.value = startValue;
        this.posX = posX;
        this.posY = posY;
    }

    public void draw(Minecraft mc, FontRenderer fontRenderer, int mouseX, int mouseY){
        drawButtons(mc, mouseX, mouseY);
        drawText(fontRenderer);
    }

    public void drawButtons(Minecraft mc, int mouseX, int mouseY){
        isHoverLeft = posX < mouseX && posX + 9 > mouseX && posY < mouseY && posY + 16 > mouseY;
        isHoverRight = posX + radius*2 + 9 < mouseX && posX + radius*2 + 18 > mouseX && posY < mouseY && posY + 16 > mouseY;

        ResourceLocation tLeft = isClickLeft && isHoverLeft ? left_click : (isHoverLeft ? left_hover : left);
        ResourceLocation tRight = isClickRight && isHoverRight ? right_click : (isHoverRight ? right_hover : right);

        mc.getTextureManager().bindTexture(tLeft);
        drawModalRectWithCustomSizedTexture(posX, posY, 0, 0, 9, 16, 9, 16);
        mc.getTextureManager().bindTexture(tRight);
        drawModalRectWithCustomSizedTexture(posX + radius*2 + 9, posY, 0, 0, 9, 16, 9, 16);
    }

    public void drawText(FontRenderer fontRenderer){
        fontRenderer.drawString(value+"", posX + radius + 9 - fontRenderer.getStringWidth(value+"")/2, 5 + posY,color);
    }

    public void mouseDown(){
        if (isHoverLeft || isHoverRight) {
            if (isHoverLeft) {
                isClickLeft = true;
            } else if (isHoverRight) {
                isClickRight = true;
            }

            playPressSound();
            value = onValueChanged(isClickLeft, value);
        }
    }

    public void mouseUp(){
        if (isHoverLeft)
            isClickLeft = false;
        else if (isHoverRight)
            isClickRight = false;
    }

    public void playPressSound() {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public abstract int onValueChanged(boolean isLeft, int value);
}
