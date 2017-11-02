package ru.minebot.extreme_energy.modules;

import net.minecraft.nbt.NBTTagCompound;

public interface IKey {
    void onModuleActivated(ChipArgs args, int keyIndex);
    int getEnergy(ChipArgs args, int power, int keyIndex);
    int[] getKeyCodes(NBTTagCompound data);
}
