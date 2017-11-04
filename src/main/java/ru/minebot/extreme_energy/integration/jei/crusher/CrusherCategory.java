package ru.minebot.extreme_energy.integration.jei.crusher;

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
import ru.minebot.extreme_energy.recipes.managers.CrusherRecipes;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CrusherCategory implements IRecipeCategory {
    @Override
    public String getUid() {
        return "meem.crusher";
    }

    @Override
    public String getTitle() {
        return "High-Pressure Crusher";
    }

    @Override
    public String getModName() {
        return "Extreme Energy";
    }

    @Override
    public IDrawable getBackground() {
        IDrawableStatic gui = new DrawableResource(new ResourceLocation("meem:textures/gui/jei/hpc.png"), 0, 0, 82, 53, 10, 0, 0, 0, 82, 53);
        return gui;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return new DrawableResource(new ResourceLocation("meem:textures/icons/crusher.png"), 0, 0, 16, 16, 0, 0, 0, 0, 16, 16);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {

    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, IRecipeWrapper iRecipeWrapper, IIngredients iIngredients) {
        IGuiItemStackGroup group = iRecipeLayout.getItemStacks();
        group.init(0, true, 0, 28);
        group.init(1, false, 60, 14);

        List<List<ItemStack>> input = iIngredients.getInputs(ItemStack.class);
        List<List<ItemStack>> output = iIngredients.getOutputs(ItemStack.class);

        group.set(0, input.get(0).get(0));

        int out = output.get(0).get(0).getCount();
        if (out == 1){
            group.set(1, output.get(0).get(0));
        }
        else{
            group.init(2, false, 60, 41);
            int c = out/2;
            ItemStack i1 = output.get(0).get(0).copy();
            ItemStack i2 = output.get(0).get(0).copy();
            i1.setCount(c);
            i2.setCount(out - c);

            group.set(1, i1);
            group.set(2, i2);
        }
    }

    public static List<CrusherWrapper> getRecipes(){
        List<CrusherRecipes.FullRecipeCrusher> r = CrusherRecipes.recipesList;
        List<CrusherWrapper> recipes = new ArrayList<>();
        for (int i = 0; i < r.size(); i++)
            recipes.add(new CrusherWrapper(r.get(i)));
        return recipes;
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return Collections.emptyList();
    }
}
