package ru.minebot.extreme_energy.items;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemNuclearFuel extends Item {
    public static final int workTime = 20000;

    public ItemNuclearFuel(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.NUCLEARFUEL.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.NUCLEARFUEL.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
