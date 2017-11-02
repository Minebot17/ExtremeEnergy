package ru.minebot.extreme_energy.other;

import net.minecraft.nbt.NBTTagCompound;

public class ImplantData {
    public int type;
    public NBTTagCompound modules;
    public NBTTagCompound implant;
    public NBTTagCompound core;
    public int playerID;

    public ImplantData(int type, NBTTagCompound modules, NBTTagCompound implant, NBTTagCompound core, int playerID) {
        this.type = type;
        this.modules = modules;
        this.implant = implant;
        this.core = core;
        this.playerID = playerID;
    }
}
