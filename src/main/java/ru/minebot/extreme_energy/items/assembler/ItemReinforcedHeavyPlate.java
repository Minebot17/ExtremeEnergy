package ru.minebot.extreme_energy.items.assembler;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemReinforcedHeavyPlate extends Item {
    public ItemReinforcedHeavyPlate(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.REINFORCEDHEAVYPLATE.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.REINFORCEDHEAVYPLATE.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
