package ru.minebot.extreme_energy.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import crafttweaker.api.item.IItemStack;
import net.minecraftforge.oredict.OreDictionary;
import ru.minebot.extreme_energy.recipes.managers.CrusherRecipes;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenClass( "mods.meem.Crusher" )
public class CrusherRegister {
    public static List<IIngredient> ignoreRecipes = new ArrayList<>();

    private CrusherRegister() {

    }

    @ZenMethod
    public static void addRecipe(IIngredient input, IItemStack output, @Optional Integer energy) {
        CraftTweakerAPI.apply(new RecipeAction("Add Crusher recipe for " + output.getDisplayName(), () -> {
            if (input instanceof IOreDictEntry){
                CrusherRecipes.ores.add(new CrusherRecipes.RecipeCrusher(((IOreDictEntry) input).getName(), RecipeAction.convert(output), energy == null ? 2000 : energy));
            }
            else {
                CrusherRecipes.putRecipe(RecipeAction.convert((IItemStack) input), RecipeAction.convert(output), energy == null ? 2000 : energy);
            }
        }));
    }

    @ZenMethod
    public static void removeRecipe(IIngredient input) {
        CraftTweakerAPI.apply(new RecipeAction("Remove Crusher recipe for "+ input.toString(),
                () -> ignoreRecipes.add(input)
        ));
    }

    public static boolean inIgnore(ItemStack input){
        return ignoreRecipes.stream().anyMatch(x ->
                x instanceof IOreDictEntry ? (OreDictionary.getOreIDs(input).length == 0 ? false : OreDictionary.getOreName(OreDictionary.getOreIDs(input)[0]).equals(((IOreDictEntry) x).getName())) : ItemStack.areItemStacksEqual(RecipeAction.convert((IItemStack) x), input)
        );
    }
}