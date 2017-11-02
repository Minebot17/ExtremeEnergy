package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import ru.minebot.extreme_energy.network.AbstractPacket;

public class PacketPlaySound extends AbstractPacket{
    protected int ID;
    protected float x;
    protected float y;
    protected float z;

    public PacketPlaySound(){}

    public PacketPlaySound(int ID, float x, float y, float z) {
        this.ID = ID;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(ID);
        buf.writeFloat(x);
        buf.writeFloat(y);
        buf.writeFloat(z);
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        ID = buf.readInt();
        x = buf.readFloat();
        y = buf.readFloat();
        z = buf.readFloat();
    }

    @Override
    public void clientHandler(EntityPlayer player) {
        player.world.playSound(x, y, z, SoundEvent.REGISTRY.getObjectById(ID), SoundCategory.PLAYERS, 1, 1, false);
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
    }
}
