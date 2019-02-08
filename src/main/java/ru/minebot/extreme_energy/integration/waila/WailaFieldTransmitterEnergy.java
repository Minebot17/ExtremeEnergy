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
import ru.minebot.extreme_energy.energy.FieldTransmitterStandart;
import ru.minebot.extreme_energy.init.ModConfig;

import java.util.ArrayList;
import java.util.List;

public class WailaFieldTransmitterEnergy implements IWailaDataProvider {
    public WailaFieldTransmitterEnergy() {
    }

    public ItemStack getWailaStack(IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        return null;
    }

    public List<String> getWailaHead(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        return null;
    }

    public List<String> getWailaBody(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        List<String> list1 = new ArrayList<>();
        FieldTransmitterStandart te = (FieldTransmitterStandart)iWailaDataAccessor.getTileEntity();
        list1.add("Receive voltage: " + te.getVoltageReceive());
        list1.add("Voltage: " + te.getVoltage());
        list1.add("Radius: " + te.getRadius());
        if (ModConfig.showFrequencyWaila) {
            list1.add("Frequency: " + te.getFrequencyReceive());
            list1.add("Convert to frequency: " + te.getFrequency());
        }

        return list1;
    }

    public List<String> getWailaTail(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        return null;
    }

    public NBTTagCompound getNBTData(EntityPlayerMP entityPlayerMP, TileEntity tileEntity, NBTTagCompound nbtTagCompound, World world, BlockPos blockPos) {
        return null;
    }
}