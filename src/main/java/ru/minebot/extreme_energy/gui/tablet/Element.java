package ru.minebot.extreme_energy.gui.tablet;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;

public abstract class Element implements IElement{
    protected static final Minecraft mc = Minecraft.getMinecraft();
    protected static final FontRenderer font = mc.fontRenderer;
    protected float posX;
    protected float posY;

    public Element(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public abstract void draw(Tessellator tes, BufferBuilder buf, float mouseX, float mouseY);
    public void postDraw(Tessellator tes, BufferBuilder buf, float mouseX, float mouseY){}

    public float getPosX() {
        return posX;
    }
    public float getPosY() {
        return posY;
    }
    public abstract float getHeight();

    public enum Align{
        LEFT(),
        CENTER(),
        RIGHT()
    }

    public static Element getElement(JsonObject json, int chapter, int article) throws Exception{
        String type = json.get("type").getAsString();
        switch (type) {
            case "label":
                return LabelWithLinks.loadFromJson(json, chapter, article);
            case "link":
                return Link.loadFromJson(json, chapter, article);
            case "image":
                return Image.loadFromJson(json, chapter, article);
            case "craft":
                return Craft.loadFromJson(json, chapter, article);
            case "render":
                return RenderItem.loadFromJson(json, chapter, article);
        }
        throw new Exception("Invalid element type");
    }
}
