package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.capability.IImplant;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.items.implants.Implant;
import ru.minebot.extreme_energy.network.AbstractPacket;
import ru.minebot.extreme_energy.other.ImplantData;

public class PacketImplantData extends AbstractPacket {
    protected ImplantData data;
    protected boolean toRemove;

    public PacketImplantData(){}

    public PacketImplantData(EntityPlayer player){
        data = player.getCapability(ImplantProvider.IMPLANT, null).getImplant();
        toRemove = data == null;
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeBoolean(toRemove);
        if (!toRemove) {
            buf.writeInt(data.type);
            ByteBufUtils.writeTag(buf, data.modules);
            ByteBufUtils.writeTag(buf, data.implant);
            ByteBufUtils.writeTag(buf, data.core);
            buf.writeInt(data.playerID);
        }
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        if (!buf.readBoolean())
            data = new ImplantData(buf.readInt(), ByteBufUtils.readTag(buf), ByteBufUtils.readTag(buf), ByteBufUtils.readTag(buf), buf.readInt());
    }

    @Override
    public void clientHandler(EntityPlayer player) {
        IImplant cap = player.getCapability(ImplantProvider.IMPLANT, null);
        if (toRemove)
            cap.removeImplant();
        else
            cap.setImplant(data);
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
    }
}
