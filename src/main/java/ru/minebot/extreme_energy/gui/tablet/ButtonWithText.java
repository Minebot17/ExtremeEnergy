package ru.minebot.extreme_energy.gui.tablet;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import org.lwjgl.opengl.GL11;

public abstract class ButtonWithText extends Button {
    protected Element text;

    public ButtonWithText(float posX, float posY, float sizeX, float sizeY, ResourceLocation texture, ResourceLocation textureHover, ResourceLocation textureClick, SoundEvent sound, float volume, String text, int color) {
        super(posX, posY, sizeX, sizeY, texture, textureHover, textureClick, sound, volume);
        this.text = new Label(posX + sizeX/2f, posY - sizeY/2f + (Label.oneCharHeight/2f), text, Align.CENTER, null, sizeX, color);
    }

    @Override
    public void draw(Tessellator tes, BufferBuilder buf, float mouseX, float mouseY) {
        super.draw(tes, buf, mouseX, mouseY);
        GL11.glTranslatef(0,0,0.0001f);
        text.draw(tes, buf, mouseX, mouseY);
        GL11.glTranslatef(0,0,-0.0001f);
    }
}
