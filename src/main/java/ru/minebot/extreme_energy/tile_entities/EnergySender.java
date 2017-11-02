package ru.minebot.extreme_energy.tile_entities;

import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import cofh.redstoneflux.api.IEnergyStorage;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.blocks.BlockCable;
import ru.minebot.extreme_energy.energy.IEnergyTransport;
import ru.minebot.extreme_energy.init.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class EnergySender extends TileEntity implements IEnergyStorage, IEnergyProvider, ITickable {

    protected EnumFacing[] outFaces = EnumFacing.values();

    protected List<Receiver> receivers = new ArrayList<>();
    protected int energy;
    protected int capacity;
    protected int maxExtract;
    protected int maxReceive;

    @Override
    public void update() {
        if (!world.isRemote && world.getTotalWorldTime()%10==0) {
            if (receivers.size() != 0){
                for (Receiver receiver : receivers){
                    int maxExtract = 100000/receiver.cableCount;
                    for (EnumFacing facing : receiver.connects){
                        int reallyMaxExtract = Math.round(extractEnergy(receiver.receiver.receiveEnergy(facing, extractEnergy(maxExtract, true), true), true)/(float)receiver.connects.size());
                        extractEnergy(receiver.receiver.receiveEnergy(facing, reallyMaxExtract, false), false);
                    }
                    IBlockState from = world.getBlockState(getPos());
                    IBlockState to = world.getBlockState(receiver.pos);
                    world.notifyBlockUpdate(getPos(), from, from, 0);
                    world.notifyBlockUpdate(receiver.pos, to, to, 0);
                    //NetworkHandler.sendToAllAround(new PacketTransportEnergy(getPos(), receiver.pos, maxExtract), new NetworkRegistry.TargetPoint(0, getPos().getX(), getPos().getY(), getPos().getZ(), 100));
                }
            }
        }
    }

    @Override
    public void onLoad() {
        calculateEnergyNetwork();
    }

    public void calculateEnergyNetwork(){
        if (!world.isRemote) {
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

    protected List<Vertex> getVertexes(List<Vertex> list, BlockPos pos, List<BlockPos> checked){
        checked.add(pos);
        boolean isCable = world.getBlockState(pos).getBlock() == ModBlocks.cable || (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof IEnergyTransport);
        List<Node> nodes = getNodes(pos);
        list.add(new Vertex(pos, nodes, isCable));
        if (isCable || checked.size() == 1)
            for (int i = 0; i < nodes.size(); i++)
                if (!checked.contains(nodes.get(i).pos))
                    getVertexes(list, nodes.get(i).pos, checked);
        return list;
    }

    protected List<Node> getNodes(BlockPos pos){
        List<Node> result = new ArrayList<>();
        EnumFacing[] localFaces = pos.equals(getPos()) ? outFaces : EnumFacing.values();
        for (int i = 0; i < localFaces.length; i++) {
            BlockPos newPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ()).offset(localFaces[i]);
            List<BlockPos> list = new ArrayList<>();
            list.add(pos);
            Node node = getNode(newPos, list, 0);
            if (node != null)
                result.add(node);
        }
        return result;
    }

    protected Node getNode(BlockPos pos, List<BlockPos> checked, int count){
        count++;
        checked.add(pos);
        TileEntity teOriginal = world.getTileEntity(pos);
        Block blockOriginal = world.getBlockState(pos).getBlock();
        if (blockOriginal == ModBlocks.cable){
            int connections = BlockCable.getConnectionCount(world, pos);
            if (connections == 2){
                for (int i = 0; i < EnumFacing.values().length; i++) {
                    BlockPos newPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ()).offset(EnumFacing.values()[i]);
                    TileEntity te = world.getTileEntity(newPos);
                    Block block = world.getBlockState(newPos).getBlock();
                    if (!checked.contains(newPos) && ((te != null && te instanceof IEnergyReceiver && ((IEnergyReceiver) te).canConnectEnergy(EnumFacing.getFacingFromVector(newPos.getX()-pos.getX(), newPos.getY()-pos.getY(), newPos.getZ()-pos.getZ()).getOpposite())) || block == ModBlocks.cable))
                        return getNode(newPos, checked, count);
                }
            }
            else if (connections == 1)
                return null;
            else {
                return new Node(pos, count, true);
            }
        }
        else if (teOriginal != null && teOriginal instanceof IEnergyReceiver){
            return new Node(pos, count, false);
        }
        return null;
    }

    protected int getPath(List<Vertex> vertexes, BlockPos start, BlockPos finish){
        boolean[] u = new boolean[vertexes.size()];
        int[] d = new int[vertexes.size()];
        for (int i = 0; i < vertexes.size(); i++)
            d[i] = vertexes.get(i).pos.equals(start) ? 0 : Integer.MAX_VALUE;

        int index = -1;
        while (!isAllTrue(u)){
            index = getMin(vertexes, u, d);
            u[index] = true;
            Vertex selected = vertexes.get(index);
            for (int i = 0; i < selected.connects.size(); i++){
                int h = getIndexOfArray(vertexes, selected.connects.get(i).pos);
                if (h != -1)
                    d[h] = Math.min(d[h], d[index] + selected.connects.get(i).distance);
            }
        }

        int end = -1;
        for (int i = 0; i < vertexes.size(); i++)
            if (vertexes.get(i).pos.equals(finish)) {
                end = i;
                break;
            }


        return d[end];
    }

    protected boolean isConnected(List<BlockPos> checked, List<Vertex> vertexes, BlockPos pos){
        checked.add(pos);
        if (world.getBlockState(pos).getBlock() != ModBlocks.cable && (world.getTileEntity(pos) == null || !(world.getTileEntity(pos) instanceof IEnergyReceiver)))
            return false;

        for (Vertex vertex : vertexes)
            if (vertex.pos.equals(pos))
                return true;

        boolean result = false;
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos newPos = pos.offset(facing);
            if (!result && !checked.contains(newPos))
                result = isConnected(checked, vertexes, newPos);
        }
        return result;
    }

    private int getIndexOfArray(List<Vertex> vertexes, BlockPos pos){
        for (int i = 0; i < vertexes.size(); i++)
            if (vertexes.get(i).pos.equals(pos))
                return i;
        return -1;
    }

    private int getMin(List<Vertex> vertexes, boolean[] u, int[] d){
        int min = Integer.MAX_VALUE;
        int r = -1;
        for (int i = 0; i < vertexes.size(); i++)
            if (!u[i] && d[i] < min){
                min = d[i];
                r = i;
            }
        return r;
    }

    private boolean isAllTrue(boolean[] array){
        boolean result = true;
        for (boolean a : array)
            if (!a)
                result = false;
        return result;
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return true;
    }

    @Override
    public int getEnergyStored(EnumFacing from) {
        return energy;
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return capacity;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energyReceived = Math.min(capacity - getEnergyStored(), Math.min(this.maxReceive, maxReceive));

        if (!simulate) {
            energy += energyReceived;
        }

        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));

        if (!simulate) {
            energy -= energyExtracted;
        }

        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        category.setInteger("energy", energy);
        category.setInteger("capacity", capacity);
        category.setInteger("maxExtract", maxExtract);
        category.setInteger("maxReceive", maxReceive);
        nbt.setTag(ExtremeEnergy.NBT_CATEGORY, category);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        energy = category.getInteger("energy");
        capacity = category.getInteger("capacity");
        maxExtract = category.getInteger("maxExtract");
        maxReceive = category.getInteger("maxReceive");
    }

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        return extractEnergy(maxExtract, simulate);
    }

    protected class Vertex {
        public boolean isCable;
        public BlockPos pos;
        public List<Node> connects;

        public Vertex(BlockPos pos, List<Node> connects, boolean isCable) {
            this.pos = pos;
            this.connects = connects;
            this.isCable = isCable;
        }
    }

    protected class Node {
        public boolean isCable;
        public BlockPos pos;
        public int distance;

        public Node(BlockPos pos, int distance, boolean isCable) {
            this.pos = pos;
            this.distance = distance;
            this.isCable = isCable;
        }
    }

    protected class Receiver {
        public BlockPos pos;
        public IEnergyReceiver receiver;
        public int cableCount;
        public List<EnumFacing> connects;

        public Receiver(IEnergyReceiver receiver, BlockPos pos, int cableCount, List<EnumFacing> connects) {
            this.receiver = receiver;
            this.cableCount = cableCount;
            this.pos = pos;
            this.connects = connects;
        }
    }
}
