package ru.minebot.extreme_energy.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class BlockHeavyMetal extends Block {
    public BlockHeavyMetal(){
        super(Material.IRON);
        setUnlocalizedName(Reference.ExtremeEnergyBlocks.HEAVYMETAL.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyBlocks.HEAVYMETAL.getRegistryName());
        setHardness(8f);
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setHarvestLevel("pickaxe", 2);
    }
}
