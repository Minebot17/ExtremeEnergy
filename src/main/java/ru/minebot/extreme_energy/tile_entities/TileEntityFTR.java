package ru.minebot.extreme_energy.tile_entities;

import cofh.redstoneflux.api.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.energy.IFieldCreatorEnergy;
import ru.minebot.extreme_energy.energy.IFieldReceiverEnergy;
import ru.minebot.extreme_energy.init.ModConfig;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.modules.ItemCapacityModule;
import ru.minebot.extreme_energy.items.modules.ItemConductionModule;
import ru.minebot.extreme_energy.items.modules.ItemVoltageModule;
import ru.minebot.extreme_energy.modules.Module;
import ru.minebot.extreme_energy.other.IModuleProvider;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityFTR extends EnergySender implements IFieldReceiverEnergy, IModuleProvider, IInventory {
    protected BlockPos link;
    protected int voltage;
    protected int maxVoltage;
    protected int frequency;
    public boolean active;
    protected ItemStack[] slots;
    protected String customName;

    public TileEntityFTR(){
        slots = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY};
        active = false;
        maxVoltage = 150;
        maxExtract = 1000;
        maxReceive = 1000;
        capacity = 200000;
    }

    @Override
    public void update(){
        super.update();
        if (!slots[0].isEmpty())
            ((IEnergyContainerItem) slots[0].getItem()).receiveEnergy(slots[0], extractEnergy(maxExtract, false), false);
    }

    @Override
    public int getSizeInventory() {
        return 3;
    }

    @Override
    public boolean isEmpty() {
        boolean result = true;
        for (int i = 0; i < getSizeInventory(); i++)
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
    public boolean isItemValidForSlot(int index, ItemStack stack){
        if (index == 0)
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
        for (int i = 0; i < getSizeInventory(); i++)
            slots[i] = ItemStack.EMPTY;
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
    public String getName() {
        return customName;
    }

    @Override
    public boolean hasCustomName() {
        return !(customName == null || customName.equals(""));
    }

    @Override
    public void applyCreatorsAround(){
        List<TileEntity> tes = ModUtils.radiusFilter(getPos(), world.loadedTileEntityList, 100);
        for (int i = 0; i < tes.size(); i++) {
            if (tes.get(i) instanceof IFieldCreatorEnergy)
                ((IFieldCreatorEnergy) tes.get(i)).applyLinkedBlocks();
        }
    }

    @Override
    public void onField(){
        if (!world.isRemote && active && world.getTotalWorldTime()%5 == 0){
            receiveEnergy(voltage, false);
            markUpdate();
        }
    }

    @Override
    public int getVoltage(){ return voltage; }

    @Override
    public int getMaxVoltage() {
        return maxVoltage;
    }

    @Override
    public int getFrequency(){ return frequency;}

    @Override
    public BlockPos getLink(){
        return link;
    }

    @Override
    public void setLink(BlockPos pos){
        if (pos == null)
            voltage = 0;
        link = pos;
        markDirty();
        markUpdate();
    }

    @Override
    public boolean hasField(){
        return link != null;
    }

    @Override
    public void setFrequency(int frequency){
        this.frequency = frequency;
        applyCreatorsAround();
        markDirty();
        markUpdate();
    }

    @Override
    public void setVoltage(int voltage){
        this.voltage = voltage;
        if (voltage > maxVoltage)
            exceedingVoltage(voltage);
        markDirty();
        markUpdate();
    }

    public void exceedingVoltage(int voltage){
        if (!world.isRemote && ModConfig.explodeMachines) {
            if (link != null)
                ((IFieldCreatorEnergy) world.getTileEntity(link)).brokeLink(getPos());
            world.createExplosion(new EntityEgg(world, getPos().getX(), getPos().getY(), getPos().getZ()), getPos().getX(), getPos().getY(), getPos().getZ(), voltage / maxVoltage, true);
        }
    }

    protected void markUpdate(){
        world.notifyBlockUpdate(getPos(), getBlockType().getDefaultState(), getBlockType().getDefaultState(), 0);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
        nbt = super.writeToNBT(nbt);
        nbt = writeToNBTFieldStats(nbt);
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        category.setBoolean("active", active);

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
    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);
        readFromNBTFieldStats(nbt);
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        active = category.getBoolean("active");

        NBTTagList list = category.getTagList("Items", 10);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot") & 255;
            //this.setInventorySlotContents(slot, new ItemStack(stackTag));
            slots[slot] = new ItemStack(stackTag);
        }
        if (nbt.hasKey("CustomName", 8)) {
            this.setCustomName(nbt.getString("CustomName"));
        }
    }

    public NBTTagCompound writeToNBTFieldStats(NBTTagCompound nbt){
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        if (hasField()) {
            category.setBoolean("HasLink", true);
            category.setInteger("LinkX", link.getX());
            category.setInteger("LinkY", link.getY());
            category.setInteger("LinkZ", link.getZ());
        }
        else
            category.setBoolean("HasLink", false);

        category.setInteger("FieldVoltage", voltage);
        category.setInteger("FieldMaxVoltage", maxVoltage);
        category.setInteger("FieldFrequency", frequency);
        nbt.setTag(ExtremeEnergy.NBT_CATEGORY, category);
        return  nbt;
    }

    public void readFromNBTFieldStats(NBTTagCompound nbt){
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        if (category.getBoolean("HasLink")){
            link = new BlockPos(category.getInteger("LinkX"), category.getInteger("LinkY"), category.getInteger("LinkZ"));
        }
        else
            link = null;
        voltage = category.getInteger("FieldVoltage");
        maxVoltage = category.getInteger("FieldMaxVoltage");
        frequency = category.getInteger("FieldFrequency");
    }

    public String getCustomName() {
        return this.customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    public void updateValueModules() {
        int cVoltage = 0;
        int cCap = 0;
        int cCond = 0;

        for (int i = 1; i < slots.length; i++)
            if (slots[i] != ItemStack.EMPTY){
                if (slots[i].getItem() instanceof ItemVoltageModule)
                    cVoltage += slots[i].getCount();
                else if (slots[i].getItem() instanceof ItemCapacityModule)
                    cCap += slots[i].getCount();
                else if (slots[i].getItem() instanceof ItemConductionModule)
                    cCond += slots[i].getCount();
            }

        maxVoltage = 150 + 150 * cVoltage;
        capacity = 100000 * cCap + 100000;
        maxReceive = 100 + 300 * cCond;
        maxExtract = 100 + 300 * cCond;
        try {
            if (voltage > maxVoltage)
                setVoltage(maxVoltage);
        }
        catch (NullPointerException e){System.out.println("Try to save, when world did not loaded");}
        markUpdate();
        markDirty();
    }

    @Override
    public Item[] getAcceptedModules() {
        return new Item[]{
                ModItems.voltageModule,
                ModItems.capacityModule,
                ModItems.conductionModule
        };
    }

    @Override
    public boolean isActive() {
        return energy < capacity;
    }
}
