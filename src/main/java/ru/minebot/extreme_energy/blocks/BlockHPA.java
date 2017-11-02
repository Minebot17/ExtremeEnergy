package ru.minebot.extreme_energy.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.energy.IFieldCreatorEnergy;
import ru.minebot.extreme_energy.energy.IFieldReceiverEnergy;
import ru.minebot.extreme_energy.init.ModGuiHandler;
import ru.minebot.extreme_energy.tile_entities.TileEntityHPA;
import ru.minebot.extreme_energy.tile_entities.TileEntityHTF;

import javax.annotation.Nullable;

public class BlockHPA extends BlockFaceActive implements ITileEntityProvider {

    public BlockHPA(){
        setUnlocalizedName(Reference.ExtremeEnergyBlocks.HPA.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyBlocks.HPA.getRegistryName());
        setHardness(5);
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setHarvestLevel("pickaxe", 2);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityHPA();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
        TileEntityHTF te = (TileEntityHTF) world.getTileEntity(pos);
        if (te.getLink() != null) {
            IFieldCreatorEnergy creator = ((IFieldCreatorEnergy) world.getTileEntity(te.getLink()));
            creator.brokeLink(te.getPos());
        }
        if (te != null && !te.isEmpty())
            InventoryHelper.dropInventoryItems(world, pos, te);
        world.removeTileEntity(pos);
        super.breakBlock(world, pos, blockstate);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn,pos,state,placer,stack);
        ((IFieldReceiverEnergy)worldIn.getTileEntity(pos)).applyCreatorsAround();
        if (stack.hasDisplayName()) {
            ((TileEntityHTF) worldIn.getTileEntity(pos)).setCustomName(stack.getDisplayName());
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.openGui(ExtremeEnergy.instance, ModGuiHandler.HPA_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }
}
