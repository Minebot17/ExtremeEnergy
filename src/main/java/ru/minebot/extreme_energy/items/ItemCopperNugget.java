package ru.minebot.extreme_energy.items;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemCopperNugget extends Item{
    public ItemCopperNugget(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.COPPERNUGGET.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.COPPERNUGGET.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
