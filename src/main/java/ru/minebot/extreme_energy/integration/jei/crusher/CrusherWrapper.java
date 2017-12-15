package ru.minebot.extreme_energy.integration.jei.crusher;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import ru.minebot.extreme_energy.recipes.managers.CrusherRecipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

public class CrusherWrapper implements IRecipeWrapper {
    private CrusherRecipes.FullRecipeCrusher recipe;
    private List<String> tooltip;

    public CrusherWrapper(CrusherRecipes.FullRecipeCrusher recipe){
        this.recipe = recipe;
        tooltip = Lists.newArrayList(recipe.getEnergy()+" RF");
    }

    @Override
    public void getIngredients(IIngredients iIngredients) {
        iIngredients.setInput(ItemStack.class, recipe.getInput());
        iIngredients.setOutput(ItemStack.class, recipe.getOutput());
    }

    @Override
    public void drawInfo(Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return mouseX > 22 && mouseX < 55 && mouseY > 15 && mouseY < 60 ? tooltip : Collections.emptyList();
    }

    @Override
    public boolean handleClick(Minecraft minecraft, int i, int i1, int i2) {
        return false;
    }

    public CrusherRecipes.FullRecipeCrusher getRecipe() {
		return recipe;
	}
}
