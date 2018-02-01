package ru.minebot.extreme_energy.tile_entities;

import net.minecraft.item.ItemStack;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.assembler.ItemElectricalCarver;
import ru.minebot.extreme_energy.items.modules.ItemVoltageModule;
import ru.minebot.extreme_energy.recipes.managers.AssemblerRecipes;

public class TileEntityHPA extends TileEntityProcessingBlock {

    public TileEntityHPA(){
        super(4, 30, true, ModItems.voltageModule);
    }

    @Override
    protected void onWorked() {
        ItemStack out = AssemblerRecipes.getOut(inventory[0], inventory[1]).copy();

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
    }

    @Override
    protected boolean canWork() {
        if (inventory[0].isEmpty() || inventory[1].isEmpty())
            return false;

        ItemStack itemstack = AssemblerRecipes.getOut(ModUtils.copyChangeCount(inventory[0]), ModUtils.copyChangeCount(inventory[1]));

        if (itemstack.isEmpty()) {
            return false;
        }
        else {
            ItemStack itemstack1 = (ItemStack)this.inventory[2];
            if (itemstack1.isEmpty()) return true;
            if (!itemstack1.isItemEqual(itemstack)) return false;
            int result = itemstack1.getCount() + itemstack.getCount();
            return result <= getInventoryStackLimit() && result <= itemstack1.getMaxStackSize();
        }
    }

    @Override
    protected int getNeedEnergy() {
        return AssemblerRecipes.getEnergy(inventory[0], inventory[1]);
    }

    @Override
    public void updateValueModules() {
        int cVoltage = 0;

        for (int i = 3; i < inventory.length; i++)
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
