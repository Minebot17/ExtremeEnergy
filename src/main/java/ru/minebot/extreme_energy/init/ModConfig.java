package ru.minebot.extreme_energy.init;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModConfig {
    public static boolean isClient;
    public static Configuration config;
    public static int portalParticleID;
    public static boolean explodeMachines;
    public static boolean nuclearExplosionReactor;
    public static int maxCapOfChunk = 10000000;
    public static int maxTeleportRadius;

    public static void register(FMLPreInitializationEvent e, boolean isClient){
        ModConfig.isClient = isClient;
        config = new Configuration(e.getSuggestedConfigurationFile());
        config.load();

        portalParticleID = config.getInt("portalParticleID", "ids", 203, 1, 256, getName("config.portalParticleID"), "config.portalParticleID.name");
        explodeMachines = config.getBoolean("explodeMachines", "general", true, getName("config.explodeMachines"), "config.explodeMachines.name");
        nuclearExplosionReactor = config.getBoolean("nuclearExplosionReactor", "general", true, getName("config.nuclearExplosionReactor"), "config.nuclearExplosionReactor.name");
        //maxCapOfChunk = config.getInt("maxCapOfChunk", "general", 10000000, 100000, 100000000, getName("config.maxCapOfChunk"), "config.maxCapOfChunk.name");
        maxTeleportRadius = config.getInt("maxTeleportRadius", "general", 100, 1, 100, getName("config.maxTeleportRadius"), "config.maxTeleportRadius.name");

        config.save();
    }

    private static String getName(String name){
        return isClient ? I18n.format(name) : name;
    }
}
