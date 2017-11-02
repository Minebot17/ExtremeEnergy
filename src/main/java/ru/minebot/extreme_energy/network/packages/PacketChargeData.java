package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.ChunkPos;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.AbstractPacket;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.other.ChargeSaveData;

import java.util.HashMap;

public class PacketChargeData extends AbstractPacket {
    protected HashMap<ChunkPos, Integer> map;

    public PacketChargeData(){}

    public PacketChargeData(HashMap<ChunkPos, Integer> map){
        this.map = map;
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(map.size());
        for (ChunkPos pos : map.keySet()) {
            buf.writeInt(pos.x);
            buf.writeInt(pos.z);
            buf.writeInt(map.get(pos));
        }
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        map = new HashMap<>();
        int count = buf.readInt();
        for (int i = 0; i < count; i++)
            map.put(new ChunkPos(buf.readInt(), buf.readInt()), buf.readInt());
    }

    /*
        @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(map.size());
        int i = 0;
        for (HashMap<ChunkPos, Integer> hashMap : map.values()) {
            buf.writeInt(new ArrayList<>(map.keySet()).get(i));
            i++;
            buf.writeInt(hashMap.size());
            for (ChunkPos pos : hashMap.keySet()) {
                buf.writeInt(pos.x);
                buf.writeInt(pos.z);
                buf.writeInt(hashMap.get(pos));
            }
        }
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        map = new HashMap<>();
        int worldCount = buf.readInt();
        for (int i = 0; i < worldCount; i++) {
            int id = buf.readInt();
            HashMap<ChunkPos, Integer> hashMap = new HashMap<>();
            int count = buf.readInt();
            for (int j = 0; j < count; j++)
                hashMap.put(new ChunkPos(buf.readInt(), buf.readInt()), buf.readInt());
            map.put(id, hashMap);
        }
    }
     */

    @Override
    public void clientHandler(EntityPlayer player) {
        ModUtils.clientChunkCharge = map;
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
        ChargeSaveData data = ChargeSaveData.getOrCreateData(player.world);
        NetworkWrapper.instance.sendTo(new PacketChargeData(data.map), player);
    }
}
