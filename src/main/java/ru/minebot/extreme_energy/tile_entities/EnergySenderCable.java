package ru.minebot.extreme_energy.tile_entities;

import cofh.redstoneflux.api.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import ru.minebot.extreme_energy.energy.IEnergyTransport;
import ru.minebot.extreme_energy.init.ModBlocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EnergySenderCable extends EnergySender implements IEnergyTransport {
    private HashMap<EnumFacing, IEnergyProvider> providers = new HashMap<>();

    public void update(){
        super.update();
        if (!world.isRemote){
            for (EnumFacing facing : EnumFacing.values()){
                if (providers.containsKey(facing))
                    receiveEnergy(providers.get(facing).extractEnergy(facing.getOpposite(), 99999999, false), false);
            }
        }
    }

    public void calculateEnergyNetwork(){
        if (!world.isRemote) {
            maxExtract = 9999999;
            maxReceive = 9999999;
            capacity = 999999999;
            boolean yes = false;
            for (EnumFacing facing : EnumFacing.values()) {
                TileEntity te = world.getTileEntity(pos.offset(facing));
                if (te != null && !(te instanceof EnergySender) && te instanceof IEnergyProvider)
                    yes = true;
            }

            TileEntity te = world.getTileEntity(pos);
            if (!yes && te != null)
                world.setBlockState(pos, ModBlocks.cable.getDefaultState());

            providers = new HashMap<>();
            for (EnumFacing facing : EnumFacing.values()){
                TileEntity te1 = world.getTileEntity(pos.offset(facing));
                if (te1 != null && te1 instanceof IEnergyProvider && !(te instanceof EnergySender))
                    providers.put(facing, (IEnergyProvider) te1);
            }

            receivers = new ArrayList<>();
            List<Vertex> vertexes = getVertexes(new ArrayList<>(), getPos(), new ArrayList<>());
            for (Vertex vertex : vertexes)
                if (!vertex.isCable && !vertex.pos.equals(getPos())) {
                    List<EnumFacing> connects = new ArrayList<>();
                    for (EnumFacing facing : EnumFacing.values())
                        if (vertex.pos.offset(facing).equals(getPos()) || isConnected(new ArrayList<>(), vertexes, vertex.pos.offset(facing)))
                            connects.add(facing);
                    receivers.add(new Receiver((IEnergyReceiver) world.getTileEntity(vertex.pos), vertex.pos, getPath(vertexes, pos, vertex.pos), connects));
                }
        }
    }

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        return extractEnergy(maxExtract, simulate);
    }

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        return receiveEnergy(maxReceive, simulate);
    }

    @Override
    public InterfaceType getTransportState(EnumFacing from) {
        return InterfaceType.BALANCE;
    }

    @Override
    public boolean setTransportState(InterfaceType state, EnumFacing from) {
        return true;
    }
}
