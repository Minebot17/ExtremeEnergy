package ru.minebot.extreme_energy.tile_entities;

import cofh.redstoneflux.api.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.blocks.BlockEB;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.items.modules.*;
import ru.minebot.extreme_energy.modules.Module;
import ru.minebot.extreme_energy.other.IModuleProvider;

import javax.annotation.Nullable;

public class TileEntityEB extends EnergySender implements IModuleProvider, IInventory, IEnergyReceiver {

    public boolean active;
    protected ItemStack[] slots;
    protected String customName;
    protected EnumFacing outFacing;
    protected int maxMaxExtract;

    public TileEntityEB(){
        slots = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY};
        capacity = 100000;
        maxExtract = 1;
        maxMaxExtract = 100;
        maxReceive = 100;
        active = true;
    }

    @Override
    public void update() {
        super.update();

        if (world.getTotalWorldTime()%5==0) {
            if (!slots[0].isEmpty())
                extractEnergy(((IEnergyContainerItem) slots[0].getItem()).receiveEnergy(slots[0], extractEnergy(maxMaxExtract, true), false), false);

            if (!slots[1].isEmpty())
                receiveEnergy(((IEnergyContainerItem) slots[1].getItem()).extractEnergy(slots[1], receiveEnergy(maxReceive, true), false), false);
        }
    }

    public int getMaxExtract(){
        return maxMaxExtract;
    }

    public void setMaxExtract(int maxExtract){
        this.maxMaxExtract = maxExtract;
        markDirty();
        world.notifyBlockUpdate(getPos(), getBlockType().getDefaultState(), getBlockType().getDefaultState(), 0);
    }

    public int getExtract(){
        return maxExtract;
    }

    public void setExtract(int extract){
        this.maxExtract = extract;
        markDirty();
        world.notifyBlockUpdate(getPos(), getBlockType().getDefaultState(), getBlockType().getDefaultState(), 0);
    }

    @Override
    public int getSizeInventory() {
        return 4;
    }

    @Override
    public boolean isEmpty() {
        boolean result = true;
        for (int i = 0; i < slots.length; i++)
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
        ItemStack result = slots[index].copy();
        slots[index] = ItemStack.EMPTY;
        updateValueModules();
        this.markDirty();
        return result;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index < 0 || index >= this.getSizeInventory())
            return;

        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit())
            stack.setCount(this.getInventoryStackLimit());

        if (!isEmpty() && stack.getCount() == 0)
            stack = ItemStack.EMPTY;

        this.slots[index] = stack;
        updateValueModules();
        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.world.getTileEntity(this.getPos()) == this && player.getDistanceSq(this.pos.add(0.5, 0.5, 0.5)) <= 64;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 0 || index == 1)
            return stack.getItem() instanceof IEnergyContainerItem;
        else if (isAccepted(stack.getItem())) {
            if (((Module) stack.getItem()).isMultiply())
                return true;
            else {
                boolean result = true;
                for (int i = 1; i < slots.length; i++)
                    if (slots[i].getItem().equals(stack.getItem())) {
                        result = false;
                        break;
                    }
                return result;
            }
        }
        else
            return false;
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
        for (int i = 0; i < slots.length; i++)
            slots[i] = ItemStack.EMPTY;
        updateValueModules();
        this.markDirty();
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.tile_entity_EB";
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.equals("");
    }

    public String getCustomName() {
        return this.customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        if (outFacing == null) {
            outFacing = world.getBlockState(getPos()).getValue(BlockEB.FACING);
            outFaces = new EnumFacing[]{outFacing};
        }
        return true;
    }

    @Override
    public void updateValueModules() {
        int cCap = 0;
        int cCond = 0;

        for (int i = 2; i < slots.length; i++)
            if (slots[i] != ItemStack.EMPTY){
                if (slots[i].getItem() instanceof ItemCapacityModule)
                    cCap += slots[i].getCount();
                else if (slots[i].getItem() instanceof ItemConductionModule)
                    cCond += slots[i].getCount();
            }

        capacity = 100000 + 100000 * cCap;
        maxReceive = 1000 + 300 * cCond;
        maxMaxExtract = 1000 + 300 * cCond;
        if (energy > capacity)
            energy = capacity;
        markDirty();
        world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 0);
    }

    @Override
    public Item[] getAcceptedModules() {
        return new Item[]{
                ModItems.capacityModule,
                ModItems.conductionModule
        };
    }

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        return from != outFacing ? receiveEnergy(maxReceive, simulate) : 0;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);

        category.setBoolean("active", active);
        category.setInteger("maxExtract", maxExtract);
        category.setInteger("maxMaxExtract", maxMaxExtract);

        NBTTagList list = new NBTTagList();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            if (!this.getStackInSlot(i).isEmpty()) {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot", (byte) i);
                this.getStackInSlot(i).writeToNBT(stackTag);
                list.appendTag(stackTag);
            }
        }
        category.setTag("Items", list);
        nbt.setTag(ExtremeEnergy.NBT_CATEGORY, category);

        if (this.hasCustomName()) {
            nbt.setString("CustomName", this.getCustomName());
        }
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);

        active = category.getBoolean("active");
        maxExtract = category.getInteger("maxExtract");
        maxMaxExtract = category.getInteger("maxMaxExtract");

        NBTTagList list = category.getTagList("Items", 10);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot") & 255;
            slots[slot] = new ItemStack(stackTag);
        }
        if (nbt.hasKey("CustomName", 8)) {
            this.setCustomName(nbt.getString("CustomName"));
        }
    }
}
