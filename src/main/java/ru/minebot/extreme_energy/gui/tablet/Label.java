package ru.minebot.extreme_energy.gui.tablet;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Label extends Element {
    protected static float textSize = 0.004f;
    protected static float oneCharHeight = 0.05f;
    protected List<String> text;
    protected Align align;
    protected TextFormatting format;
    protected float width;
    protected float height;
    protected int color;

    protected Label(float posX, float posY){super(posX, posY);}

    public Label(float posX, float posY, String text, Align align, int color){
        this(posX, posY, text, align, null, 0, color);
    }

    public Label(float posX, float posY, String text, Align align, TextFormatting format, int color){
        this(posX, posY, text, align, format, 0, color);
    }

    public Label(float posX, float posY, String text, Align align, TextFormatting format, float width, int color){
        this(posX, posY, new String[]{text}, align, width, color);
        this.format = format;
    }

    public Label(float posX, float posY, String[] text, Align align, float width, int color) {
        this(posX, posY, text, align, null, width, color);
    }

    public Label(float posX, float posY, String[] text, Align align, TextFormatting format, float width, int color) {
        super(posX, posY);
        this.format = format;
        this.align = align;
        this.width = width;
        this.color = color;

        List<String> translated = new ArrayList<>();
        for (String key : text)
            translated.add(I18n.format(key));

        List<String> toRender = new ArrayList<>();
        if (width != 0){
            for(String line : translated) {
                String[] words = line.split(" ");
                List<String> result = new ArrayList<>();
                result.add(words[0]);
                int j = 0;
                for (int i = 1; i < words.length; i++) {
                    if ((float)font.getStringWidth(result.get(j) + words[i])/(1f/textSize) < width) {
                        result.set(j, result.get(j) + (result.get(j).length() == 0 ? "" : " ") + words[i]);
                    }
                    else {
                        j++;
                    }
                }
                toRender.addAll(result);
            }
        }
        else
            toRender.addAll(Arrays.asList(text));
        this.text = toRender;
        this.height = toRender.size() * oneCharHeight;
    }

    @Override
    public void draw(Tessellator tes, BufferBuilder buf, float mouseX, float mouseY) {
        for (int i = 0; i < text.size(); i++){
            float localPosX = posX*(1f/textSize);
            float localPosY = (posY - i*oneCharHeight)*(1f/textSize);
            String localFormat = format != null ? format.toString() : "";
            if (align != Align.LEFT)
                localPosX = align == Align.RIGHT ? localPosX - font.getStringWidth(text.get(i)) : localPosX - font.getStringWidth(localFormat+text.get(i))/2;

            glPushMatrix();
            glScaled(textSize, -textSize, 1f);
            font.drawString(localFormat+text.get(i), localPosX, -localPosY, color, false);
            GlStateManager.color(1, 1, 1);
            glPopMatrix();
        }

    }

    public float getHeight(){
        return height;
    }
}
