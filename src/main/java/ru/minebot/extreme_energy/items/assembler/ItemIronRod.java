package ru.minebot.extreme_energy.items.assembler;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemIronRod extends Item{
    public ItemIronRod(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.IRONROD.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.IRONROD.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
