package ru.minebot.extreme_energy.integration.jei.sawmill;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.gui.elements.DrawableResource;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import ru.minebot.extreme_energy.recipes.managers.SawmillRecipes;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SawmillCategory implements IRecipeCategory<SawmillWrapper> {

    @Override
    public String getUid() {
        return "meem.sawmill";
    }

    @Override
    public String getTitle() {
        return "High-Automatic Sawmill";
    }

    @Override
    public String getModName() {
        return "Extreme Energy";
    }

    @Override
    public IDrawable getBackground() {
        return new DrawableResource(new ResourceLocation("meem:textures/gui/jei/has.png"), 0, 0, 82, 26, 10, 0, 0, 0, 82, 26);
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return new DrawableResource(new ResourceLocation("meem:textures/icons/sawmill.png"), 0, 0, 16, 16, 0, 0, 0, 0, 16, 16);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {

    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, SawmillWrapper recipe, IIngredients iIngredients) {
        IGuiItemStackGroup group = iRecipeLayout.getItemStacks();
        group.init(0, true, 0, 14);
        group.init(1, false, 60, 14);

        group.set(0,recipe.getRecipe().getInput());
        group.set(1, recipe.getRecipe().getOutput());
    }

    public static List<SawmillWrapper> getRecipes(){
        List<SawmillRecipes.FullRecipeSawmill> r = SawmillRecipes.recipesList;
        List<SawmillWrapper> recipes = new ArrayList<>();
        for (int i = 0; i < r.size(); i++)
            recipes.add(new SawmillWrapper(r.get(i)));
        return recipes;
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return Collections.emptyList();
    }
}
