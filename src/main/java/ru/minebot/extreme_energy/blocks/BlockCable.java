package ru.minebot.extreme_energy.blocks;

import cofh.redstoneflux.api.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
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
import ru.minebot.extreme_energy.tile_entities.EnergySender;

import javax.annotation.Nullable;
import java.util.List;

public class BlockCable extends Block{

    private static final PropertyBool
            NORTH = PropertyBool.create("north"),
            SOUTH = PropertyBool.create("south"),
            WEST = PropertyBool.create("west"),
            EAST = PropertyBool.create("east"),
            UP = PropertyBool.create("up"),
            DOWN = PropertyBool.create("down");

    public BlockCable(){
        super(Material.IRON);
        setName();
        setHardness(1f);
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setHarvestLevel("pickaxe", 0);
        setDefaultState(blockState.getBaseState()
                .withProperty(NORTH, false)
                .withProperty(SOUTH, false)
                .withProperty(WEST, false)
                .withProperty(EAST, false)
                .withProperty(UP, false)
                .withProperty(DOWN, false)
        );
    }

    protected void setName(){
        setUnlocalizedName(Reference.ExtremeEnergyBlocks.CABLE.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyBlocks.CABLE.getRegistryName());
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        IBlockState result = blockState.getBaseState()
                .withProperty(NORTH, isCable(worldIn, pos, EnumFacing.NORTH))
                .withProperty(SOUTH, isCable(worldIn, pos, EnumFacing.SOUTH))
                .withProperty(WEST, isCable(worldIn, pos, EnumFacing.WEST))
                .withProperty(EAST, isCable(worldIn, pos, EnumFacing.EAST))
                .withProperty(UP, isCable(worldIn, pos, EnumFacing.UP))
                .withProperty(DOWN, isCable(worldIn, pos, EnumFacing.DOWN));
        return result;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NORTH, SOUTH, WEST, EAST, UP, DOWN);
    }

    private boolean isCable(IBlockAccess worldIn, BlockPos pos, EnumFacing facing){
        TileEntity te = worldIn.getTileEntity(pos.offset(facing));
        Block block = worldIn.getBlockState(pos.offset(facing)).getBlock();
        return block == ModBlocks.cable || (te != null && te instanceof IEnergyConnection && ((IEnergyConnection) te).canConnectEnergy(facing.getOpposite()));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        int[] array = new int[6];
        int i = 5;
        while (meta != 0 && meta != 1){
            array[i] = meta%2;
            meta /= 2;
            i--;
        }
        array[0] = meta;

        return blockState.getBaseState()
                .withProperty(NORTH, array[0] == 1)
                .withProperty(SOUTH, array[1] == 1)
                .withProperty(WEST, array[2] == 1)
                .withProperty(EAST, array[3] == 1)
                .withProperty(UP, array[4] == 1)
                .withProperty(DOWN, array[5] == 1);
    }

    public static int getConnectionCount(World world, BlockPos pos){
        IBlockState state = ((BlockCable)world.getBlockState(pos).getBlock()).getActualState(world.getBlockState(pos), world, pos);
        int count = 0;
        boolean[] array = new boolean[]{
                state.getValue(NORTH),
                state.getValue(SOUTH),
                state.getValue(WEST),
                state.getValue(EAST),
                state.getValue(UP),
                state.getValue(DOWN)
        };
        for (int i = 0; i < array.length; i++)
            if (array[i])
                count++;
        return count;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int[] array = new int[]{
                state.getValue(NORTH) ? 1 : 0,
                state.getValue(SOUTH) ? 1 : 0,
                state.getValue(WEST) ? 1 : 0,
                state.getValue(EAST) ? 1 : 0,
                state.getValue(UP) ? 1 : 0,
                state.getValue(DOWN) ? 1 : 0
        };

        int result = 0;
        for (int i = array.length - 1; i <= 0; i++){
            result += array[array.length - 1 - i] * Math.pow(2, i);
        }
        return result;
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

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        for (EnumFacing facing : EnumFacing.values()) {
            TileEntity te = worldIn.getTileEntity(pos.offset(facing));
            if (te != null && !(te instanceof EnergySender) && te instanceof IEnergyProvider) {
                worldIn.setBlockState(pos, ModBlocks.cableWithTile.getDefaultState());
            }
        }
    }

    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        state = getActualState(state, source, pos);
        boolean
                north = state.getValue(NORTH),
                south = state.getValue(SOUTH),
                west = state.getValue(WEST),
                east = state.getValue(EAST),
                up = state.getValue(UP),
                down = state.getValue(DOWN);

        if (!north && !south && !west && !east && !up && !down)
            return new AxisAlignedBB(0.375d, 0.375d, 0.375d, 0.625d, 0.625d, 0.625d);

        if (north && south && !west && !east && !up && !down)
            return new AxisAlignedBB(0.375d, 0.375d, 0, 0.625d, 0.625d, 1);
        if (!north && !south && west && east && !up && !down)
            return new AxisAlignedBB(0, 0.375d, 0.375d, 1, 0.625d, 0.625d);
        if (!north && !south && !west && !east && up && down)
            return new AxisAlignedBB(0.375d, 0, 0.375d, 0.625d, 1, 0.625d);

        if (north && !south && !west && !east && !up && !down)
            return new AxisAlignedBB(0.375d, 0.375d, 0, 0.625d, 0.625d, 0.625d);
        if (!north && !south && west && !east && !up && !down)
            return new AxisAlignedBB(0, 0.375d, 0.375d, 0.625d, 0.625d, 0.625d);
        if (!north && !south && !west && !east && up)
            return new AxisAlignedBB(0.375d, 0.375d, 0.375d, 0.625d, 1, 0.625d);
        if (!north && south && !west && !east && !up && !down)
            return new AxisAlignedBB(0.375d, 0.375d, 1, 0.625d, 0.625d, 0.375d);
        if (!north && !south && !west && east && !up && !down)
            return new AxisAlignedBB(1, 0.375d, 0.375d, 0.375d, 0.625d, 0.625d);
        if (!north && !south && !west && !east)
            return new AxisAlignedBB(0.375d, 0.625d, 0.375d, 0.625d, 0, 0.625d);

        if (!up && !down)
            return new AxisAlignedBB(0, 0.375d, 0, 1, 0.625d, 1);

        if (!north & !south)
            return new AxisAlignedBB(0, 0, 0.375d, 1, 1, 0.625d);

        if (!east & !west)
            return new AxisAlignedBB(0.375d, 0, 0, 0.625d, 1, 1);

        return FULL_BLOCK_AABB;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(ModUtils.getDescription(stack.getItem()));
    }
}
