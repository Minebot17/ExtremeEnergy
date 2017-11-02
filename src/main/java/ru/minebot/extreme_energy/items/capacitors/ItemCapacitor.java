package ru.minebot.extreme_energy.items.capacitors;

import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemCapacitor extends Capacitor {

    public ItemCapacitor(int capacity, int maxReceive, int maxExtract){
        super(capacity, maxReceive, maxExtract);
        setUnlocalizedName(Reference.ExtremeEnergyItems.CAPACITOR.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.CAPACITOR.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setMaxStackSize(1);
        setMaxDamage(capacity);
    }
}
