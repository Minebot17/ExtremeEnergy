package ru.minebot.extreme_energy.tile_entities;

import net.minecraft.item.ItemStack;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.items.modules.ItemVoltageModule;
import ru.minebot.extreme_energy.recipes.managers.SawmillRecipes;

public class TileEntityHAS extends TileEntityProcessingBlock {
    public TileEntityHAS() {
        super(3, 22, true, ModItems.voltageModule);
    }

    @Override
    protected void onWorked() {
        ItemStack output = SawmillRecipes.getOut(inventory[0].getItem()).copy();
        if (inventory[1].isEmpty())
            setInventorySlotContents(1, output);
        else
            inventory[1].setCount(inventory[1].getCount() + output.getCount());

        if (inventory[0].getCount() == 1)
            removeStackFromSlot(0);
        else
            inventory[0].setCount(inventory[0].getCount() - 1);
    }

    @Override
    protected boolean canWork() {
        if (inventory[0].isEmpty())
            return false;

        ItemStack itemstack = SawmillRecipes.getOut(inventory[0].getItem());

        if (itemstack.isEmpty()) {
            return false;
        }
        else {
            ItemStack itemstack1 = (ItemStack)this.inventory[1];
            if (itemstack1.isEmpty()) return true;
            if (!itemstack1.isItemEqual(itemstack)) return false;
            int result = itemstack1.getCount() + itemstack.getCount();
            return result <= getInventoryStackLimit() && result <= itemstack1.getMaxStackSize();
        }
    }

    @Override
    protected int getNeedEnergy() {
        return SawmillRecipes.getEnergy(inventory[0].getItem());
    }

    @Override
    public void updateValueModules() {
        int cVoltage = 0;

        for (int i = 2; i < inventory.length; i++)
            if (inventory[i] != ItemStack.EMPTY && inventory[i].getItem() instanceof ItemVoltageModule){
                cVoltage += inventory[i].getCount();
            }

        maxVoltage = 150 + 150 * cVoltage;
        try {
            if (voltage > maxVoltage)
                setVoltage(maxVoltage);
            markDirty();
            markUpdate();
        }
        catch (NullPointerException e){
            System.out.println("Try to save, when world did not loaded");
        }
    }
}
