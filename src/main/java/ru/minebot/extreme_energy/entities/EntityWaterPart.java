package ru.minebot.extreme_energy.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketSpawnParticle;

import java.util.List;

public class EntityWaterPart extends Entity {
    protected EntityLivingBase owner;
    protected Vec3d vector;
    protected double gravity;
    protected float speed;
    protected int time = 0;
    protected int maxAge = 200;

    public EntityWaterPart(World world){super(world);}

    public EntityWaterPart(EntityLivingBase owner, Vec3d vector, float speed, float gravity) {
        super(owner.world);
        this.owner = owner;
        this.vector = vector;
        this.speed = speed;
        this.gravity = gravity;
        time = 0;
        setPositionAndUpdate(owner.getPosition().getX() + vector.x + ModUtils.random.nextFloat()-0.5f, owner.getPosition().getY() + vector.y + ModUtils.random.nextFloat()-0.5f, owner.getPosition().getZ() + vector.z + ModUtils.random.nextFloat()-0.5f);
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public void onUpdate(){
        super.onUpdate();
        setPositionAndUpdate(posX + vector.x * speed, posY + vector.y * speed, posZ + vector.z * speed);
        vector.addVector(vector.x*-0.001, -gravity, vector.y*-0.001);
        NetworkWrapper.instance.sendToAllAround(new PacketSpawnParticle(
                EnumParticleTypes.WATER_SPLASH.getParticleID(),
                (float)getPositionVector().x,
                (float)getPositionVector().y,
                (float)getPositionVector().z,
                0.1f, 0.1f, 0.1f,
                2, 0.2f, 0.05f
        ), new NetworkRegistry.TargetPoint(
                0,
                getPositionVector().x,
                getPositionVector().y,
                getPositionVector().z,
                32
        ));
        time++;
        if (time > maxAge)
            setDead();
        if (world.getTotalWorldTime()%4==0) {
            List<EntityLivingBase> baseList = world.getEntities(EntityLivingBase.class, input -> ModUtils.inRadius(getPosition(), input.getPosition(), 1));
            for (EntityLivingBase base : baseList)
                base.extinguish();

            RayTraceResult ray = world.rayTraceBlocks(getPositionVector(), getPositionVector().add(new Vec3d(vector.x, vector.y, vector.z)), true, true, false);
            if (ray != null) {
                for (int x = -1; x <= 1; x++)
                    for (int y = -1; y <= 1; y++)
                        for (int z = -1; z <= 1; z++) {
                            BlockPos newPos = new BlockPos(x+posX, y+posY, z+posZ);
                            if (world.getBlockState(newPos).getBlock() == Blocks.FIRE)
                                world.setBlockToAir(newPos);
                        }
                setDead();
            }
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }
}
