package ru.minebot.extreme_energy.modules;

public interface IGenerator extends IModuleTier{
    default void update(ChipArgs args){}
    int generateEnergy(ChipArgs args);
}
