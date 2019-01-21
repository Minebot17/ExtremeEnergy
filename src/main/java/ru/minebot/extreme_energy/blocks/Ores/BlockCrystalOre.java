package ru.minebot.extreme_energy.blocks.Ores;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.init.ModConfig;
import ru.minebot.extreme_energy.init.ModItems;

import java.util.Random;

public class BlockCrystalOre extends Block{

    public BlockCrystalOre(){
        super(Material.IRON);
        setUnlocalizedName(Reference.ExtremeEnergyBlocks.CRYSTALORE.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyBlocks.CRYSTALORE.getRegistryName());
        setHardness(4f);
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
        return ModConfig.poweredCrystals ? ModItems.crystalActive : ModItems.crystal;
    }

    @Override
    public int quantityDropped(Random par1Random){
        return par1Random.nextInt(2) + 1;
    }
}
