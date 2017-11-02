package ru.minebot.extreme_energy.modules;

import net.minecraft.tileentity.TileEntity;

public abstract class ModuleAddValue extends Module {

    public ModuleAddValue(String unlocName, String regName, int maxSize, boolean multiply){
        super(unlocName, regName, maxSize, multiply);
    }
}
