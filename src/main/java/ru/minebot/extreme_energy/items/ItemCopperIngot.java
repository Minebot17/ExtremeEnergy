package ru.minebot.extreme_energy.items;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemCopperIngot extends Item{

    public ItemCopperIngot(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.COPPERINGOT.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.COPPERINGOT.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
