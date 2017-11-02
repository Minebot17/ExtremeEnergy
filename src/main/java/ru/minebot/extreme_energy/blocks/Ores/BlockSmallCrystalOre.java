package ru.minebot.extreme_energy.blocks.Ores;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.init.ModItems;

import java.util.Random;

public class BlockSmallCrystalOre extends Block {

    public BlockSmallCrystalOre(){
        super(Material.IRON);
        setUnlocalizedName(Reference.ExtremeEnergyBlocks.SMALLCRYSTALORE.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyBlocks.SMALLCRYSTALORE.getRegistryName());
        setHardness(2f);
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
        return ModItems.smallCrystalActive;
    }

    @Override
    public int quantityDropped(Random par1Random){
        return par1Random.nextInt(2) + 1;
    }
}
