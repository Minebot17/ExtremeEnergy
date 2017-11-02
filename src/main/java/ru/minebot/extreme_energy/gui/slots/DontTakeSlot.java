package ru.minebot.extreme_energy.gui.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class DontTakeSlot extends Slot {
    public DontTakeSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return false;
    }
}
