package ru.minebot.extreme_energy.items.equipment;

import cofh.redstoneflux.api.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.init.ModGuiHandler;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.implants.Implant;
import ru.minebot.extreme_energy.other.ImplantData;

public class ItemEnergyTool extends ItemTool{

    public static int maxCap = 500000;

    public ItemEnergyTool() {
        super(1, 1, ToolMaterial.DIAMOND, null);
        setUnlocalizedName(Reference.ExtremeEnergyItems.ENERGYTOOL.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.ENERGYTOOL.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setMaxDamage(5);
        setHasSubtypes(true);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (playerIn.isSneaking()) {
            BlockPos pos = playerIn.getPosition();
            playerIn.openGui(ExtremeEnergy.instance, ModGuiHandler.TOOL_SCREEN, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return new ActionResult(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        NBTTagCompound tag = ModUtils.getNotNullCategory(stack);
        return tag.getInteger("energy") >= tag.getInteger("power") * 750 ? (getEffective(state, tag.getInteger("mode")) != 0 ? tag.getInteger("power")*7 : 0.5f) : 0.1f;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        return true;
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @javax.annotation.Nullable net.minecraft.entity.player.EntityPlayer player, @javax.annotation.Nullable IBlockState blockState) {
        NBTTagCompound tag = ModUtils.getNotNullCategory(stack);
        return tag.getInteger("energy") >= tag.getInteger("power")*750 ? getEffective(blockState, tag.getInteger("mode")) : 0;
    }

    protected int getEffective(IBlockState block, int mode){
        if (block == null)
            return 3;
        String tool = block.getBlock().getHarvestTool(block);
        if (tool == null)
            return 3;
        if (mode != 0 && ((tool.equals("pickaxe") && mode != 1) || (tool.equals("shovel") && mode != 2) || (tool.equals("axe") && mode != 3) || (tool.equals("hoe") && mode != 4) || (tool.equals("shears") && mode != 5)))
            return 0;
        else
            return 3;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem() ? true : oldStack.getMetadata() != newStack.getMetadata();
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        NBTTagCompound tag = ModUtils.getNotNullCategory(stack);
        if (tag.getInteger("energy") >= tag.getInteger("power")* 750)
            tag.setInteger("energy", tag.getInteger("energy") - tag.getInteger("power") * 750);
        else
            return false;
        return true;
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return oldStack.getItem() != newStack.getItem() ? true : oldStack.getMetadata() != newStack.getMetadata();
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = player.getHeldItem(hand);
        NBTTagCompound tag = ModUtils.getNotNullCategory(itemstack);

        if ((tag.getInteger("mode") == 2 || (tag.getInteger("mode") == 0 && tag.getInteger("rbmmode") == 0) && player.isCreative() ? true : getEnergyForAction(tag)))
            return useShovel(player, worldIn, pos, hand, facing);
        else if ((tag.getInteger("mode") == 4 || tag.getInteger("mode") == 0) && player.isCreative() ? true : getEnergyForAction(tag))
            return useHoe(player, worldIn, pos, hand, facing);
        else
            return EnumActionResult.PASS;
    }

    private boolean getEnergyForAction(NBTTagCompound tag){
        if (tag.getInteger("energy") >= 100){
            tag.setInteger("energy", tag.getInteger("energy") - 100);
            return true;
        }
        else
            return false;
    }

    protected EnumActionResult useHoe(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing){
        ItemStack itemstack = player.getHeldItem(hand);

        if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack))
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(itemstack, player, worldIn, pos);
            if (hook != 0) return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;

            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (facing != EnumFacing.DOWN && worldIn.isAirBlock(pos.up()))
            {
                if (block == Blocks.GRASS || block == Blocks.GRASS_PATH)
                {
                    this.setBlock(itemstack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
                    return EnumActionResult.SUCCESS;
                }

                if (block == Blocks.DIRT)
                {
                    switch ((BlockDirt.DirtType)iblockstate.getValue(BlockDirt.VARIANT))
                    {
                        case DIRT:
                            this.setBlock(itemstack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
                            return EnumActionResult.SUCCESS;
                        case COARSE_DIRT:
                            this.setBlock(itemstack, player, worldIn, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
                            return EnumActionResult.SUCCESS;
                    }
                }
            }

            return EnumActionResult.PASS;
        }
    }

    protected EnumActionResult useShovel(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing){
        ItemStack itemstack = player.getHeldItem(hand);

        if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack))
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (facing != EnumFacing.DOWN && worldIn.getBlockState(pos.up()).getMaterial() == Material.AIR && block == Blocks.GRASS)
            {
                IBlockState iblockstate1 = Blocks.GRASS_PATH.getDefaultState();
                worldIn.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

                if (!worldIn.isRemote)
                {
                    worldIn.setBlockState(pos, iblockstate1, 11);
                }

                return EnumActionResult.SUCCESS;
            }
            else
            {
                return EnumActionResult.PASS;
            }
        }
    }

    protected void setBlock(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state) {
        worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

        if (!worldIn.isRemote)
        {
            worldIn.setBlockState(pos, state, 11);
        }
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, net.minecraft.entity.player.EntityPlayer player) {
        NBTTagCompound tag = ModUtils.getNotNullCategory(itemstack);
        if (player.world.isRemote || player.capabilities.isCreativeMode || !(tag.getBoolean("harvestLeaves") && (tag.getInteger("mode") == 0 || tag.getInteger("mode") == 5))) {
            return false;
        }
        Block block = player.world.getBlockState(pos).getBlock();
        if (block instanceof net.minecraftforge.common.IShearable)
        {
            net.minecraftforge.common.IShearable target = (net.minecraftforge.common.IShearable)block;
            if (target.isShearable(itemstack, player.world, pos))
            {
                java.util.List<ItemStack> drops = target.onSheared(itemstack, player.world, pos,
                        net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.FORTUNE, itemstack));
                java.util.Random rand = new java.util.Random();

                for (ItemStack stack : drops)
                {
                    float f = 0.7F;
                    double d  = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    double d1 = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    double d2 = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    net.minecraft.entity.item.EntityItem entityitem = new net.minecraft.entity.item.EntityItem(player.world, (double)pos.getX() + d, (double)pos.getY() + d1, (double)pos.getZ() + d2, stack);
                    entityitem.setDefaultPickupDelay();
                    player.world.spawnEntity(entityitem);
                }

                player.addStat(net.minecraft.stats.StatList.getBlockStats(block));
                player.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
                return true;
            }
        }
        return false;
    }

    public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
        NBTTagCompound tag = ModUtils.getNotNullCategory(itemstack);
        if (entity.world.isRemote || !((tag.getInteger("mode") == 0 || tag.getInteger("mode") == 5)) && !getEnergyForAction(tag))
        {
            return false;
        }
        if (entity instanceof net.minecraftforge.common.IShearable)
        {
            net.minecraftforge.common.IShearable target = (net.minecraftforge.common.IShearable)entity;
            BlockPos pos = new BlockPos(entity.posX, entity.posY, entity.posZ);
            if (target.isShearable(itemstack, entity.world, pos))
            {
                java.util.List<ItemStack> drops = target.onSheared(itemstack, entity.world, pos,
                        net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.FORTUNE, itemstack));

                java.util.Random rand = new java.util.Random();
                for(ItemStack stack : drops)
                {
                    net.minecraft.entity.item.EntityItem ent = entity.entityDropItem(stack, 1.0F);
                    ent.motionY += rand.nextFloat() * 0.05F;
                    ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                    ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityIn;
            NBTTagCompound data = ModUtils.getNotNullCategory(stack);
            int extractSpeed = 500;

            if (data.getBoolean("powCap") && data.getInteger("energy") != ItemEnergyTool.maxCap) {
                int energy = data.getInteger("energy");
                for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                    ItemStack stack1 = player.inventory.getStackInSlot(i);
                    if (stack1.getItem() instanceof IEnergyContainerItem) {
                        energy += ((IEnergyContainerItem) stack1.getItem()).extractEnergy(stack1, extractSpeed, false);
                    }
                }
                data.setInteger("energy", energy > ItemEnergyTool.maxCap ? ItemEnergyTool.maxCap : energy);
            }

            if (data.getBoolean("powImplant") && data.getInteger("energy") != ItemEnergyTool.maxCap){
                ImplantData iData = player.getCapability(ImplantProvider.IMPLANT, null).getImplant();
                if (iData != null && iData.implant.getInteger("energy") > extractSpeed && iData.implant.getBoolean("isOn")){
                    int energy = data.getInteger("energy");
                    iData.implant.setInteger("energy", iData.implant.getInteger("energy") - extractSpeed);
                    energy += extractSpeed;
                    data.setInteger("energy", energy > ItemEnergyTool.maxCap ? ItemEnergyTool.maxCap : energy);
                }
            }
        }
    }
}
