package ru.minebot.extreme_energy.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.tile_entities.TileEntityLightningRod;

import javax.annotation.Nullable;
import java.util.List;

public class BlockLightningRod extends Block implements ITileEntityProvider {
    public BlockLightningRod(){
        super(Material.PISTON);
        setUnlocalizedName(Reference.ExtremeEnergyBlocks.LIGHTNINGROD.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyBlocks.LIGHTNINGROD.getRegistryName());
        setHardness(5);
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setHarvestLevel("pickaxe", 2);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityLightningRod();
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(ModUtils.getDescription(stack.getItem()));
    }
}
