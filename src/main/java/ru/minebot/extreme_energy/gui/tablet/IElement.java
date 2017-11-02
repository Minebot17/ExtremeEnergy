package ru.minebot.extreme_energy.gui.tablet;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;

public interface IElement {
    void draw(Tessellator tes, BufferBuilder buf, float mouseX, float mouseY);
    float getPosX();
    float getPosY();
    float getHeight();
}
