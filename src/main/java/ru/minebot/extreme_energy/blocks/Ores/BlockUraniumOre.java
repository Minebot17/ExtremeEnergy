package ru.minebot.extreme_energy.blocks.Ores;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.init.ModItems;

import java.util.Random;

public class BlockUraniumOre extends Block {
    public BlockUraniumOre(){
        super(Material.IRON);
        setUnlocalizedName(Reference.ExtremeEnergyBlocks.URANIUMORE.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyBlocks.URANIUMORE.getRegistryName());
        setHardness(6f);
        setLightLevel(0.2f);
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public int quantityDropped(Random random) {
        return random.nextInt(2) + 1;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ModItems.uranium;
    }
}
