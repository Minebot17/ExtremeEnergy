package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.AbstractPacket;

public class PacketFrequencyItem extends AbstractPacket{
    protected int frequency;

    public PacketFrequencyItem(){}

    public PacketFrequencyItem(int frequency){
        this.frequency = frequency;
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(frequency);
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        frequency = buf.readInt();
    }

    @Override
    public void clientHandler(EntityPlayer player) {
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
        if (!player.inventory.getCurrentItem().isEmpty()) {
            NBTTagCompound tag = ModUtils.getNotNullCategory(player.inventory.getCurrentItem());
            tag.setInteger("frequency", frequency);
        }
    }
}
