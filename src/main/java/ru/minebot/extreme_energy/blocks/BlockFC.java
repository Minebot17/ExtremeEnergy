package ru.minebot.extreme_energy.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.init.ModConfig;
import ru.minebot.extreme_energy.init.ModGuiHandler;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.FuncArgs;
import ru.minebot.extreme_energy.modules.ModuleFunctional;
import ru.minebot.extreme_energy.tile_entities.TileEntityFC;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockFC extends Block implements ITileEntityProvider{
    public BlockFC(){
        super(Material.PISTON);
        setUnlocalizedName(Reference.ExtremeEnergyBlocks.FC.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyBlocks.FC.getRegistryName());
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

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityFC();
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote){
            boolean active = worldIn.isBlockPowered(pos);
            TileEntityFC te = (TileEntityFC)worldIn.getTileEntity(pos);

            te.setActive(!active);
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
        TileEntityFC te = (TileEntityFC) world.getTileEntity(pos);
        if (te != null) {
            for (int i = 0; i < te.getSizeInventory(); i++)
                if (te.getStackInSlot(i).getItem() instanceof ModuleFunctional)
                    ((ModuleFunctional) te.getStackInSlot(i).getItem()).removeModule(te.getFuncArgs());
            if (!te.isEmpty())
                InventoryHelper.dropInventoryItems(world, pos, te);
            world.removeTileEntity(pos);
        }
        super.breakBlock(world, pos, blockstate);
    }

    @Override
    public void onBlockExploded(World worldIn, BlockPos pos, Explosion explosionIn) {
        breakBlock(worldIn, pos, worldIn.getBlockState(pos));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntityFC te = ((TileEntityFC) worldIn.getTileEntity(pos));
        if (stack.hasDisplayName()) {
            te.setCustomName(stack.getDisplayName());
        }
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.openGui(ExtremeEnergy.instance, ModGuiHandler.FC_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(ModUtils.getDescription(stack.getItem()));
    }
}
