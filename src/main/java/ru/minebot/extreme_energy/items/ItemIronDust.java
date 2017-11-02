package ru.minebot.extreme_energy.items;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemIronDust extends Item{
    public ItemIronDust(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.IRONDUST.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.IRONDUST.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
