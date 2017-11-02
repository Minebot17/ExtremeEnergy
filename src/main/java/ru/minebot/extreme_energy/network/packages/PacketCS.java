package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import ru.minebot.extreme_energy.energy.IFrequencyHandler;
import ru.minebot.extreme_energy.network.AbstractPacket;
import ru.minebot.extreme_energy.tile_entities.TileEntityCS;

public class PacketCS extends AbstractPacket {
    protected BlockPos pos;
    protected int mode;
    protected int bound;

    public PacketCS(){}

    public PacketCS(BlockPos pos, int mode, int bound){
        this.pos = pos;
        this.mode = mode;
        this.bound = bound;
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(mode);
        buf.writeInt(bound);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        mode = buf.readInt();
        bound = buf.readInt();
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
        if (t != null && t instanceof TileEntityCS && player.getPosition().getDistance(t.getPos().getX(), t.getPos().getY(), t.getPos().getZ()) < 5) {
            TileEntityCS te = (TileEntityCS) t;
            te.bound = bound;
            te.mode = mode;
        }
    }
}
