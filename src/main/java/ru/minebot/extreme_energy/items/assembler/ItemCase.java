package ru.minebot.extreme_energy.items.assembler;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemCase extends Item {
    public ItemCase(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.CASE.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.CASE.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
