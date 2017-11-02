package ru.minebot.extreme_energy.gui.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.minebot.extreme_energy.items.ItemIdCard;
import ru.minebot.extreme_energy.other.IPublicPrivateProvider;

public class CardSlot extends Slot{
    protected IPublicPrivateProvider handler;

    public CardSlot(IPublicPrivateProvider handler, IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.handler = handler;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof ItemIdCard;
    }

    @Override
    public void onSlotChanged() {
        handler.cardsChanged();
        super.onSlotChanged();
    }
}
