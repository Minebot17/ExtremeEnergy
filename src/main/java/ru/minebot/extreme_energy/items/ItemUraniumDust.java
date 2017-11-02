package ru.minebot.extreme_energy.items;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemUraniumDust extends Item {
    public ItemUraniumDust(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.URANIUMDUST.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.URANIUMDUST.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
