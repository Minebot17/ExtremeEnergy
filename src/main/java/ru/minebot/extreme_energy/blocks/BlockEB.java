package ru.minebot.extreme_energy.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.energy.IFieldCreatorEnergy;
import ru.minebot.extreme_energy.init.ModGuiHandler;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.tile_entities.TileEntityEB;

import javax.annotation.Nullable;
import java.util.List;

public class BlockEB extends Block implements ITileEntityProvider{

    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public BlockEB(){
        super(Material.PISTON);
        setUnlocalizedName(Reference.ExtremeEnergyBlocks.EB.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyBlocks.EB.getRegistryName());
        setHardness(5);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setHarvestLevel("pickaxe", 2);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityEB();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.getFacingFromVector((float)placer.getLookVec().x, (float)placer.getLookVec().y, (float)placer.getLookVec().z).getOpposite()), 2);
        if (stack.hasDisplayName()) {
            ((TileEntityEB) worldIn.getTileEntity(pos)).setCustomName(stack.getDisplayName());
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(ModUtils.getDescription(stack.getItem()));
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
        TileEntityEB te = (TileEntityEB) world.getTileEntity(pos);
        if (te != null && !te.isEmpty())
            InventoryHelper.dropInventoryItems(world, pos, te);
        world.removeTileEntity(pos);
        super.breakBlock(world, pos, blockstate);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.openGui(ExtremeEnergy.instance, ModGuiHandler.EB_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }
}
