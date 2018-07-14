package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.energy.IFrequencyHandler;
import ru.minebot.extreme_energy.energy.IRadiusHandler;
import ru.minebot.extreme_energy.network.AbstractPacket;

public class PacketTileRadius extends AbstractPacket {
    protected BlockPos pos;
    protected int radius;

    public PacketTileRadius(){}

    public PacketTileRadius(BlockPos pos){
        this.pos = pos;
        IRadiusHandler handler = (IRadiusHandler) Minecraft.getMinecraft().world.getTileEntity(pos);
        this.radius = handler.getRadius();
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(radius);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        radius = buf.readInt();
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
        if (t != null && t instanceof IRadiusHandler) {
            IRadiusHandler te = (IRadiusHandler)t;
            te.setRadius(radius);
        }
    }
}
