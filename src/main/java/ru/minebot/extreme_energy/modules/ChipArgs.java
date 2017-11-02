package ru.minebot.extreme_energy.modules;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;

public class ChipArgs {
    public EntityPlayer player;
    public boolean isModuleActive;
    public boolean isImplantPowered;
    public boolean isImplantCharging;
    public float voltageReceiving;
    public int energy;
    public NBTTagCompound data;
    public RayTraceResult blocksRay;
    public int entityCollide;

    public ChipArgs(EntityPlayer player, boolean isModuleActive, boolean isImplantPowered, boolean isImplantCharging, float voltageReceiving, int energy, NBTTagCompound data) {
        this(player, isModuleActive, isImplantPowered, isImplantCharging, voltageReceiving, energy, data, null, 0);
    }

    public ChipArgs(EntityPlayer player, boolean isModuleActive, boolean isImplantPowered, boolean isImplantCharging, float voltageReceiving, int energy, NBTTagCompound data, RayTraceResult blocksRay, int entityCollide) {
        this.player = player;
        this.isModuleActive = isModuleActive;
        this.isImplantPowered = isImplantPowered;
        this.isImplantCharging = isImplantCharging;
        this.voltageReceiving = voltageReceiving;
        this.energy = energy;
        this.data = data;
        this.blocksRay = blocksRay;
        this.entityCollide = entityCollide;
    }
}
