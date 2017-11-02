package ru.minebot.extreme_energy.events.events_sg;

import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SteamEvent implements IEventSG {
    @Override
    public float getChance(World world, BlockPos pos) {
        int count = 0;
        for (int x = -5; x <= 5; x++)
            for (int y = -5; y <= 5; y++)
                for (int z = -5; z <= 5; z++) {
                    BlockPos newPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    if (world.getBlockState(newPos).getBlock() == Blocks.WATER)
                        count++;
                }
        return count > 20 ? 0.8f : 0.2f;
    }

    @Override
    public void onEvent(World world, BlockPos pos) {
        for (int x = -5; x <= 5; x++)
            for (int y = -5; y <= 5; y++)
                for (int z = -5; z <= 5; z++){
                    BlockPos newPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    if (world.getBlockState(newPos).getBlock() == Blocks.WATER)
                        world.setBlockToAir(newPos);
                }
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 1, 1, false);
    }
}
