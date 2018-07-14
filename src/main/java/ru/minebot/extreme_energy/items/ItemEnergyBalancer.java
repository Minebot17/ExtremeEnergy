package ru.minebot.extreme_energy.items;

import cofh.redstoneflux.api.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.init.ModGuiHandler;
import ru.minebot.extreme_energy.init.ModUtils;

import javax.annotation.Nullable;
import java.util.List;

public class ItemEnergyBalancer extends Item {
    public ItemEnergyBalancer(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.ENERGYBALANCER.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.ENERGYBALANCER.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!world.isRemote && world.getTotalWorldTime()%10==0){
            ItemStack[] slots = new ItemStack[] {ItemStack.EMPTY, ItemStack.EMPTY};
            NBTTagCompound category = ModUtils.getNotNullCategory(itemStack);
            NBTTagList list = (NBTTagList) category.getTag("Items");
            if (list == null)
                return;

            for (int i = 0; i < list.tagCount(); ++i) {
                NBTTagCompound stackTag = list.getCompoundTagAt(i);
                int slot = stackTag.getByte("Slot") & 255;
                slots[slot] = new ItemStack(stackTag);
            }

            if (!slots[0].isEmpty() && !slots[1].isEmpty()) {
                float percent = ModUtils.getNotNullCategory(itemStack).getInteger("sliderPos")/21f;
                IEnergyContainerItem left = (IEnergyContainerItem) slots[0].getItem();
                IEnergyContainerItem right = (IEnergyContainerItem) slots[1].getItem();
                int maxLeft = left.getMaxEnergyStored(slots[0]);
                int maxRight = right.getMaxEnergyStored(slots[1]);
                int leftEnergy = left.getEnergyStored(slots[0]);
                int rightEnergy = right.getEnergyStored(slots[1]);
                int allEnergy = leftEnergy + rightEnergy;
                int toRight = (int)Math.min(maxRight, percent * allEnergy);
                int toLeft = allEnergy - toRight;
                if (toLeft > maxLeft) {
                    toLeft = maxLeft;
                    toRight = allEnergy - toLeft;
                }

                NBTTagCompound category2 = ModUtils.getNotNullCategory(itemStack);
                if (leftEnergy < toLeft) {
                    int extract = left.receiveEnergy(slots[0], right.extractEnergy(slots[1], toLeft - leftEnergy, true), true);
                    left.receiveEnergy(slots[0], right.extractEnergy(slots[1], extract, false), false);
                    category2.setBoolean("done", false);
                }
                else if (rightEnergy < toRight) {
                    int extract = right.receiveEnergy(slots[1], left.extractEnergy(slots[0], toRight - rightEnergy, true) , true);
                    right.receiveEnergy(slots[1], left.extractEnergy(slots[0], extract, false) , false);
                    category2.setBoolean("done", false);
                }
                else
                    category2.setBoolean("done", true);


                NBTTagList list2 = new NBTTagList();
                for (int i = 0; i < 2; ++i) {
                    if (!slots[i].isEmpty()) {
                        NBTTagCompound stackTag = new NBTTagCompound();
                        stackTag.setByte("Slot", (byte) i);
                        stackTag = slots[i].writeToNBT(stackTag);
                        list2.appendTag(stackTag);
                    }
                }
                category2.setTag("Items", list2);
                itemStack.getTagCompound().setTag(ExtremeEnergy.NBT_CATEGORY, category);
            }
            else
                ModUtils.getNotNullCategory(itemStack).setBoolean("done", true);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        BlockPos pos = playerIn.getPosition();
        playerIn.openGui(ExtremeEnergy.instance, ModGuiHandler.ENERGYBALANCER_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(ModUtils.getDescription(stack.getItem()));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem();
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return oldStack.getItem() != newStack.getItem();
    }
}
