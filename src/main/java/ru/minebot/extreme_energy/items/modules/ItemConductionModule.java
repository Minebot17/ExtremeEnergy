package ru.minebot.extreme_energy.items.modules;

import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.modules.ModuleAddValue;

public class ItemConductionModule extends ModuleAddValue {
    public ItemConductionModule(){
        super(Reference.ExtremeEnergyItems.MODULECONDUCTION.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULECONDUCTION.getRegistryName(), 64, false);
    }

    @Override
    public int getTier() {
        return 0;
    }
}
