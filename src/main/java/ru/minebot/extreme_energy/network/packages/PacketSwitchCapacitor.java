package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.AbstractPacket;

public class PacketSwitchCapacitor extends AbstractPacket {
    protected boolean state;

    public PacketSwitchCapacitor(){}

    public PacketSwitchCapacitor(boolean state){
        this.state = state;
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeBoolean(state);
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        state = buf.readBoolean();
    }

    @Override
    public void clientHandler(EntityPlayer player) {
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
        if (!player.inventory.getCurrentItem().isEmpty()) {
            NBTTagCompound tag = ModUtils.getNotNullCategory(player.inventory.getCurrentItem());
            tag.setBoolean("state", state);
        }
    }
}
