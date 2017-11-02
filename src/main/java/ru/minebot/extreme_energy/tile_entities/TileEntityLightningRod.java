package ru.minebot.extreme_energy.tile_entities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import ru.minebot.extreme_energy.init.ModBlocks;

public class TileEntityLightningRod extends EnergySender {
    public int pillars;

    public TileEntityLightningRod(){
        super();
        outFaces = EnumFacing.HORIZONTALS;
        capacity = 1000000;
        maxExtract = 100000;
        maxReceive = 100000;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        calculatePillars();
    }

    public void calculatePillars(){
        pillars = 0;
        BlockPos thisPos = getPos();
        thisPos = thisPos.up();
        while (world.getBlockState(thisPos).getBlock() == ModBlocks.metalPillar){
            pillars++;
            thisPos = thisPos.up();
        }
        markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        nbt.setInteger("pillars", pillars);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        pillars = nbt.getInteger("pillars");
    }
}
