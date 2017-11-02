package ru.minebot.extreme_energy.entities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ru.minebot.extreme_energy.init.LightningEvents;
import ru.minebot.extreme_energy.init.ModConfig;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.ItemPowerSuppressor;
import ru.minebot.extreme_energy.particles.ParticleCustomPortal;

import javax.annotation.Nullable;
import java.util.List;

public class EntityNuclearBomb extends EntityTNTPrimed {
    private EntityItem item;
    private int timer = 0;

    public EntityNuclearBomb(World world){
        super(world);
        setFuse(200);
    }

    public EntityNuclearBomb(World world, EntityLivingBase owner, double x, double y, double z) {
        super(world, x, y, z, owner);
        setFuse(200);
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (!this.hasNoGravity()) {
            this.motionY -= 0.03999999910593033D;
        }

        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
            this.motionY *= -0.5D;
        }

        setFuse(getFuse()-1);

        if (getFuse() <= 0) {
            List<EntityItem> entity = world.getEntities(EntityItem.class, input -> ModUtils.inRadius(getPosition(), input.getPosition(), 5) && input.getItem().getItem() instanceof ItemPowerSuppressor);
            if (entity.size() != 0) {
                if (item == null || item.isDead)
                    item = (EntityItem) ModUtils.getNearestEntity(entity, getPosition());
                timer++;
                powerSuppress();
            }
            else if (!this.world.isRemote) {
                setDead();
                ModUtils.createNuclearExplosion(world, getPosition(), true);
            }
        }
        else {
            this.handleWaterMovement();
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    protected void powerSuppress(){
        if (world.isRemote){
            if (world.getTotalWorldTime()%5==0) {
                world.playSound(getPosition().getX(), getPosition().getY(), getPosition().getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 1f, 1, false);
                ModUtils.spawnParticles(world, EnumParticleTypes.EXPLOSION_LARGE.getParticleID(),
                        (float) getPositionVector().x,
                        (float) getPositionVector().y,
                        (float) getPositionVector().z,
                        0, 0, 0, 1, 0.5f, 0
                );
            }
            if (world.getTotalWorldTime()%10==0){
                LightningEvents.spawnLightning(world, getPositionVector(), item.getPositionVector(), LightningEvents.Type.SMALL);
            }

            Vec3d beginPos = getPositionVector().addVector(ModUtils.random.nextFloat()*2f-1f, ModUtils.random.nextFloat()*2f-1f, ModUtils.random.nextFloat()*2f-1f);
            Vec3d speed = item.getPositionVector().addVector(0, 0.3f, 0).subtract(beginPos).normalize().scale(0.1f);

            Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(ModConfig.portalParticleID, beginPos.x, beginPos.y, beginPos.z, speed.x, speed.y, speed.z);
        }
        if (timer > 200) {
            if (!world.isRemote)
                world.spawnEntity(new EntityItem(world, getPositionVector().x, getPositionVector().y+0.2f, getPositionVector().z, new ItemStack(ModItems.californium, ModUtils.random.nextInt(3)+1)));
            item.setDead();
            setDead();
        }
    }

    protected void writeEntityToNBT(NBTTagCompound compound) {
        if (!isDead) {
            compound.setShort("Fuse", (short) this.getFuse());
            compound.setInteger("Timer", timer);
            if (item != null)
                compound.setInteger("Item", item.getEntityId());
        }
    }

    protected void readEntityFromNBT(NBTTagCompound compound) {
        if (!isDead) {
            this.setFuse(compound.getShort("Fuse"));
            timer = compound.getInteger("Timer");
            item = (EntityItem) world.getEntityByID(compound.getInteger("Item"));
        }
    }
}
