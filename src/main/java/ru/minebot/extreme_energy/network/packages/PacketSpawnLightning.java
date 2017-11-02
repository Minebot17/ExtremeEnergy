package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import ru.minebot.extreme_energy.init.LightningEvents;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.AbstractPacket;
import ru.minebot.extreme_energy.network.NetworkWrapper;

public class PacketSpawnLightning extends AbstractPacket {
    protected Vec3d from;
    protected Vec3d to;
    protected LightningEvents.Type type;

    public PacketSpawnLightning(){}

    public PacketSpawnLightning(Vec3d from, Vec3d to, LightningEvents.Type type){
        this.from = new Vec3d((float)from.x, (float)from.y, (float)from.z);
        this.to = new Vec3d((float)to.x, (float)to.y, (float)to.z);
        this.type = type;
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeDouble(from.x);
        buf.writeDouble(from.y);
        buf.writeDouble(from.z);
        buf.writeDouble(to.x);
        buf.writeDouble(to.y);
        buf.writeDouble(to.z);
        buf.writeInt(type.getID());
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        from = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        to = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        type = LightningEvents.Type.getType(buf.readInt());
    }

    @Override
    public void clientHandler(EntityPlayer player) {
        LightningEvents.spawnLightning(player.world, new Vec3d(from.x, from.y, from.z), new Vec3d(to.x, to.y, to.z), type);
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
        NetworkWrapper.instance.sendToAllAround(new PacketSpawnLightning(new Vec3d(from.x, from.y, from.z), new Vec3d(to.x, to.y, to.z), type), ModUtils.getTargetPoint(player, 64));
    }
}
