package ru.minebot.extreme_energy.items.assembler;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemToughIronRod extends Item {
    public ItemToughIronRod(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.TOUGHIRONROD.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.TOUGHIRONROD.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
