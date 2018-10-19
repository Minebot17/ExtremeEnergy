package ru.minebot.extreme_energy.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkWrapper {
    public static NetworkWrapper instance = new NetworkWrapper("meemChanel");
    private final List<Class<? extends AbstractPacket>> ID_TO_PACKET = new ArrayList<>();
    private final Map<Class<? extends AbstractPacket>, Short> PACKET_TO_ID = new HashMap<>();

    private final FMLEventChannel channel;
    private final String          name;

    public NetworkWrapper(String name) {
        this.name = name;
        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(name);
        channel.register(this);
    }

    public void registerPacket(Class<? extends AbstractPacket> packet) {
        try {
            packet.getDeclaredConstructor();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (!PACKET_TO_ID.containsKey(packet)) {
            ID_TO_PACKET.add(packet);
            PACKET_TO_ID.put(packet, (short) (ID_TO_PACKET.size() - 1));
        }
    }

    @SubscribeEvent
    public void onServerPacket(FMLNetworkEvent.ServerCustomPacketEvent e)
    {
        if (!e.getPacket().channel().equals(name)) {
            return;
        }

        try {
            ByteBuf buf = e.getPacket().payload();
            AbstractPacket msg = ID_TO_PACKET.get(buf.readShort()).newInstance();
            msg.fromBytes(buf);
            msg.serverHandler(((NetHandlerPlayServer) e.getHandler()).player);
        }
        catch (Throwable exc) {
            exc.printStackTrace();
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent e)
    {
        if (!e.getPacket().channel().equals(name)) {
            return;
        }

        try {
            ByteBuf buf = e.getPacket().payload();
            AbstractPacket msg = ID_TO_PACKET.get(buf.readShort()).newInstance();
            msg.fromBytes(buf);
            msg.clientHandler(Minecraft.getMinecraft().player);
        }
        catch (Throwable exc) {
            exc.printStackTrace();
        }
    }

    private FMLProxyPacket createPacket(AbstractPacket msg)
    {
        Class clazz = msg.getClass();
        if (!PACKET_TO_ID.containsKey(clazz)) {
            System.out.println("Try to send unknow packet");
        }
        ByteBuf buf = msg.getOrCreateByteBuf();
        buf.writeShort(PACKET_TO_ID.get(clazz));
        msg.toBytes(buf);
        return new FMLProxyPacket(new PacketBuffer(buf), name);
    }

    public void sendToServer(AbstractPacket packet)
    {
        channel.sendToServer(createPacket(packet));
    }

    public void sendTo(AbstractPacket packet, EntityPlayerMP player)
    {
        channel.sendTo(createPacket(packet), player);
    }

    public void sendToAll(AbstractPacket packet)
    {
        channel.sendToAll(createPacket(packet));
    }

    public void sendToAllAround(AbstractPacket packet, NetworkRegistry.TargetPoint point)
    {
        channel.sendToAllAround(createPacket(packet), point);
    }

    public void sendToDimension(AbstractPacket packet, int dimensionId)
    {
        channel.sendToDimension(createPacket(packet), dimensionId);
    }
}
