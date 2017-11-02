package ru.minebot.extreme_energy.recipes.assembler;

import net.minecraft.item.ItemStack;

public class RecipeAssemblerString {
    private String nameFirst;
    private String nameSecond;
    private ItemStack stack;
    private int energy;

    public RecipeAssemblerString(String nameFirst, String nameSecond, ItemStack stack, int energy){
        this.nameFirst = nameFirst;
        this.nameSecond = nameSecond;
        this.energy = energy;
        this.stack = stack;
    }

    public String getNameFirst(){ return nameFirst; }
    public String getNameSecond(){ return nameSecond; }
    public ItemStack getStack(){ return stack; }
    public int getEnergy(){ return energy; }
}
