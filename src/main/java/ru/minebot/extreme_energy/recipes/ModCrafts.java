package ru.minebot.extreme_energy.recipes;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import static net.minecraftforge.fml.common.registry.GameRegistry.*;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import ru.minebot.extreme_energy.init.ModBlocks;
import ru.minebot.extreme_energy.init.ModItems;

public class ModCrafts {

    public static void register(){
        // C - copper, H - heavy ingot, S - small crystal, M - crystal, B - big crystal, K - cable
        //<editor-fold desc="Blocks">
        //<editor-fold desc="Generators">
        addShapedRecipe(new ResourceLocation("meem:sg"), null, new ItemStack(ModBlocks.sg), "CHC", "VSV", "CHC", 'C', ModItems.copperIngot, 'S', ModItems.smallCrystal, 'V', ModItems.voltageModule, 'H', ModItems.heavyIngot);
        addShapedRecipe(new ResourceLocation("meem:fg"), null, new ItemStack(ModBlocks.fg), "CHC", "VSV", "CHC", 'C', ModItems.copperIngot, 'S', ModItems.smallCrystal, 'V', ModItems.ironRod, 'H', ModItems.heavyIngot);
        addShapedRecipe(new ResourceLocation("meem:tg"), null, new ItemStack(ModBlocks.tg), "CHC", "VSV", "CHC", 'C', ModItems.copperIngot, 'S', ModItems.smallCrystal, 'V', ModItems.ironCylinder, 'H', ModItems.heavyIngot);
        addShapedRecipe(new ResourceLocation("meem:nr"), null, new ItemStack(ModBlocks.nr), "CHC", "VBV", "CHC", 'C', ModItems.copperIngot, 'B', ModItems.bigCrystal, 'V', ModItems.heatModule, 'H', ModItems.reinforcedHeavyPlate);
        //</editor-fold>
        //<editor-fold desc="Basic">
        addShapedRecipe(new ResourceLocation("meem:hpa"), null, new ItemStack(ModBlocks.hpa), "CCC", "CSC", "HHH", 'C', ModItems.copperIngot, 'S', ModItems.smallCrystal, 'H', ModItems.heavyIngot);
        addShapedRecipe(new ResourceLocation("meem:hvg"), null, new ItemStack(ModBlocks.hvg), "HAH", "PMP", "VJV", 'A', ModItems.antenna, 'M', ModItems.crystal, 'H', ModItems.heavyIngot, 'P', ModItems.processor, 'V', ModItems.voltageModule, 'J', ModItems.heavyPlate);
        addShapedRecipe(new ResourceLocation("meem:htf"), null, new ItemStack(ModBlocks.htf), "CAC", "RMR", "CHC", 'C', ModItems.copperIngot, 'M', ModItems.crystal, 'H', ModItems.heavyIngot, 'R', ModItems.ironRod, 'A', ModItems.antenna);
        addShapedRecipe(new ResourceLocation("meem:hpc"), null, new ItemStack(ModBlocks.hpc), "CAC", "HMH", "CHC", 'C', ModItems.copperIngot, 'M', ModItems.crystal, 'H', ModItems.heavyIngot, 'A', ModItems.antenna);
        addShapedRecipe(new ResourceLocation("meem:fc"), null, new ItemStack(ModBlocks.fc), "XAX", "VSV", "HJH", 'X', Items.AIR, 'V', ModItems.voltageModule, 'S', ModItems.smallCrystal, 'H', ModItems.heavyIngot, 'A', ModItems.antenna, 'J', ModItems.heavyPlate);
        //</editor-fold>
        //<editor-fold desc="Extra">
        addShapedRecipe(new ResourceLocation("meem:ft"), null, new ItemStack(ModBlocks.ft), "AGA", "HBH", "HPH", 'G', Blocks.GLASS, 'B', ModItems.bigCrystal, 'H', ModItems.heavyIngot, 'A', ModItems.antenna, 'P', ModItems.processor);
        addShapedRecipe(new ResourceLocation("meem:ftr"), null, new ItemStack(ModBlocks.ftr), "HAH", "KBK", "HPH", 'K', ModBlocks.cable, 'B', ModItems.bigCrystal, 'H', ModItems.heavyIngot, 'A', ModItems.antenna, 'P', ModItems.processor);
        addShapedRecipe(new ResourceLocation("meem:ee"), null, new ItemStack(ModBlocks.ee), "CKC", "VXV", "CKC", 'X', Items.AIR, 'K', ModBlocks.cable, 'C', ModItems.copperIngot, 'V', ModItems.voltageModule);
        addShapedRecipe(new ResourceLocation("meem:cs"), null, new ItemStack(ModBlocks.cs), "ISI", "IRI", "IEI", 'I', Items.IRON_INGOT, 'S', ModItems.smallCrystal, 'R', Blocks.REDSTONE_BLOCK, 'E', ModItems.lossReduceModule);
        addShapedRecipe(new ResourceLocation("meem:eb"), null, new ItemStack(ModBlocks.eb), "CMC", "KUK", "CMC", 'M', ModItems.capacityModule, 'C', ModItems.copperIngot, 'U', ModItems.smallCapacitor, 'K', ModBlocks.cable);
        addShapedRecipe(new ResourceLocation("meem:nb"), null, new ItemStack(ModBlocks.nb), "IWI", "FFF", "IWI", 'I', Items.IRON_INGOT, 'F', ModItems.nuclearFuel, 'W', ModItems.copperWires);
        addShapedRecipe(new ResourceLocation("meem:lightningRod"), null, new ItemStack(ModBlocks.lightningRod), "XHX", "KMK", "HHH", 'X', Items.AIR, 'H', ModItems.heavyIngot, 'K', ModBlocks.cable, 'M', ModItems.crystal);
        addShapedRecipe(new ResourceLocation("meem:metalPillar"), null, new ItemStack(ModBlocks.metalPillar), "IKI", "IKI", "IKI", 'I', Items.IRON_INGOT, 'K', ModBlocks.cable);
        //</editor-fold>
        //<editor-fold desc="Other">
        addShapedRecipe(new ResourceLocation("meem:cable"), null, new ItemStack(ModBlocks.cable, 6), "III", "WRW", "III", 'I', Items.IRON_INGOT, 'W', ModItems.copperWires, 'R', Items.REDSTONE);
        addShapedRecipe(new ResourceLocation("meem:cable"), null, new ItemStack(ModBlocks.cable, 3), "GGG", "RRR", "GGG", 'G', Blocks.GLASS, 'R', Items.REDSTONE);
        addShapedRecipe(new ResourceLocation("meem:heavyMetal"), null, new ItemStack(ModBlocks.heavyMetal), "HHX", "HHX", "XXX", 'X', Items.AIR, 'H', ModItems.heavyIngot);
        addShapedRecipe(new ResourceLocation("meem:copper"), null, new ItemStack(ModBlocks.copper), "CCC", "CCC", "CCC", 'C', ModItems.copperIngot);
        //</editor-fold>
        //</editor-fold>
        //<editor-fold desc="Items">
        //<editor-fold desc="Implants">
        addShapedRecipe(new ResourceLocation("meem:baseImplant"), null, new ItemStack(ModItems.baseImplant), "SHS", "VUV", "HHH", 'H', ModItems.heavyIngot, 'V', ModItems.voltageModule, 'S', ModItems.smallCrystal, 'U', ModItems.smallCapacitor);
        addShapedRecipe(new ResourceLocation("meem:advancedImplant"), null, new ItemStack(ModItems.advancedImplant), "MHM", "VUV", "HHH", 'H', ModItems.heavyPlate, 'V', Items.DIAMOND, 'M', ModItems.crystal, 'U', ModItems.capacitor);
        addShapedRecipe(new ResourceLocation("meem:extremeImplant"), null, new ItemStack(ModItems.extremeImplant), "BHB", "VUV", "EHE", 'H', ModItems.reinforcedHeavyPlate, 'V', Items.EMERALD, 'B', ModItems.bigCrystal, 'U', ModItems.bigCapacitor, 'E', Items.ENDER_PEARL);
        addShapedRecipe(new ResourceLocation("meem:basicCore"), null, new ItemStack(ModItems.basicCore), "KKK", "HPH", "KKK", 'H', ModItems.heavyIngot, 'P', ModItems.processor, 'K', ModBlocks.cable);
        addShapedRecipe(new ResourceLocation("meem:advancedCore"), null, new ItemStack(ModItems.advancedCore), "KDK", "HPH", "KDK", 'H', ModItems.heavyIngot, 'D', Items.DIAMOND, 'P', ModItems.processor, 'K', ModBlocks.cable);
        addShapedRecipe(new ResourceLocation("meem:extremeCore"), null, new ItemStack(ModItems.extremeCore), "KDK", "HPH", "KDK", 'H', Items.ENDER_PEARL, 'D', Items.EMERALD, 'P', ModItems.processor, 'K', ModBlocks.cable);
        //</editor-fold>
        //<editor-fold desc="Capacitors">
        addShapedRecipe(new ResourceLocation("meem:smallCapacitor"), null, new ItemStack(ModItems.smallCapacitor), "IRI", "RSR", "IRI", 'I', Items.IRON_INGOT, 'R', Items.REDSTONE, 'S', ModItems.smallCrystal);
        addShapedRecipe(new ResourceLocation("meem:capacitor"), null, new ItemStack(ModItems.capacitor), "CUC", "GMG", "CUC", 'C', ModItems.copperIngot, 'G', Items.GOLD_INGOT, 'U', ModItems.smallCapacitor, 'M', ModItems.crystal);
        addShapedRecipe(new ResourceLocation("meem:bigCapacitor"), null, new ItemStack(ModItems.bigCapacitor), "HUH", "DMD", "HUH", 'H', ModItems.heavyIngot, 'D', Items.DIAMOND, 'U', ModItems.capacitor, 'M', ModItems.bigCrystal);
        //</editor-fold>
        //<editor-fold desc="Modules">
        addShapedRecipe(new ResourceLocation("meem:lifeModule"), null, new ItemStack(ModItems.lifeModule), "III", "SUS", "III", 'I', Items.IRON_INGOT, 'S', ModItems.smallCrystal, 'U', Items.GOLDEN_APPLE);
        addShapedRecipe(new ResourceLocation("meem:electricalModule"), null, new ItemStack(ModItems.electricalModule), "III", "SUS", "III", 'I', Items.IRON_INGOT, 'S', ModItems.smallCrystal, 'U', Items.GOLDEN_CARROT);
        addShapedRecipe(new ResourceLocation("meem:aggressiveModule"), null, new ItemStack(ModItems.aggressiveModule), "III", "SUS", "III", 'I', Items.IRON_INGOT, 'S', ModItems.smallCrystal, 'U', Items.GOLDEN_SWORD);
        addShapedRecipe(new ResourceLocation("meem:strangeModule"), null, new ItemStack(ModItems.strangeModule), "III", "SUS", "III", 'I', Items.IRON_INGOT, 'S', ModItems.smallCrystal, 'U', Items.ENDER_EYE);
        addShapedRecipe(new ResourceLocation("meem:luckModule"), null, new ItemStack(ModItems.luckModule), "III", "SUS", "III", 'I', Items.IRON_INGOT, 'S', ModItems.smallCrystal, 'U', Items.FISH);

        addShapedRecipe(new ResourceLocation("meem:teleportModule"), null, new ItemStack(ModItems.teleportModule), "GGG", "MUM", "GGG", 'G', Items.GOLD_INGOT, 'M', ModItems.crystal, 'U', Items.ENDER_PEARL);
        addShapedRecipe(new ResourceLocation("meem:plantModule"), null, new ItemStack(ModItems.plantModule), "GGG", "MUM", "GGG", 'G', Items.GOLD_INGOT, 'M', ModItems.crystal, 'U', Items.FLOWER_POT);
        addShapedRecipe(new ResourceLocation("meem:waterModule"), null, new ItemStack(ModItems.waterModule), "GGG", "MUM", "GGG", 'G', Items.GOLD_INGOT, 'M', ModItems.crystal, 'U', Items.PRISMARINE_SHARD);
        addShapedRecipe(new ResourceLocation("meem:lavaModule"), null, new ItemStack(ModItems.lavaModule), "GGG", "MUM", "GGG", 'G', Items.GOLD_INGOT, 'M', ModItems.crystal, 'U', Items.BLAZE_POWDER);
        addShapedRecipe(new ResourceLocation("meem:securityModule"), null, new ItemStack(ModItems.securityModule), "GGG", "MUM", "GGG", 'G', Items.GOLD_INGOT, 'M', ModItems.crystal, 'U', Items.NETHER_STAR);

        addShapedRecipe(new ResourceLocation("meem:windModule"), null, new ItemStack(ModItems.windModule), "RPR", "BUB", "RPR", 'R', ModItems.reinforcedHeavyPlate, 'P', ModItems.heavyPlate, 'B', ModItems.bigCrystal, 'U', Items.GHAST_TEAR);
        addShapedRecipe(new ResourceLocation("meem:shieldModule"), null, new ItemStack(ModItems.shieldModule), "RPR", "BUB", "RPR", 'R', ModItems.reinforcedHeavyPlate, 'P', ModItems.heavyPlate, 'B', ModItems.bigCrystal, 'U', ModItems.forceFieldComponent);
        addShapedRecipe(new ResourceLocation("meem:lightningModule"), null, new ItemStack(ModItems.lightningModule), "RPR", "BUB", "RPR", 'R', ModItems.reinforcedHeavyPlate, 'P', ModItems.heavyPlate, 'B', ModItems.bigCrystal, 'U', ModBlocks.hvg);
        //</editor-fold>
        //<editor-fold desc="Generators">
        addShapedRecipe(new ResourceLocation("meem:hungerEnergyModule"), null, new ItemStack(ModItems.hungerEnergyModule), "IKI", "KUK", "IKI", 'I', Items.IRON_INGOT, 'K', ModBlocks.cable, 'U', Items.APPLE);
        addShapedRecipe(new ResourceLocation("meem:motionEnergyModule"), null, new ItemStack(ModItems.motionEnergyModule), "IKI", "KUK", "IKI", 'I', Items.IRON_INGOT, 'K', ModBlocks.cable, 'U', Items.SUGAR);
        addShapedRecipe(new ResourceLocation("meem:sunEnergyModule"), null, new ItemStack(ModItems.sunEnergyModule), "GKG", "KUK", "GKG", 'G', Items.GOLD_INGOT, 'K', ModBlocks.cable, 'U', Blocks.DAYLIGHT_DETECTOR);
        addShapedRecipe(new ResourceLocation("meem:chunkEnergyModule"), null, new ItemStack(ModItems.chunkEnergyModule), "GKG", "KUK", "GKG", 'G', Items.GOLD_INGOT, 'K', ModBlocks.cable, 'U', ModItems.crystal);
        addShapedRecipe(new ResourceLocation("meem:lavaEnergyModule"), null, new ItemStack(ModItems.lavaEnergyModule), "RKR", "KUK", "RKR", 'R', ModItems.reinforcedHeavyPlate, 'K', ModBlocks.cable, 'U', Items.MAGMA_CREAM);
        addShapedRecipe(new ResourceLocation("meem:nuclearEnergyModule"), null, new ItemStack(ModItems.nuclearEnergyModule), "RKR", "KUK", "RKR", 'R', ModItems.reinforcedHeavyPlate, 'K', ModBlocks.cable, 'U', ModBlocks.nr);
        //</editor-fold>
        //<editor-fold desc="Visualization">
        addShapedRecipe(new ResourceLocation("meem:linksInfoModule"), null, new ItemStack(ModItems.linksInfoModule), "IKI", "IUI", "IKI", 'I', Items.IRON_INGOT, 'K', ModBlocks.cable, 'U', ModItems.smallCrystal);
        addShapedRecipe(new ResourceLocation("meem:entityInfoModule"), null, new ItemStack(ModItems.entityInfoModule), "IKI", "IUI", "IKI", 'I', Items.IRON_INGOT, 'K', ModBlocks.cable, 'U', Items.EGG);
        addShapedRecipe(new ResourceLocation("meem:pathInfoModule"), null, new ItemStack(ModItems.pathInfoModule), "GKG", "GUG", "GKG", 'G', Items.GOLD_INGOT, 'K', ModBlocks.cable, 'U', ModItems.lossReduceModule);
        addShapedRecipe(new ResourceLocation("meem:mapInfoModule"), null, new ItemStack(ModItems.mapInfoModule), "RKR", "RUR", "RKR", 'R', ModItems.reinforcedHeavyPlate, 'K', ModBlocks.cable, 'U', Items.MAP);
        //</editor-fold>
        //<editor-fold desc="Equipment">
        addShapedRecipe(new ResourceLocation("meem:energyHelmet"), null, new ItemStack(ModItems.energyHelmet), "FIF", "IXI", "XXX", 'X', Items.AIR, 'I', Items.IRON_INGOT, 'F', ModItems.forceFieldComponent);
        addShapedRecipe(new ResourceLocation("meem:energyChestplate"), null, new ItemStack(ModItems.energyChestplate), "IXI", "FBF", "IFI", 'X', Items.AIR, 'I', Items.IRON_INGOT, 'F', ModItems.forceFieldComponent, 'B', ModItems.bigCrystal);
        addShapedRecipe(new ResourceLocation("meem:energyLeggings"), null, new ItemStack(ModItems.energyLeggings), "IFI", "FXF", "IXI", 'X', Items.AIR, 'I', Items.IRON_INGOT, 'F', ModItems.forceFieldComponent);
        addShapedRecipe(new ResourceLocation("meem:energyBoots"), null, new ItemStack(ModItems.energyBoots), "XXX", "FXF", "IXI", 'X', Items.AIR, 'I', Items.IRON_INGOT, 'F', ModItems.forceFieldComponent);

        addShapedRecipe(new ResourceLocation("meem:heavyHelmet"), null, new ItemStack(ModItems.heavyHelmet), "PFP", "RXR", "XXX", 'X', Items.AIR, 'P', ModItems.heavyPlate, 'R', ModItems.reinforcedHeavyPlate, 'F', ModItems.forceFieldComponent);
        addShapedRecipe(new ResourceLocation("meem:heavyChestplate"), null, new ItemStack(ModItems.heavyChestplate), "PXP", "RUR", "RFR", 'X', Items.AIR, 'P', ModItems.heavyPlate, 'R', ModItems.reinforcedHeavyPlate, 'F', ModItems.forceFieldComponent, 'U', ModItems.bigCapacitor);
        addShapedRecipe(new ResourceLocation("meem:heavyLeggings"), null, new ItemStack(ModItems.heavyLeggings), "PFP", "RXR", "RXR", 'X', Items.AIR, 'P', ModItems.heavyPlate, 'R', ModItems.reinforcedHeavyPlate, 'F', ModItems.forceFieldComponent);
        addShapedRecipe(new ResourceLocation("meem:heavyBoots"), null, new ItemStack(ModItems.heavyBoots), "XXX", "PXP", "RPR", 'X', Items.AIR, 'P', ModItems.heavyPlate, 'R', ModItems.reinforcedHeavyPlate);

        addShapedRecipe(new ResourceLocation("meem:energySword"), null, new ItemStack(ModItems.energySword), "XRX", "XFX", "XBX", 'X', Items.AIR, 'R', ModItems.reinforcedHeavyPlate, 'F', ModItems.forceFieldComponent, 'B', ModItems.bigCrystal);
        addShapedRecipe(new ResourceLocation("meem:energyTool"), null, new ItemStack(ModItems.energyTool), "RFR", "XBX", "XTX", 'X', Items.AIR, 'R', ModItems.reinforcedHeavyPlate, 'F', ModItems.forceFieldComponent, 'B', ModItems.bigCrystal, 'T', ModItems.toughIronRod);
        //</editor-fold>
        //<editor-fold desc="Other">
        addShapedRecipe(new ResourceLocation("meem:injector"), null, new ItemStack(ModItems.injector), "XXI", "XGX", "RXX", 'X', Items.AIR, 'R', ModItems.ironRod, 'I', Items.IRON_INGOT, 'G', Blocks.GLASS);
        addShapedRecipe(new ResourceLocation("meem:frequencyInstaller"), null, new ItemStack(ModItems.frequencyInstaller), "XPX", "XVX", "XLX", 'X', Items.AIR, 'P', ModItems.processor, 'V', ModItems.voltageModule, 'L', ModItems.electricalBoard);
        addShapedRecipe(new ResourceLocation("meem:reinforcedHeavyPlate"), null, new ItemStack(ModItems.reinforcedHeavyPlate), "XDX", "XPX", "XDX", 'X', Items.AIR, 'P', ModItems.heavyPlate, 'D', Items.DIAMOND);
        addShapedRecipe(new ResourceLocation("meem:fullCapsule"), null, new ItemStack(ModItems.fullCapsule), "XDX", "DCD", "XBX", 'X', Items.AIR, 'D', ModItems.coalDust, 'B', Items.WATER_BUCKET, 'C', ModItems.capsule);
        addShapedRecipe(new ResourceLocation("meem:nuclearFuel"), null, new ItemStack(ModItems.nuclearFuel), "XDX", "DCD", "XBX", 'X', Items.AIR, 'D', ModItems.uraniumDust, 'B', ModItems.capsule, 'C', ModItems.coalDust);
        addShapedRecipe(new ResourceLocation("meem:powerSuppressor"), null, new ItemStack(ModItems.powerSuppressor), "FHF", "HUH", "FHF", 'F', ModItems.forceFieldComponent, 'H', ModItems.heavyIngot, 'U', ModItems.smallCapacitor);
        addShapedRecipe(new ResourceLocation("meem:copperIngot"), null, new ItemStack(ModItems.copperIngot), "XXX", "XXX", "XXX", 'X', ModItems.copperNugget);
        addShapedRecipe(new ResourceLocation("meem:grenade"), null, new ItemStack(ModItems.grenade), "IGI", "GSG", "IGI", 'S', ModItems.smallCrystal, 'G', Items.GUNPOWDER, 'I', Items.IRON_INGOT);
        addShapedRecipe(new ResourceLocation("meem:energyBalancer"), null, new ItemStack(ModItems.energyBalancer), "III", "SKS", "III", 'S', ModItems.smallCrystal, 'K', ModBlocks.cable, 'I', Items.IRON_INGOT);
        addShapelessRecipe(new ResourceLocation("meem:chargeDetector"), null, new ItemStack(ModItems.chargeDetector), getItem(ModItems.antenna), getItem(ModItems.voltageModule));
        addShapelessRecipe(new ResourceLocation("meem:idCard"), null, new ItemStack(ModItems.idCard), getItem(Items.DYE), getItem(Items.IRON_INGOT));
        addShapelessRecipe(new ResourceLocation("meem:tablet"), null, new ItemStack(ModItems.tablet), getItem(Items.PAPER), getItem(ModItems.smallCrystal));
        addShapelessRecipe(new ResourceLocation("meem:copperNugget"), null, new ItemStack(ModItems.copperNugget, 9), getItem(ModItems.copperIngot));
        addShapelessRecipe(new ResourceLocation("meem:copperIngot"), null, new ItemStack(ModItems.copperIngot, 9), getItem(ModBlocks.copper));
        addShapelessRecipe(new ResourceLocation("meem:heavyIngot"), null, new ItemStack(ModItems.heavyIngot, 4), getItem(ModBlocks.heavyMetal));
        //</editor-fold>
        //</editor-fold>
        //<editor-fold desc="Smelt">
        addSmelting(ModBlocks.copperOre, new ItemStack(ModItems.copperIngot), 10);
        addSmelting(ModItems.copperDust, new ItemStack(ModItems.copperIngot), 10);
        addSmelting(ModItems.heavyDust, new ItemStack(ModItems.heavyIngot), 10);
        addSmelting(Blocks.IRON_BLOCK, new ItemStack(ModItems.heavyIngot), 10);
        addSmelting(ModItems.ironDust, new ItemStack(Items.IRON_INGOT), 10);
        addSmelting(ModItems.goldDust, new ItemStack(Items.GOLD_INGOT), 10);
        //</editor-fold>
        ForgeRegistries.RECIPES.register(new FullInjectorRecipe().setRegistryName(new ResourceLocation("meem:fullinjectorrecipe")));
    }

    private static Ingredient getItem(Item item){
        return Ingredient.fromItem(item);
    }

    private static Ingredient getItem(Block item){
        return Ingredient.fromItem(Item.getItemFromBlock(item));
    }
}
