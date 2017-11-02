package ru.minebot.extreme_energy.items.implants;

import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.init.ModGuiHandler;

public class ItemAdvancedImplant extends Implant {
    public ItemAdvancedImplant(){
        super(Reference.ExtremeEnergyItems.ADVANCEDIMPLANT.getUnlocalizedName(), Reference.ExtremeEnergyItems.ADVANCEDIMPLANT.getRegistryName(), ModGuiHandler.AI_GUI);
    }
}
