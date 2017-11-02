package ru.minebot.extreme_energy.recipes.assembler;

import net.minecraft.item.Item;

public class DoubleItem{
    private Item i1;
    private Item i2;
    public DoubleItem(Item i1, Item i2){
        this.i1 = i1;
        this.i2 = i2;
    }
    public Item getItemFirst(){ return i1; }
    public Item getItemSecond(){ return i2; }

    @Override
    public boolean equals(Object obj){
        DoubleItem item = (DoubleItem) obj;
        if (i1.equals(item.getItemFirst()) || i2.equals(item.getItemSecond()))
            return true;
        return false;
    }

    @Override
    public int hashCode(){
        return i1.hashCode() + i2.hashCode();
    }
}
