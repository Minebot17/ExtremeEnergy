package ru.minebot.extreme_energy.integration.crafttweaker;

import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;

public class RecipeAction implements IAction {

    private String description;
    private Runnable action;

    public RecipeAction(String description, Runnable action){
        this.description = description;
        this.action = action;
    }

    @Override
    public void apply() {
        action.run();
    }

    @Override
    public String describe() {
        return description;
    }

    public static ItemStack convert(IItemStack iStack) {
        if (iStack == null)
            return ItemStack.EMPTY;
        else
            return (ItemStack) iStack.getInternal();
    }

}
