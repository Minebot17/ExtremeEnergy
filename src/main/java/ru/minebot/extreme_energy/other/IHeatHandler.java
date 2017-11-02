package ru.minebot.extreme_energy.other;

public interface IHeatHandler {
    void setHeat(int value);
    void setMaxHeat(int value);
    int getHeat();
    int getMaxHeat();
    void overheat();
}
