package ru.minebot.extreme_energy.tile_entities;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.other.IModuleProvider;

public class InventorySword implements IInventory, IModuleProvider {
    public ItemStack sword;
    private ItemStack[] slots;
    private World world;

    public InventorySword(World world, ItemStack sword){
        this.world = world;
        slots = new ItemStack[1];
        slots[0] = ItemStack.EMPTY;
        this.sword = sword;
        if (!ModUtils.getNotNullTag(sword).hasKey("Items"))
            save();
        else
            load();
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return slots[0].isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index == 0 ? slots[0] : ItemStack.EMPTY;
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
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return isAccepted(stack.getItem());
    }

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
        slots[0] = ItemStack.EMPTY;
    }

    @Override
    public String getName() {
        return sword.getDisplayName();
    }

    @Override
    public boolean hasCustomName() {
        return sword.hasDisplayName();
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(sword.getDisplayName());
    }

    public void save(){
        if (!world.isRemote) {
            //NBTTagCompound category = ModUtils.getNotNullCategory(sword);
            NBTTagList list = new NBTTagList();
            for (int i = 0; i < this.getSizeInventory(); ++i) {
                if (!this.getStackInSlot(i).isEmpty()) {
                    NBTTagCompound stackTag = new NBTTagCompound();
                    stackTag.setByte("Slot", (byte) i);
                    stackTag = this.getStackInSlot(i).writeToNBT(stackTag);
                    list.appendTag(stackTag);
                }
            }
            //category.setTag("Items", list);
            //sword.getTagCompound().setTag(ExtremeEnergy.NBT_CATEGORY, category);
            sword.getTagCompound().setTag("Items", list);
        }
    }

    public void load(){
        //NBTTagCompound category = ModUtils.getNotNullCategory(sword);
        NBTTagList list = sword.getTagCompound().getTagList("Items", 10);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            this.slots[i] = new ItemStack(stackTag);
        }
    }

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
        return new Item[]{
                ModItems.aggressiveModule,
                ModItems.lifeModule,
                ModItems.strangeModule,
                ModItems.electricalModule,
                ModItems.teleportModule,
                ModItems.lavaModule,
                ModItems.windModule,
                ModItems.shieldModule,
                ModItems.lightningModule
        };
    }
}
