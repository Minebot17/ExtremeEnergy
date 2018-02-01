package ru.minebot.extreme_energy.tile_entities;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.modules.ItemVoltageModule;
import ru.minebot.extreme_energy.recipes.managers.CrusherRecipes;

public class TileEntityHPC extends TileEntityProcessingBlock {

    public TileEntityHPC(){
        super(4, 30, true, ModItems.voltageModule);
    }

    @Override
    protected void onWorked() {
        ItemStack out = CrusherRecipes.getOut(ModUtils.copyChangeCount(inventory[0])).copy();
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
    }

    @Override
    protected boolean canWork() {
        if (inventory[0].isEmpty())
            return false;

        ItemStack itemstack = CrusherRecipes.getOut(ModUtils.copyChangeCount(inventory[0])).copy();

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
            return (i1 <= getInventoryStackLimit() || i2 <= getInventoryStackLimit()) && (i1 <= itemstack1.getMaxStackSize() || i2 <= itemstack1.getMaxStackSize());
        }
    }

    @Override
    protected int getNeedEnergy() {
        return CrusherRecipes.getEnergy(ModUtils.copyChangeCount(inventory[0]));
    }

    protected boolean isSlotValid(ItemStack stack, int index){
        if (!stack.isEmpty()) {
            if (inventory[index].isEmpty() || (inventory[index].isItemEqual(stack) && inventory[index].getCount() + stack.getCount() <= inventory[index].getMaxStackSize()))
                return true;
        }
        return false;
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
