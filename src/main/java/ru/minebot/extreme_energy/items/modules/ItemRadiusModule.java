package ru.minebot.extreme_energy.items.modules;

import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.modules.ModuleAddValue;

public class ItemRadiusModule extends ModuleAddValue {
    public ItemRadiusModule(){
        super(Reference.ExtremeEnergyItems.MODULERADIUS.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULERADIUS.getRegistryName(), 19, false);
    }

    @Override
    public int getTier() {
        return 0;
    }
}
