package ru.minebot.extreme_energy;

public class Reference {
    public static final String MOD_ID = "meem";
    public static final String NAME = "Extreme Energy";
    public static final String VERSION = "1.1.7";
    public static final String ACCEPTED_VERSIONS = "[1.12.2]";

    public static final String CLIENT_PROXY_CLASS = "ru.minebot.extreme_energy.proxy.ClientProxy";
    public static final String SERVER_PROXY_CLASS = "ru.minebot.extreme_energy.proxy.ServerProxy";

    public enum ExtremeEnergyItems{
        SMALLCRYSTAL("smallCrystal", "ItemSmallCrystal"),
        CRYSTAL("crystal", "ItemCrystal"),
        BIGCRYSTAL("bigCrystal", "ItemBigCrystal"),
        SMALLCAPACITOR("smallCapacitor", "ItemSmallCapacitor"),
        CAPACITOR("capacitor", "ItemCapacitor"),
        BIGCAPACITOR("bigCapacitor", "ItemBigCapacitor"),
        CREATIVECAPACITOR("creativeCapacitor", "ItemCreativeCapacitor"),
        COPPERINGOT("copperIngot", "ItemCopperIngot"),
        HEAVYINGOT("heavyIngot", "ItemHeavyIngot"),
        COPPERDUST("copperDust", "ItemCopperDust"),
        COPPERNUGGET("copperNugget", "ItemCopperNugget"),
        IRONDUST("ironDust", "ItemIronDust"),
        GOLDDUST("goldDust", "ItemGoldDust"),
        COALDUST("coalDust", "ItemCoalDust"),
        HEAVYDUST("heavyDust", "ItemHeavyDust"),
        IDCARD("idCard", "ItemIdCard"),
        FREQUENCYINSTALLER("frequencyInstaller", "ItemFrequencyInstaller"),
        CHARGEDETECTOR("chargeDetector", "ItemChargeDetector"),
        TABLET("tablet", "ItemTablet"),
        ENERGYBALANCER("energyBalancer", "ItemEnergyBalancer"),
        FULLCAPSULE("fullCapsule", "ItemFullCapsule"),
        URANIUM("uranium", "ItemUranium"),
        URANIUMDUST("uraniumDust", "ItemUraniumDust"),
        CALIFORNIUM("californium", "ItemCalifornium"),
        NUCLEARFUEL("nuclearFuel", "ItemNuclearFuel"),
        POWERSUPPRESSOR("powerSuppressor", "ItemPowerSuppressor"),
        ANTENNA("antenna", "ItemAntenna"),
        CASE("case", "ItemCase"),
        COPPERBLANK("copperBlank", "ItemCopperBlank"),
        COPPERWIRES("copperWires", "ItemCopperWires"),
        ELECTRICALBOARD("electricalBoard", "ItemElectricalBoard"),
        ELECTRICCARVER("electricCarver", "ItemElectricCarver"),
        IRONBLANK("ironBlank", "ItemIronBlank"),
        IRONCYLINDER("ironCylinder", "ItemIronCylinder"),
        IRONROD("ironRod", "ItemIronRod"),
        PROCESSOR("processor", "ItemProcessor"),
        FORCEFIELDCOMPONENT("forceFieldComponent", "ItemForceFieldComponent"),
        HEAVYBLANK("heavyBlank", "ItemHeavyBlank"),
        HEAVYPLATE("heavyPlate", "ItemHeavyPlate"),
        CAPSULE("capsule", "ItemCapsule"),
        TOUGHIRONROD("toughIronRod", "ItemToughIronRod"),
        REINFORCEDHEAVYPLATE("reinforcedHeavyPlate", "ItemReinforcedHeavyPlate"),

        BASICCORE("basicCore", "ItemBasicCore"),
        ADVANCEDCORE("advancedCore", "ItemAdvancedCore"),
        EXTREMECORE("extremeCore", "ItemExtremeCore"),
        BASEIMPLANT("baseImplant", "ItemBaseImplant"),
        ADVANCEDIMPLANT("advancedImplant", "ItemAdvancedImplant"),
        EXTREMEIMPLANT("extremeImplant", "ItemExtremeImplant"),
        INJECTOR("injector", "ItemInjector"),
        FULLINJECTOR("fullInjector", "ItemFullInjector"),

        ENERGYTOOL("energyTool", "ItemEnergyTool"),
        ENERGYSWORD("energySword", "ItemEnergySword"),
        GRENADE("grenade", "ItemGrenade"),

        ENERGYARMOR("energyArmor", "ItemEnergyArmor"),
        HEAVYARMOR("heavyArmor", "ItemHeavyArmor"),

        MODULEINFOLINKS("linksInfoModule", "ItemLinksInfoModule"),
        MODULEINFOENTITY("entityInfoModule", "ItemEntityInfoModule"),
        MODULEINFOMAP("mapInfoModule", "ItemMapInfoModule"),
        MODULEINFOPATH("pathInfoModule", "ItemPathInfoModule"),

        MODULEENERGYHUNGRY("hungryEnergyModule", "ItemHungryEnergyModule"),
        MODULEENERGYMOTION("motionEnergyModule", "ItemMotionEnergyModule"),
        MODULEENERGYSUN("sunEnergyModule", "ItemSunEnergyModule"),
        MODULEENERGYCHUNK("chunkEnergyModule", "ItemChunkEnergyModule"),
        MODULEENERGYLAVA("lavaEnergyModule", "ItemLavaEnergyModule"),
        MODULEENERGYNUCLEAR("nuclearEnergyModule", "ItemNuclearEnergyModule"),

        MODULEHEAT("heatModule", "ItemHeatModule"),
        MODULELIGHTNING("lightningModule", "ItemLightningModule"),
        MODULESHIELD("shieldModule", "ItemShieldModule"),
        MODULEWIND("windModule", "ItemWindModule"),
        MODULESECURITY("securityModule", "ItemSecurityModule"),
        MODULEWATER("waterModule", "ItemWaterModule"),
        MODULELAVA("lavaModule", "ItemLavaModule"),
        MODULETELEPORT("teleportModule", "ItemTeleportModule"),
        MODULEPLANT("plantModule", "ItemPlantModule"),
        MODULEELECTRIC("electricalModule", "ItemElectricalModule"),
        MODULEAGGRESSIVE("aggressiveModule", "ItemAggressiveModule"),
        MODULESTRANGE("strangeModule", "ItemStrangeModule"),
        MODULELUCK("luckModule", "ItemLuckModule"),
        MODULELIFE("lifeModule", "ItemLifeModule"),
        MODULEVOLTAGE("voltageModule", "ItemVoltageModule"),
        MODULERADIUS("radiusModule", "ItemRadiusModule"),
        MODULECAPACITY("capacityModule", "ItemCapacityModule"),
        MODULECONDUCTION("conductionModule", "ItemConductionModule"),
        MODULELOSSREDUCTION("lossReductionModule", "ItemLossReductionModule");

        private String unlocalizedName;
        private String registryName;

        ExtremeEnergyItems(String unlocalizedName, String registryName){
            this.unlocalizedName = unlocalizedName;
            this.registryName = registryName;
        }

        public String getUnlocalizedName(){
            return unlocalizedName;
        }

        public String getRegistryName(){
            return registryName;
        }
    }

    public enum ExtremeEnergyBlocks{
        LIGHTNINGROD("lightningRod", "BlockLightningRod"),
        METALPILLAR("metalPillar", "BlockMetalPillar"),
        SHIELD("shield", "BlockShield"),
        CABLE("cable", "BlockCable"),
        SMALLCRYSTALORE("smallCrystalOre", "BlockSmallCrystalOre"),
        CRYSTALORE("crystalOre", "BlockCrystalOre"),
        BIGCRYSTALORE("bigCrystalOre", "BlockBigCrystalOre"),
        URANIUMORE("uraniumOre", "BlockUraniumOre"),
        COPPERORE("copperOre", "BlockCopperOre"),
        COPPER("copper", "BlockCopper"),
        HEAVYMETAL("heavyMetal", "BlockHeavyMetal"),
        HVG("hvg", "BlockHighVoltageGenerator"),
        HTF("htf", "BlockHighTemperatureFurnace"),
        HPC("hpc", "BlockHighPressureCrusher"),
        HPA("hpa", "BlockHighPrecisionAssembler"),
        HAS("has", "BlockHighAutomatedSawmill"),
        FT("ft", "BlockFieldTransmitter"),
        FTr("ftr", "BlockFieldTransducer"),
        SG("sg", "BlockStaticGenerator"),
        FG("fg", "BlockFuelGenerator"),
        TG("tg", "BlockThermalGenerator"),
        EE("ee", "BlockEarthingElectrode"),
        EB("eb", "BlockEnergyBattery"),
        CS("cs", "BlockChargeSensor"),
        NR("nr", "BlockNuclearReactor"),
        NB("nb", "BlockNuclearBomb"),
        FC("fc", "BlockFieldConverter");

        private String unlocalizedName;
        private String registryName;

        ExtremeEnergyBlocks(String unlocalizedName, String registryName){
            this.unlocalizedName = unlocalizedName;
            this.registryName = registryName;
        }

        public String getUnlocalizedName(){
            return unlocalizedName;
        }

        public String getRegistryName(){
            return registryName;
        }
    }
}