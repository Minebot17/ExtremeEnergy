package ru.minebot.extreme_energy.events.events_sg;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.minebot.extreme_energy.init.ModConfig;

public class ExplodeEvent implements IEventSG {
    @Override
    public float getChance(World world, BlockPos pos) {
        return 0.2f;
    }

    @Override
    public void onEvent(World world, BlockPos pos) {
        if (ModConfig.explodeMachines)
            world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 6, true);
        else
            world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 1, true);
    }
}
