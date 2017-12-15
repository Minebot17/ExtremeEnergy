package ru.minebot.extreme_energy.integration.jei;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ru.minebot.extreme_energy.gui.HasGui;
import ru.minebot.extreme_energy.gui.HpaGui;
import ru.minebot.extreme_energy.gui.HpcGui;
import ru.minebot.extreme_energy.gui.containers.HpaContainer;
import ru.minebot.extreme_energy.init.ModBlocks;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.integration.jei.assembler.AssemblerCategory;
import ru.minebot.extreme_energy.integration.jei.crusher.CrusherCategory;
import ru.minebot.extreme_energy.integration.jei.sawmill.SawmillCategory;
import ru.minebot.extreme_energy.items.ItemCalifornium;

@JEIPlugin
public class JeiPlugin implements IModPlugin {

    @Override
    public void registerItemSubtypes(ISubtypeRegistry iSubtypeRegistry) {

    }

    @Override
    public void registerIngredients(IModIngredientRegistration iModIngredientRegistration) {

    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration iRecipeCategoryRegistration) {
        iRecipeCategoryRegistration.addRecipeCategories(new AssemblerCategory());
        iRecipeCategoryRegistration.addRecipeCategories(new CrusherCategory());
        iRecipeCategoryRegistration.addRecipeCategories(new SawmillCategory());
    }

    @Override
    public void register(IModRegistry iModRegistry) {
        IIngredientBlacklist blacklist = iModRegistry.getJeiHelpers().getIngredientBlacklist();
        blacklist.addIngredientToBlacklist(new ItemStack(ModItems.fullInjector));
        blacklist.addIngredientToBlacklist(new ItemStack(ModBlocks.shield));
        blacklist.addIngredientToBlacklist(new ItemStack(ModBlocks.cableWithTile));

        iModRegistry.addRecipeCatalyst(new ItemStack(ModBlocks.htf), "minecraft.smelting");
        iModRegistry.addRecipeCatalyst(new ItemStack(ModBlocks.hpa), "meem.assembler");
        iModRegistry.addRecipeCatalyst(new ItemStack(ModBlocks.hpc), "meem.crusher");
        iModRegistry.addRecipeCatalyst(new ItemStack(ModBlocks.has), "meem.sawmill");
        iModRegistry.addRecipes(AssemblerCategory.getRecipes(), "meem.assembler");
        iModRegistry.addRecipes(CrusherCategory.getRecipes(), "meem.crusher");
        iModRegistry.addRecipes(SawmillCategory.getRecipes(), "meem.sawmill");
        iModRegistry.addRecipeClickArea(HpaGui.class, 69, 32, 29, 17, "meem.assembler");
        iModRegistry.addRecipeClickArea(HpcGui.class, 69, 25, 30, 41, "meem.crusher");
        iModRegistry.addRecipeClickArea(HasGui.class, 72, 30, 22, 22, "meem.sawmill");
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime iJeiRuntime) {

    }
}
