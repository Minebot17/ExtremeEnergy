package ru.minebot.extreme_energy.items;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemPowerSuppressor extends Item {
    public ItemPowerSuppressor(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.POWERSUPPRESSOR.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.POWERSUPPRESSOR.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setMaxStackSize(1);
    }
}
