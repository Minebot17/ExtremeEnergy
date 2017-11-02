package ru.minebot.extreme_energy.gui.tablet;

public interface IClickable {
    void onMouseDown();
    void onMouseUp();
    void onMouseMove();
    boolean isHover(float mouseX, float mouseY);
}
