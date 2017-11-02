package ru.minebot.extreme_energy.init;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ru.minebot.extreme_energy.items.*;
import ru.minebot.extreme_energy.items.assembler.*;
import ru.minebot.extreme_energy.items.capacitors.ItemBigCapacitor;
import ru.minebot.extreme_energy.items.capacitors.ItemCapacitor;
import ru.minebot.extreme_energy.items.capacitors.ItemCreativeCapacitor;
import ru.minebot.extreme_energy.items.capacitors.ItemSmallCapacitor;
import ru.minebot.extreme_energy.items.crystals.*;
import ru.minebot.extreme_energy.items.equipment.*;
import ru.minebot.extreme_energy.items.implants.*;
import ru.minebot.extreme_energy.items.modules.*;

public class ModItems {
    public static Item
            energyHelmet,
            energyChestplate,
            energyLeggings,
            energyBoots,
            heavyHelmet,
            heavyChestplate,
            heavyLeggings,
            heavyBoots,

            energySword,
            energyTool,
            grenade,

            basicCore,
            advancedCore,
            extremeCore,
            baseImplant,
            advancedImplant,
            extremeImplant,
            injector,
            fullInjector,

            idCard,
            frequencyInstaller,
            chargeDetector,
            tablet,
            energyBalancer,
            fullCapsule,
            nuclearFuel,
            powerSuppressor,

            antenna,
            case_,
            copperBlank,
            copperWires,
            electricalBoard,
            electricCarver,
            ironBlank,
            ironCylinder,
            ironRod,
            processor,
            forceFieldComponent,
            heavyBlank,
            heavyPlate,
            capsule,
            toughIronRod,
            reinforcedHeavyPlate,

            smallCrystal,
            crystal,
            bigCrystal,
            smallCrystalActive,
            crystalActive,
            bigCrystalActive,

            californium,
            uranium,
            uraniumDust,
            copperIngot,
            copperDust,
            copperNugget,
            ironDust,
            goldDust,
            coalDust,
            heavyIngot,
            heavyDust,

            smallCapacitor,
            capacitor,
            bigCapacitor,
            creativeCapacitor,

            voltageModule,
            radiusModule,
            lossReduceModule,
            heatModule,
            capacityModule,
            conductionModule,
            lifeModule,
            electricalModule,
            aggressiveModule,
            strangeModule,
            luckModule,
            teleportModule,
            plantModule,
            waterModule,
            lavaModule,
            securityModule,
            windModule,
            shieldModule,
            lightningModule,
            hungerEnergyModule,
            motionEnergyModule,
            sunEnergyModule,
            chunkEnergyModule,
            lavaEnergyModule,
            nuclearEnergyModule,
            linksInfoModule,
            entityInfoModule,
            pathInfoModule,
            mapInfoModule;

    public static void init(){
        energyHelmet = new ItemEnergyArmor("Helmet", 1, EntityEquipmentSlot.HEAD);
        energyChestplate = new ItemEnergyArmor("Chestplate", 1, EntityEquipmentSlot.CHEST);
        energyLeggings = new ItemEnergyArmor("Leggings", 2, EntityEquipmentSlot.LEGS);
        energyBoots = new ItemEnergyArmor("Boots", 1, EntityEquipmentSlot.FEET);
        heavyHelmet = new ItemHeavyArmor("Helmet", 1, EntityEquipmentSlot.HEAD);
        heavyChestplate = new ItemHeavyArmor("Chestplate", 1, EntityEquipmentSlot.CHEST);
        heavyLeggings = new ItemHeavyArmor("Leggings", 2, EntityEquipmentSlot.LEGS);
        heavyBoots = new ItemHeavyArmor("Boots", 1, EntityEquipmentSlot.FEET);

        energySword = new ItemEnergySword();
        energyTool = new ItemEnergyTool();
        grenade = new ItemGrenade();

        basicCore = new ItemBasicCore();
        advancedCore = new ItemAdvancedCore();
        extremeCore = new ItemExtremeCore();
        baseImplant = new ItemBaseImplant();
        advancedImplant = new ItemAdvancedImplant();
        extremeImplant = new ItemExtremeImplant();
        injector = new ItemInjector();
        fullInjector = new ItemFullInjector();

        idCard = new ItemIdCard();
        frequencyInstaller = new ItemFrequencyInstaller();
        chargeDetector = new ItemChargeDetector();
        tablet = new ItemTablet();
        energyBalancer = new ItemEnergyBalancer();
        fullCapsule = new ItemFullCapsule();
        nuclearFuel = new ItemNuclearFuel();
        powerSuppressor = new ItemPowerSuppressor();

        antenna = new ItemAntenna();
        case_ = new ItemCase();
        copperBlank = new ItemCopperBlank();
        copperWires = new ItemCopperWires();
        electricalBoard = new ItemElectricalBoard();
        electricCarver = new ItemElectricalCarver();
        ironBlank = new ItemIronBlank();
        ironCylinder = new ItemIronCylinder();
        ironRod = new ItemIronRod();
        processor = new ItemProcessor();
        forceFieldComponent = new ItemForceFieldComponent();
        heavyBlank = new ItemHeavyBlank();
        heavyPlate = new ItemHeavyPlate();
        capsule = new ItemCapsule();
        toughIronRod = new ItemToughIronRod();
        reinforcedHeavyPlate = new ItemReinforcedHeavyPlate();

        smallCrystal = new ItemSmallCrystal();
        crystal = new ItemCrystal();
        bigCrystal = new ItemBigCrystal();
        smallCrystalActive = new ItemSmallCrystalActive();
        crystalActive = new ItemCrystalActive();
        bigCrystalActive = new ItemBigCrystalActive();

        californium = new ItemCalifornium();
        uranium = new ItemUranium();
        uraniumDust = new ItemUraniumDust();
        copperIngot = new ItemCopperIngot();
        copperDust = new ItemCopperDust();
        copperNugget = new ItemCopperNugget();
        ironDust = new ItemIronDust();
        goldDust = new ItemGoldDust();
        coalDust = new ItemCoalDust();
        heavyIngot = new ItemHeavyIngot();
        heavyDust = new ItemHeavyDust();

        smallCapacitor = new ItemSmallCapacitor(250000, 500, 500);
        capacitor = new ItemCapacitor(1500000, 10000, 10000);
        bigCapacitor = new ItemBigCapacitor(5000000, 100000, 100000);
        creativeCapacitor = new ItemCreativeCapacitor();

        voltageModule = new ItemVoltageModule();
        radiusModule = new ItemRadiusModule();
        lossReduceModule = new ItemLossReductionModule();
        heatModule = new ItemHeatModule();
        capacityModule = new ItemCapacityModule();
        conductionModule = new ItemConductionModule();
        lifeModule = new ItemLifeModule();
        electricalModule = new ItemElectricalModule();
        aggressiveModule = new ItemAggressiveModule();
        strangeModule = new ItemStrangeModule();
        luckModule = new ItemLuckModule();
        teleportModule = new ItemTeleportModule();
        plantModule = new ItemPlantModule();
        waterModule = new ItemWaterModule();
        lavaModule = new ItemLavaModule();
        securityModule = new ItemSecurityModule();
        windModule = new ItemWindModule();
        shieldModule = new ItemShieldModule();
        lightningModule = new ItemLightningModule();
        hungerEnergyModule = new ItemHungerEnergyModule();
        motionEnergyModule = new ItemMotionEnergyModule();
        sunEnergyModule = new ItemSunEnergyModule();
        chunkEnergyModule = new ItemChunkEnergyModule();
        lavaEnergyModule = new ItemLavaEnergyModule();
        nuclearEnergyModule = new ItemNuclearEnergyModule();
        linksInfoModule = new ItemLinksInfoModule();
        entityInfoModule = new ItemEntityInfoModule();
        pathInfoModule = new ItemPathInfoModule();
        mapInfoModule = new ItemMapInfoModule();
    }

    public static void register(){
        ForgeRegistries.ITEMS.register(energyHelmet);
        ForgeRegistries.ITEMS.register(energyChestplate);
        ForgeRegistries.ITEMS.register(energyLeggings);
        ForgeRegistries.ITEMS.register(energyBoots);
        ForgeRegistries.ITEMS.register(heavyHelmet);
        ForgeRegistries.ITEMS.register(heavyChestplate);
        ForgeRegistries.ITEMS.register(heavyLeggings);
        ForgeRegistries.ITEMS.register(heavyBoots);

        ForgeRegistries.ITEMS.register(energySword);
        ForgeRegistries.ITEMS.register(energyTool);
        ForgeRegistries.ITEMS.register(grenade);

        ForgeRegistries.ITEMS.register(basicCore);
        ForgeRegistries.ITEMS.register(advancedCore);
        ForgeRegistries.ITEMS.register(extremeCore);
        ForgeRegistries.ITEMS.register(baseImplant);
        ForgeRegistries.ITEMS.register(advancedImplant);
        ForgeRegistries.ITEMS.register(extremeImplant);
        ForgeRegistries.ITEMS.register(injector);
        ForgeRegistries.ITEMS.register(fullInjector);

        ForgeRegistries.ITEMS.register(idCard);
        ForgeRegistries.ITEMS.register(frequencyInstaller);
        ForgeRegistries.ITEMS.register(chargeDetector);
        ForgeRegistries.ITEMS.register(tablet);
        ForgeRegistries.ITEMS.register(energyBalancer);
        ForgeRegistries.ITEMS.register(fullCapsule);
        ForgeRegistries.ITEMS.register(nuclearFuel);
        ForgeRegistries.ITEMS.register(powerSuppressor);

        ForgeRegistries.ITEMS.register(antenna);
        ForgeRegistries.ITEMS.register(case_);
        ForgeRegistries.ITEMS.register(copperBlank);
        ForgeRegistries.ITEMS.register(copperWires);
        ForgeRegistries.ITEMS.register(electricalBoard);
        ForgeRegistries.ITEMS.register(electricCarver);
        ForgeRegistries.ITEMS.register(ironBlank);
        ForgeRegistries.ITEMS.register(ironCylinder);
        ForgeRegistries.ITEMS.register(ironRod);
        ForgeRegistries.ITEMS.register(processor);
        ForgeRegistries.ITEMS.register(forceFieldComponent);
        ForgeRegistries.ITEMS.register(heavyBlank);
        ForgeRegistries.ITEMS.register(heavyPlate);
        ForgeRegistries.ITEMS.register(capsule);
        ForgeRegistries.ITEMS.register(toughIronRod);
        ForgeRegistries.ITEMS.register(reinforcedHeavyPlate);

        ForgeRegistries.ITEMS.register(smallCrystal);
        ForgeRegistries.ITEMS.register(crystal);
        ForgeRegistries.ITEMS.register(bigCrystal);
        ForgeRegistries.ITEMS.register(smallCrystalActive);
        ForgeRegistries.ITEMS.register(crystalActive);
        ForgeRegistries.ITEMS.register(bigCrystalActive);

        ForgeRegistries.ITEMS.register(californium);
        ForgeRegistries.ITEMS.register(uranium);
        ForgeRegistries.ITEMS.register(uraniumDust);
        ForgeRegistries.ITEMS.register(copperIngot);
        ForgeRegistries.ITEMS.register(copperDust);
        ForgeRegistries.ITEMS.register(copperNugget);
        ForgeRegistries.ITEMS.register(ironDust);
        ForgeRegistries.ITEMS.register(goldDust);
        ForgeRegistries.ITEMS.register(coalDust);
        ForgeRegistries.ITEMS.register(heavyIngot);
        ForgeRegistries.ITEMS.register(heavyDust);

        ForgeRegistries.ITEMS.register(smallCapacitor);
        ForgeRegistries.ITEMS.register(capacitor);
        ForgeRegistries.ITEMS.register(bigCapacitor);
        ForgeRegistries.ITEMS.register(creativeCapacitor);

        ForgeRegistries.ITEMS.register(voltageModule);
        ForgeRegistries.ITEMS.register(radiusModule);
        ForgeRegistries.ITEMS.register(lossReduceModule);
        ForgeRegistries.ITEMS.register(heatModule);
        ForgeRegistries.ITEMS.register(capacityModule);
        ForgeRegistries.ITEMS.register(conductionModule);
        ForgeRegistries.ITEMS.register(lifeModule);
        ForgeRegistries.ITEMS.register(electricalModule);
        ForgeRegistries.ITEMS.register(aggressiveModule);
        ForgeRegistries.ITEMS.register(strangeModule);
        ForgeRegistries.ITEMS.register(luckModule);
        ForgeRegistries.ITEMS.register(teleportModule);
        ForgeRegistries.ITEMS.register(plantModule);
        ForgeRegistries.ITEMS.register(waterModule);
        ForgeRegistries.ITEMS.register(lavaModule);
        ForgeRegistries.ITEMS.register(securityModule);
        ForgeRegistries.ITEMS.register(windModule);
        ForgeRegistries.ITEMS.register(shieldModule);
        ForgeRegistries.ITEMS.register(lightningModule);
        ForgeRegistries.ITEMS.register(hungerEnergyModule);
        ForgeRegistries.ITEMS.register(motionEnergyModule);
        ForgeRegistries.ITEMS.register(sunEnergyModule);
        ForgeRegistries.ITEMS.register(chunkEnergyModule);
        ForgeRegistries.ITEMS.register(lavaEnergyModule);
        ForgeRegistries.ITEMS.register(nuclearEnergyModule);
        ForgeRegistries.ITEMS.register(linksInfoModule);
        ForgeRegistries.ITEMS.register(entityInfoModule);
        ForgeRegistries.ITEMS.register(pathInfoModule);
        ForgeRegistries.ITEMS.register(mapInfoModule);
    }

    public static void registerRenders(){
        registerRender(energyHelmet);
        registerRender(energyChestplate);
        registerRender(energyLeggings);
        registerRender(energyBoots);
        registerRender(heavyHelmet);
        registerRender(heavyChestplate);
        registerRender(heavyLeggings);
        registerRender(heavyBoots);

        registerRenderMeta(energySword, 2);
        registerRenderMeta(energyTool, 6);
        registerRender(grenade);

        registerRender(basicCore);
        registerRender(advancedCore);
        registerRender(extremeCore);
        registerRender(baseImplant);
        registerRender(advancedImplant);
        registerRender(extremeImplant);
        registerRender(injector);
        registerRender(fullInjector);

        registerRender(idCard);
        registerRender(frequencyInstaller);
        registerRender(chargeDetector);
        registerRender(tablet);
        registerRender(energyBalancer);
        registerRender(fullCapsule);
        registerRender(nuclearFuel);
        registerRender(powerSuppressor);

        registerRender(antenna);
        registerRender(case_);
        registerRender(copperBlank);
        registerRender(copperWires);
        registerRender(electricalBoard);
        registerRender(electricCarver);
        registerRender(ironBlank);
        registerRender(ironCylinder);
        registerRender(ironRod);
        registerRender(processor);
        registerRender(forceFieldComponent);
        registerRender(heavyBlank);
        registerRender(heavyPlate);
        registerRender(capsule);
        registerRender(toughIronRod);
        registerRender(reinforcedHeavyPlate);

        registerRender(smallCrystal);
        registerRender(crystal);
        registerRender(bigCrystal);
        registerRender(smallCrystalActive);
        registerRender(crystalActive);
        registerRender(bigCrystalActive);

        registerRender(californium);
        registerRender(uranium);
        registerRender(uraniumDust);
        registerRender(copperIngot);
        registerRender(copperDust);
        registerRender(copperNugget);
        registerRender(ironDust);
        registerRender(goldDust);
        registerRender(coalDust);
        registerRender(heavyIngot);
        registerRender(heavyDust);

        registerRender(smallCapacitor);
        registerRender(capacitor);
        registerRender(bigCapacitor);
        registerRender(creativeCapacitor);

        registerRender(voltageModule);
        registerRender(radiusModule);
        registerRender(lossReduceModule);
        registerRender(heatModule);
        registerRender(capacityModule);
        registerRender(conductionModule);
        registerRender(lifeModule);
        registerRender(electricalModule);
        registerRender(aggressiveModule);
        registerRender(strangeModule);
        registerRender(luckModule);
        registerRender(teleportModule);
        registerRender(plantModule);
        registerRender(waterModule);
        registerRender(lavaModule);
        registerRender(securityModule);
        registerRender(windModule);
        registerRender(shieldModule);
        registerRender(lightningModule);
        registerRender(hungerEnergyModule);
        registerRender(motionEnergyModule);
        registerRender(sunEnergyModule);
        registerRender(chunkEnergyModule);
        registerRender(lavaEnergyModule);
        registerRender(nuclearEnergyModule);
        registerRender(linksInfoModule);
        registerRender(entityInfoModule);
        registerRender(pathInfoModule);
        registerRender(mapInfoModule);
    }

    private static void registerRender(Item item){
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    private static void registerRenderMeta(Item item, int metaCount){
        ResourceLocation[] res = new ResourceLocation[metaCount];
        for (int i = 0; i < metaCount; i++)
            res[i] =  new ResourceLocation(item.getRegistryName().toString()+i);
        ModelLoader.setCustomMeshDefinition(item, stack -> new ModelResourceLocation(new ResourceLocation(stack.getItem().getRegistryName().toString()+stack.getMetadata()), "inventory"));
        ModelBakery.registerItemVariants(item, res);
    }
}
