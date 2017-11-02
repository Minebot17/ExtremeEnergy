package ru.minebot.extreme_energy.items.implants;

import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.init.ModGuiHandler;

public class ItemExtremeCore extends Core {
    public ItemExtremeCore() {
        super(Reference.ExtremeEnergyItems.EXTREMECORE.getUnlocalizedName(), Reference.ExtremeEnergyItems.EXTREMECORE.getRegistryName(), ModGuiHandler.EXTREME_CORE_GUI, 10);
    }
}
