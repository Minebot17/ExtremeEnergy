package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.items.implants.Implant;
import ru.minebot.extreme_energy.network.AbstractPacket;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.other.ImplantData;

public class PacketImplantDataWhenJoin extends AbstractPacket {
    protected ImplantData data;

    public PacketImplantDataWhenJoin(){}

    public PacketImplantDataWhenJoin(ImplantData data){
        this.data = data;
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        if (data != null) {
            buf.writeBoolean(true);
            buf.writeInt(data.type);
            ByteBufUtils.writeTag(buf, data.modules);
            ByteBufUtils.writeTag(buf, data.implant);
            ByteBufUtils.writeTag(buf, data.core);
            buf.writeInt(data.playerID);
        }
        else
            buf.writeBoolean(false);
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        if (buf.readBoolean())
            data = new ImplantData(buf.readInt(), ByteBufUtils.readTag(buf), ByteBufUtils.readTag(buf), ByteBufUtils.readTag(buf), buf.readInt());
    }

    @Override
    public void clientHandler(EntityPlayer player) {
        player.getCapability(ImplantProvider.IMPLANT, null).setImplant(data);
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
        ImplantData data = player.getCapability(ImplantProvider.IMPLANT, null).getImplant();
        if (data != null)
            NetworkWrapper.instance.sendTo(new PacketImplantDataWhenJoin(data), player);
    }
}
