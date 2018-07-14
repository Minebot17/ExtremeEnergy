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
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
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
import ru.minebot.extreme_energy.init.ModBlocks;
import ru.minebot.extreme_energy.init.ModGuiHandler;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.tile_entities.TileEntityFTR;
import ru.minebot.extreme_energy.tile_entities.TileEntityNR;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockNR extends Block implements ITileEntityProvider {
    public static final PropertyBool active = PropertyBool.create("active");

    public BlockNR(){
        super(Material.PISTON);
        setUnlocalizedName(Reference.ExtremeEnergyBlocks.NR.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyBlocks.NR.getRegistryName());
        setDefaultState(blockState.getBaseState().withProperty(active, false));
        setHardness(5);
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setHarvestLevel("pickaxe", 2);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityNR();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.AIR;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote){
            boolean active = worldIn.isBlockPowered(pos);
            TileEntityNR te = (TileEntityNR) worldIn.getTileEntity(pos);
            ModUtils.setState(worldIn, pos, blockState.getBaseState().withProperty(BlockNR.active, active));
            worldIn.notifyBlockUpdate(pos, blockState.getBaseState().withProperty(BlockNR.active, !active), blockState.getBaseState().withProperty(BlockNR.active, active), 0);

            te.active = !active;
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
        TileEntityNR te = (TileEntityNR) world.getTileEntity(pos);
        if (te != null) {
            if (!te.isEmpty())
                InventoryHelper.dropInventoryItems(world, pos, te);
            if (te.getHeat() > 0)
                InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.heatModule, 2));
            else
                InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModBlocks.nr));
        }
        world.removeTileEntity(pos);
        super.breakBlock(world, pos, blockstate);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(active, false));
        TileEntityNR te = ((TileEntityNR) worldIn.getTileEntity(pos));
        if (stack.hasDisplayName()) {
            te.setCustomName(stack.getDisplayName());
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.openGui(ExtremeEnergy.instance, ModGuiHandler.NR_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
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
