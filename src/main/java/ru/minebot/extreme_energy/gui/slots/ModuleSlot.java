package ru.minebot.extreme_energy.gui.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.minebot.extreme_energy.modules.Module;
import ru.minebot.extreme_energy.other.IModuleProvider;

public class ModuleSlot extends Slot {

    protected IModuleProvider provider;
    protected int tier;

    public ModuleSlot(IModuleProvider provider, IInventory inventoryIn, int index, int xPosition, int yPosition) {
        this(provider, inventoryIn, index, xPosition, yPosition, -1);
    }

    public ModuleSlot(IModuleProvider provider, IInventory inventoryIn, int index, int xPosition, int yPosition, int tier) {
        super(inventoryIn, index, xPosition, yPosition);
        this.provider = provider;
        this.tier = tier;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof Module && (tier == -1 || ((Module) stack.getItem()).getTier() <= tier) && inventory.isItemValidForSlot(getSlotIndex(), stack);
    }

    @Override
    public void onSlotChanged() {
        provider.updateValueModules();

        super.onSlotChanged();
    }
}
