package ru.minebot.extreme_energy.tile_entities;

import cofh.redstoneflux.api.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.ForgeEventFactory;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.blocks.BlockFaceActive;
import ru.minebot.extreme_energy.init.ModBlocks;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.Module;

public class TileEntityFG extends TileEntitySG {

    public boolean isBurn;
    public int burnPhase;
    protected int burnTime;
    protected int totalBurnTime;

    public TileEntityFG(){
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
        }

        if (!world.isRemote && !isBurn && getEnergyStored() < getMaxEnergyStored() && !slots[1].isEmpty()){
            isBurn = true;
            totalBurnTime = getBurnTime(slots[1]);
            if (los != 0)
                totalBurnTime += getBurnTime(slots[1])*((float)los/12f);
            burnTime = totalBurnTime;
            burnPhase = 0;
            if (slots[1].getCount() != 1)
                slots[1].setCount(slots[1].getCount() - 1);
            else
                removeStackFromSlot(1);
            ModUtils.setState(world, getPos(), world.getBlockState(getPos()).withProperty(BlockFaceActive.ACTIVE, true));
            markDirty();
            markUpdate();
        }

        if (!world.isRemote && isBurn  && world.getTotalWorldTime()%5==0 && getEnergyStored() < getMaxEnergyStored()){
            burnTime -= 5;
            burnPhase = Math.round(((float) (totalBurnTime - burnTime) / (float) totalBurnTime) * 13f);
            energy = Math.min(capacity, energy + 100);

            if (burnTime <= 0){
                isBurn = false;
                burnPhase = 0;
                burnTime = 0;
                totalBurnTime = 0;
                ModUtils.setState(world, getPos(), world.getBlockState(getPos()).withProperty(BlockFaceActive.ACTIVE, false));
            }

            markDirty();
            markUpdate();
        }

        if (!slots[0].isEmpty())
            ((IEnergyContainerItem) slots[0].getItem()).receiveEnergy(slots[0], extractEnergy(maxExtract, false), false);
    }

    private int getBurnTime(ItemStack stack){
        if (stack.isEmpty())
            return 0;

        int burnTime = ForgeEventFactory.getItemBurnTime(stack);
        if (burnTime != -1) return burnTime;
        Item item = stack.getItem();
        return item == Item.getItemFromBlock(Blocks.WOODEN_SLAB) ? 150 : (item == Item.getItemFromBlock(Blocks.WOOL) ? 100 : (item == Item.getItemFromBlock(Blocks.CARPET) ? 67 : (item == Item.getItemFromBlock(Blocks.LADDER) ? 300 : (item == Item.getItemFromBlock(Blocks.WOODEN_BUTTON) ? 100 : (Block.getBlockFromItem(item).getDefaultState().getMaterial() == Material.WOOD ? 300 : (item == Item.getItemFromBlock(Blocks.COAL_BLOCK) ? 16000 : (item instanceof ItemTool && "WOOD".equals(((ItemTool)item).getToolMaterialName()) ? 200 : (item instanceof ItemSword && "WOOD".equals(((ItemSword)item).getToolMaterialName()) ? 200 : (item instanceof ItemHoe && "WOOD".equals(((ItemHoe)item).getMaterialName()) ? 200 : (item == Items.STICK ? 100 : (item != Items.BOW && item != Items.FISHING_ROD ? (item == Items.SIGN ? 200 : (item == Items.COAL ? 1600 : (item != Item.getItemFromBlock(Blocks.SAPLING) && item != Items.BOWL ? (item == Items.BLAZE_ROD ? 2400 : (item instanceof ItemDoor && item != Items.IRON_DOOR ? 200 : (item instanceof ItemBoat ? 400 : 0))) : 100))) : 300)))))))))));
    }

    private void markUpdate(){
        world.notifyBlockUpdate(getPos(), ModBlocks.sg.getDefaultState(), ModBlocks.sg.getDefaultState(), 0);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);

        category.setBoolean("isBurn", isBurn);
        category.setInteger("burnTime", burnTime);
        category.setInteger("burnPhase", burnPhase);

        nbt.setTag(ExtremeEnergy.NBT_CATEGORY, category);

        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        isBurn = category.getBoolean("isBurn");
        burnTime = category.getInteger("burnTime");
        burnPhase = category.getInteger("burnPhase");
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 0)
            return stack.getItem() instanceof IEnergyContainerItem;
        else if (index == 1)
            return getBurnTime(stack) != 0;
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
