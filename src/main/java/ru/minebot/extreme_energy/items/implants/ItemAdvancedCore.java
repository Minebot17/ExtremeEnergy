package ru.minebot.extreme_energy.items.implants;

import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.init.ModGuiHandler;

public class ItemAdvancedCore extends Core {
    public ItemAdvancedCore() {
        super(Reference.ExtremeEnergyItems.ADVANCEDCORE.getUnlocalizedName(), Reference.ExtremeEnergyItems.ADVANCEDCORE.getRegistryName(), ModGuiHandler.ADVANCED_CORE_GUI, 6);
    }
}
