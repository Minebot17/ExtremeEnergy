package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import ru.minebot.extreme_energy.network.AbstractPacket;
import ru.minebot.extreme_energy.other.IPublicPrivateProvider;

public class PacketPublicPrivate extends AbstractPacket {
    protected BlockPos pos;
    protected boolean isPublic;

    public PacketPublicPrivate(){}

    public PacketPublicPrivate(BlockPos pos, boolean isPublic){
        this.pos = pos;
        this.isPublic = isPublic;
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeBoolean(isPublic);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        isPublic = buf.readBoolean();
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
        try {
            IPublicPrivateProvider te = (IPublicPrivateProvider) player.world.getTileEntity(pos);
            if (isPublic)
                te.setPublic();
            else
                te.setPrivate();
        }
        catch (NullPointerException e){e.printStackTrace();}
    }
}
