package ru.minebot.extreme_energy.items.capacitors;

import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemBigCapacitor extends  Capacitor{

    public ItemBigCapacitor(int capacity, int maxReceive, int maxExtract){
        super(capacity, maxReceive, maxExtract);
        setUnlocalizedName(Reference.ExtremeEnergyItems.BIGCAPACITOR.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.BIGCAPACITOR.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setMaxStackSize(1);
        setMaxDamage(capacity);
    }
}
