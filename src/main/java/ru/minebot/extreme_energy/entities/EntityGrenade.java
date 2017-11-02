package ru.minebot.extreme_energy.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import ru.minebot.extreme_energy.init.LightningEvents;
import ru.minebot.extreme_energy.init.ModConfig;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketSpawnLightning;
import ru.minebot.extreme_energy.network.packages.PacketSpawnParticle;

import java.util.ConcurrentModificationException;
import java.util.List;

public class EntityGrenade extends Entity{
    protected EntityLivingBase owner;
    protected Vec3d vector;
    protected int age;

    private boolean stop;

    public EntityGrenade(World world){ super(world); }

    public EntityGrenade(EntityLivingBase owner, Vec3d vector) {
        super(owner.world);
        this.owner = owner;
        this.vector = vector;
        age = 0;
        Vec3d pos = owner.getPositionVector();
        setPositionAndUpdate(pos.x, pos.y + 1f, pos.z);
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public void onUpdate(){
        super.onUpdate();
        if (vector == null)
            setDead();
        if (isDead)
            return;

        age++;
        if (age < 100) {
            if (!stop) {
                RayTraceResult ray = world.rayTraceBlocks(getPositionVector(), vector.add(getPositionVector()), false, true, false);
                if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
                    if (ray.sideHit == EnumFacing.DOWN) {
                        setPositionAndUpdate(ray.hitVec.x, ray.hitVec.y - 0.05f, ray.hitVec.z);
                        vector = new Vec3d(vector.x, -vector.y, vector.z);
                    } else if (ray.sideHit == EnumFacing.UP) {
                        setPositionAndUpdate(ray.hitVec.x, ray.hitVec.y + 0.05f, ray.hitVec.z);
                        stop = true;
                    } else {
                        setPositionAndUpdate(ray.hitVec.x, ray.hitVec.y, ray.hitVec.z);
                        vector = new Vec3d(-vector.x, vector.y, -vector.z);
                    }
                } else
                    setPositionAndUpdate(posX + vector.x, posY + vector.y, posZ + vector.z);

                vector = new Vec3d(vector.x * 0.95f, Math.max(vector.y - 0.05f, -2), vector.z * 0.95f);
            }
        }
        else if (age == 100){
            NetworkWrapper.instance.sendToAllAround(new PacketSpawnParticle(EnumParticleTypes.SPELL_INSTANT.getParticleID(),
                    (float)posX, (float)posY, (float)posZ,
                    0.1f, 0.1f, 0.1f,
                    50, 0, 0.1f
            ), new NetworkRegistry.TargetPoint(world.provider.getDimension(), posX, posY, posZ, 32));
            world.playSound((float)posX, (float)posY, (float)posZ, SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.MASTER, 2, 1, false);
        }
        else if (age < 120){
            setPositionAndUpdate(posX, posY + 0.025f, posZ);
        }
        else if (age < 300){
            if (age%10==0) {
                try {
                    List<EntityLivingBase> entities = world.getEntities(EntityLivingBase.class, input -> {
                        if (input == null)
                            return false;

                        RayTraceResult ray = world.rayTraceBlocks(getPositionVector(), input.getPositionVector(), false, true, false);
                        return (ray == null || ray.typeOfHit == RayTraceResult.Type.MISS) && ModUtils.random.nextInt(2) == 0 && ModUtils.inRadius(getPosition(), input.getPosition(), 5);
                    });

                    for (EntityLivingBase entity : entities)
                        if (owner == null || entity != owner) {
                            NetworkWrapper.instance.sendToAllAround(new PacketSpawnLightning(getPositionVector().addVector(0, 0.16f, 0), entity.getPositionVector(), LightningEvents.Type.STANDART),
                                    new NetworkRegistry.TargetPoint(world.provider.getDimension(), posX, posY, posZ, 32));
                            entity.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("meem:electricShock"), 40, 2));
                        }
                }
                catch (ConcurrentModificationException e) { e.printStackTrace(); }
            }
        }
        else {
            world.createExplosion(this, posX, posY, posZ, 1, true);
            setDead();
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        age = compound.getInteger("age");
        stop = compound.getBoolean("stop");
        if (compound.getBoolean("beOwner")) {
            int id =  compound.getInteger("owner");
            Entity entity = world.getEntityByID(id);
            owner = entity != null ? (EntityLivingBase) entity : null;
        }
        else
            owner = null;
        vector = new Vec3d(compound.getDouble("vecX"), compound.getDouble("vecY"), compound.getDouble("vecZ"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("age", age);
        compound.setBoolean("stop", stop);
        compound.setBoolean("beOwner", owner != null);
        if (owner != null)
            compound.setInteger("owner", owner.getEntityId());
        compound.setDouble("vecX", vector.x);
        compound.setDouble("vecY", vector.y);
        compound.setDouble("vecZ", vector.z);
    }
}
