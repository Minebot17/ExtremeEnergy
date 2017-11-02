package ru.minebot.extreme_energy.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.tile_entities.EnergySenderCable;

import javax.annotation.Nullable;

public class BlockCableWithEntity extends BlockCable implements ITileEntityProvider {

    public BlockCableWithEntity(){
        super();
        setCreativeTab(null);
    }

    @Override
    protected void setName(){
        setUnlocalizedName(Reference.ExtremeEnergyBlocks.CABLE.getUnlocalizedName()+"_tile");
        setRegistryName(Reference.ExtremeEnergyBlocks.CABLE.getRegistryName()+"_tile");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new EnergySenderCable();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {}
}
