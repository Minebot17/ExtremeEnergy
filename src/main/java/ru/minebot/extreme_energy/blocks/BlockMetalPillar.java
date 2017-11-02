package ru.minebot.extreme_energy.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.init.ModBlocks;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.tile_entities.TileEntityLightningRod;

import javax.annotation.Nullable;
import java.util.List;

public class BlockMetalPillar extends Block {
    public BlockMetalPillar(){
        super(Material.PISTON);
        setUnlocalizedName(Reference.ExtremeEnergyBlocks.METALPILLAR.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyBlocks.METALPILLAR.getRegistryName());
        setHardness(5);
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setHarvestLevel("pickaxe", 2);
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

    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.3125f, 1, 0.3125f, 0.6875f, 0, 0.6875f);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        startCalculate(worldIn, pos);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        startCalculate(worldIn, pos);
    }

    private void startCalculate(World worldIn, BlockPos pos){
        if (!worldIn.isRemote){
            BlockPos rodPos = pos;
            rodPos = rodPos.down();
            int i = 31;
            while(i > 0){
                if (worldIn.getBlockState(rodPos).getBlock() == ModBlocks.lightningRod){
                    ((TileEntityLightningRod)worldIn.getTileEntity(rodPos)).calculatePillars();
                    break;
                }
                rodPos = rodPos.down();
                i--;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(ModUtils.getDescription(stack.getItem()));
    }
}
