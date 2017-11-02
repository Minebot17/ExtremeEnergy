package ru.minebot.extreme_energy.items.assembler;

import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemForceFieldComponent extends Item {
    public ItemForceFieldComponent(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.FORCEFIELDCOMPONENT.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.FORCEFIELDCOMPONENT.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
