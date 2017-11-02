package ru.minebot.extreme_energy.items.crystals;

import cofh.redstoneflux.api.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;

import javax.annotation.Nullable;
import java.util.List;

public class Crystal extends Item {

    protected int maxCharge;
    protected int power; // 1 - 3

    public Crystal(String unlocName, String regName, int maxCharge, int power){
        setUnlocalizedName(unlocName);
        setRegistryName(regName);
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setMaxStackSize(64);
        this.maxCharge = maxCharge;
        this.power = power + 1;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected){
        try {
            if (entityIn instanceof EntityPlayer && !((EntityPlayer) entityIn).isCreative()) {
                EntityPlayer player = (EntityPlayer) entityIn;
                ItemStack item = null;
                for (int i = 0; i < player.inventory.getSizeInventory(); i++)
                    if (player.inventory.getStackInSlot(i).getItem() instanceof IEnergyContainerItem)
                        item = player.inventory.getStackInSlot(i);
                boolean a = true;
                if (item != null)
                    if (((IEnergyContainerItem)item.getItem()).receiveEnergy(item, extract(stack, ((EntityPlayer) entityIn).inventory, itemSlot), false) != 0)
                        a = false;
                if (a) {
                    extract(stack, ((EntityPlayer) entityIn).inventory, itemSlot);
                    player.addPotionEffect(new PotionEffect(Potion.getPotionById(20), 10, power * 2));
                    player.addPotionEffect(new PotionEffect(Potion.getPotionById(1), 10, 1));
                }
            }
        }
        catch (Exception e){}
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
        NBTTagCompound tag = ModUtils.getNotNullCategory(stack);
        if (!tag.hasKey("charge"))
            tag.setInteger("charge", maxCharge);
        if (Minecraft.getMinecraft().player != null && !Minecraft.getMinecraft().player.isCreative())
            tooltip.add(tag.getInteger("charge") + "/" + maxCharge + " RF");
    }

    public int extract(ItemStack stack, IInventory inv, int slot){
        NBTTagCompound tag = ModUtils.getNotNullCategory(stack);
        if (!tag.hasKey("charge"))
            tag.setInteger("charge", maxCharge);
        tag.setInteger("charge", tag.getInteger("charge") - getMaxExtract());
        if (tag.getInteger("charge") < 1) {
            inv.removeStackFromSlot(slot);
            inv.setInventorySlotContents(slot, power == 1 ? new ItemStack(ModItems.smallCrystal) : power == 2 ? new ItemStack(ModItems.crystal) : new ItemStack(ModItems.bigCrystal));
        }
        return getMaxExtract();
    }

    public int getMaxExtract(){ return power == 1 ? 500 : power == 2 ? 5000 : 50000; }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.getItem().equals(newStack.getItem());
    }
}
