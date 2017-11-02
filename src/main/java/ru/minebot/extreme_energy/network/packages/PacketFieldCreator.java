package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import ru.minebot.extreme_energy.energy.IFieldCreatorEnergy;
import ru.minebot.extreme_energy.energy.IRadiusHandler;
import ru.minebot.extreme_energy.network.AbstractPacket;

public class PacketFieldCreator extends AbstractPacket {
    protected BlockPos pos;
    protected int voltage;
    protected int radius;

    public PacketFieldCreator(){}

    public PacketFieldCreator(BlockPos pos, int voltage, int radius){
        this.pos = pos;
        this.voltage = voltage;
        this.radius = radius;
    }

    @Override
    public void toBytes(final ByteBuf buf) {

        buf.writeInt(radius);
        buf.writeInt(voltage);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        radius = buf.readInt();
        voltage = buf.readInt();
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
            try {
                IFieldCreatorEnergy te = (IFieldCreatorEnergy)t;
                if (radius <= te.getMaxRadius() && voltage <= te.getMaxVoltage()) {
                    te.setRadius(radius);
                    te.setVoltage(voltage);
                }
            } catch (ClassCastException e) {
                IRadiusHandler te = (IRadiusHandler) t;
                if (radius <= te.getMaxRadius())
                    te.setRadius(radius);
            }
        }
    }
}
