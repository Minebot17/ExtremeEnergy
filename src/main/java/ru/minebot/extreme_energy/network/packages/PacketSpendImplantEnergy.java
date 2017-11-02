package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import ru.minebot.extreme_energy.capability.IImplant;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.network.AbstractPacket;
import ru.minebot.extreme_energy.other.ImplantData;

public class PacketSpendImplantEnergy extends AbstractPacket {
    protected int energy;

    public PacketSpendImplantEnergy(){}

    public PacketSpendImplantEnergy(int energy){
        this.energy = energy;
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(energy);
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        energy = buf.readInt();
    }

    @Override
    public void clientHandler(EntityPlayer player) {
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
        IImplant cap = player.getCapability(ImplantProvider.IMPLANT, null);
        if (cap.hasImplant()){
            ImplantData data = cap.getImplant();
            if (data.implant.getInteger("energy") > energy)
                data.implant.setInteger("energy", data.implant.getInteger("energy") - energy);
            cap.setImplant(data);
        }
    }
}
