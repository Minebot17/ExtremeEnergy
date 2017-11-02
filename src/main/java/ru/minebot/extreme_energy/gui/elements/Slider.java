package ru.minebot.extreme_energy.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public abstract class Slider extends Gui {
    private ResourceLocation textureSlider;
    private ResourceLocation textureBack0;
    private ResourceLocation textureBack1;
    private int posX;
    private int posY;
    private int count;

    public Slider(int posX, int posY, int count){
        textureSlider = new ResourceLocation("meem:textures/gui/slider/slider.png");
        textureBack0 = new ResourceLocation("meem:textures/gui/slider/sliderback0.png");
        textureBack1 = new ResourceLocation("meem:textures/gui/slider/sliderback1.png");
        this.posX = posX;
        this.posY = posY;
        this.count = count + 1;
    }

    public void draw(Minecraft mc, int sliderPos){
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        for (int i = 0; i < count; i++) {
            mc.getTextureManager().bindTexture(i%2==0 ? textureBack0 : textureBack1);
            drawModalRectWithCustomSizedTexture(posX + i * 5, posY, 0, 0, 5, 3, 5, 3);
        }

        mc.getTextureManager().bindTexture(textureSlider);
        drawModalRectWithCustomSizedTexture(posX + sliderPos * 5, posY - 3, 0, 0, 5, 10, 5, 10);
    }

    public boolean mousePressed(int mouseX, int mouseY){
        boolean inSlider = mouseY > posY - 3 && mouseY < posY + 4 && mouseX > posX && mouseX < posX + count * 5;
        if (inSlider){
            int sliderPos = -1;
            for (int i = 1; i <= count; i++)
                if (mouseX < posX + i * 5) {
                    sliderPos = i - 1;
                    break;
                }

            if (sliderPos == -1)
                try {
                    throw new Exception("Trying to select a non-existent slider position");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            onSliderPosChanged(sliderPos);
            return true;
        }
        return false;
    }

    public void mouseDown(int mouseX, int mouseY){
        if (mousePressed(mouseX, mouseY))
            playPressSound(Minecraft.getMinecraft().getSoundHandler());
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public abstract void onSliderPosChanged(int sliderPos);
}
