package ru.minebot.extreme_energy.items.modules;

import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.modules.ModuleAddValue;

public class ItemCapacityModule extends ModuleAddValue {
    public ItemCapacityModule(){
        super(Reference.ExtremeEnergyItems.MODULECAPACITY.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULECAPACITY.getRegistryName(), 64, false);
    }

    @Override
    public int getTier() {
        return 0;
    }
}
