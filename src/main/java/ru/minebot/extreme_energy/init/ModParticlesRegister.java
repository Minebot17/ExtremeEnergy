package ru.minebot.extreme_energy.init;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.particles.ParticleCustomPortal;

public class ModParticlesRegister {

    public static void register(){
        Minecraft.getMinecraft().effectRenderer.registerParticle(ModConfig.portalParticleID, new ParticleCustomPortal.Factory());
    }
}
