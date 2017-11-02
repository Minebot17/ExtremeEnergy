package ru.minebot.extreme_energy.items.implants;

import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.init.ModGuiHandler;

public class ItemExtremeImplant extends Implant{
    public ItemExtremeImplant(){
        super(Reference.ExtremeEnergyItems.EXTREMEIMPLANT.getUnlocalizedName(), Reference.ExtremeEnergyItems.EXTREMEIMPLANT.getRegistryName(), ModGuiHandler.EI_GUI);
    }
}
