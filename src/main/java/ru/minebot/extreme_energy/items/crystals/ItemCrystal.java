package ru.minebot.extreme_energy.items.crystals;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemCrystal extends Item{

    public ItemCrystal(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.CRYSTAL.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.CRYSTAL.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
