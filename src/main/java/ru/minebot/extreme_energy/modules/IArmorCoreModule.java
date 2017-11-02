package ru.minebot.extreme_energy.modules;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public interface IArmorCoreModule {
    void onDamaged(LivingHurtEvent event, Entity attacker, int power, NBTTagCompound data, EntityPlayer player);
    int getEnergy(int power);
}
