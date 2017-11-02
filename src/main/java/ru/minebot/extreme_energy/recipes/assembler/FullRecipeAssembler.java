package ru.minebot.extreme_energy.recipes.assembler;

import net.minecraft.item.ItemStack;

public class FullRecipeAssembler {
    protected DoubleItem input;
    protected ItemStack output;
    protected int energy;

    public FullRecipeAssembler(DoubleItem input, ItemStack output, int energy){
        this.input = input;
        this.output = output;
        this.energy = energy;
    }

    public DoubleItem getInput(){
        return input;
    }

    public ItemStack getOutput(){
        return output;
    }

    public int getEnergy(){
        return energy;
    }
}
