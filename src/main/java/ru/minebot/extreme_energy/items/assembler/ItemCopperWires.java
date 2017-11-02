package ru.minebot.extreme_energy.items.assembler;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemCopperWires extends Item{
    public ItemCopperWires(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.COPPERWIRES.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.COPPERWIRES.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
