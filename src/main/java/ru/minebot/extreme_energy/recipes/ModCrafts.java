package ru.minebot.extreme_energy.recipes;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import static net.minecraftforge.fml.common.registry.GameRegistry.*;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import ru.minebot.extreme_energy.init.ModBlocks;
import ru.minebot.extreme_energy.init.ModItems;

public class ModCrafts {

    public static void register(){
        IRecipe[] oreDictRecipes = new IRecipe[]{
                shaped(ModBlocks.sg, "CHC", "VSV", "CHC", 'C', "ingotCopper", 'S', ModItems.smallCrystal, 'V', ModItems.voltageModule, 'H', "ingotSteel"),
                shaped(ModBlocks.fg, "CHC", "VSV", "CHC", 'C', "ingotCopper", 'S', ModItems.smallCrystal, 'V', "stickIron", 'H', "ingotSteel"),
                shaped(ModBlocks.tg, "CHC", "VSV", "CHC", 'C', "ingotCopper", 'S', ModItems.smallCrystal, 'V', ModItems.ironCylinder, 'H', "ingotSteel"),
                shaped(ModBlocks.tg, "CHC", "VSV", "CHC", 'C', "ingotCopper", 'S', ModItems.smallCrystal, 'V', ModItems.ironCylinder, 'H', "ingotSteel"),
                shaped(ModBlocks.nr, "CHC", "VBV", "CHC", 'C', "ingotCopper", 'B', ModItems.bigCrystal, 'V', ModItems.heatModule, 'H', ModItems.reinforcedHeavyPlate),
                shaped(ModBlocks.has, "CAC", "RMR", "CHC", 'C', "ingotCopper", 'M', ModItems.crystal, 'A', ModItems.antenna, 'R', ModItems.electricCarver, 'H', "ingotSteel"),
                shaped(ModBlocks.hpa, "CCC", "CSC", "HHH", 'C', "ingotCopper", 'S', ModItems.smallCrystal, 'H', "ingotSteel"),
                shaped(ModBlocks.hvg, "HAH", "PMP", "VJV", 'A', ModItems.antenna, 'M', ModItems.crystal, 'H', "ingotSteel", 'P', ModItems.processor, 'V', ModItems.voltageModule, 'J', ModItems.heavyPlate),
                shaped(ModBlocks.htf, "CAC", "RMR", "CHC", 'C', "ingotCopper", 'M', ModItems.crystal, 'H', "ingotSteel", 'R', "stickIron", 'A', ModItems.antenna),
                shaped(ModBlocks.hpc, "CAC", "HMH", "CHC", 'C', "ingotCopper", 'M', ModItems.crystal, 'H', "ingotSteel", 'A', ModItems.antenna),
                shaped(ModBlocks.fc, "XAX", "VSV", "HJH", 'X', Items.AIR, 'V', ModItems.voltageModule, 'S', ModItems.smallCrystal, 'H', "ingotSteel", 'A', ModItems.antenna, 'J', ModItems.heavyPlate),
                shaped(ModBlocks.ft, "AGA", "HBH", "HPH", 'G', Blocks.GLASS, 'B', ModItems.bigCrystal, 'H', "ingotSteel", 'A', ModItems.antenna, 'P', ModItems.processor),
                shaped(ModBlocks.ftr, "HAH", "KBK", "HPH", 'K', ModBlocks.cable, 'B', ModItems.bigCrystal, 'H', "ingotSteel", 'A', ModItems.antenna, 'P', ModItems.processor),
                shaped(ModBlocks.ee, "CKC", "VXV", "CKC", 'X', Items.AIR, 'K', ModBlocks.cable, 'C', "ingotCopper", 'V', ModItems.voltageModule),
                shaped(ModBlocks.cs, "ISI", "IRI", "IEI", 'I', Items.IRON_INGOT, 'S', ModItems.smallCrystal, 'R', Blocks.REDSTONE_BLOCK, 'E', ModItems.lossReduceModule),
                shaped(ModBlocks.eb, "CMC", "KUK", "CMC", 'M', ModItems.capacityModule, 'C', "ingotCopper", 'U', ModItems.smallCapacitor, 'K', ModBlocks.cable),
                shaped(ModBlocks.nb, "IWI", "FFF", "IWI", 'I', Items.IRON_INGOT, 'F', ModItems.nuclearFuel, 'W', ModItems.copperWires),
                shaped(ModBlocks.lightningRod, "XHX", "KMK", "HHH", 'X', Items.AIR, 'H', "ingotSteel", 'K', ModBlocks.cable, 'M', ModItems.crystal),
                shaped(ModBlocks.metalPillar, "IKI", "IKI", "IKI", 'I', Items.IRON_INGOT, 'K', ModBlocks.cable),
                shaped("meem:cable6", new ItemStack(ModBlocks.cable, 6), "III", "WRW", "III", 'I', Items.IRON_INGOT, 'W', ModItems.copperWires, 'R', Items.REDSTONE),
                shaped("meem:cable3", new ItemStack(ModBlocks.cable, 3), "GGG", "RRR", "GGG", 'G', Blocks.GLASS, 'R', Items.REDSTONE),
                shaped(ModBlocks.heavyMetal, "HHX", "HHX", "XXX", 'X', Items.AIR, 'H', "ingotSteel"),
                shaped(ModBlocks.copper, "CCC", "CCC", "CCC", 'C', "ingotCopper"),
                shaped(ModItems.baseImplant, "SHS", "VUV", "HHH", 'H', "ingotSteel", 'V', ModItems.voltageModule, 'S', ModItems.smallCrystal, 'U', ModItems.smallCapacitor),
                shaped(ModItems.advancedImplant, "MHM", "VUV", "HHH", 'H', ModItems.heavyPlate, 'V', Items.DIAMOND, 'M', ModItems.crystal, 'U', ModItems.capacitor),
                shaped(ModItems.extremeImplant, "BHB", "VUV", "EHE", 'H', ModItems.reinforcedHeavyPlate, 'V', Items.EMERALD, 'B', ModItems.bigCrystal, 'U', ModItems.bigCapacitor, 'E', Items.ENDER_PEARL),
                shaped(ModItems.basicCore, "KKK", "HPH", "KKK", 'H', "ingotSteel", 'P', ModItems.processor, 'K', ModBlocks.cable),
                shaped(ModItems.advancedCore, "KDK", "HPH", "KDK", 'H', "ingotSteel", 'D', Items.DIAMOND, 'P', ModItems.processor, 'K', ModBlocks.cable),
                shaped(ModItems.extremeCore, "KDK", "HPH", "KDK", 'H', Items.ENDER_PEARL, 'D', Items.EMERALD, 'P', ModItems.processor, 'K', ModBlocks.cable),
                shaped(ModItems.smallCapacitor, "IRI", "RSR", "IRI", 'I', Items.IRON_INGOT, 'R', Items.REDSTONE, 'S', ModItems.smallCrystal),
                shaped(ModItems.capacitor, "CUC", "GMG", "CUC", 'C', "ingotCopper", 'G', Items.GOLD_INGOT, 'U', ModItems.smallCapacitor, 'M', ModItems.crystal),
                shaped(ModItems.bigCapacitor, "HUH", "DMD", "HUH", 'H', "ingotSteel", 'D', Items.DIAMOND, 'U', ModItems.capacitor, 'M', ModItems.bigCrystal),
                shaped(ModItems.lifeModule, "III", "SUS", "III", 'I', Items.IRON_INGOT, 'S', ModItems.smallCrystal, 'U', Items.GOLDEN_APPLE),
                shaped(ModItems.electricalModule, "III", "SUS", "III", 'I', Items.IRON_INGOT, 'S', ModItems.smallCrystal, 'U', Items.GOLDEN_CARROT),
                shaped(ModItems.aggressiveModule, "III", "SUS", "III", 'I', Items.IRON_INGOT, 'S', ModItems.smallCrystal, 'U', Items.GOLDEN_SWORD),
                shaped(ModItems.strangeModule, "III", "SUS", "III", 'I', Items.IRON_INGOT, 'S', ModItems.smallCrystal, 'U', Items.ENDER_EYE),
                shaped(ModItems.luckModule, "III", "SUS", "III", 'I', Items.IRON_INGOT, 'S', ModItems.smallCrystal, 'U', Items.FISH),
                shaped(ModItems.teleportModule, "GGG", "MUM", "GGG", 'G', Items.GOLD_INGOT, 'M', ModItems.crystal, 'U', Items.ENDER_PEARL),
                shaped(ModItems.plantModule, "GGG", "MUM", "GGG", 'G', Items.GOLD_INGOT, 'M', ModItems.crystal, 'U', Items.FLOWER_POT),
                shaped(ModItems.waterModule, "GGG", "MUM", "GGG", 'G', Items.GOLD_INGOT, 'M', ModItems.crystal, 'U', Items.PRISMARINE_SHARD),
                shaped(ModItems.lavaModule, "GGG", "MUM", "GGG", 'G', Items.GOLD_INGOT, 'M', ModItems.crystal, 'U', Items.BLAZE_POWDER),
                shaped(ModItems.securityModule, "GGG", "MUM", "GGG", 'G', Items.GOLD_INGOT, 'M', ModItems.crystal, 'U', Items.NETHER_STAR),
                shaped(ModItems.windModule, "RPR", "BUB", "RPR", 'R', ModItems.reinforcedHeavyPlate, 'P', ModItems.heavyPlate, 'B', ModItems.bigCrystal, 'U', Items.GHAST_TEAR),
                shaped(ModItems.shieldModule, "RPR", "BUB", "RPR", 'R', ModItems.reinforcedHeavyPlate, 'P', ModItems.heavyPlate, 'B', ModItems.bigCrystal, 'U', ModItems.forceFieldComponent),
                shaped(ModItems.lightningModule, "RPR", "BUB", "RPR", 'R', ModItems.reinforcedHeavyPlate, 'P', ModItems.heavyPlate, 'B', ModItems.bigCrystal, 'U', ModBlocks.hvg),
                shaped(ModItems.hungerEnergyModule, "IKI", "KUK", "IKI", 'I', Items.IRON_INGOT, 'K', ModBlocks.cable, 'U', Items.APPLE),
                shaped(ModItems.motionEnergyModule, "IKI", "KUK", "IKI", 'I', Items.IRON_INGOT, 'K', ModBlocks.cable, 'U', Items.SUGAR),
                shaped(ModItems.sunEnergyModule, "GKG", "KUK", "GKG", 'G', Items.GOLD_INGOT, 'K', ModBlocks.cable, 'U', Blocks.DAYLIGHT_DETECTOR),
                shaped(ModItems.chunkEnergyModule, "GKG", "KUK", "GKG", 'G', Items.GOLD_INGOT, 'K', ModBlocks.cable, 'U', ModItems.crystal),
                shaped(ModItems.lavaEnergyModule, "RKR", "KUK", "RKR", 'R', ModItems.reinforcedHeavyPlate, 'K', ModBlocks.cable, 'U', Items.MAGMA_CREAM),
                shaped(ModItems.nuclearEnergyModule, "RKR", "KUK", "RKR", 'R', ModItems.reinforcedHeavyPlate, 'K', ModBlocks.cable, 'U', ModBlocks.nr),
                shaped(ModItems.linksInfoModule, "IKI", "IUI", "IKI", 'I', Items.IRON_INGOT, 'K', ModBlocks.cable, 'U', ModItems.smallCrystal),
                shaped(ModItems.entityInfoModule, "IKI", "IUI", "IKI", 'I', Items.IRON_INGOT, 'K', ModBlocks.cable, 'U', Items.EGG),
                shaped(ModItems.pathInfoModule, "GKG", "GUG", "GKG", 'G', Items.GOLD_INGOT, 'K', ModBlocks.cable, 'U', ModItems.lossReduceModule),
                shaped(ModItems.mapInfoModule, "RKR", "RUR", "RKR", 'R', ModItems.reinforcedHeavyPlate, 'K', ModBlocks.cable, 'U', Items.MAP),
                shaped(ModItems.energyHelmet, "FIF", "IXI", "XXX", 'X', Items.AIR, 'I', Items.IRON_INGOT, 'F', ModItems.forceFieldComponent),
                shaped(ModItems.energyChestplate, "IXI", "FBF", "IFI", 'X', Items.AIR, 'I', Items.IRON_INGOT, 'F', ModItems.forceFieldComponent, 'B', ModItems.bigCrystal),
                shaped(ModItems.energyLeggings, "IFI", "FXF", "IXI", 'X', Items.AIR, 'I', Items.IRON_INGOT, 'F', ModItems.forceFieldComponent),
                shaped(ModItems.energyBoots, "XXX", "FXF", "IXI", 'X', Items.AIR, 'I', Items.IRON_INGOT, 'F', ModItems.forceFieldComponent),
                shaped(ModItems.heavyHelmet, "PFP", "RXR", "XXX", 'X', Items.AIR, 'P', ModItems.heavyPlate, 'R', ModItems.reinforcedHeavyPlate, 'F', ModItems.forceFieldComponent),
                shaped(ModItems.heavyChestplate, "PXP", "RUR", "RFR", 'X', Items.AIR, 'P', ModItems.heavyPlate, 'R', ModItems.reinforcedHeavyPlate, 'F', ModItems.forceFieldComponent, 'U', ModItems.bigCapacitor),
                shaped(ModItems.heavyLeggings, "PFP", "RXR", "RXR", 'X', Items.AIR, 'P', ModItems.heavyPlate, 'R', ModItems.reinforcedHeavyPlate, 'F', ModItems.forceFieldComponent),
                shaped(ModItems.heavyBoots, "XXX", "PXP", "RPR", 'X', Items.AIR, 'P', ModItems.heavyPlate, 'R', ModItems.reinforcedHeavyPlate),
                shaped(ModItems.energySword, "XRX", "XFX", "XBX", 'X', Items.AIR, 'R', ModItems.reinforcedHeavyPlate, 'F', ModItems.forceFieldComponent, 'B', ModItems.bigCrystal),
                shaped(ModItems.energyTool, "RFR", "XBX", "XTX", 'X', Items.AIR, 'R', ModItems.reinforcedHeavyPlate, 'F', ModItems.forceFieldComponent, 'B', ModItems.bigCrystal, 'T', ModItems.toughIronRod),
                shaped(ModItems.injector, "XXI", "XGX", "RXX", 'X', Items.AIR, 'R', "stickIron", 'I', Items.IRON_INGOT, 'G', Blocks.GLASS),
                shaped(ModItems.frequencyInstaller, "XPX", "XVX", "XLX", 'X', Items.AIR, 'P', ModItems.processor, 'V', ModItems.voltageModule, 'L', ModItems.electricalBoard),
                shaped(ModItems.reinforcedHeavyPlate, "XDX", "XPX", "XDX", 'X', Items.AIR, 'P', ModItems.heavyPlate, 'D', Items.DIAMOND),
                shaped(ModItems.fullCapsule, "XDX", "DCD", "XBX", 'X', Items.AIR, 'D', "dustCoal", 'B', Items.WATER_BUCKET, 'C', ModItems.capsule),
                shaped(ModItems.nuclearFuel, "XDX", "DCD", "XBX", 'X', Items.AIR, 'D', "dustUranium", 'B', ModItems.capsule, 'C', "dustCoal"),
                shaped(ModItems.powerSuppressor, "FHF", "HUH", "FHF", 'F', ModItems.forceFieldComponent, 'H', "ingotSteel", 'U', ModItems.smallCapacitor),
                shaped(ModItems.copperIngot, "XXX", "XXX", "XXX", 'X', ModItems.copperNugget),
                shaped(ModItems.grenade, "IGI", "GSG", "IGI", 'S', ModItems.smallCrystal, 'G', Items.GUNPOWDER, 'I', Items.IRON_INGOT),
                shaped(ModItems.energyBalancer, "III", "SKS", "III", 'S', ModItems.smallCrystal, 'K', ModBlocks.cable, 'I', Items.IRON_INGOT),

                shapeless(new ItemStack(ModItems.chargeDetector), getItem(ModItems.antenna), getItem(ModItems.voltageModule)),
                shapeless(new ItemStack(ModItems.idCard), getItem(Items.DYE), getItem(Items.IRON_INGOT)),
                shapeless(new ItemStack(ModItems.tablet), getItem(Items.PAPER), getItem(ModItems.smallCrystal)),
                shapeless(new ItemStack(ModItems.copperNugget, 9), getItem(ModItems.copperIngot)),
                shapeless(new ItemStack(ModItems.copperIngot, 9), getItem(ModBlocks.copper)),
                shapeless(new ItemStack(ModItems.heavyIngot, 4), getItem(ModBlocks.heavyMetal)),
                shapeless("meem:nullableSmall" ,new ItemStack(ModItems.smallCapacitor), getItem(ModItems.smallCapacitor)),
                shapeless("meem:nullableMedium" ,new ItemStack(ModItems.capacitor), getItem(ModItems.capacitor)),
                shapeless("meem:nullableBig" ,new ItemStack(ModItems.bigCapacitor), getItem(ModItems.bigCapacitor))
        };
        // C - copper, H - heavy ingot, S - small crystal, M - crystal, B - big crystal, K - cable
        //<editor-fold desc="Smelt">
        addSmelting(ModBlocks.copperOre, new ItemStack(ModItems.copperIngot), 10);
        addSmelting(ModItems.copperDust, new ItemStack(ModItems.copperIngot), 10);
        addSmelting(ModItems.heavyDust, new ItemStack(ModItems.heavyIngot), 10);
        addSmelting(Blocks.IRON_BLOCK, new ItemStack(ModItems.heavyIngot, 3), 10);
        addSmelting(ModItems.ironDust, new ItemStack(Items.IRON_INGOT), 10);
        addSmelting(ModItems.goldDust, new ItemStack(Items.GOLD_INGOT), 10);
        //</editor-fold>
        ForgeRegistries.RECIPES.register(new FullInjectorRecipe().setRegistryName(new ResourceLocation("meem:fullinjectorrecipe")));
        for (IRecipe recipe : oreDictRecipes)
            ForgeRegistries.RECIPES.register(recipe.getRegistryName() == null ? recipe.setRegistryName(new ResourceLocation("meem", recipe.getRecipeOutput().getUnlocalizedName())) : recipe);
    }

    private static Ingredient getItem(Item item){
        return Ingredient.fromItem(item);
    }

    private static Ingredient getItem(Block item){
        return Ingredient.fromItem(Item.getItemFromBlock(item));
    }

    private static IRecipe shaped(ItemStack result, Object... objs){
        return new ShapedOreRecipe(null, result, objs);
    }

    private static IRecipe shaped(String rgName, ItemStack result, Object... objs){
        return new ShapedOreRecipe(null, result, objs).setRegistryName(new ResourceLocation(rgName));
    }

    private static IRecipe shaped(Block result, Object... objs){
        return new ShapedOreRecipe(null, result, objs);
    }

    private static IRecipe shaped(Item result, Object... objs){
        return new ShapedOreRecipe(null, result, objs);
    }

    private static IRecipe shapeless(String registerName, ItemStack result, Object... objs){
        return new ShapelessOreRecipe(null, result, objs).setRegistryName(new ResourceLocation(registerName));
    }

    private static IRecipe shapeless(ItemStack result, Object... objs){
        return new ShapelessOreRecipe(null, result, objs);
    }
}
