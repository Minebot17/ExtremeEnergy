package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import ru.minebot.extreme_energy.energy.IFrequencyHandler;
import ru.minebot.extreme_energy.network.AbstractPacket;

public class PacketFrequency extends AbstractPacket{
    protected BlockPos pos;
    protected int frequency;

    public PacketFrequency(){}

    public PacketFrequency(BlockPos pos, int frequency){
        this.pos = pos;
        this.frequency = frequency;
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(frequency);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        frequency = buf.readInt();
        int m, n, b;
        m = buf.readInt();
        n = buf.readInt();
        b = buf.readInt();
        pos = new BlockPos(m, n, b);
    }

    @Override
    public void clientHandler(EntityPlayer player) {
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
        TileEntity t = player.world.getTileEntity(pos);
        if (t != null && t instanceof IFrequencyHandler) {
            IFrequencyHandler te = (IFrequencyHandler)t;
            te.setFrequency(frequency);
        }
    }
}
