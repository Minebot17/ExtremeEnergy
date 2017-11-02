package ru.minebot.extreme_energy.items.capacitors;

import cofh.redstoneflux.api.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.energy.ItemEnergyContainer;
import ru.minebot.extreme_energy.init.ModUtils;

import javax.annotation.Nullable;
import java.util.List;

public class Capacitor extends ItemEnergyContainer {

    public Capacitor(int capacity, int maxReceive, int maxExtract){
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected){
        //try{
            //tagCompound(stack);
            //stack.setItemDamage(capacity - stack.getTagCompound().getInteger("Energy"));
        //}
        //catch (Exception e){}
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        if (stack.hasTagCompound()){
            NBTTagCompound tag = ModUtils.getNotNullCategory(stack);
            tooltip.add(tag.getInteger("Energy") + "/" + capacity +" RF");
        }
        else
            tooltip.add("0/" + capacity + " RF");
    }

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        NBTTagCompound category = ModUtils.getNotNullCategory(container);
        int energy = category.getInteger("Energy");
        int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));

        if (!simulate) {
            energy += energyReceived;
            category.setInteger("Energy", energy);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        NBTTagCompound category = ModUtils.getNotNullCategory(container);
        int energy = category.getInteger("Energy");
        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));

        if (!simulate) {
            energy -= energyExtracted;
            category.setInteger("Energy", energy);
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        NBTTagCompound category = ModUtils.getNotNullCategory(container);
        return category.getInteger("Energy");
    }
}
