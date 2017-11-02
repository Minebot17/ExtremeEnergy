package ru.minebot.extreme_energy.items.modules;

import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.modules.ModuleAddValue;

public class ItemHeatModule extends ModuleAddValue {

    public ItemHeatModule(){
        super(Reference.ExtremeEnergyItems.MODULEHEAT.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULEHEAT.getRegistryName(), 30, false);
    }

    @Override
    public int getTier() {
        return 0;
    }
}
