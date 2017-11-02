package ru.minebot.extreme_energy.gui.tablet;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;

public interface IScene {
    void draw(Tessellator tes, BufferBuilder buf, float mouseX, float mouseY);
    void onMouseDown();
    void onMouseMove();
    void onMouseUp();
    void onWheelScrolled(int delta);
    void onKeyPressed();
}
