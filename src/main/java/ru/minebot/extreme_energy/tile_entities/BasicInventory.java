package ru.minebot.extreme_energy.tile_entities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import ru.minebot.extreme_energy.other.IModuleProvider;

public abstract class BasicInventory implements IInventory, IModuleProvider {
    private int size;
    private Item[] modules;
    public ItemStack itemStack;
    protected ItemStack[] slots;

    public BasicInventory(ItemStack itemStack, int size, Item[] modules){
        this.size = size;
        this.modules = modules;
        slots = new ItemStack[size];
        for (int i = 0; i < size; i++)
            slots[i] = ItemStack.EMPTY;
        this.itemStack = itemStack;
        if (!itemStack.hasTagCompound())
            save();
        else
            load();
    }

    @Override
    public int getSizeInventory() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        boolean result = true;
        for (int i = 0; i < size; i++)
            if (slots[i] != ItemStack.EMPTY) {
                result = false;
                break;
            }
        return result;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index < 0 || index >= this.getSizeInventory())
            return ItemStack.EMPTY;
        else {
            return slots[index];
        }
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (!this.getStackInSlot(index).isEmpty()) {
            ItemStack itemstack;

            if (this.getStackInSlot(index).getCount() <= count) {
                itemstack = this.getStackInSlot(index);
                this.setInventorySlotContents(index, ItemStack.EMPTY);
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.getStackInSlot(index).splitStack(count);

                if (this.getStackInSlot(index).getCount() <= 0) {
                    this.setInventorySlotContents(index, ItemStack.EMPTY);
                } else {
                    //Just to show that changes happened
                    this.setInventorySlotContents(index, this.getStackInSlot(index));
                }

                this.markDirty();
                return itemstack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (index < 0 || index > getSizeInventory() - 1)
            return ItemStack.EMPTY;

        ItemStack st = slots[index].copy();
        slots[index] = ItemStack.EMPTY;
        markDirty();
        return st;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (!(index < 0 || index > getSizeInventory() - 1)) {
            slots[index] = stack;
            markDirty();
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
        save();
        updateValueModules();
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public abstract boolean isItemValidForSlot(int index, ItemStack stack);

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++)
            slots[i] = ItemStack.EMPTY;
    }

    @Override
    public String getName() {
        return itemStack.getDisplayName();
    }

    @Override
    public boolean hasCustomName() {
        return itemStack.hasDisplayName();
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(itemStack.getDisplayName());
    }

    public abstract void save();

    public abstract void load();

    @Override
    public void updateValueModules() {

    }

    @Override
    public boolean isAccepted(Item item) {
        Item[] items = getAcceptedModules();
        for (int i = 0; i < items.length; i++)
            if (items[i].equals(item))
                return true;
        return false;
    }

    @Override
    public Item[] getAcceptedModules() {
        return modules;
    }
}
