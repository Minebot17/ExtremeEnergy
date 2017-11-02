package ru.minebot.extreme_energy.energy;

public interface IVoltageHandler {
    int getMaxVoltage();
    int getVoltage();
    void setVoltage(int voltage);
}
