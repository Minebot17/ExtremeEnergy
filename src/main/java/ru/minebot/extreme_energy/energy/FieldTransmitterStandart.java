package ru.minebot.extreme_energy.energy;

import cofh.redstoneflux.api.*;
import cofh.redstoneflux.api.IEnergyStorage;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.init.ModConfig;
import ru.minebot.extreme_energy.init.ModUtils;

import java.util.List;

public abstract class FieldTransmitterStandart extends FieldCreatorStandart implements IFieldReceiverEnergy {
    protected BlockPos link;
    protected int voltageReceive;
    protected int maxVoltageReceive;
    protected int frequencyReceive;

    public abstract int convertReceiveVoltage(int voltageReceive);

    @Override
    public void applyCreatorsAround(){
        List<TileEntity> tes = ModUtils.radiusFilter(getPos(), world.loadedTileEntityList, 100);
        for (int i = 0; i < tes.size(); i++) {
            if (tes.get(i) instanceof IFieldCreatorEnergy)
                ((IFieldCreatorEnergy) tes.get(i)).applyLinkedBlocks();
        }
    }

    public void setFrequencyReceive(int frequencyReceive){
        this.frequencyReceive = frequencyReceive;
        applyCreatorsAround();
        markDirty();
        markUpdate();
    }

    public int getFrequencyReceive(){
        return frequencyReceive;
    }

    public int getVoltageReceive(){
        return voltageReceive;
    }

    public int getMaxVoltageReceive() {
        return maxVoltageReceive;
    }

    @Override
    public int getEnergyStored() {
        return link != null ? ((IEnergyStorage)world.getTileEntity(link)).getEnergyStored() : 0;
    }

    @Override
    public int getMaxEnergyStored() {
        return link != null ? ((IEnergyStorage)world.getTileEntity(link)).getMaxEnergyStored() : 0;
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return false;
    }

    @Override
    public int getVoltage(){ return voltage; }

    @Override
    public int getMaxVoltage() {
        return 99999;
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
            voltageReceive = 0;
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
        applyLinkedBlocks();
        markDirty();
        markUpdate();
    }

    @Override
    public void setVoltage(int voltage){
        this.voltageReceive = voltage;
        this.voltage = convertReceiveVoltage(voltage);
        if (voltageReceive > maxVoltageReceive)
            exceedingVoltage(voltageReceive);
        markDirty();
        markUpdate();
    }

    public void exceedingVoltage(int voltage){
        if (ModConfig.explodeMachines) {
            if (link != null)
                ((IFieldCreatorEnergy) world.getTileEntity(link)).brokeLink(getPos());
            world.createExplosion(new EntityEgg(world, getPos().getX(), getPos().getY(), getPos().getZ()), getPos().getX(), getPos().getY(), getPos().getZ(), voltage / maxVoltage, true);
        }
    }

    protected void markUpdate(){
        world.notifyBlockUpdate(getPos(), getBlockType().getDefaultState(), getBlockType().getDefaultState(), 0);
    }

    @Override
    public NBTTagCompound writeToNBTFieldStats(NBTTagCompound nbt){
        nbt = super.writeToNBTFieldStats(nbt);
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        if (hasField()) {
            category.setBoolean("HasReceiveLink", true);
            category.setInteger("LinkX", link.getX());
            category.setInteger("LinkY", link.getY());
            category.setInteger("LinkZ", link.getZ());
        }
        else
            category.setBoolean("HasReceiveLink", false);

        category.setInteger("FieldReceiveVoltage", voltageReceive);
        category.setInteger("FieldReceiveMaxVoltage", maxVoltageReceive);
        category.setInteger("FieldReceiveFrequency", frequencyReceive);
        nbt.setTag(ExtremeEnergy.NBT_CATEGORY, category);
        return  nbt;
    }

    @Override
    public void readFromNBTFieldStats(NBTTagCompound nbt){
        super.readFromNBTFieldStats(nbt);
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        if (category.getBoolean("HasReceiveLink")){
            link = new BlockPos(category.getInteger("LinkX"), category.getInteger("LinkY"), category.getInteger("LinkZ"));
        }
        else
            link = null;
        voltageReceive = category.getInteger("FieldReceiveVoltage");
        maxVoltageReceive = category.getInteger("FieldReceiveMaxVoltage");
        frequencyReceive = category.getInteger("FieldReceiveFrequency");
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        TileEntity entity = world.getTileEntity(link);
        return link != null && entity != null ? ((IEnergyStorage)entity).receiveEnergy(maxReceive, simulate) : 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        TileEntity entity = world.getTileEntity(link);
        return link != null && entity != null ? ((IEnergyStorage)entity).extractEnergy(maxExtract, simulate) : 0;
    }
}
