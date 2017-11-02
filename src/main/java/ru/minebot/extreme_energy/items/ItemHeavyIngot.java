package ru.minebot.extreme_energy.items;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemHeavyIngot extends Item {
    public ItemHeavyIngot(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.HEAVYINGOT.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.HEAVYINGOT.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
