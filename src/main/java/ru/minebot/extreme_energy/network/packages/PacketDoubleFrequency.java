package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.energy.FieldTransmitterStandart;
import ru.minebot.extreme_energy.energy.IFrequencyHandler;
import ru.minebot.extreme_energy.network.AbstractPacket;

public class PacketDoubleFrequency extends AbstractPacket {
    protected BlockPos pos;
    protected int frequency;
    protected int frequencyReceive;

    public PacketDoubleFrequency(){}

    public PacketDoubleFrequency(BlockPos pos){
        this.pos = pos;
        FieldTransmitterStandart transmitterStandart = (FieldTransmitterStandart) Minecraft.getMinecraft().world.getTileEntity(pos);
        this.frequency = transmitterStandart.getFrequency();
        this.frequencyReceive = transmitterStandart.getFrequencyReceive();
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(frequencyReceive);
        buf.writeInt(frequency);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        frequencyReceive = buf.readInt();
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
            FieldTransmitterStandart te = (FieldTransmitterStandart)t;
            te.setFrequency(frequency);
            te.setFrequencyReceive(frequencyReceive);
        }
    }
}
