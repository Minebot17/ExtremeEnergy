package ru.minebot.extreme_energy.events.events_player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import ru.minebot.extreme_energy.init.LightningEvents;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketSpawnLightning;

import java.util.List;

public class LightningWithPlayersEvent implements IEventPlayer {

    @Override
    public void onEvent(EntityPlayer player, int voltage) {
        List<EntityPlayer> players = ModUtils.radiusFilterPlayers(player.getPosition(), player.world.playerEntities, 5);
        for (EntityPlayer p : players){
            NetworkWrapper.instance.sendToAllAround(
                    new PacketSpawnLightning(
                            player.getPositionVector().addVector(0,1,0),
                            p.getPositionVector().addVector(0, 1, 0),
                            LightningEvents.Type.STANDART
                            ),
                    ModUtils.getTargetPoint(player, 32)
            );
            p.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 1);
        }
    }

    @Override
    public float getChance(int value) {
        return ((-(1f/1000f))*(((float) value - 6000f)*((float) value - 6000f)) + 1000f)/200f;
    }

    @Override
    public int getRarity(int value) {
        return 6000;
    }
}
