package ru.minebot.extreme_energy.gui.tablet;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import org.lwjgl.opengl.GL11;

public abstract class Button extends Element implements IClickable {
    protected ResourceLocation texture;
    protected ResourceLocation textureHover;
    protected ResourceLocation textureClick;
    protected SoundEvent sound;
    protected float sizeX;
    protected float sizeY;
    protected boolean isMouseDown;
    protected float volume;

    public Button(float posX, float posY, float sizeX, float sizeY, ResourceLocation texture, ResourceLocation textureHover, ResourceLocation textureClick, SoundEvent sound, float volume){
        super(posX, posY);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.texture = texture;
        this.textureHover = textureHover;
        this.textureClick = textureClick;
        this.sound = sound;
        this.volume = volume;
    }

    @Override
    public void draw(Tessellator tes, BufferBuilder buf, float mouseX, float mouseY) {
        boolean isHover = isHover(mouseX, mouseY);
        ResourceLocation tex = isMouseDown && isHover ? textureClick : (isHover ? textureHover : texture);
        mc.getTextureManager().bindTexture(tex);
        buf.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
        buf.pos(posX, posY, 0).tex(0, 0).endVertex();
        buf.pos(posX, posY-sizeY, 0).tex(0, 1).endVertex();
        buf.pos(posX+sizeX, posY-sizeY, 0).tex(1, 1).endVertex();
        buf.pos(posX+sizeX, posY, 0).tex(1, 0).endVertex();
        tes.draw();
    }

    @Override
    public void onMouseDown() {
        isMouseDown = true;
    }

    @Override
    public void onMouseUp() {
        if (isMouseDown) {
            isMouseDown = false;
            mc.player.playSound(sound, volume, 1);
            action();
        }
    }

    @Override
    public void onMouseMove() {

    }

    public abstract void action();

    @Override
    public boolean isHover(float mouseX, float mouseY) {
        return mouseX > posX && mouseX < posX + sizeX && mouseY < posY && mouseY > posY - sizeY;
    }

    public float getHeight(){
        return sizeY;
    }
}
