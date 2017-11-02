package ru.minebot.extreme_energy.items;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemFullCapsule extends Item {
    public static final int freezeTime = 2400;

    public ItemFullCapsule(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.FULLCAPSULE.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.FULLCAPSULE.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
