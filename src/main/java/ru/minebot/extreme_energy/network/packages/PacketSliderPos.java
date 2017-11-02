package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.AbstractPacket;

public class PacketSliderPos extends AbstractPacket {
    protected int sliderPos;

    public PacketSliderPos(){}

    public PacketSliderPos(int sliderPos){
        this.sliderPos = sliderPos;
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(sliderPos);
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        sliderPos = buf.readInt();
    }

    @Override
    public void clientHandler(EntityPlayer player) {
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
        if (!player.inventory.getCurrentItem().isEmpty()) {
            NBTTagCompound tag = ModUtils.getNotNullCategory(player.inventory.getCurrentItem());
            tag.setInteger("sliderPos", sliderPos);
        }
    }
}
