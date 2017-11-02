package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ru.minebot.extreme_energy.energy.IFrequencyHandler;
import ru.minebot.extreme_energy.network.AbstractPacket;

public class PacketEntityMotion extends AbstractPacket {
    protected int id;
    protected Vec3d motion;

    public PacketEntityMotion(){}

    public PacketEntityMotion(Entity entity){
        this.id = entity.getEntityId();
        this.motion = new Vec3d(entity.motionX, entity.motionY, entity.motionZ);
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(id);
        buf.writeDouble(motion.x);
        buf.writeDouble(motion.y);
        buf.writeDouble(motion.z);
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        id = buf.readInt();
        double m, n, b;
        m = buf.readDouble();
        n = buf.readDouble();
        b = buf.readDouble();
        motion = new Vec3d(m, n, b);
    }

    @Override
    public void clientHandler(EntityPlayer player) {
        Entity entity = player.world.getEntityByID(id);
        if (entity != null) {
            entity.motionX = motion.x;
            entity.motionY = motion.y;
            entity.motionZ = motion.z;
        }
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
    }
}
