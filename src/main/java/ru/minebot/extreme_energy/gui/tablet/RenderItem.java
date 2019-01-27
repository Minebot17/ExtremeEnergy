package ru.minebot.extreme_energy.gui.tablet;

import com.google.gson.JsonObject;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class RenderItem extends Element {
    protected final net.minecraft.client.renderer.RenderItem renderer = mc.getRenderItem();
    protected ItemStack stack;
    protected Align align;
    protected float width;

    public RenderItem(float posX, float posY, ItemStack stack, Align align, float width) {
        super(posX, posY);
        this.stack = stack;
        this.align = align;
        this.width = width;
    }

    @Override
    public void draw(Tessellator tes, BufferBuilder buf, float mouseX, float mouseY) {
        float localX = posX;
        if (align != Align.LEFT)
            localX = align == Align.CENTER ? posX - width/2 : posX - width;

        RenderHelper.enableGUIStandardItemLighting();
        glColor4f(1, 1, 1, 1);
        float scale = 0.005f * (width/0.08f);
        glPushMatrix();
        glTranslated(0, 0, -0.00001f);
        glScalef(scale, -scale, 0.00001f);
        glEnable(GL_DEPTH_TEST);
        mc.getRenderItem().renderItemIntoGUI(stack, (int)(localX*(1f/scale)), -(int)(posY*(1f/scale)));
        glDisable(GL_DEPTH_TEST);
        glPopMatrix();
        RenderHelper.disableStandardItemLighting();
    }

    @Override
    public float getHeight() {
        return width;
    }

    public static Element loadFromJson(JsonObject json, int chapter, int article){
        ItemStack stack = ItemStack.EMPTY;
        try {
            Item item = Item.REGISTRY.getObject(new ResourceLocation("meem:" + json.get("item").getAsString().toLowerCase()));
            if (item == null)
                throw new NullPointerException("Output is invalid: c" + chapter + "/a" + article);
            stack = new ItemStack(item);
        }
        catch (NullPointerException e){ mc.crashed(new CrashReport("Output is invalid", e)); }
        float width = json.get("width").getAsFloat();
        String str = json.has("align") ? json.get("align").getAsString() : null;
        Align align = str == null ? Align.CENTER : str.equals("l") ? Align.LEFT : str.equals("c") ? Align.CENTER : Align.RIGHT;
        return new RenderItem(align == Align.LEFT ? TabletRender.leftX : align == Align.CENTER ? TabletRender.centerX : TabletRender.rightX, 0, stack, align, width);
    }
}
