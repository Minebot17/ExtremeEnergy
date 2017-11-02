package ru.minebot.extreme_energy.items.assembler;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemElectricalCarver extends Item{
    public ItemElectricalCarver(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.ELECTRICCARVER.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.ELECTRICCARVER.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
