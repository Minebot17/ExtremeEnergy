package ru.minebot.extreme_energy.items.assembler;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemElectricalBoard extends Item{
    public ItemElectricalBoard(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.ELECTRICALBOARD.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.ELECTRICALBOARD.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
