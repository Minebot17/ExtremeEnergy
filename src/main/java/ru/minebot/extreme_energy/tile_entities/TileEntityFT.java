package ru.minebot.extreme_energy.tile_entities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.BlockPos;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.energy.FieldTransmitterStandart;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.items.modules.ItemLossReductionModule;
import ru.minebot.extreme_energy.items.modules.ItemRadiusModule;
import ru.minebot.extreme_energy.items.modules.ItemSecurityModule;
import ru.minebot.extreme_energy.items.modules.ItemVoltageModule;
import ru.minebot.extreme_energy.modules.Module;
import ru.minebot.extreme_energy.other.IModuleProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntityFT extends FieldTransmitterStandart implements IInventory, IModuleProvider {
    private ItemStack[] inventory;
    private String customName;

    public TileEntityFT(){
        inventory = new ItemStack[3];
        for (int i = 0; i < inventory.length; i++)
            inventory[i] = ItemStack.EMPTY;
        this.maxRadius = 10;
        this.maxVoltageReceive = 150;
        this.radius = 1;
        links = new ArrayList<>();
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
        return 3;
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
        if (isAccepted(stack.getItem())) {
            if (((Module) stack.getItem()).isMultiply())
                return true;
            else {
                boolean result = true;
                for (int i = 0; i < inventory.length; i++)
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
        return this.hasCustomName() ? this.customName : "container.tile_entity_FT";
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.equals("");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

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

        for (int i = 0; i < inventory.length; i++)
            if (inventory[i] != ItemStack.EMPTY){
                if (inventory[i].getItem() instanceof ItemVoltageModule)
                    cVoltage += inventory[i].getCount();
                else if (inventory[i].getItem() instanceof ItemRadiusModule)
                    cRadius += inventory[i].getCount();
                else if (inventory[i].getItem() instanceof ItemLossReductionModule)
                    cLossReduction += inventory[i].getCount();
                else if (inventory[i].getItem() instanceof ItemSecurityModule)
                    cSecurity += inventory[i].getCount();
            }

        maxVoltageReceive = 150 + 150 * cVoltage;
        maxRadius = 10 + 5 * cRadius;
        lossReduce = cLossReduction;
        enableEvents = cSecurity == 0;
        try {
            if (voltageReceive > maxVoltageReceive)
                exceedingVoltage(voltageReceive);
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
                ModItems.securityModule
        };
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public int convertReceiveVoltage(int voltageReceive) {
        return voltageReceive;
    }

    @Override
    public void onField() {

    }
}
