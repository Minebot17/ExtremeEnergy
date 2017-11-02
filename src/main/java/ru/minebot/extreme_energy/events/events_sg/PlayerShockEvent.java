package ru.minebot.extreme_energy.events.events_sg;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ru.minebot.extreme_energy.init.LightningEvents;
import ru.minebot.extreme_energy.init.ModConfig;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketSpawnLightning;

import java.util.List;

public class PlayerShockEvent implements IEventSG {
    @Override
    public float getChance(World world, BlockPos pos) {
        List<EntityPlayer> players = ModUtils.radiusFilterPlayers(pos, world.playerEntities, 10);
        return players.size() == 0 ? 0 : 0.5f;
    }

    @Override
    public void onEvent(World world, BlockPos pos) {
        List<EntityPlayer> players = ModUtils.radiusFilterPlayers(pos, world.playerEntities, 10);
        for (EntityPlayer player : players){
            NetworkWrapper.instance.sendToAllAround(
                    new PacketSpawnLightning(
                            new Vec3d(pos).addVector(0.5f,0.5f,0.5f),
                            player.getPositionVector(),
                            LightningEvents.Type.BIG
                    ),
                    ModUtils.getTargetPoint(player, 32)
            );
            player.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 6);
            player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("meem:electricShock"), 60, 0));
        }
    }
}
