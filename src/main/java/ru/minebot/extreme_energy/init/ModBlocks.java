package ru.minebot.extreme_energy.init;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ru.minebot.extreme_energy.blocks.*;
import ru.minebot.extreme_energy.blocks.Ores.*;

public class ModBlocks {
    public static Block
            shield,
            cable,
            cableWithTile,
            smallCristalOre,
            cristalOre,
            bigCristalOre,
            uraniumOre,
            copperOre,
            copper,
            heavyMetal,
            hvg,
            htf,
            hpc,
            hpa,
            fc,
            ft,
            sg,
            fg,
            tg,
            ee,
            eb,
            cs,
            nr,
            nb,
            ftr,
            lightningRod,
            metalPillar;

    public static void init(){
        lightningRod = new BlockLightningRod();
        metalPillar = new BlockMetalPillar();
        shield = new BlockShield();
        cable = new BlockCable();
        cableWithTile = new BlockCableWithEntity();
        smallCristalOre = new BlockSmallCrystalOre();
        cristalOre = new BlockCrystalOre();
        bigCristalOre = new BlockBigCrystalOre();
        uraniumOre = new BlockUraniumOre();
        copperOre = new BlockCopperOre();
        copper = new BlockCopper();
        heavyMetal = new BlockHeavyMetal();
        hvg = new BlockHVG();
        htf = new BlockHTF();
        hpc = new BlockHPC();
        hpa = new BlockHPA();
        fc = new BlockFC();
        ft = new BlockFT();
        sg = new BlockSG();
        fg = new BlockFG();
        tg = new BlockTG();
        ee = new BlockEE();
        eb = new BlockEB();
        cs = new BlockCS();
        nr = new BlockNR();
        nb = new BlockNB();
        ftr = new BlockFTR();
    }

    public static void register(){
        registerBlock(lightningRod);
        registerBlock(metalPillar);
        registerBlock(shield);
        registerBlock(cable);
        registerBlock(cableWithTile);
        registerBlock(smallCristalOre);
        registerBlock(cristalOre);
        registerBlock(bigCristalOre);
        registerBlock(uraniumOre);
        registerBlock(copperOre);
        registerBlock(copper);
        registerBlock(heavyMetal);
        registerBlock(hvg);
        registerBlock(htf);
        registerBlock(hpc);
        registerBlock(hpa);
        registerBlock(fc);
        registerBlock(ft);
        registerBlock(sg);
        registerBlock(fg);
        registerBlock(tg);
        registerBlock(ee);
        registerBlock(eb);
        registerBlock(cs);
        registerBlock(nr);
        registerBlock(nb);
        registerBlock(ftr);
    }

    public static void registerRenders(){
        registerRender(lightningRod);
        registerRender(metalPillar);
        registerRender(shield);
        registerRender(cable);
        registerRender(cableWithTile);
        registerRender(smallCristalOre);
        registerRender(cristalOre);
        registerRender(bigCristalOre);
        registerRender(uraniumOre);
        registerRender(copperOre);
        registerRender(copper);
        registerRender(heavyMetal);
        registerRender(hvg);
        registerRender(htf);
        registerRender(hpc);
        registerRender(hpa);
        registerRender(fc);
        registerRender(ft);
        registerRender(sg);
        registerRender(fg);
        registerRender(tg);
        registerRender(ee);
        registerRender(eb);
        registerRender(cs);
        registerRender(nr);
        registerRender(nb);
        registerRender(ftr);
    }

    private static void registerBlock(Block block){
        ForgeRegistries.BLOCKS.register(block);
        ItemBlock item = new ItemBlock(block);
        item.setRegistryName(block.getRegistryName());
        ForgeRegistries.ITEMS.register(item);
    }

    private static void registerRender(Block block){
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }
}
