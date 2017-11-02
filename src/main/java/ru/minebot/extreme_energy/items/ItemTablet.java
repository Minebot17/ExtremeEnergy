package ru.minebot.extreme_energy.items;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemTablet extends Item {
    public ItemTablet(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.TABLET.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.TABLET.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
