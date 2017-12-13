package ru.minebot.extreme_energy.recipes;

import net.minecraft.item.ItemStack;

public class DoubleItem{
    private ItemStack i1;
    private ItemStack i2;
    public DoubleItem(ItemStack i1, ItemStack i2){
        this.i1 = i1;
        this.i2 = i2;
    }
    public ItemStack getItemFirst(){ return i1; }
    public ItemStack getItemSecond(){ return i2; }

    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof DoubleItem))
            return false;

        DoubleItem item = (DoubleItem) obj;
        if (ItemStack.areItemStacksEqual(i1, item.getItemFirst()) && ItemStack.areItemStacksEqual(i2, item.getItemSecond()) || ItemStack.areItemStacksEqual(i2, item.getItemFirst()) && ItemStack.areItemStacksEqual(i1, item.getItemSecond()))
            return true;
        return false;
    }

    @Override
    public int hashCode(){
        return i1.hashCode() + i2.hashCode();
    }
}
