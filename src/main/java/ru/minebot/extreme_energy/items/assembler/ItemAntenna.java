package ru.minebot.extreme_energy.items.assembler;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemAntenna extends Item{
    public ItemAntenna(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.ANTENNA.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.ANTENNA.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
