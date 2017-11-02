package ru.minebot.extreme_energy.items.assembler;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemCapsule extends Item{
    public ItemCapsule(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.CAPSULE.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.CAPSULE.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
