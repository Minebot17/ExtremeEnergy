package ru.minebot.extreme_energy.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class BlockCopper extends Block {
    public BlockCopper(){
        super(Material.IRON);
        setUnlocalizedName(Reference.ExtremeEnergyBlocks.COPPER.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyBlocks.COPPER.getRegistryName());
        setHardness(5f);
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setHarvestLevel("pickaxe", 1);
    }
}
