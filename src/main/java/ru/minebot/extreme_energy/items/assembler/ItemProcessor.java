package ru.minebot.extreme_energy.items.assembler;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemProcessor extends Item{
    public ItemProcessor(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.PROCESSOR.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.PROCESSOR.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
