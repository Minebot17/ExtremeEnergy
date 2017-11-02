package ru.minebot.extreme_energy.items;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemUranium extends Item {
    public ItemUranium(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.URANIUM.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.URANIUM.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
