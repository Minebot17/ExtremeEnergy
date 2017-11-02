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
import ru.minebot.extreme_energy.energy.FieldReceiverEnergyStandart;
import ru.minebot.extreme_energy.energy.IFieldCreatorEnergy;
import ru.minebot.extreme_energy.energy.IRadiusHandler;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.modules.ItemLossReductionModule;
import ru.minebot.extreme_energy.items.modules.ItemRadiusModule;
import ru.minebot.extreme_energy.items.modules.ItemVoltageModule;
import ru.minebot.extreme_energy.modules.FuncArgs;
import ru.minebot.extreme_energy.modules.ModuleFunctional;
import ru.minebot.extreme_energy.other.IModuleProvider;
import ru.minebot.extreme_energy.other.IPublicPrivateProvider;

import javax.annotation.Nullable;

public class TileEntityFC extends FieldReceiverEnergyStandart implements IInventory, IModuleProvider, IPublicPrivateProvider, IRadiusHandler {

    protected boolean active;
    protected ItemStack[] inventory;
    protected String customName;
    protected int radius;
    protected int maxRadius;
    protected int lossReduce;
    protected int[] ids;

    public TileEntityFC(){
        this.maxVoltage = 150;
        this.maxRadius = 10;
        radius = 1;
        this.frequency = 0;
        inventory = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY,ItemStack.EMPTY, ItemStack.EMPTY};
        active = true;
    }

    @Override
    public boolean isActive() {
        return active && hasFuncModule();
    }

    @Override
    public void onField(){
        ItemStack stack = getFuncModule();
        if (!stack.isEmpty())
            ((ModuleFunctional)stack.getItem()).firstUpdate(getFuncArgs());
    }

    @Override
    public void onLoad() {
        ItemStack stack = getFuncModule();
        if (!stack.isEmpty())
            ((ModuleFunctional)stack.getItem()).loadWorld(getFuncArgs(), active);
    }

    @Override
    public void setLink(BlockPos pos){
        super.setLink(pos);
        if (!getFuncModule().isEmpty())
            ((ModuleFunctional)getFuncModule().getItem()).changeLink(getFuncArgs());
    }

    @Override
    public int getSizeInventory() {
        return 8;
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
                removeStackFromSlot(index);
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.getStackInSlot(index).splitStack(count);

                if (this.getStackInSlot(index).getCount() <= 0) {
                    this.removeStackFromSlot(index);
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
        if (inventory[index].getItem() instanceof ModuleFunctional)
            ((ModuleFunctional)inventory[index].getItem()).removeModule(getFuncArgs());
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
        if (stack.getItem() instanceof ModuleFunctional)
            ((ModuleFunctional)stack.getItem()).putModule(getFuncArgs());
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
        ItemStack stack = getFuncModule();
        if (!stack.isEmpty())
            ((ModuleFunctional)stack.getItem()).openInventory(getFuncArgs());
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        ItemStack stack = getFuncModule();
        if (!stack.isEmpty())
            ((ModuleFunctional)stack.getItem()).closeInventory(getFuncArgs());
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return isAccepted(stack.getItem()) && (!(stack.getItem() instanceof ModuleFunctional) || !hasFuncModule());
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
        return this.hasCustomName() ? this.customName : "container.tile_entity_FC";
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
        return new SPacketUpdateTileEntity(getPos(), 4, getUpdateTag());
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

        if (ids != null){
            category.setBoolean("isPublic", false);
            category.setIntArray("ids", ids);
        }
        else
            category.setBoolean("isPublic", true);

        category.setBoolean("isActive", active);
        category.setInteger("FieldMaxRadius", maxRadius);
        category.setInteger("FieldRadius", radius);

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

        if (!category.getBoolean("isPublic")) {
            ids = category.getIntArray("ids");
        }
        else
            ids = null;

        active = category.getBoolean("isActive");
        maxRadius = category.getInteger("FieldMaxRadius");
        radius = category.getInteger("FieldRadius");

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

        for (int i = 0; i < inventory.length; i++)
            if (inventory[i] != ItemStack.EMPTY){
                if (inventory[i].getItem() instanceof ItemVoltageModule)
                    cVoltage += inventory[i].getCount();
                else if (inventory[i].getItem() instanceof ItemRadiusModule)
                    cRadius += inventory[i].getCount();
                else if (inventory[i].getItem() instanceof ItemLossReductionModule)
                    cLossReduction += inventory[i].getCount();
            }

        maxVoltage = 150 + 150 * cVoltage;
        maxRadius = 10 + 5 * cRadius;
        lossReduce = cLossReduction;
        try {
            if (voltage > maxVoltage)
                setVoltage(maxVoltage);
            if (radius > maxRadius)
                setRadius(maxRadius);
            markDirty();
            markUpdate();
        }
        catch (NullPointerException e){System.out.println("Try to save, when world did not loaded");}
    }

    @Override
    public Item[] getAcceptedModules() {
        return new Item[]{
                ModItems.voltageModule,
                ModItems.lossReduceModule,
                ModItems.radiusModule,
                ModItems.lifeModule,
                ModItems.electricalModule,
                ModItems.aggressiveModule,
                ModItems.strangeModule,
                ModItems.luckModule,
                ModItems.teleportModule,
                ModItems.plantModule,
                ModItems.waterModule,
                ModItems.lavaModule,
                ModItems.windModule,
                ModItems.shieldModule,
                ModItems.lightningModule
        };
    }

    public boolean hasFuncModule(){
        for (int i = 0; i < inventory.length; i++)
            if (inventory[i].getItem() instanceof ModuleFunctional)
                return true;
        return false;
    }

    public ItemStack getFuncModule(){
        for (int i = 0; i < inventory.length; i++)
            if (inventory[i].getItem() instanceof ModuleFunctional)
                return inventory[i];
        return ItemStack.EMPTY;
    }

    @Override
    public int getMaxRadius() {
        return maxRadius;
    }

    @Override
    public int getRadius() {
        return radius;
    }

    @Override
    public void setRadius(int radius) {
        this.radius = radius;
        ItemStack stack = getFuncModule();
        if (!stack.isEmpty())
            ((ModuleFunctional)stack.getItem()).changeRadius(getFuncArgs());
        markDirty();
        markUpdate();
    }

    @Override
    public void setMaxRadius(int radius) {
        this.maxRadius = radius;
        markDirty();
        markUpdate();
    }

    @Override
    public void setVoltage(int voltage){
        super.setVoltage(voltage);
        ItemStack stack = getFuncModule();
        if (!stack.isEmpty())
            ((ModuleFunctional)stack.getItem()).changeVoltage(getFuncArgs());
    }

    public void setActive(boolean active) {
        this.active = active;
        ItemStack stack = getFuncModule();
        if (!stack.isEmpty())
            ((ModuleFunctional)stack.getItem()).changeActive(getFuncArgs(), active);
    }

    public boolean getActive(){
        return active;
    }

    public String[] getModuleInfo(){
        ItemStack stack = getFuncModule();
        if (!stack.isEmpty())
            return ((ModuleFunctional)stack.getItem()).getInfo();
        return null;
    }

    @Override
    public void cardsChanged() {
        if (!isPublic())
            setPrivate();
    }

    @Override
    public void setPrivate() {
        int[] toArray = new int[5];
        for (int i = 0; i < 5; i++)
            if (!inventory[i].isEmpty()) {
                try { toArray[i] = ModUtils.getNotNullCategory(inventory[i]).getInteger("id"); }
                catch (NullPointerException e){ toArray[i] = 0; }
            }
        ids = toArray;
        markDirty();
        markUpdate();
    }

    @Override
    public void setPublic() {
        ids = null;
        markDirty();
        markUpdate();
    }

    @Override
    public boolean isPublic() {
        return ids == null;
    }

    public FuncArgs getFuncArgs(){
        return new FuncArgs(world, getPos(), getFrequency(), radius, voltage, lossReduce, isPublic(), ids);
    }

    public int[] getIds(){
        return ids;
    }
}
