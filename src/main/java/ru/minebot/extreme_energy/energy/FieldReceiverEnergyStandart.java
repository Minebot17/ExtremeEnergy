package ru.minebot.extreme_energy.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.init.ModConfig;
import ru.minebot.extreme_energy.init.ModUtils;

import java.util.List;

public abstract class FieldReceiverEnergyStandart extends TileEntity implements IFieldReceiverEnergy, ITickable {

    protected BlockPos link;
    protected int voltage;
    protected int maxVoltage;
    protected int frequency;

    @Override
    public void update(){
        if (!world.isRemote){
            if (link == null) {
                int voltage = ModUtils.extractEnergyFromChunk(world, new ChunkPos(getPos()), true) / 8;
                setVoltage(voltage);
                if (voltage != 0) {
                    if (isActive())
                        onField();
                }
            }
        }
    }

    @Override
    public void applyCreatorsAround(){
        List<TileEntity> tes = ModUtils.radiusFilter(getPos(), world.loadedTileEntityList, 100);
        for (int i = 0; i < tes.size(); i++) {
            if (tes.get(i) instanceof IFieldCreatorEnergy)
                ((IFieldCreatorEnergy) tes.get(i)).applyLinkedBlocks();
        }
    }

    public abstract void onField();

    @Override
    public int getVoltage(){ return voltage; }

    @Override
    public int getMaxVoltage() {
        return maxVoltage;
    }

    @Override
    public int getFrequency(){ return frequency;}

    @Override
    public BlockPos getLink(){
        return link;
    }

    @Override
    public void setLink(BlockPos pos){
        if (pos == null)
            voltage = 0;
        link = pos;
        markDirty();
        markUpdate();
    }

    @Override
    public boolean hasField(){
        return link != null;
    }

    @Override
    public void setFrequency(int frequency){
        this.frequency = frequency;
        applyCreatorsAround();
        markDirty();
        markUpdate();
    }

    @Override
    public void setVoltage(int voltage){
        this.voltage = voltage;
        if (voltage > maxVoltage)
            exceedingVoltage(voltage);
        markDirty();
        markUpdate();
    }

    public void exceedingVoltage(int voltage){
        if (ModConfig.explodeMachines) {
            if (link != null)
                ((IFieldCreatorEnergy) world.getTileEntity(link)).brokeLink(getPos());
            world.createExplosion(null, getPos().getX(), getPos().getY(), getPos().getZ(), voltage / maxVoltage, true);
        }
    }

    protected void markUpdate(){
        world.notifyBlockUpdate(getPos(), getBlockType().getDefaultState(), getBlockType().getDefaultState(), 0);
    }

    public NBTTagCompound writeToNBTFieldStats(NBTTagCompound nbt){
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        if (hasField()) {
            category.setBoolean("HasLink", true);
            category.setInteger("LinkX", link.getX());
            category.setInteger("LinkY", link.getY());
            category.setInteger("LinkZ", link.getZ());
        }
        else
            category.setBoolean("HasLink", false);

        category.setInteger("FieldVoltage", voltage);
        category.setInteger("FieldMaxVoltage", maxVoltage);
        category.setInteger("FieldFrequency", frequency);
        nbt.setTag(ExtremeEnergy.NBT_CATEGORY, category);
        return  nbt;
    }

    public void readFromNBTFieldStats(NBTTagCompound nbt){
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        if (category.getBoolean("HasLink")){
            link = new BlockPos(category.getInteger("LinkX"), category.getInteger("LinkY"), category.getInteger("LinkZ"));
        }
        else
            link = null;
        voltage = category.getInteger("FieldVoltage");
        maxVoltage = category.getInteger("FieldMaxVoltage");
        if (maxVoltage == 0)
            maxVoltage = 150;
        frequency = category.getInteger("FieldFrequency");
    }

    @Override
    public abstract boolean isActive();
}
