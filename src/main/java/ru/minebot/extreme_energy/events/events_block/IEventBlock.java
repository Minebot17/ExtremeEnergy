package ru.minebot.extreme_energy.events.events_block;

import net.minecraft.util.math.BlockPos;
import ru.minebot.extreme_energy.events.IEvent;

public interface IEventBlock extends IEvent {
    void onEvent(BlockPos pos, int voltage);
    boolean update(BlockPos pos, int voltage);
    void endEvent(BlockPos pos, int voltage);
}
