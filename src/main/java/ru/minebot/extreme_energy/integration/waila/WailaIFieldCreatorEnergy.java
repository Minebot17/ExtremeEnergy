package ru.minebot.extreme_energy.integration.waila;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.minebot.extreme_energy.energy.IFieldCreatorEnergy;
import ru.minebot.extreme_energy.init.ModConfig;

import java.util.List;

public class WailaIFieldCreatorEnergy implements IWailaDataProvider {

    public WailaIFieldCreatorEnergy() {

    }

    public ItemStack getWailaStack(IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        return null;
    }

    public List<String> getWailaHead(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        return null;
    }

    public List<String> getWailaBody(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        IFieldCreatorEnergy te = (IFieldCreatorEnergy)iWailaDataAccessor.getTileEntity();
        list.add("Energy: " + te.getEnergyStored() + "/" + te.getMaxEnergyStored() + " RF");
        list.add("Voltage: " + te.getVoltage());
        list.add("Radius: " + te.getRadius());
        if (ModConfig.showFrequencyWaila) {
            list.add("Frequency: " + te.getFrequency());
        }

        return list;
    }

    public List<String> getWailaTail(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        return null;
    }

    public NBTTagCompound getNBTData(EntityPlayerMP entityPlayerMP, TileEntity tileEntity, NBTTagCompound nbtTagCompound, World world, BlockPos blockPos) {
        return null;
    }
}
