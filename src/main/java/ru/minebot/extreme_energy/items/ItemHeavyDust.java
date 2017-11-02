package ru.minebot.extreme_energy.items;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemHeavyDust extends Item {
    public ItemHeavyDust(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.HEAVYDUST.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.HEAVYDUST.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
