package ru.minebot.extreme_energy.network.packages;

import cofh.redstoneflux.api.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import ru.minebot.extreme_energy.network.AbstractPacket;

public class PacketTransportEnergy extends AbstractPacket {
    protected BlockPos from;
    protected BlockPos to;
    protected int maxExtract;

    public PacketTransportEnergy(){}

    public PacketTransportEnergy(BlockPos from, BlockPos to, int maxExtract) {
        this.from = from;
        this.to = to;
        this.maxExtract = maxExtract;
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(maxExtract);
        buf.writeInt(from.getX());
        buf.writeInt(from.getY());
        buf.writeInt(from.getZ());
        buf.writeInt(to.getX());
        buf.writeInt(to.getY());
        buf.writeInt(to.getZ());
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        maxExtract = buf.readInt();
        int m, n, b, k, l, f;
        m = buf.readInt();
        n = buf.readInt();
        b = buf.readInt();
        k = buf.readInt();
        l = buf.readInt();
        f = buf.readInt();
        from = new BlockPos(m, n, b);
        to = new BlockPos(k, l, f);
    }

    @Override
    public void clientHandler(EntityPlayer player) {
        IEnergyProvider from = (IEnergyProvider) player.world.getTileEntity(this.from);
        IEnergyReceiver to = (IEnergyReceiver) player.world.getTileEntity(this.to);
        to.receiveEnergy(EnumFacing.UP, from.extractEnergy(EnumFacing.UP, maxExtract, false), false);
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
    }
}
