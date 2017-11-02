package ru.minebot.extreme_energy.gui.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SingleSlot extends Slot {

    public SingleSlot(IInventory inventory, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return inventory.isItemValidForSlot(getSlotIndex(), stack);
    }
}
