package ru.minebot.extreme_energy.particles;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticlePortal;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleCustomPortal extends ParticlePortal {

    protected ParticleCustomPortal(World worldIn, Vec3d pos, Vec3d speed) {
        super(worldIn, pos.x, pos.y, pos.z, speed.x, speed.y, speed.z);
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        float f = (float)this.particleAge / (float)this.particleMaxAge;
        float f1 = -f + f * f * 2.0F;
        float f2 = 1.0F - f1;
        posX += motionX * f2;
        posY += motionY * f2;
        posZ += motionZ * f2;

        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
        }
    }

    public static class Factory implements IParticleFactory {
        public Particle createParticle(int particleID, World worldIn, double xIn, double yIn, double zIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
            return new ParticleCustomPortal(worldIn, new Vec3d(xIn, yIn, zIn), new Vec3d(xSpeedIn, ySpeedIn, zSpeedIn));
        }
    }
}
