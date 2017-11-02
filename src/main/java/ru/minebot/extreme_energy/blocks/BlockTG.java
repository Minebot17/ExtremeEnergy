package ru.minebot.extreme_energy.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.init.ModGuiHandler;
import ru.minebot.extreme_energy.tile_entities.TileEntityTG;

import javax.annotation.Nullable;

public class BlockTG extends BlockFaceActive implements ITileEntityProvider {

    public BlockTG(){
        setUnlocalizedName(Reference.ExtremeEnergyBlocks.TG.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyBlocks.TG.getRegistryName());
        setHardness(5);
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setHarvestLevel("pickaxe", 2);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityTG();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
        TileEntityTG te = (TileEntityTG) world.getTileEntity(pos);
        if (te != null && !te.isEmpty())
            InventoryHelper.dropInventoryItems(world, pos, te);
        world.removeTileEntity(pos);
        super.breakBlock(world, pos, blockstate);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn,pos,state,placer,stack);
        if (stack.hasDisplayName()) {
            ((TileEntityTG) worldIn.getTileEntity(pos)).setCustomName(stack.getDisplayName());
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn.inventory.getCurrentItem().getItem() == Items.LAVA_BUCKET){
            TileEntityTG te = (TileEntityTG)worldIn.getTileEntity(pos);
            playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, te.fill(playerIn.inventory.getCurrentItem(), playerIn.isCreative()));
        }
        else if (!worldIn.isRemote) {
            playerIn.openGui(ExtremeEnergy.instance, ModGuiHandler.TG_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }
}
