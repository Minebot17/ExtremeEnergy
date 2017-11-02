package ru.minebot.extreme_energy.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketSpawnParticle;

import java.util.List;

public class EntityWindCharge extends Entity {

    protected int time;
    protected EntityLivingBase owner;
    protected Vec3d vector;
    protected float speed;
    protected int damage;
    protected EnumParticleTypes particle;

    public EntityWindCharge(World world){super(world);}

    public EntityWindCharge(EntityLivingBase owner, Vec3d vector, float speed, int damage, EnumParticleTypes particle) {
        super(owner.world);
        this.owner = owner;
        this.vector = vector;
        this.speed = speed;
        this.damage = damage;
        this.particle = particle;
        time = (int)(10f/(float)speed);
        setPositionAndUpdate(owner.getPosition().getX() + vector.x, owner.getPosition().getY() + vector.y, owner.getPosition().getZ() + vector.z);
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public void onUpdate(){
        super.onUpdate();
        setPositionAndUpdate(posX + vector.x * speed, posY + vector.y * speed, posZ + vector.z * speed);
        NetworkWrapper.instance.sendToAllAround(new PacketSpawnParticle(
                particle.getParticleID(),
                (float)getPositionVector().x,
                (float)getPositionVector().y,
                (float)getPositionVector().z,
                0.1f, 0.1f, 0.1f,
                10, 0.2f, 0.05f
        ), new NetworkRegistry.TargetPoint(
                0,
                getPositionVector().x,
                getPositionVector().y,
                getPositionVector().z,
                32
        ));
        time--;
        if (time <= 0)
            setDead();
        List<EntityLivingBase> baseList = world.getEntities(EntityLivingBase.class, input -> ModUtils.inRadius(getPosition(), input.getPosition(), 1));
        for (EntityLivingBase base : baseList)
            base.attackEntityFrom(DamageSource.MAGIC, damage);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }
}
