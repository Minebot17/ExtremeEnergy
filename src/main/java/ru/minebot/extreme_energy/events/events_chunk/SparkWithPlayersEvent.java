package ru.minebot.extreme_energy.events.events_chunk;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ru.minebot.extreme_energy.init.LightningEvents;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketSpawnLightning;

import java.util.List;

public class SparkWithPlayersEvent implements IEventChunk{
    @Override
    public float getChance(int value) {
        return (-(1f/1000000f)*(((float) value - 5000000f)*((float) value - 5000000f))+1000000f)/5000000f;
    }

    @Override
    public int onEvent(World world, EntityPlayer player) {
        int toEnergy = 0;
        List<EntityPlayer> players = ModUtils.radiusFilterPlayers(player.getPosition(), world.playerEntities, 1);
        for (EntityPlayer p : players){
            if (p != player) {
                NetworkWrapper.instance.sendToAllAround(
                        new PacketSpawnLightning(
                                player.getPositionVector().addVector(0, 1, 0),
                                p.getPositionVector().addVector(0, 1, 0),
                                LightningEvents.Type.TINY
                        ),
                        ModUtils.getTargetPoint(player, 32)
                );
                p.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 1);
                toEnergy += 10000;
            }
        }
        return toEnergy;
    }
}
