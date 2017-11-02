package ru.minebot.extreme_energy.items.crystals;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemSmallCrystal extends Item{

    public ItemSmallCrystal(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.SMALLCRYSTAL.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.SMALLCRYSTAL.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
