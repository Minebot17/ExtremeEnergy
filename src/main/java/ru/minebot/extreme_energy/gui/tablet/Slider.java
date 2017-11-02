package ru.minebot.extreme_energy.gui.tablet;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

import java.util.List;

public class Slider extends Element implements IClickable, IWheel {
    protected float thisPos;
    protected int pageCount;
    protected float hoveringPos;
    protected ResourceLocation edge;
    protected ResourceLocation center;
    protected ResourceLocation edgeBackground;
    protected ResourceLocation centerBackground;
    protected float edgeHeight;
    protected float edgeBackgroundHeight;
    protected float sliderHeight;
    protected float width;
    protected float height;
    protected boolean mirrored;

    public Slider(float posX, float posY, int pageCount, ResourceLocation edge, ResourceLocation center, ResourceLocation edgeBackground, ResourceLocation centerBackground, float edgeHeight, float edgeBackgroundHeight, float width, float height, boolean mirrored) {
        super(posX, posY);
        this.edge = edge;
        this.center = center;
        this.edgeBackground = edgeBackground;
        this.centerBackground = centerBackground;
        this.edgeHeight = edgeHeight;
        this.edgeBackgroundHeight = edgeBackgroundHeight;
        this.width = width;
        this.height = height;
        this.pageCount = pageCount;
        this.mirrored = mirrored;
        thisPos = 0;
        sliderHeight = height/(float)pageCount;
    }

    @Override
    public void draw(Tessellator tes, BufferBuilder buf, float mouseX, float mouseY) {

        // Background
        mc.getTextureManager().bindTexture(edgeBackground);
        drawRect(tes, buf, posX, posY, width, edgeBackgroundHeight, mirrored, false);
        mc.getTextureManager().bindTexture(centerBackground);
        drawRect(tes, buf, posX, posY - edgeBackgroundHeight, width, height - edgeBackgroundHeight*2, mirrored, false);
        mc.getTextureManager().bindTexture(edgeBackground);
        drawRect(tes, buf, posX, posY - (height - edgeBackgroundHeight), width, edgeBackgroundHeight, mirrored, true);

        // Slider
        glTranslatef(0,0,0.001f);
        float y = posY + thisPos;
        mc.getTextureManager().bindTexture(edge);
        drawRect(tes, buf, posX, y, width, edgeHeight, mirrored, false);
        mc.getTextureManager().bindTexture(center);
        drawRect(tes, buf, posX, y - edgeHeight, width, sliderHeight - edgeHeight*2, mirrored,false);
        mc.getTextureManager().bindTexture(edge);
        drawRect(tes, buf, posX, y - (sliderHeight - edgeHeight), width, edgeHeight, mirrored,true);
        glTranslatef(0,0,-0.001f);

        if (isHover(mouseX, mouseY)) {
            float localY = mouseY + posY;
            float begin = -(height/pageCount/2);
            float end = -(height - height/pageCount/2);
            hoveringPos = localY > begin ? 0 : localY < end ? end - begin : localY - begin;
        }
        else if (hoveringPos != -1)
            hoveringPos = -1;
    }

    @Override
    public void onMouseDown() {
        thisPos = hoveringPos;
    }

    @Override
    public void onMouseUp() {

    }

    @Override
    public void onMouseMove() {
        thisPos = hoveringPos;
    }

    @Override
    public void onWheelScrolled(int delta) {
        thisPos = thisPos + (float)delta/2400f;
        if (thisPos > 0)
            thisPos = 0;
        else if (thisPos < -(height - height/pageCount))
            thisPos = -(height - height/pageCount);
    }

    @Override
    public boolean isHover(float mouseX, float mouseY) {
        return mouseX > posX && mouseX < posX + width && mouseY < posY && mouseY > posY - height;
    }

    @Override
    public float getHeight() {
        return height;
    }

    private void drawRect(Tessellator tes, BufferBuilder buf, float posX, float posY, float sizeX, float sizeY, boolean mirroredX, boolean mirroredY){
        buf.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
        buf.pos(posX, posY, 0).tex(mirroredX ? 1 :0, mirroredY ? 1 : 0).endVertex();
        buf.pos(posX, posY-sizeY, 0).tex( mirroredX ? 1 : 0, mirroredY ? 0 : 1).endVertex();
        buf.pos(posX+sizeX, posY-sizeY, 0).tex(mirroredX ? 0 : 1, mirroredY ? 0 : 1).endVertex();
        buf.pos(posX+sizeX, posY, 0).tex(mirroredX ? 0 : 1, mirroredY ? 1 : 0).endVertex();
        tes.draw();
    }
}
