package ru.minebot.extreme_energy.items.capacitors;

import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemSmallCapacitor extends Capacitor {

    public ItemSmallCapacitor(int capacity, int maxReceive, int maxExtract){
        super(capacity, maxReceive, maxExtract);
        setUnlocalizedName(Reference.ExtremeEnergyItems.SMALLCAPACITOR.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.SMALLCAPACITOR.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setMaxStackSize(1);
        setMaxDamage(capacity);
    }
}
