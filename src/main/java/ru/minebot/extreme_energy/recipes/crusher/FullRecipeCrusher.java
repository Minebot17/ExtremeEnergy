package ru.minebot.extreme_energy.recipes.crusher;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FullRecipeCrusher {
    protected Item input;
    protected ItemStack output;
    protected int energy;

    public FullRecipeCrusher(Item input, ItemStack output, int energy){
        this.input = input;
        this.output = output;
        this.energy = energy;
    }

    public Item getInput(){
        return input;
    }

    public ItemStack getOutput(){
        return output;
    }

    public int getEnergy(){
        return energy;
    }
}
