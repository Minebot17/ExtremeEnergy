package ru.minebot.extreme_energy.tile_entities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.blocks.BlockFaceActive;
import ru.minebot.extreme_energy.energy.FieldReceiverEnergyStandart;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.other.IModuleProvider;

import javax.annotation.Nullable;

public abstract class TileEntityProcessingBlock extends FieldReceiverEnergyStandart implements IInventory, IModuleProvider {
    protected ItemStack[] inventory;
    protected String customName;

    public boolean isWork;
    public int workPhase;
    protected int workTime;
    protected int totalWorkTime;
    protected int needEnergy;

    private int slotCount;
    private int stages;
    private boolean setState;
    private Item[] acceptedModules;

    public TileEntityProcessingBlock(int slotCount, int stages, boolean setState, Item... acceptedModules){
        this.slotCount = slotCount;
        this.stages = stages;
        this.setState = setState;
        this.acceptedModules = acceptedModules;
        maxVoltage = 150;
        frequency = 0;
        totalWorkTime = 200;
        workPhase = 0;
        workTime = 0;
        isWork = false;

        inventory = new ItemStack[slotCount];
        for (int i = 0; i < slotCount; i++)
            inventory[i] = ItemStack.EMPTY;
    }

    @Override
    public void onField(){
        if (totalWorkTime != needEnergy){
            totalWorkTime = needEnergy;
            workTime = totalWorkTime;
            workPhase = 0;
        }
        workTime -= getVoltage();
        int workPhaseOld = workPhase;
        workPhase = Math.round(((float) (totalWorkTime - workTime) / (float) totalWorkTime) * stages);

        if (workTime < 0) {
            isWork = false;
            workPhase = 0;
            workTime = totalWorkTime;
            onWorked();
            if (setState)
                ModUtils.setState(world, getPos(), world.getBlockState(getPos()).withProperty(BlockFaceActive.ACTIVE, false));

            markDirty();
            markUpdate();
        }
        else if (workPhase != workPhaseOld) {
            markDirty();
            markUpdate();
        }
    }

    @Override
    public void update(){
        super.update();
        if (canWork() && !isWork) {
            isWork = true;
            workPhase = 0;
            workTime = totalWorkTime;
            needEnergy = getNeedEnergy();
            if (setState)
                ModUtils.setState(world, getPos(), world.getBlockState(getPos()).withProperty(BlockFaceActive.ACTIVE, true));
            markDirty();
            markUpdate();
        } else if (!canWork() && isWork) {
            isWork = false;
            workPhase = 0;
            workTime = totalWorkTime;
            needEnergy = getNeedEnergy();
            if (setState)
                ModUtils.setState(world, getPos(), world.getBlockState(getPos()).withProperty(BlockFaceActive.ACTIVE, false));
            markDirty();
            markUpdate();
        }
    }

    protected abstract void onWorked();

    protected abstract boolean canWork();

    protected abstract int getNeedEnergy();

    @Override
    public boolean isActive(){ return isWork; };

    @Override
    public int getSizeInventory() {
        return slotCount;
    }

    @Override
    public boolean isEmpty(){
        boolean result = true;
        for (int i = 0; i < inventory.length; i++)
            if (inventory[i] != ItemStack.EMPTY) {
                result = false;
                break;
            }
        return result;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index < 0 || index >= this.getSizeInventory())
            return ItemStack.EMPTY;
        return this.inventory[index];
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
        ItemStack result = inventory[index].copy();
        inventory[index] = ItemStack.EMPTY;
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

        this.inventory[index] = stack;
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
        markDirty();
        markUpdate();
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
        for (int i = 0; i < inventory.length; i++)
            inventory[i] = ItemStack.EMPTY;
        this.markDirty();
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.tile_entity_processing";
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

        nbt = writeToNBTFieldStats(nbt);
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        category.setBoolean("isWork", isWork);
        category.setInteger("workTime", workTime);
        category.setInteger("workPhase", workPhase);
        category.setInteger("totalWorkTime", totalWorkTime);
        category.setInteger("needEnergy", needEnergy);

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

        readFromNBTFieldStats(nbt);
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        isWork = category.getBoolean("isWork");
        workTime = category.getInteger("workTime");
        workPhase = category.getInteger("workPhase");
        totalWorkTime = category.getInteger("totalWorkTime");
        needEnergy = category.getInteger("needEnergy");

        NBTTagList list = category.getTagList("Items", 10);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot") & 255;
            //this.setInventorySlotContents(slot, new ItemStack(stackTag));
            inventory[slot] = new ItemStack(stackTag);
        }
        if (nbt.hasKey("CustomName", 8)) {
            this.setCustomName(nbt.getString("CustomName"));
        }
    }

    @Override
    public Item[] getAcceptedModules() {
        return acceptedModules;
    }
}
