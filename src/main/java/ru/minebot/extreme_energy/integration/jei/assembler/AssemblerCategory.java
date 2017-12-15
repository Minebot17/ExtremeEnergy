package ru.minebot.extreme_energy.integration.jei.assembler;

import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.gui.elements.DrawableResource;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import ru.minebot.extreme_energy.recipes.managers.AssemblerRecipes;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssemblerCategory implements IRecipeCategory<AssemblerWrapper> {

    @Override
    public String getUid() {
        return "meem.assembler";
    }

    @Override
    public String getTitle() {
        return "High-Precision Assembler";
    }

    @Override
    public String getModName() {
        return "Extreme Energy";
    }

    @Override
    public IDrawable getBackground() {
        return new DrawableResource(new ResourceLocation("meem:textures/gui/jei/hpa.png"), 0, 0, 102, 26, 15, 0, 0, 0, 102, 26);
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return new DrawableResource(new ResourceLocation("meem:textures/icons/assembler.png"), 0, 0, 16, 16, 0, 0, 0, 0, 16, 16);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {

    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, AssemblerWrapper recipe, IIngredients iIngredients) {
        IGuiItemStackGroup group = iRecipeLayout.getItemStacks();
        group.init(0, true, 0, 19);
        group.init(1, true, 20, 19);
        group.init(2, false, 80, 19);

        group.set(0, recipe.getRecipe().getInput().getItemFirst());
        group.set(1, recipe.getRecipe().getInput().getItemSecond());
        group.set(2, recipe.getRecipe().getOutput());
    }

    public static List<AssemblerWrapper> getRecipes(){
        List<AssemblerRecipes.FullRecipeAssembler> r = AssemblerRecipes.recipesList;
        List<AssemblerWrapper> recipes = new ArrayList<>();
        for (int i = 0; i < r.size(); i++)
            recipes.add(new AssemblerWrapper(r.get(i)));
        return recipes;
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return Collections.emptyList();
    }
}
