package ru.minebot.extreme_energy.integration.jei.sawmill;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import ru.minebot.extreme_energy.recipes.managers.SawmillRecipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

public class SawmillWrapper implements IRecipeWrapper {
    private SawmillRecipes.FullRecipeSawmill recipe;
    private List<String> tooltip;

    public SawmillWrapper(SawmillRecipes.FullRecipeSawmill recipe){
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
        return mouseX > 22 && mouseX < 55 && mouseY > 12 && mouseY < 30 ? tooltip : Collections.emptyList();
    }

    @Override
    public boolean handleClick(Minecraft minecraft, int i, int i1, int i2) {
        return false;
    }

    public SawmillRecipes.FullRecipeSawmill getRecipe() {
		return recipe;
	}
}
