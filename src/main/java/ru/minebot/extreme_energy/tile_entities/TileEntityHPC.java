package ru.minebot.extreme_energy.tile_entities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.ChunkPos;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.blocks.BlockFaceActive;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.recipes.crusher.CrusherRecipes;

import javax.annotation.Nullable;

public class TileEntityHPC extends TileEntityHTF {

    protected int needEnergy;

    public TileEntityHPC(){
        this.maxVoltage = 150;
        this.frequency = 0;
        totalBurnTime = 123;
        burnPhase = 0;
        inventory = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY};
    }

    @Override
    public void onField(){
        if (totalBurnTime != needEnergy){
            totalBurnTime = needEnergy;
            burnTime = totalBurnTime;
            burnPhase = 0;
        }
        burnTime-=voltage;
        burnPhase = Math.round(((float) (totalBurnTime - burnTime) / (float) totalBurnTime) * 30f);

        if (burnTime < 0) {
            isBurn = false;
            burnPhase = 0;
            burnTime = totalBurnTime;

            ItemStack out = CrusherRecipes.getOut(inventory[0].getItem()).copy();
            int c1 = 0;
            int c2 = 0;
            if (isSlotValid(out, 1) && isSlotValid(out, 2)) {
                if (out.getCount() != 1) {
                    c1 = out.getCount() / 2;
                    c2 = out.getCount() - c1;
                } else
                    c1 = 1;
            }
            else if (isSlotValid(out, 1))
                c1 = out.getCount();
            else
                c2 = out.getCount();

            if (c1 != 0){
                if (inventory[1].isEmpty()){
                    ItemStack out1 = out.copy();
                    out1.setCount(c1);
                    setInventorySlotContents(1, out1);
                }
                else
                    inventory[1].setCount(inventory[1].getCount() + c1);
            }
            if (c2 != 0) {
                if (inventory[2].isEmpty()){
                    ItemStack out2 = out.copy();
                    out2.setCount(c2);
                    setInventorySlotContents(2, out2);
                }
                else
                    inventory[2].setCount(inventory[2].getCount() + c2);
            }

            if (inventory[0].getCount() == 1)
                removeStackFromSlot(0);
            else
                inventory[0].setCount(inventory[0].getCount() - 1);
            ModUtils.setState(world, getPos(), world.getBlockState(getPos()).withProperty(BlockFaceActive.ACTIVE, false));
        }
        markDirty();
        markUpdate();
    }

    @Override
    public void update(){
        if (!world.isRemote){
            int voltage = ModUtils.extractEnergyFromChunk(world, new ChunkPos(getPos()), true)/8;
            if (voltage != 0 && link == null) {
                setVoltage(voltage);
                if (isActive())
                    onField();
            }
        }

        if (canSmelt(inventory[0]) && !isBurn) {
            isBurn = true;
            burnPhase = 0;
            needEnergy = CrusherRecipes.getEnergy(inventory[0].getItem());
            burnTime = needEnergy;
            ModUtils.setState(world, getPos(), world.getBlockState(getPos()).withProperty(BlockFaceActive.ACTIVE, true));
            markDirty();
            markUpdate();
        } else if (!canSmelt(inventory[0]) && isBurn) {
            isBurn = false;
            burnPhase = 0;
            needEnergy = CrusherRecipes.getEnergy(inventory[0].getItem());
            burnTime = needEnergy;
            ModUtils.setState(world, getPos(), world.getBlockState(getPos()).withProperty(BlockFaceActive.ACTIVE, false));
            markDirty();
            markUpdate();
        }
    }

    @Override
    public int getSizeInventory() {
        return 4;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.tile_entity_HPC";
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 2, getUpdateTag());
    }

    @Override
    protected boolean canSmelt(ItemStack itemStack){
        if (itemStack.isEmpty())
            return false;

        ItemStack itemstack = CrusherRecipes.getOut(itemStack.getItem()).copy();

        if (itemstack.isEmpty())
        {
            return false;
        }
        else {
            ItemStack itemstack1 = (ItemStack) this.inventory[1];
            ItemStack itemstack2 = (ItemStack) this.inventory[2];
            if (itemstack1.isEmpty() || itemstack2.isEmpty()) return true;
            if (!itemstack1.isItemEqual(itemstack) || !itemstack1.isItemEqual(itemstack)) return false;
            int i1 = itemstack.getCount() + itemstack1.getCount();
            int i2 = itemstack.getCount() + itemstack2.getCount();
            return (i1 <= getInventoryStackLimit() || i2 <= getInventoryStackLimit()) && (i1 <= itemstack1.getMaxStackSize() || i2 <= itemstack1.getMaxStackSize()); // Forge fix: make furnace respect stack sizes in furnace recipes
        }
    }

    protected boolean isSlotValid(ItemStack stack, int index){
        if (!stack.isEmpty()) {
            if (inventory[index].isEmpty() || (inventory[index].isItemEqual(stack) && inventory[index].getCount() + stack.getCount() <= inventory[index].getMaxStackSize()))
                return true;
        }
        return false;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        nbt = writeToNBTFieldStats(nbt);
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        category.setInteger("needEnergy", needEnergy);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        needEnergy = category.getInteger("needEnergy");
    }
}
