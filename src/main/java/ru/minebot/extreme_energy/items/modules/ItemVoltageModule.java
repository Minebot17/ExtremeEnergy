package ru.minebot.extreme_energy.items.modules;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.modules.ModuleAddValue;

import java.util.List;

public class ItemVoltageModule extends ModuleAddValue {

    public ItemVoltageModule(){
        super(Reference.ExtremeEnergyItems.MODULEVOLTAGE.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULEVOLTAGE.getRegistryName(), 64, false);
    }

    @Override
    public int getTier() {
        return 0;
    }
}
