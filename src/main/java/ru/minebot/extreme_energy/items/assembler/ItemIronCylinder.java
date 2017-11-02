package ru.minebot.extreme_energy.items.assembler;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemIronCylinder extends Item{
    public ItemIronCylinder(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.IRONCYLINDER.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.IRONCYLINDER.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
