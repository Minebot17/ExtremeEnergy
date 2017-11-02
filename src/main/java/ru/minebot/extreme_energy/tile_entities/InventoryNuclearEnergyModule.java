package ru.minebot.extreme_energy.tile_entities;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.ItemCalifornium;

public class InventoryNuclearEnergyModule extends BasicInventory {

    public InventoryNuclearEnergyModule(ItemStack itemStack) {
        super(itemStack, 1, new Item[]{});
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return stack.getItem() instanceof ItemCalifornium;
    }

    @Override
    public void save() {
        NBTTagCompound category = ModUtils.getNotNullCategory(itemStack);
        category.setInteger("californium", slots[0].getCount());
    }

    @Override
    public void load() {
        NBTTagCompound category = ModUtils.getNotNullCategory(itemStack);
        slots = new ItemStack[]{new ItemStack(ModItems.californium, category.getInteger("californium"))};
    }
}
