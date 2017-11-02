package ru.minebot.extreme_energy.items.modules;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.modules.ModuleAddValue;

import java.util.List;

public class ItemLossReductionModule extends ModuleAddValue {

    public ItemLossReductionModule() {
        super(Reference.ExtremeEnergyItems.MODULELOSSREDUCTION.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULELOSSREDUCTION.getRegistryName(), 4, true);
    }

    @Override
    public int getTier() {
        return 0;
    }
}
