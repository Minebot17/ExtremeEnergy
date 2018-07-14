package ru.minebot.extreme_energy.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.energy.IFieldCreatorEnergy;
import ru.minebot.extreme_energy.init.ModBlocks;
import ru.minebot.extreme_energy.init.ModGuiHandler;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.modules.ItemCapacityModule;
import ru.minebot.extreme_energy.tile_entities.TileEntityEB;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
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

        NBTTagCompound tag = ModUtils.getNotNullCategory(stack);
        if (!tag.hasKey("energy"))
            return;

        TileEntityEB te = (TileEntityEB) worldIn.getTileEntity(pos);
        if (tag.getInteger("cells") != 0)
            te.setInventorySlotContents(2, new ItemStack(ModItems.capacityModule, tag.getInteger("cells")));

        te.updateValueModules();
        te.setEnergy(tag.getInteger("energy"));
        te.markDirty();
        worldIn.notifyBlockUpdate(te.getPos(), worldIn.getBlockState(te.getPos()), worldIn.getBlockState(te.getPos()), 0);
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

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(ModUtils.getDescription(stack.getItem()));
        NBTTagCompound tag = ModUtils.getNotNullCategory(stack);
        if (tag.hasKey("energy")){
            tooltip.add(tag.getInteger("energy")+"/"+tag.getInteger("maxEnergy")+" RF");
            tooltip.add(tag.getInteger("cells")+" capacity modules");
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
        TileEntityEB te = (TileEntityEB) world.getTileEntity(pos);
        if (te != null){
            ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("energy", te.getEnergyStored());
            tag.setInteger("maxEnergy", te.getMaxEnergyStored());
            tag.setInteger("cells", (te.getMaxEnergyStored()-100000)/100000);

            ModUtils.getNotNullTag(stack).setTag(ExtremeEnergy.NBT_CATEGORY, tag);
            world.spawnEntity(new EntityItem(world, te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), stack));

            if (te.getMaxEnergyStored() != 100000){
                int index = -1;
                for (int i = 2; i < te.getSizeInventory(); i++){
                    if (te.getStackInSlot(i).getItem() instanceof ItemCapacityModule)
                        index = i;
                }
                if (index != -1)
                    te.removeStackFromSlot(index);
            }
            InventoryHelper.dropInventoryItems(world, pos, te);
        }
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
