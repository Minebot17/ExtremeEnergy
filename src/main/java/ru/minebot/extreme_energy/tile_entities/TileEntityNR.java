package ru.minebot.extreme_energy.tile_entities;

import cofh.redstoneflux.api.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.blocks.BlockFaceActive;
import ru.minebot.extreme_energy.other.IHeatHandler;
import ru.minebot.extreme_energy.init.ModConfig;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.ItemFullCapsule;
import ru.minebot.extreme_energy.items.ItemNuclearFuel;
import ru.minebot.extreme_energy.items.modules.ItemCapacityModule;
import ru.minebot.extreme_energy.items.modules.ItemConductionModule;
import ru.minebot.extreme_energy.items.modules.ItemHeatModule;
import ru.minebot.extreme_energy.items.modules.ItemLossReductionModule;
import ru.minebot.extreme_energy.modules.Module;
import ru.minebot.extreme_energy.other.IModuleProvider;

import javax.annotation.Nullable;

public class TileEntityNR extends EnergySender implements IInventory, IModuleProvider, IHeatHandler {
    private final int energyGive = 250;
    public boolean active;
    protected ItemStack[] slots;
    protected String customName;

    protected int fuel;
    protected int heat;
    protected int maxHeat;
    protected int freeze;
    protected int los;

    public TileEntityNR(){
        slots = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY};
        capacity = 1000000;
        maxExtract = 300;
        maxReceive = 300;
        active = true;
        maxHeat = 10000;
    }

    @Override
    public void update(){
        super.update();
        if (world.getTotalWorldTime()%5==0){
            if (active && energy < capacity && fuel > 0){
                energy = Math.min(capacity, energy + energyGive);
                if (freeze == 0)
                    heat += 5;
                if (world.getBlockState(getPos()) != getBlockType().getDefaultState().withProperty(BlockFaceActive.ACTIVE, true)){
                    ModUtils.setState(world, getPos(), world.getBlockState(getPos()).withProperty(BlockFaceActive.ACTIVE, true));
                    markDirty();
                }
                fuel -= 5;
            }
            else if (world.getBlockState(getPos()) != getBlockType().getDefaultState().withProperty(BlockFaceActive.ACTIVE, false)){
                ModUtils.setState(world, getPos(), world.getBlockState(getPos()).withProperty(BlockFaceActive.ACTIVE, false));
                markDirty();
            }
            else if (heat > 0)
                heat -= los*3+1;

            if (freeze != 0 && heat > 0)
                heat -= 10;

            if (heat < 0)
                heat = 0;
            if (freeze < 0)
                freeze = 0;
            if (freeze > 0)
                freeze -= 5;

            if (fuel == 0 && slots[1].getItem() instanceof ItemNuclearFuel){
                slots[1].shrink(1);
                if (slots[1].getCount() == 0)
                    removeStackFromSlot(1);
                fuel = ItemNuclearFuel.workTime;
            }

            if (freeze == 0 && slots[0].getItem() instanceof ItemFullCapsule){
                slots[0].shrink(1);
                if (slots[0].getCount() == 0)
                    removeStackFromSlot(1);
                freeze = ItemFullCapsule.freezeTime;
            }

            if (heat >= maxHeat){
                overheat();
                freeze = 10000;
            }

            if (!slots[2].isEmpty())
                ((IEnergyContainerItem) slots[2].getItem()).receiveEnergy(slots[2], extractEnergy(maxExtract, false), false);
            markUpdate();
        }
    }

    @Override
    public void overheat(){
        if (ModConfig.nuclearExplosionReactor)
            ModUtils.createNuclearExplosion(world, getPos(), true);
        else
            world.createExplosion(null, getPos().getX(), getPos().getY(), getPos().getZ(), 1, true);
    }

    private void markUpdate(){
        world.notifyBlockUpdate(getPos(), getBlockType().getDefaultState(), getBlockType().getDefaultState(), 0);
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
            return stack.getItem() instanceof ItemFullCapsule;
        else if (index == 1)
            return stack.getItem() instanceof ItemNuclearFuel;
        if (index == 2)
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
        return this.hasCustomName() ? this.customName : "container.tile_entity_NR";
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
    public Item[] getAcceptedModules() {
        return new Item[]{
                ModItems.lossReduceModule,
                ModItems.capacityModule,
                ModItems.conductionModule,
                ModItems.heatModule
        };
    }

    @Override
    public void updateValueModules() {
        int cLossReduction = 0;
        int cCap = 0;
        int cCond = 0;
        int cHeat = 0;

        for (int i = getSizeInventory()-3; i < slots.length; i++)
            if (slots[i] != ItemStack.EMPTY){
                if (slots[i].getItem() instanceof ItemLossReductionModule)
                    cLossReduction += slots[i].getCount();
                else if (slots[i].getItem() instanceof ItemCapacityModule)
                    cCap += slots[i].getCount();
                else if (slots[i].getItem() instanceof ItemConductionModule)
                    cCond += slots[i].getCount();
                else if (slots[i].getItem() instanceof ItemHeatModule)
                    cHeat += slots[i].getCount();
            }

        los = cLossReduction;
        capacity = cCap * 100000 + 100000;
        maxReceive = 300 * cCond + 100;
        maxExtract = 300 * cCond + 100;
        maxHeat = 10000 + 1000 * cHeat;

        if (energy > capacity)
            energy = capacity;
        if (heat > maxHeat)
            overheat();
        markDirty();
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
        category.setInteger("fuel", fuel);
        category.setInteger("heat", heat);
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
        fuel = category.getInteger("fuel");
        heat = category.getInteger("heat");
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
    public int getHeat() {
        return heat;
    }

    @Override
    public void setHeat(int heat) {
        this.heat = heat;
    }

    @Override
    public int getMaxHeat() {
        return maxHeat;
    }

    @Override
    public void setMaxHeat(int maxHeat) {
        this.maxHeat = maxHeat;
    }

    public int getFuel() {
        return fuel;
    }
}
