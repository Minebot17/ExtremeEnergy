package ru.minebot.extreme_energy.items.capacitors;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.lwjgl.util.vector.Vector3f;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.energy.ItemEnergyContainer;
import ru.minebot.extreme_energy.init.ModGuiHandler;
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
        NBTTagCompound category = ModUtils.getNotNullCategory(stack);
        if (!category.hasKey("state")){
            category.setBoolean("state", false);
            category.setInteger("frequency", 0);
        }
        //try{
            //tagCompound(stack);
            //stack.setItemDamage(capacity - stack.getTagCompound().getInteger("Energy"));
        //}
        //catch (Exception e){}
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!(this instanceof ItemCreativeCapacitor)) {
            BlockPos pos = playerIn.getPosition();
            playerIn.openGui(ExtremeEnergy.instance, ModGuiHandler.CAPACITOR_SCREEN, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        if (stack.hasTagCompound()){
            NBTTagCompound tag = ModUtils.getNotNullCategory(stack);
            tooltip.add(tag.getInteger("Energy") + "/" + capacity +" RF");
            boolean charging = tag.getBoolean("state");
            tooltip.add("Passive charging: " + (charging ? "on" : "off"));
            if (charging)
                tooltip.add("Frequency: " + tag.getInteger("frequency"));
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
