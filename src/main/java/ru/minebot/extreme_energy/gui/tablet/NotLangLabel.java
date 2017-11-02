package ru.minebot.extreme_energy.gui.tablet;

import net.minecraft.util.text.TextFormatting;

import java.util.Collections;

public class NotLangLabel extends Label {
    public NotLangLabel(float posX, float posY, String text, Align align, TextFormatting format, int color) {
        super(posX, posY);
        this.format = format;
        this.align = align;
        this.color = color;
        this.text = Collections.singletonList(text);
        this.height = oneCharHeight;
    }
}
