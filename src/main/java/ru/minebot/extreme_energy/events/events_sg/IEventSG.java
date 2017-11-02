package ru.minebot.extreme_energy.events.events_sg;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.minebot.extreme_energy.events.IEvent;

public interface IEventSG {
    void onEvent(World world, BlockPos pos);
    float getChance(World world, BlockPos pos);
}
