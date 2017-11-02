package ru.minebot.extreme_energy.items;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemGoldDust extends Item{
    public ItemGoldDust(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.GOLDDUST.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.GOLDDUST.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
