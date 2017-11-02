package ru.minebot.extreme_energy.items.implants;

import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.init.ModGuiHandler;

public class ItemBaseImplant extends Implant{

    public ItemBaseImplant(){
        super(Reference.ExtremeEnergyItems.BASEIMPLANT.getUnlocalizedName(), Reference.ExtremeEnergyItems.BASEIMPLANT.getRegistryName(), ModGuiHandler.BI_GUI);
    }
}
