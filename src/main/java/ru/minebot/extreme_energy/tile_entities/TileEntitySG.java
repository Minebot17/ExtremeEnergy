package ru.minebot.extreme_energy.tile_entities;

import cofh.redstoneflux.api.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.ChunkPos;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.blocks.BlockFaceActive;
import ru.minebot.extreme_energy.other.IHeatHandler;
import ru.minebot.extreme_energy.events.Events;
import ru.minebot.extreme_energy.events.events_sg.IEventSG;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.ItemFullCapsule;
import ru.minebot.extreme_energy.items.modules.*;
import ru.minebot.extreme_energy.modules.Module;
import ru.minebot.extreme_energy.other.IModuleProvider;

import javax.annotation.Nullable;

public class TileEntitySG extends EnergySender implements IInventory, IModuleProvider, IHeatHandler {

    public boolean active;
    protected int power;
    protected int maxPower;
    protected int heat;
    protected int maxHeat;
    protected ItemStack[] slots;
    protected String customName;
    protected int freeze;
    protected int los;

    public TileEntitySG(){
        slots = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY};
        capacity = 100000;
        maxExtract = 100;
        maxReceive = 100;
        active = true;

        maxHeat = 1000;
        maxPower = 100;
    }

    @Override
    public void update() {
        super.update();
        if (!world.isRemote && world.getTotalWorldTime()%5==0) {
            if (active && power != 0 && energy < capacity){
                energy = Math.min(capacity, energy + ModUtils.extractEnergyFromChunk(world, new ChunkPos(getPos()), power));
                if (freeze == 0)
                    heat += (int)((float)power/100f) == 0 ? 1 : (int)((float)power/100f);
                if (world.getBlockState(getPos()) != getBlockType().getDefaultState().withProperty(BlockFaceActive.ACTIVE, true)){
                    ModUtils.setState(world, getPos(), world.getBlockState(getPos()).withProperty(BlockFaceActive.ACTIVE, true));
                    markDirty();
                }
            }
            else if (world.getBlockState(getPos()).getValue(BlockFaceActive.ACTIVE)){
                ModUtils.setState(world, getPos(), world.getBlockState(getPos()).withProperty(BlockFaceActive.ACTIVE, false));
                markDirty();
            }
            else if (heat > 0)
                heat -= los+1;

            if (freeze != 0 && heat > 0)
                heat -= 10;

            if (heat < 0)
                heat = 0;
            if (freeze < 0)
                freeze = 0;
            if (freeze > 0)
                freeze -= power/10;

            if (freeze == 0 && slots[1].getItem() instanceof ItemFullCapsule){
                slots[1].shrink(1);
                if (slots[1].getCount() == 0)
                    removeStackFromSlot(1);
                freeze = ItemFullCapsule.freezeTime;
            }

            if (heat >= maxHeat){
                overheat();
                freeze = 10000;
            }

            if (!slots[0].isEmpty())
                ((IEnergyContainerItem) slots[0].getItem()).receiveEnergy(slots[0], extractEnergy(maxExtract, false), false);
            markUpdate();
        }
    }

    public void overheat(){
        IEventSG choice = null;
        IEventSG[] events = Events.sgEvents;
        while(choice == null)
            for (IEventSG event : events){
                float chance = ModUtils.random.nextFloat();
                if (chance < event.getChance(world, getPos()));
                choice = event;
            }
        choice.onEvent(world, getPos());
    }

    @Override
    public int getSizeInventory() {
        return 6;
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
        if (index == 0)
            return stack.getItem() instanceof IEnergyContainerItem;
        else if (index == 1)
            return stack.getItem() instanceof ItemFullCapsule;
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
        return this.hasCustomName() ? this.customName : "container.tile_entity_SG";
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
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);

        category.setBoolean("active", active);
        category.setInteger("power", power);
        category.setInteger("heat", heat);
        category.setInteger("maxPower", maxPower);
        category.setInteger("maxHeat", maxHeat);
        category.setInteger("freeze", freeze);
        category.setInteger("los", los);

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
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);

        active = category.getBoolean("active");
        power = category.getInteger("power");
        heat = category.getInteger("heat");
        maxPower = category.getInteger("maxPower");
        maxHeat = category.getInteger("maxHeat");
        freeze = category.getInteger("freeze");
        los = category.getInteger("los");

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
    public void updateValueModules() {
        int cVoltage = 0;
        int cLossReduction = 0;
        int cCap = 0;
        int cCond = 0;
        int cHeat = 0;

        for (int i = getSizeInventory()-4; i < slots.length; i++)
            if (slots[i] != ItemStack.EMPTY){
                if (slots[i].getItem() instanceof ItemVoltageModule)
                    cVoltage += slots[i].getCount();
                else if (slots[i].getItem() instanceof ItemLossReductionModule)
                    cLossReduction += slots[i].getCount();
                else if (slots[i].getItem() instanceof ItemCapacityModule)
                    cCap += slots[i].getCount();
                else if (slots[i].getItem() instanceof ItemConductionModule)
                    cCond += slots[i].getCount();
                else if (slots[i].getItem() instanceof ItemHeatModule)
                    cHeat += slots[i].getCount();
            }

        maxPower = 100 * cVoltage + 100;
        los = cLossReduction;
        capacity = cCap * 100000 + 100000;
        maxReceive = 300 * cCond + 100;
        maxExtract = 300 * cCond + 100;
        maxHeat = 1000 + 1000 * cHeat;

        if (energy > capacity)
            energy = capacity;
        if (power > maxPower)
            power = maxPower;
        if (heat > maxHeat)
            overheat();
        markDirty();
    }

    @Override
    public Item[] getAcceptedModules() {
        return new Item[]{
                ModItems.voltageModule,
                ModItems.lossReduceModule,
                ModItems.capacityModule,
                ModItems.conductionModule,
                ModItems.heatModule
        };
    }

    private void markUpdate(){
        world.notifyBlockUpdate(getPos(), getBlockType().getDefaultState(), getBlockType().getDefaultState(), 0);
    }

    @Override
    public int getHeat(){
        return heat;
    }

    @Override
    public int getMaxHeat(){
        return maxHeat;
    }

    @Override
    public void setHeat(int heat){
        this.heat = heat;
        markDirty();
        markUpdate();
    }

    @Override
    public void setMaxHeat(int maxHeat){
        this.maxHeat = maxHeat;
        markDirty();
        markUpdate();
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
        markDirty();
        markUpdate();
    }

    public int getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(int maxPower) {
        this.maxPower = maxPower;
        markDirty();
        markUpdate();
    }
}
