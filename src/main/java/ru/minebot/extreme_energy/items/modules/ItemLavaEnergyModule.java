package ru.minebot.extreme_energy.items.modules;

import net.minecraft.potion.Potion;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.modules.ChipArgs;
import ru.minebot.extreme_energy.modules.IGenerator;
import ru.minebot.extreme_energy.modules.Module;

public class ItemLavaEnergyModule extends Module implements IGenerator {
    public ItemLavaEnergyModule() {
        super(Reference.ExtremeEnergyItems.MODULEENERGYLAVA.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULEENERGYLAVA.getRegistryName(), 1, false);
    }

    @Override
    public int generateEnergy(ChipArgs args) {
        if (!args.player.world.isRemote) {
            if (args.player.isPotionActive(Potion.getPotionById(12)))
                return 0;
            if (args.player.isBurning()){
                args.player.extinguish();
                return 2000;
            }
            return args.player.isInLava() ? 5000 : 0;
        }
        return 0;
    }

    @Override
    public int getTier() {
        return 2;
    }
}
