package ru.minebot.extreme_energy.items.modules;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.modules.ChipArgs;
import ru.minebot.extreme_energy.modules.IGenerator;
import ru.minebot.extreme_energy.modules.Module;

public class ItemSunEnergyModule extends Module implements IGenerator{
    public ItemSunEnergyModule() {
        super(Reference.ExtremeEnergyItems.MODULEENERGYSUN.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULEENERGYSUN.getRegistryName(), 1, false);
    }

    @Override
    public int generateEnergy(ChipArgs args) {
        if (!args.player.world.isRemote) {
            RayTraceResult ray = args.player.world.rayTraceBlocks(args.player.getPositionVector(), args.player.getPositionVector().addVector(0, 255f, 0));
            boolean isDay = args.player.world.isDaytime();
            boolean onSun = ray == null || ray.typeOfHit == RayTraceResult.Type.MISS;
            return onSun ? (isDay ? 100 : 10) : 0;
        }
        return 0;
    }

    @Override
    public int getTier() {
        return 1;
    }
}
