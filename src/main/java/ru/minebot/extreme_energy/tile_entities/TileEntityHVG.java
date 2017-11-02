package ru.minebot.extreme_energy.tile_entities;

import cofh.redstoneflux.api.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.energy.FieldCreatorStandart;
import net.minecraft.util.ITickable;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModSoundHandler;
import ru.minebot.extreme_energy.items.crystals.Crystal;
import ru.minebot.extreme_energy.items.modules.*;
import ru.minebot.extreme_energy.modules.Module;
import ru.minebot.extreme_energy.modules.ModuleAddValue;
import ru.minebot.extreme_energy.other.IModuleProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class TileEntityHVG extends FieldCreatorStandart implements IInventory, ITickable, IModuleProvider {

    private ItemStack[] inventory;
    private String customName;

    public TileEntityHVG(){
        inventory = new ItemStack[6];
        for (int i = 0; i < inventory.length; i++)
            inventory[i] = ItemStack.EMPTY;
        this.capacity = 100000;
        this.maxReceive = 1000;
        this.maxRadius = 10;
        this.maxVoltage = 150;
        this.radius = 1;
        this.voltage = 10;
        this.frequency = 0;
        links = new ArrayList<>();
    }

    @Override
    public void update() {
        super.update();
        ItemStack stack = getStackInSlot(0);

        if (world.getTotalWorldTime()%5==0 && !stack.isEmpty() && getEnergyStored() < getMaxEnergyStored()) {
            if (stack.getItem() instanceof  IEnergyContainerItem)
                receiveEnergy(((IEnergyContainerItem) stack.getItem()).extractEnergy(stack, maxReceive, false), false);
            else if (stack.getItem() instanceof Crystal)
                receiveEnergy(((Crystal)stack.getItem()).extract(stack, this, 0), false);
        }
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

    public String getCustomName() {
        return this.customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    public int getSizeInventory() {
        return 6;
    }

    @Override
    public boolean isEmpty() {
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
        else {
            return inventory[index];
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
        ItemStack result = inventory[index].copy();
        inventory[index] = ItemStack.EMPTY;
        updateValueModules();
        this.markDirty();
        return ItemStack.EMPTY;
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
        if (index == 0)
            return (stack.getItem() instanceof IEnergyContainerItem) || (stack.getItem() instanceof Crystal);
        else if (isAccepted(stack.getItem())) {
            if (((Module) stack.getItem()).isMultiply())
                return true;
            else {
                boolean result = true;
                for (int i = 1; i < inventory.length; i++)
                    if (inventory[i].getItem().equals(stack.getItem())) {
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
        for (int i = 0; i < inventory.length; i++)
            inventory[i] = ItemStack.EMPTY;
        updateValueModules();
        this.markDirty();
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.tile_entity_HVG";
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.equals("");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        nbt = writeToNBTEnergy(nbt);
        nbt = writeToNBTFieldStats(nbt);
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        category.setBoolean("fieldActive", active);

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

        readFromNBTEnergy(nbt);
        readFromNBTFieldStats(nbt);
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        active = category.getBoolean("fieldActive");

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
    public void updateValueModules(){
        int cVoltage = 0;
        int cRadius = 0;
        int cLossReduction = 0;
        int cSecurity = 0;
        int cCap = 0;
        int cCond = 0;

        for (int i = 1; i < inventory.length; i++)
            if (inventory[i] != ItemStack.EMPTY){
                if (inventory[i].getItem() instanceof ItemVoltageModule)
                    cVoltage += inventory[i].getCount();
                else if (inventory[i].getItem() instanceof ItemRadiusModule)
                    cRadius += inventory[i].getCount();
                else if (inventory[i].getItem() instanceof ItemLossReductionModule)
                    cLossReduction += inventory[i].getCount();
                else if (inventory[i].getItem() instanceof ItemSecurityModule)
                    cSecurity += inventory[i].getCount();
                else if (inventory[i].getItem() instanceof ItemCapacityModule)
                    cCap += inventory[i].getCount();
                else if (inventory[i].getItem() instanceof ItemConductionModule)
                    cCond += inventory[i].getCount();
            }

        maxVoltage = 150 + 150 * cVoltage;
        maxRadius = 10 + 5 * cRadius;
        lossReduce = cLossReduction;
        enableEvents = cSecurity == 0;
        capacity = 100000 + 100000 * cCap;
        maxReceive = 100 + 300 * cCond;
        try {
            if (energy > capacity)
                energy = capacity;
            if (voltage > maxVoltage)
                setVoltage(maxVoltage);
            if (radius > maxRadius)
                setRadius(maxRadius);
            applyLinkedBlocks();
        }
        catch (NullPointerException e){System.out.println("Try to save, when world did not loaded");}
    }

    @Override
    public Item[] getAcceptedModules() {
        return new Item[]{
                ModItems.voltageModule,
                ModItems.radiusModule,
                ModItems.lossReduceModule,
                ModItems.securityModule,
                ModItems.capacityModule,
                ModItems.conductionModule
        };
    }
}
