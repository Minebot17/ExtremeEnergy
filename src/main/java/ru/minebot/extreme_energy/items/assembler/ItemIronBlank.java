package ru.minebot.extreme_energy.items.assembler;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemIronBlank extends Item{
    public ItemIronBlank(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.IRONBLANK.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.IRONBLANK.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
