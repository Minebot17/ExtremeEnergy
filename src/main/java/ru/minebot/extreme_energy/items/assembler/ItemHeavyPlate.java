package ru.minebot.extreme_energy.items.assembler;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemHeavyPlate extends Item {
    public ItemHeavyPlate(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.HEAVYPLATE.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.HEAVYPLATE.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
