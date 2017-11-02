package ru.minebot.extreme_energy.items.assembler;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemHeavyBlank extends Item{
    public ItemHeavyBlank(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.HEAVYBLANK.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.HEAVYBLANK.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
