package ru.minebot.extreme_energy.items.modules;

import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.modules.ChipArgs;
import ru.minebot.extreme_energy.modules.IGenerator;
import ru.minebot.extreme_energy.modules.Module;

public class ItemHungerEnergyModule extends Module implements IGenerator {

    public ItemHungerEnergyModule() {
        super(Reference.ExtremeEnergyItems.MODULEENERGYHUNGRY.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULEENERGYHUNGRY.getRegistryName(), 1, false);
    }

    @Override
    public int generateEnergy(ChipArgs args) {
        if (!args.player.world.isRemote) {
            boolean norm = args.player.getFoodStats().getFoodLevel() > 4;
            if (args.player.world.getTotalWorldTime() % 600 == 0 && norm) {
                args.player.getFoodStats().setFoodLevel(args.player.getFoodStats().getFoodLevel() - 1);
            }
            return norm ? 50 : 0;
        }
        return 0;
    }

    @Override
    public int getTier() {
        return 0;
    }
}
