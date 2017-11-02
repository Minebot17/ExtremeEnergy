package ru.minebot.extreme_energy.tile_entities;

import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.ChunkPos;
import ru.minebot.extreme_energy.blocks.BlockFaceActive;
import ru.minebot.extreme_energy.energy.FieldReceiverEnergyStandart;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.assembler.ItemElectricalCarver;
import ru.minebot.extreme_energy.recipes.assembler.AssemblerRecipes;
import ru.minebot.extreme_energy.recipes.crusher.CrusherRecipes;

import javax.annotation.Nullable;

public class TileEntityHPA extends TileEntityHPC{

    public TileEntityHPA(){
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

            ItemStack out = AssemblerRecipes.getOut(inventory[0].getItem(), inventory[1].getItem()).copy();

            if (!(inventory[0].getItem() instanceof ItemElectricalCarver)) {
                if (inventory[0].getCount() == 1)
                    removeStackFromSlot(0);
                else
                    inventory[0].setCount(inventory[0].getCount() - 1);
            }

            if (!(inventory[1].getItem() instanceof ItemElectricalCarver)) {
                if (inventory[1].getCount() == 1)
                    removeStackFromSlot(1);
                else
                    inventory[1].setCount(inventory[1].getCount() - 1);
            }

            if (inventory[2].isEmpty())
                setInventorySlotContents(2, out);
            else
                inventory[2].setCount(inventory[2].getCount() + 1);
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

        if (canSmelt(inventory[0], inventory[1]) && !isBurn) {
            isBurn = true;
            burnPhase = 0;
            needEnergy = AssemblerRecipes.getEnergy(inventory[0].getItem(), inventory[1].getItem());
            burnTime = needEnergy;
            ModUtils.setState(world, getPos(), world.getBlockState(getPos()).withProperty(BlockFaceActive.ACTIVE, true));
            markDirty();
            markUpdate();
        } else if (!canSmelt(inventory[0], inventory[1]) && isBurn) {
            isBurn = false;
            burnPhase = 0;
            needEnergy = AssemblerRecipes.getEnergy(inventory[0].getItem(), inventory[1].getItem());
            burnTime = needEnergy;
            ModUtils.setState(world, getPos(), world.getBlockState(getPos()).withProperty(BlockFaceActive.ACTIVE, false));
            markDirty();
            markUpdate();
        }
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.tile_entity_HPA";
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 3, getUpdateTag());
    }

    protected boolean canSmelt(ItemStack itemStack1, ItemStack itemStack2){
        if (itemStack1.isEmpty() || itemStack2.isEmpty())
            return false;

        ItemStack itemstack = AssemblerRecipes.getOut(itemStack1.getItem(), itemStack2.getItem());

        if (itemstack.isEmpty())
        {
            return false;
        }
        else
        {
            ItemStack itemstack1 = (ItemStack)this.inventory[2];
            if (itemstack1.isEmpty()) return true;
            if (!itemstack1.isItemEqual(itemstack)) return false;
            int result = itemstack1.getCount() + itemstack.getCount();
            return result <= getInventoryStackLimit() && result <= itemstack1.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
        }
    }
}
