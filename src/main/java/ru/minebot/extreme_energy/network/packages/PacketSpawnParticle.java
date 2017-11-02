package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.AbstractPacket;

public class PacketSpawnParticle extends AbstractPacket {
    protected int type;
    protected float x;
    protected float y;
    protected float z;
    protected float xSpeed;
    protected float ySpeed;
    protected float zSpeed;
    protected int count;
    protected float randomXPos;
    protected float randomXMotion;
    protected float randomYPos;
    protected float randomYMotion;
    protected float randomZPos;
    protected float randomZMotion;

    public PacketSpawnParticle(){}

    public PacketSpawnParticle(int type, Vec3d pos, Vec3d speed, int count){
        this(type, (float)pos.x, (float)pos.y, (float)pos.z, (float)speed.x, (float)speed.y, (float)speed.z, count, 0, 0);
    }

    public PacketSpawnParticle(int type, float x, float y, float z, float xSpeed, float ySpeed, float zSpeed, int count){
        this(type, x, y, z, xSpeed, ySpeed, zSpeed, count, 0, 0);
    }

    public PacketSpawnParticle(int type, float x, float y, float z, float xSpeed, float ySpeed, float zSpeed, int count, float randomPos, float randomMotion){
        this(type, x, y, z, xSpeed, ySpeed, zSpeed, count, randomPos, randomMotion, randomPos, randomMotion, randomPos, randomMotion);
    }

    public PacketSpawnParticle(int type, float x, float y, float z, float xSpeed, float ySpeed, float zSpeed, int count, float randomXPos, float randomXMotion, float randomYPos, float randomYMotion, float randomZPos, float randomZMotion) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.zSpeed = zSpeed;
        this.count = count;
        this.randomXPos = randomXPos;
        this.randomXMotion = randomXMotion;
        this.randomYPos = randomYPos;
        this.randomYMotion = randomYMotion;
        this.randomZPos = randomZPos;
        this.randomZMotion = randomZMotion;
    }


    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(type);
        buf.writeInt(count);
        buf.writeFloat(x);
        buf.writeFloat(y);
        buf.writeFloat(z);
        buf.writeFloat(xSpeed);
        buf.writeFloat(ySpeed);
        buf.writeFloat(zSpeed);
        buf.writeFloat(randomXPos);
        buf.writeFloat(randomYPos);
        buf.writeFloat(randomZPos);
        buf.writeFloat(randomXMotion);
        buf.writeFloat(randomYMotion);
        buf.writeFloat(randomZMotion);
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        type = buf.readInt();
        count = buf.readInt();
        x = buf.readFloat();
        y = buf.readFloat();
        z = buf.readFloat();
        xSpeed = buf.readFloat();
        ySpeed = buf.readFloat();
        zSpeed = buf.readFloat();
        randomXPos = buf.readFloat();
        randomYPos = buf.readFloat();
        randomZPos = buf.readFloat();
        randomXMotion = buf.readFloat();
        randomYMotion = buf.readFloat();
        randomZMotion = buf.readFloat();
    }

    @Override
    public void clientHandler(EntityPlayer player) {
        if (player != null && player.world != null)
            ModUtils.spawnParticles(
                    player.world,
                    type,
                    x, y, z,
                    xSpeed, ySpeed, zSpeed,
                    count,
                    randomXPos, randomXMotion,
                    randomYPos, randomYMotion,
                    randomZPos, randomZMotion
            );
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
    }
}
