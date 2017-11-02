package ru.minebot.extreme_energy.tile_entities;

import cofh.redstoneflux.api.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.blocks.BlockFaceActive;
import ru.minebot.extreme_energy.init.ModBlocks;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.ItemFullCapsule;
import ru.minebot.extreme_energy.modules.Module;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketTransportEnergy;

public class TileEntityTG extends TileEntitySG {

    public int maxMilliBuckets = 10000;
    public int milliBuckets;

    public TileEntityTG(){
        super();
        slots = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY};
    }

    @Override
    public int getSizeInventory() {
        return 4;
    }

    @Override
    public void update() {
        if (!world.isRemote && world.getTotalWorldTime()%5==0) {
            if (receivers.size() != 0){
                for (Receiver receiver : receivers){
                    extractEnergy(receiver.receiver.receiveEnergy(EnumFacing.UP, extractEnergy(100000/receiver.cableCount, true), false), false);
                    IBlockState from = world.getBlockState(getPos());
                    IBlockState to = world.getBlockState(receiver.pos);
                    world.notifyBlockUpdate(getPos(), from, from, 0);
                    world.notifyBlockUpdate(receiver.pos, to, to, 0);
                }
            }

            if (slots[1].getItem() == Items.LAVA_BUCKET && milliBuckets + 1000 < maxMilliBuckets ){
                setInventorySlotContents(1, new ItemStack(Items.BUCKET));
                milliBuckets += 1000;
                ModUtils.setState(world, getPos(), world.getBlockState(getPos()).withProperty(BlockFaceActive.ACTIVE, true));
                markDirty();
                world.notifyBlockUpdate(getPos(), ModBlocks.tg.getDefaultState(), ModBlocks.tg.getDefaultState(), 0);
            }
            if (milliBuckets > 0 && getEnergyStored() < getMaxEnergyStored()){
                milliBuckets -= 50;
                energy += 500 + 500 * ((float)los/12f);
                markDirty();
                world.notifyBlockUpdate(getPos(), ModBlocks.tg.getDefaultState(), ModBlocks.tg.getDefaultState(), 0);
            }
            if (world.getBlockState(getPos()).getValue(BlockFaceActive.ACTIVE) && (milliBuckets <= 0 || getEnergyStored() >= getMaxEnergyStored()))
                ModUtils.setState(world, getPos(), world.getBlockState(getPos()).withProperty(BlockFaceActive.ACTIVE, false));

            if (!slots[0].isEmpty())
                ((IEnergyContainerItem) slots[0].getItem()).receiveEnergy(slots[0], extractEnergy(maxExtract, false), false);
        }
    }

    public ItemStack fill(ItemStack bucket, boolean isCreative){
        if (milliBuckets + 1000 <= maxMilliBuckets){
            milliBuckets += 1000;
            if (!isCreative)
                bucket = new ItemStack(Items.BUCKET);
            ModUtils.setState(world, getPos(), world.getBlockState(getPos()).withProperty(BlockFaceActive.ACTIVE, true));
        }
        return bucket;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        category.setInteger("milliBuckets", milliBuckets);
        nbt.setTag(ExtremeEnergy.NBT_CATEGORY, category);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        milliBuckets = category.getInteger("milliBuckets");
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 0)
            return stack.getItem() instanceof IEnergyContainerItem;
        else if (index == 1)
            return stack.getItem() instanceof ItemBucket;
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
    public Item[] getAcceptedModules() {
        return new Item[]{
                ModItems.lossReduceModule,
                ModItems.capacityModule,
                ModItems.conductionModule
        };
    }
}
