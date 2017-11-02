package ru.minebot.extreme_energy.gui.tablet;

import com.google.gson.JsonObject;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Image extends Element{
    protected ResourceLocation image;
    protected Align align;
    protected float width;
    protected float height;

    public Image(float posX, float posY, ResourceLocation image, Align align, float width, float height) {
        super(posX, posY);
        this.image = image;
        this.align = align;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Tessellator tes, BufferBuilder buf, float mouseX, float mouseY) {
        float localX = posX;
        if (align != Align.LEFT)
            localX = align == Align.CENTER ? posX - width/2 : posX - width;

        mc.getTextureManager().bindTexture(image);
        buf.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
        buf.pos(localX, posY, 0).tex(0, 0).endVertex();
        buf.pos(localX, posY-height, 0).tex(0, 1).endVertex();
        buf.pos(localX+width, posY-height, 0).tex(1, 1).endVertex();
        buf.pos(localX+width, posY, 0).tex(1, 0).endVertex();
        tes.draw();
    }

    @Override
    public float getHeight() {
        return height;
    }

    public static Element loadFromJson(JsonObject json, int chapter, int article){
        ResourceLocation image = new ResourceLocation("meem:tablet/c" + chapter + "/" + json.get("url").getAsString());
        float width = json.get("width").getAsFloat();
        float height = json.get("height").getAsFloat();
        String str = json.has("align") ? json.get("align").getAsString() : null;
        Align align = str == null ? Align.CENTER : str.equals("l") ? Align.LEFT : str.equals("c") ? Align.CENTER : Align.RIGHT;
        return new Image(align == Align.LEFT ? TabletRender.leftX : align == Align.CENTER ? TabletRender.centerX : TabletRender.rightX, 0, image, align, width, height);
    }
}
