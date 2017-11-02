package ru.minebot.extreme_energy.recipes.crusher;

import net.minecraft.item.ItemStack;

public class RecipeCrusher {
    private String name;
    private ItemStack stack;
    private int energy;

    public RecipeCrusher(String name, ItemStack stack, int energy){
        this.name = name;
        this.stack = stack;
        this.energy = energy;
    }

    public String getName(){ return name; }
    public ItemStack getStack(){ return stack; }
    public int getEnergy(){ return energy; }
}

