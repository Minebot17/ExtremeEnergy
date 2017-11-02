package ru.minebot.extreme_energy.tile_entities;

import cofh.redstoneflux.api.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.init.ModUtils;

public class InventoryEnergyBalancer extends BasicInventory {
    private World world;

    public InventoryEnergyBalancer(World world, ItemStack itemStack) {
        super(itemStack, 2, new Item[0]);
        this.world = world;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return stack.getItem() instanceof IEnergyContainerItem;
    }

    @Override
    public void save() {
        if (!world.isRemote) {
            NBTTagCompound category = ModUtils.getNotNullCategory(itemStack);
            NBTTagList list = new NBTTagList();
            for (int i = 0; i < this.getSizeInventory(); ++i) {
                if (!this.getStackInSlot(i).isEmpty()) {
                    NBTTagCompound stackTag = new NBTTagCompound();
                    stackTag.setByte("Slot", (byte) i);
                    stackTag = this.getStackInSlot(i).writeToNBT(stackTag);
                    list.appendTag(stackTag);
                }
            }
            category.setTag("Items", list);
            itemStack.getTagCompound().setTag(ExtremeEnergy.NBT_CATEGORY, category);
        }
    }

    @Override
    public void load() {
        NBTTagCompound category = ModUtils.getNotNullCategory(itemStack);
        NBTTagList list = (NBTTagList) category.getTag("Items");
        if (list == null)
            return;
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot") & 255;
            slots[slot] = new ItemStack(stackTag);
        }
    }
}
