package ru.minebot.extreme_energy.items.assembler;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemCopperBlank extends Item{
    public ItemCopperBlank(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.COPPERBLANK.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.COPPERBLANK.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
