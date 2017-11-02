package ru.minebot.extreme_energy.gui.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class BasicContainer extends Container {
    protected IInventory player;
    public IInventory te;
    private int countTESlots;

    public BasicContainer(IInventory player, IInventory te, int countTESlots){
        this.player = player;
        this.te = te;
        this.countTESlots = countTESlots;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.te.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
        ItemStack previous = ItemStack.EMPTY;
        Slot slot = (Slot) this.inventorySlots.get(fromSlot);

        if (slot != null && slot.getHasStack()) {
            ItemStack current = slot.getStack();
            previous = current.copy();

            if (fromSlot < countTESlots) {
                // From TE Inventory to Player Inventory
                if (!this.mergeItemStack(current, countTESlots, 36 + countTESlots, true))
                    return ItemStack.EMPTY;
            } else {
                // From Player Inventory to TE Inventory
                if (!this.mergeItemStack(current, 0, countTESlots, false))
                    return ItemStack.EMPTY;
            }

            if (current.getCount() == 0)
                slot.putStack((ItemStack) ItemStack.EMPTY);
            else
                slot.onSlotChanged();

            if (current.getCount() == previous.getCount())
                return ItemStack.EMPTY;
            slot.onTake(playerIn, current);

        }
        return previous;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        if (clickTypeIn == ClickType.SWAP) return ItemStack.EMPTY;
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }
}
