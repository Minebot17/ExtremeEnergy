package ru.minebot.extreme_energy.events.events_sg;

import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SmeltEvent implements IEventSG {

    @Override
    public void onEvent(World world, BlockPos pos) {
        world.getBlockState(pos).getBlock().breakBlock(world, pos, world.getBlockState(pos));
        world.setBlockState(pos, Blocks.LAVA.getDefaultState());
        for (int x = -3; x < 3; x++)
            for (int y = -1; y < 1; y++)
                for (int z = -3; z < 3; z++){
                    BlockPos newPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    if (world.getBlockState(newPos).getBlock() != Blocks.AIR && !(world.getBlockState(newPos).getBlock() instanceof BlockLiquid) && world.getBlockState(newPos.up()).getBlock() == Blocks.AIR)
                        world.setBlockState(newPos, Blocks.FIRE.getDefaultState());
                }
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 1, 1, false);
    }

    @Override
    public float getChance(World world, BlockPos pos) {
        return 0.2f;
    }
}
