package ru.minebot.extreme_energy.events.events_chunk;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ru.minebot.extreme_energy.init.LightningEvents;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketSpawnLightning;

public class LightningBetweenBlocksEvent implements IEventChunk {
    @Override
    public float getChance(int value) {
        return (-(1f/1000000f)*(((float) value - 8000002f)*((float) value - 8000002f))+1000000f)/20000000f;
    }

    @Override
    public int onEvent(World world, EntityPlayer player) {
        float x = (ModUtils.random.nextFloat()-0.5f)*10 + player.getPosition().getX();
        float z = (ModUtils.random.nextFloat()-0.5f)*10 + player.getPosition().getZ();
        int start = 0;
        BlockPos downPos = null;
        BlockPos upPos = null;
        for (int i = 0; i < 256; i++) {
            if (world.getBlockState(new BlockPos((int) x, i, (int) z)).getBlock() != Blocks.AIR &&
                    world.getBlockState(new BlockPos((int) x, i + 1, (int) z)).getBlock() == Blocks.AIR &&
                    (downPos == null || new Vec3d(downPos).distanceTo(player.getPositionVector()) > new Vec3d(x, i, z).distanceTo(player.getPositionVector()))) {
                downPos = new BlockPos(x, i, z);
                start = i+1;
            }
        }
        for (int i = start; i < 256; i++) {
            if (world.getBlockState(new BlockPos((int) x, i, (int) z)).getBlock() != Blocks.AIR &&
                    world.getBlockState(new BlockPos((int) x, i - 1, (int) z)).getBlock() == Blocks.AIR &&
                    (upPos == null || new Vec3d(upPos).distanceTo(player.getPositionVector()) > new Vec3d(x, i, z).distanceTo(player.getPositionVector())))
                upPos = new BlockPos(x, i, z);
        }

        if (downPos != null && upPos != null) {
            NetworkWrapper.instance.sendToAllAround(
                    new PacketSpawnLightning(
                            new Vec3d(downPos),
                            new Vec3d(upPos),
                            LightningEvents.Type.SMALL
                    ),
                    ModUtils.getTargetPoint(player, 32)
            );
            return 25000;
        }
        return 0;
    }
}
