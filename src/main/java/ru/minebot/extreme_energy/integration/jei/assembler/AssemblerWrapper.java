package ru.minebot.extreme_energy.integration.jei.assembler;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import ru.minebot.extreme_energy.recipes.managers.AssemblerRecipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssemblerWrapper implements IRecipeWrapper {
    private AssemblerRecipes.FullRecipeAssembler recipe;

    public AssemblerWrapper(AssemblerRecipes.FullRecipeAssembler recipe){
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients iIngredients) {
        List<ItemStack> inp = new ArrayList<>();
        inp.add(new ItemStack(recipe.getInput().getItemFirst()));
        inp.add(new ItemStack(recipe.getInput().getItemSecond()));
        iIngredients.setInputs(ItemStack.class, inp);
        iIngredients.setOutput(ItemStack.class, recipe.getOutput());
    }

    @Override
    public void drawInfo(Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        List<String> list = new ArrayList<>();
        list.add(recipe.getEnergy()+" RF");
        return mouseX > 38 && mouseX < 75 && mouseY > 18 && mouseY < 38 ? list : Collections.emptyList();
    }

    @Override
    public boolean handleClick(Minecraft minecraft, int i, int i1, int i2) {
        return false;
    }
}
