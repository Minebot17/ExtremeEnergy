package ru.minebot.extreme_energy.tile_entities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.oredict.OreDictionary;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.blocks.BlockFaceActive;
import ru.minebot.extreme_energy.blocks.BlockHTF;
import ru.minebot.extreme_energy.energy.FieldReceiverEnergyStandart;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.modules.ItemRadiusModule;
import ru.minebot.extreme_energy.items.modules.ItemVoltageModule;
import ru.minebot.extreme_energy.other.IModuleProvider;

import javax.annotation.Nullable;

public class TileEntityHTF extends FieldReceiverEnergyStandart implements IInventory, IModuleProvider {

    protected ItemStack[] inventory;
    protected String customName;

    public boolean isBurn;
    public int burnPhase;
    protected int burnTime;
    protected int totalBurnTime;

    public TileEntityHTF(){
        this.maxVoltage = 150;
        this.frequency = 0;
        inventory = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY};
        totalBurnTime = 200;
        burnPhase = 0;
    }

    @Override
    public void onField(){
        if (totalBurnTime != voltageToSpeed(voltage)){
            totalBurnTime = voltageToSpeed(voltage);
            burnTime = totalBurnTime;
            burnPhase = 0;
        }
        burnTime--;
        burnPhase = Math.round(((float) (totalBurnTime - burnTime) / (float) totalBurnTime) * 25f);

        if (burnTime < 0) {
            isBurn = false;
            burnPhase = 0;
            burnTime = totalBurnTime;

            if (inventory[1].isEmpty())
                setInventorySlotContents(1, FurnaceRecipes.instance().getSmeltingResult(inventory[0]).copy());
            else
                inventory[1].setCount(inventory[1].getCount() + 1);

            if (inventory[0].getCount() == 1)
                removeStackFromSlot(0);
            else
                inventory[0].setCount(inventory[0].getCount() - 1);
            ModUtils.setState(world, getPos(), world.getBlockState(getPos()).withProperty(BlockFaceActive.ACTIVE, false));
        }
        markDirty();
        markUpdate();
    }

    public boolean isActive(){ return isBurn; };

    @Override
    public void update(){
        super.update();
        if (canSmelt(inventory[0]) && !isBurn) {
            isBurn = true;
            burnPhase = 0;
            burnTime = totalBurnTime;
            ModUtils.setState(world, getPos(), world.getBlockState(getPos()).withProperty(BlockFaceActive.ACTIVE, true));
            markDirty();
            markUpdate();
        } else if (!canSmelt(inventory[0]) && isBurn) {
            isBurn = false;
            burnPhase = 0;
            burnTime = totalBurnTime;
            ModUtils.setState(world, getPos(), world.getBlockState(getPos()).withProperty(BlockFaceActive.ACTIVE, false));
            markDirty();
            markUpdate();
        }
    }

    private int voltageToSpeed(int voltage){
        return (int)(2000f/((float)voltage+10000f)*2000f-222f);
    }

    @Override
    public int getSizeInventory() {
        return 3;
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
        return this.hasCustomName() ? this.customName : "container.tile_entity_HTF";
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
        return new SPacketUpdateTileEntity(getPos(), 1, getUpdateTag());
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
        category.setBoolean("isBurn", isBurn);
        category.setInteger("burnTime", burnTime);
        category.setInteger("burnPhase", burnPhase);
        category.setInteger("totalBurnTime", totalBurnTime);

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
        isBurn = category.getBoolean("isBurn");
        burnTime = category.getInteger("burnTime");
        burnPhase = category.getInteger("burnPhase");

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

    protected boolean canSmelt(ItemStack itemStack){
        if (itemStack.isEmpty())
            return false;

        ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(itemStack);

        if (itemstack.isEmpty())
        {
            return false;
        }
        else
        {
            ItemStack itemstack1 = (ItemStack)this.inventory[1];
            if (itemstack1.isEmpty()) return true;
            if (!itemstack1.isItemEqual(itemstack)) return false;
            int result = itemstack1.getCount() + itemstack.getCount();
            return result <= getInventoryStackLimit() && result <= itemstack1.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
        }
    }

    @Override
    public void updateValueModules(){
        int cVoltage = 0;

        for (int i = 2; i < inventory.length; i++)
            if (inventory[i] != ItemStack.EMPTY && inventory[i].getItem() instanceof ItemVoltageModule){
                cVoltage += inventory[i].getCount();
            }

        maxVoltage = 150 + 150 * cVoltage;
        try {
            if (voltage > maxVoltage)
                setVoltage(maxVoltage);
            markDirty();
            markUpdate();
        }
        catch (NullPointerException e){
            System.out.println("Try to save, when world did not loaded");
        }
    }

    @Override
    public Item[] getAcceptedModules() {
        return new Item[]{ModItems.voltageModule};
    }
}
