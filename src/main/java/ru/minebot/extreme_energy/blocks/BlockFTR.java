package ru.minebot.extreme_energy.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.init.ModGuiHandler;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.tile_entities.TileEntityFTR;

import javax.annotation.Nullable;
import java.util.List;

public class BlockFTR extends Block implements ITileEntityProvider {
    public static final PropertyBool active = PropertyBool.create("active");

    public BlockFTR(){
        super(Material.PISTON);
        setUnlocalizedName(Reference.ExtremeEnergyBlocks.FTr.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyBlocks.FTr.getRegistryName());
        setDefaultState(blockState.getBaseState().withProperty(active, false));
        setHardness(5);
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setHarvestLevel("pickaxe", 2);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityFTR();
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote){
            boolean active = worldIn.isBlockPowered(pos);
            TileEntityFTR te = (TileEntityFTR) worldIn.getTileEntity(pos);
            ModUtils.setState(worldIn, pos, blockState.getBaseState().withProperty(BlockFTR.active, active));
            worldIn.notifyBlockUpdate(pos, blockState.getBaseState().withProperty(BlockFTR.active, !active), blockState.getBaseState().withProperty(BlockFTR.active, active), 0);

            te.active = active;
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
        TileEntityFTR te = (TileEntityFTR) world.getTileEntity(pos);
        try {
            if (!te.isEmpty())
                InventoryHelper.dropInventoryItems(world, pos, te);
            world.removeTileEntity(pos);
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        super.breakBlock(world, pos, blockstate);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(active, false));
        TileEntityFTR te = ((TileEntityFTR) worldIn.getTileEntity(pos));
        if (stack.hasDisplayName()) {
            te.setCustomName(stack.getDisplayName());
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.openGui(ExtremeEnergy.instance, ModGuiHandler.FTR_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return (IBlockState)getDefaultState().withProperty(active, meta != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((Boolean)state.getValue(active)) ? 0 : 1;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, active);
    }

    @Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(ModUtils.getDescription(stack.getItem()));
    }
}
