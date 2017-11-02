package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import ru.minebot.extreme_energy.init.ModConfig;
import ru.minebot.extreme_energy.network.AbstractPacket;

public class PacketConfig extends AbstractPacket {
    private boolean explodeMachines;
    private boolean nuclearExplosionReactor;
    private boolean showFrequencyWaila;
    private int maxCapOfChunk;
    private int maxTeleportRadius;

    public PacketConfig(){
        explodeMachines = ModConfig.explodeMachines;
        nuclearExplosionReactor = ModConfig.nuclearExplosionReactor;
        maxCapOfChunk = ModConfig.maxCapOfChunk;
        maxTeleportRadius = ModConfig.maxTeleportRadius;
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeBoolean(explodeMachines);
        buf.writeBoolean(nuclearExplosionReactor);
        buf.writeBoolean(showFrequencyWaila);
        buf.writeInt(maxCapOfChunk);
        buf.writeInt(maxTeleportRadius);
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        explodeMachines = buf.readBoolean();
        nuclearExplosionReactor = buf.readBoolean();
        showFrequencyWaila = buf.readBoolean();
        maxCapOfChunk = buf.readInt();
        maxTeleportRadius = buf.readInt();
    }

    @Override
    public void clientHandler(EntityPlayer player) {
        ModConfig.explodeMachines = explodeMachines;
        ModConfig.nuclearExplosionReactor = nuclearExplosionReactor;
        ModConfig.maxCapOfChunk = maxCapOfChunk;
        ModConfig.maxTeleportRadius = maxTeleportRadius;
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
    }
}
