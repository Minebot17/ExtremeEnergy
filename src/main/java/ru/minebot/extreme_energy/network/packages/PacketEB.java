package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.network.AbstractPacket;
import ru.minebot.extreme_energy.tile_entities.TileEntityEB;

public class PacketEB extends AbstractPacket {
    protected BlockPos pos;
    protected int extract;

    public PacketEB(){}

    @SideOnly(Side.CLIENT)
    public PacketEB(BlockPos pos){
        this.pos = pos;
        TileEntityEB te = (TileEntityEB) Minecraft.getMinecraft().world.getTileEntity(pos);
        this.extract = te.getExtract();
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(extract);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        extract = buf.readInt();
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
        if (t != null && t instanceof TileEntityEB) {
            TileEntityEB te = (TileEntityEB)t;
            te.setExtract(extract);
        }
    }
}
