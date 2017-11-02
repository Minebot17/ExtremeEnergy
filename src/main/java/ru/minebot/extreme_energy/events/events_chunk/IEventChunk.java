package ru.minebot.extreme_energy.events.events_chunk;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import ru.minebot.extreme_energy.events.IEvent;

public interface IEventChunk extends IEvent {
    int onEvent(World world, EntityPlayer player);
    default int getRarity(int value){ return 20; } // Do not touch!

}
