package ru.minebot.extreme_energy.modules;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface ISwordModule {
    void onEntityHit(EntityLivingBase target, EntityLivingBase attacker, ItemStack sword, int power);
    default void onHit(EntityLivingBase attacker, ItemStack sword, int power){}
    int getEnergy(ItemStack sword, int power);
}
