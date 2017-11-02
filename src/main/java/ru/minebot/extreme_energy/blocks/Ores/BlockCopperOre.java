package ru.minebot.extreme_energy.blocks.Ores;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class BlockCopperOre extends Block {

    public BlockCopperOre(){
        super(Material.IRON);
        setUnlocalizedName(Reference.ExtremeEnergyBlocks.COPPERORE.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyBlocks.COPPERORE.getRegistryName());
        setHardness(3f);
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setHarvestLevel("pickaxe", 1);
    }
}
