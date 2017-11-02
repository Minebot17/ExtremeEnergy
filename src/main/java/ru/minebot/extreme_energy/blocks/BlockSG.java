package ru.minebot.extreme_energy.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.init.ModBlocks;
import ru.minebot.extreme_energy.init.ModGuiHandler;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.tile_entities.TileEntitySG;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockSG extends BlockFaceActive implements ITileEntityProvider {

    public BlockSG(){
        setUnlocalizedName(Reference.ExtremeEnergyBlocks.SG.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyBlocks.SG.getRegistryName());
        setHardness(5);
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setHarvestLevel("pickaxe", 2);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntitySG();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.AIR;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
        TileEntitySG te = (TileEntitySG) world.getTileEntity(pos);
        if (te != null) {
            if (!te.isEmpty())
                InventoryHelper.dropInventoryItems(world, pos, te);
            if (te.getHeat() > 0)
                InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.voltageModule, 2));
            else
                InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModBlocks.sg));
        }
        world.removeTileEntity(pos);
        super.breakBlock(world, pos, blockstate);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (stack.hasDisplayName()) {
            ((TileEntitySG) worldIn.getTileEntity(pos)).setCustomName(stack.getDisplayName());
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.openGui(ExtremeEnergy.instance, ModGuiHandler.SG_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }
}
