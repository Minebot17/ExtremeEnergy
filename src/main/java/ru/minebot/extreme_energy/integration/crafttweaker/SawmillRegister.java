package ru.minebot.extreme_energy.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import ru.minebot.extreme_energy.recipes.managers.SawmillRecipes;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenClass( "mods.meem.Sawmill" )
public class SawmillRegister {
    public static List<IIngredient> ignoreRecipes = new ArrayList<>();

    private SawmillRegister() {

    }

    @ZenMethod
    public static void addRecipe(IIngredient input, IItemStack output, @Optional Integer energy) {
        CraftTweakerAPI.apply(new RecipeAction("Add Sawmill recipe for " + output.getDisplayName(), () -> {
            if (input instanceof IOreDictEntry){
                SawmillRecipes.ores.add(new SawmillRecipes.RecipeSawmill(((IOreDictEntry) input).getName(), RecipeAction.convert(output), energy));
            }
            else {
                SawmillRecipes.putRecipe(RecipeAction.convert((IItemStack) input).getItem(), RecipeAction.convert(output), energy);
            }
        }));
    }

    @ZenMethod
    public static void removeRecipe(IIngredient input) {
        CraftTweakerAPI.apply(new RecipeAction("Remove Sawmill recipe for "+ input.toString(),
                () -> ignoreRecipes.add(input)
        ));
    }

    public static boolean inIgnore(ItemStack input){
        return ignoreRecipes.stream().anyMatch(x ->
                x instanceof IOreDictEntry ? (OreDictionary.getOreIDs(input).length == 0 ? false : OreDictionary.getOreName(OreDictionary.getOreIDs(input)[0]).equals(((IOreDictEntry) x).getName())) : ItemStack.areItemStacksEqual(RecipeAction.convert((IItemStack) x), input)
        );
    }
}
