package ru.minebot.extreme_energy.integration.jei.crusher;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import ru.minebot.extreme_energy.recipes.assembler.FullRecipeAssembler;
import ru.minebot.extreme_energy.recipes.crusher.FullRecipeCrusher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CrusherWrapper implements IRecipeWrapper {
    private FullRecipeCrusher recipe;

    public CrusherWrapper(FullRecipeCrusher recipe){
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients iIngredients) {
        iIngredients.setInput(ItemStack.class, new ItemStack(recipe.getInput()));
        iIngredients.setOutput(ItemStack.class, recipe.getOutput());
    }

    @Override
    public void drawInfo(Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        List<String> list = new ArrayList<>();
        list.add(recipe.getEnergy()+" RF");
        return mouseX > 22 && mouseX < 55 && mouseY > 15 && mouseY < 60 ? list : Collections.emptyList();
    }

    @Override
    public boolean handleClick(Minecraft minecraft, int i, int i1, int i2) {
        return false;
    }
}
