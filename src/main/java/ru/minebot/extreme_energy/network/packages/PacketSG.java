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
import ru.minebot.extreme_energy.tile_entities.TileEntitySG;

public class PacketSG extends AbstractPacket {
    protected BlockPos pos;
    protected int power;

    public PacketSG(){}

    public PacketSG(BlockPos pos){
        this.pos = pos;
        TileEntitySG te = (TileEntitySG) Minecraft.getMinecraft().world.getTileEntity(pos);
        this.power = te.getPower();
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(power);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        power = buf.readInt();
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
        if (t != null && t instanceof TileEntitySG) {
            TileEntitySG te = (TileEntitySG)t;
            te.setPower(power);
        }
    }
}
