package ru.minebot.extreme_energy.items.implants;

import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.init.ModGuiHandler;

public class ItemBasicCore extends Core {
    public ItemBasicCore() {
        super(Reference.ExtremeEnergyItems.BASICCORE.getUnlocalizedName(), Reference.ExtremeEnergyItems.BASICCORE.getRegistryName(), ModGuiHandler.BASIC_CORE_GUI, 3);
    }
}
