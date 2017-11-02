package ru.minebot.extreme_energy.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import ru.minebot.extreme_energy.init.LightningEvents;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketSpawnLightning;

import java.util.List;

public class EntityLightningChain extends Entity {
    protected Entity owner;
    protected Vec3d begin;
    protected Vec3d end;
    protected int maxAge;
    protected int damage;
    protected int time = 0;
    protected int delay;

    public EntityLightningChain(World world){super(world);}

    public EntityLightningChain(Entity owner, Vec3d begin, Vec3d end, int maxAge, int damage) {
        super(owner.world);
        this.owner = owner;
        this.begin = begin;
        this.end = end;
        this.maxAge = maxAge;
        this.damage = damage;
        this.delay = getType().time;
        setPositionAndUpdate(begin.x, begin.y, begin.z);
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public void onUpdate(){
        super.onUpdate();
        if (!world.isRemote && world.getTotalWorldTime()%delay==0) {
            ModUtils.addEnergyToChunk(world, new ChunkPos(getPosition()), damage*10);
            NetworkWrapper.instance.sendToAllAround(new PacketSpawnLightning(
                    begin, end, getType()
            ), new NetworkRegistry.TargetPoint(
                    0,
                    getPositionVector().x,
                    getPositionVector().y,
                    getPositionVector().z,
                    48
            ));
            List<Entity> list = world.getEntitiesInAABBexcluding(owner, new AxisAlignedBB(begin.addVector(-0.5f, -0.5f, -0.5f), end.addVector(0.5f, 0.5f, 0.5f)), input -> input instanceof EntityLivingBase);
            for (Entity entity : list)
                entity.attackEntityFrom(DamageSource.LIGHTNING_BOLT, damage);
            time += 5;
            if (time > maxAge)
                setDead();
        }
    }

    private LightningEvents.Type getType(){
        return  damage <= 2 ? LightningEvents.Type.TINY : damage <= 4 ? LightningEvents.Type.SMALL : damage <= 6 ? LightningEvents.Type.STANDART : LightningEvents.Type.BIG;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }
}
