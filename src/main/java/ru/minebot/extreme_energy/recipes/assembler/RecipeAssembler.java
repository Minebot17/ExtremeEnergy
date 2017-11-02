package ru.minebot.extreme_energy.recipes.assembler;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipeAssembler {
    private String nameFirst;
    private Item itemSecond;
    private ItemStack stack;
    private int energy;

    public RecipeAssembler(String nameFirst, Item itemSecond, ItemStack stack, int energy){
        this.nameFirst = nameFirst;
        this.itemSecond = itemSecond;
        this.energy = energy;
        this.stack = stack;
    }

    public String getNameFirst(){ return nameFirst; }
    public Item getItemSecond(){ return itemSecond; }
    public ItemStack getStack(){ return stack; }
    public int getEnergy(){ return energy; }
}

