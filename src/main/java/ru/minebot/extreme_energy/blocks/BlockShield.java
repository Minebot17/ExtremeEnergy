package ru.minebot.extreme_energy.blocks;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.init.ModBlocks;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.modules.ItemShieldModule;
import ru.minebot.extreme_energy.tile_entities.TileEntityFC;
import ru.minebot.extreme_energy.tile_entities.TileEntityFG;

import java.util.List;

public class BlockShield extends Block {
    public static PropertyBool isFC = PropertyBool.create("isfc");
    public BlockShield() {
        super(Material.GLASS);
        setUnlocalizedName(Reference.ExtremeEnergyBlocks.SHIELD.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyBlocks.SHIELD.getRegistryName());
        setBlockUnbreakable();
        setDefaultState(blockState.getBaseState().withProperty(isFC, false));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return blockAccess.getBlockState(pos.offset(side)).getBlock() != this;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        deleteBlock(worldIn, pos, state);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        deleteBlock(worldIn, pos, state);
    }

    private void deleteBlock(World worldIn, BlockPos pos, IBlockState state){
        if (!worldIn.isRemote && state.equals(ModBlocks.shield.getDefaultState()) && (!ModUtils.shields.containsKey(worldIn.provider.getDimension()) || (ModUtils.shields.containsKey(worldIn.provider.getDimension()) && !ModUtils.shields.get(worldIn.provider.getDimension()).containsKey(pos))))
            worldIn.setBlockToAir(pos);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(isFC, meta != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(isFC) ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, isFC);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!state.equals(getDefaultState())){
            List<TileEntity> tes = ModUtils.radiusFilter(pos, worldIn.loadedTileEntityList, 100);
            for (TileEntity t : tes){
                if (t instanceof TileEntityFC){
                    TileEntityFC te = (TileEntityFC)t;
                    if (ItemShieldModule.shields.containsKey(te.getPos()) && ItemShieldModule.shields.get(te.getPos()).shields.contains(pos)) {
                        if (te.isPublic()) {
                            ModUtils.addShieldToAdd(worldIn, pos, 60);
                            worldIn.setBlockToAir(pos);
                            return true;
                        }
                        else {
                            int[] ids = te.getIds();
                            for (int id : ids)
                                if (playerIn.getUniqueID().hashCode() == id) {
                                    ModUtils.addShieldToAdd(worldIn, pos, 60);
                                    worldIn.setBlockToAir(pos);
                                    return true;
                                }
                        }
                    }
                }
            }
        }
        return false;
    }
}
