package ru.minebot.extreme_energy.init;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModConfig {
    public static boolean isClient;
    public static Configuration config;
    public static int portalParticleID;
    public static boolean showFrequencyWaila;
    public static boolean explodeMachines;
    public static boolean nuclearExplosionReactor;
    public static boolean poweredCrystals;
    public static boolean randomChunkCharge;
    public static int maxCapOfChunk = 10000000;
    public static int maxTeleportRadius;

    public static boolean enableGeneration;
    public static GenerationSetting smallCristalOreSetting;
    public static GenerationSetting cristalOreSetting;
    public static GenerationSetting bigCristalOreSetting;
    public static GenerationSetting copperOreSetting;
    public static GenerationSetting uraniumOreSetting;

    public static void register(FMLPreInitializationEvent e, boolean isClient){
        ModConfig.isClient = isClient;
        config = new Configuration(e.getSuggestedConfigurationFile());
        config.load();

        portalParticleID = config.getInt("portalParticleID", "ids", 203, 1, 256, getName("config.portalParticleID"), "config.portalParticleID.name");
        showFrequencyWaila = config.getBoolean("showFrequencyWaila", "general", true, getName("config.showFrequencyWaila"), "config.showFrequencyWaila.name");
        explodeMachines = config.getBoolean("explodeMachines", "general", true, getName("config.explodeMachines"), "config.explodeMachines.name");
        nuclearExplosionReactor = config.getBoolean("nuclearExplosionReactor", "general", true, getName("config.nuclearExplosionReactor"), "config.nuclearExplosionReactor.name");
        poweredCrystals = config.getBoolean("poweredCrystals", "general", true, getName("config.poweredCrystals"), "config.poweredCrystals.name");
        randomChunkCharge = config.getBoolean("randomChunkCharge", "general", false, getName("config.randomChunkCharge"), "config.randomChunkCharge.name");
        //maxCapOfChunk = config.getInt("maxCapOfChunk", "general", 10000000, 100000, 100000000, getName("config.maxCapOfChunk"), "config.maxCapOfChunk.name");
        maxTeleportRadius = config.getInt("maxTeleportRadius", "general", 100, 1, 100, getName("config.maxTeleportRadius"), "config.maxTeleportRadius.name");


        enableGeneration = config.getBoolean("enableGeneration", "generation", true, getName("config.enableGeneration"), "config.enableGeneration.name");
        smallCristalOreSetting = new GenerationSetting("smallCristalOre",
                3,1,10,
                10,0,100,
                30,0,100,
                60,0,100
        );
        cristalOreSetting = new GenerationSetting("cristalOre",
                3,1,10,
                5,0,100,
                15,0,100,
                40,0,100
        );
        bigCristalOreSetting = new GenerationSetting("bigCristalOre",
                3,1,10,
                2,0,100,
                0,0,100,
                15,0,100
        );
        copperOreSetting = new GenerationSetting("copperOre",
                3,1,10,
                10,0,100,
                20,0,100,
                50,0,100
        );
        uraniumOreSetting = new GenerationSetting("uraniumOre",
                3,1,10,
                15,0,100,
                2,0,100,
                30,0,100
        );

        config.save();
    }

    private static String getName(String name){
        return isClient ? I18n.format(name) : name;
    }

    public static class GenerationSetting {
        public int count;
        public int chance;
        public int minHeight;
        public int maxHeight;

        public GenerationSetting(int count, int chance, int minHeight, int maxHeight) {
            this.count = count;
            this.chance = chance;
            this.minHeight = minHeight;
            this.maxHeight = maxHeight;
        }

        public GenerationSetting(String name, int countD, int countMin, int countMax, int chanceD, int chanceMin, int chanceMax, int minHeightD, int minHeightMin, int minHeightMax, int maxHeightD, int maxHeightMin, int maxHeightMax){
            String str = name + "_BlockCount";
            count = config.getInt(str, "generation", countD, countMin, countMax, getName("config." + str), "config." + str + ".name");
            str = name + "_Chance";
            chance = config.getInt(str, "generation", chanceD, chanceMin, chanceMax, getName("config." + str), "config." + str + ".name");
            str = name + "_MinHeight";
            minHeight = config.getInt(str, "generation", minHeightD, minHeightMin, minHeightMax, getName("config." + str), "config." + str + ".name");
            str = name + "_MaxHeight";
            maxHeight = config.getInt(str, "generation", maxHeightD, maxHeightMin, maxHeightMax, getName("config." + str), "config." + str + ".name");
        }
    }
}
