package ru.minebot.extreme_energy.tile_entities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.ChunkPos;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.blocks.BlockCS;
import ru.minebot.extreme_energy.init.ModConfig;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.other.ChargeSaveData;

import javax.annotation.Nullable;

public class TileEntityCS extends TileEntity implements ITickable {
    public int mode;
    public int bound;
    public int signal;

    public TileEntityCS(){
        mode = 1;
        bound = 5000000;
    }

    @Override
    public void update() {
        if (!world.isRemote){
            if (mode == 0) {
                if (signal != 0) {
                    signal = 0;
                    markDirty();
                    ModUtils.setState(world, pos, getBlockType().getDefaultState().withProperty(BlockCS.ACTIVE, false));
                    world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
                }
            }
            else if (mode == 1 || mode == 2){
                Integer charge = ChargeSaveData.getOrCreateData(world).map.get(new ChunkPos(getPos()));
                boolean active = charge >= bound;
                if (mode == 2)
                    active = !active;
                if (active && signal != 15) {
                    signal = 15;
                    markDirty();
                    ModUtils.setState(world, pos, getBlockType().getDefaultState().withProperty(BlockCS.ACTIVE, true));
                    world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
                }
                else if (!active && signal != 0) {
                    signal = 0;
                    markDirty();
                    ModUtils.setState(world, pos, getBlockType().getDefaultState().withProperty(BlockCS.ACTIVE, false));
                    world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
                }
            }
            else if (mode == 3){
                Integer charge = ChargeSaveData.getOrCreateData(world).map.get(new ChunkPos(getPos()));
                int power = (int)((float)charge / (float)ModConfig.maxCapOfChunk * 15f);
                //signal = (int)(((mode == 3 ? 1f : -1f)*(float)charge / (float)ModConfig.maxCapOfChunk * 15f) + ((mode == 3 ? -1 : 1)*(float)bound/(float)ModConfig.maxCapOfChunk * 15f));
                if (signal != power) {
                    signal = power;
                    markDirty();
                    ModUtils.setState(world, pos, getBlockType().getDefaultState().withProperty(BlockCS.ACTIVE, true));
                    world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
                }
                else if (power == 0){
                    signal = 0;
                    markDirty();
                    ModUtils.setState(world, pos, getBlockType().getDefaultState().withProperty(BlockCS.ACTIVE, false));
                    world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
                }
            }
            else if (mode == 4)
                if (signal != 15) {
                    signal = 15;
                    markDirty();
                    ModUtils.setState(world, pos, getBlockType().getDefaultState().withProperty(BlockCS.ACTIVE, true));
                    world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
                }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        mode = category.getInteger("mode");
        bound = category.getInteger("bound");
        signal = category.getInteger("signal");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
        nbt = super.writeToNBT(nbt);
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        category.setInteger("mode", mode);
        category.setInteger("bound", bound);
        category.setInteger("signal", signal);
        nbt.setTag(ExtremeEnergy.NBT_CATEGORY, category);
        return nbt;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    private void markUpdate(){
        world.notifyBlockUpdate(getPos(), getBlockType().getDefaultState(), getBlockType().getDefaultState(), 0);
    }
}
