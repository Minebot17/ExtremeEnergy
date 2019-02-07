/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2013 - 2017, AlgorithmX2, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package ru.minebot.extreme_energy.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import crafttweaker.api.item.IItemStack;
import net.minecraftforge.oredict.OreDictionary;
import ru.minebot.extreme_energy.recipes.managers.AssemblerRecipes;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenClass( "mods.meem.Assembler" )
public class AssemblerRegister {
    public static List<IgnoreRecipe> ignoreRecipes = new ArrayList<>();

    private AssemblerRegister() {

    }

    @ZenMethod
    public static void addRecipe(IIngredient inputFirst, IIngredient inputSecond, IItemStack output, @Optional Integer energy) {
        CraftTweakerAPI.apply(new RecipeAction("Add Assembler recipe for " + output.getDisplayName(), () -> {
            if (inputFirst instanceof IOreDictEntry && inputSecond instanceof IOreDictEntry){
                AssemblerRecipes.possibleOres.add(new AssemblerRecipes.RecipeAssemblerString(((IOreDictEntry) inputFirst).getName(), ((IOreDictEntry) inputSecond).getName(), RecipeAction.convert(output), energy));
            }
            else if (inputFirst instanceof IOreDictEntry){
                AssemblerRecipes.halfDictionary.add(new AssemblerRecipes.RecipeAssembler(((IOreDictEntry) inputFirst).getName(), RecipeAction.convert((IItemStack) inputSecond), RecipeAction.convert(output), energy));
            }
            else if (inputSecond instanceof IOreDictEntry){
                AssemblerRecipes.halfDictionary.add(new AssemblerRecipes.RecipeAssembler(((IOreDictEntry) inputSecond).getName(), RecipeAction.convert((IItemStack) inputFirst), RecipeAction.convert(output), energy));
            }
            else {
                AssemblerRecipes.putRecipe(RecipeAction.convert((IItemStack) inputFirst), RecipeAction.convert((IItemStack) inputSecond), RecipeAction.convert(output), energy);
            }
        }));

        /*CraftTweakerAPI.apply(new RecipeAction("Add Assembler recipe for " + output.getDisplayName(),
                () -> AssemblerRecipes.recipesList.add(new FullRecipeAssembler(
                        new DoubleItem(RecipeAction.convert(inputFirst), RecipeAction.convert(inputSecond)),
                        RecipeAction.convert(output),
                        energy == null ? 2000 : energy))
        ));*/
    }

    @ZenMethod
    public static void removeRecipe(IIngredient inputFirst, IIngredient inputSecond) {
        CraftTweakerAPI.apply(new RecipeAction("Remove Assembler recipe for "+ inputFirst.toString() + " + " + inputSecond.toString(),
                () -> ignoreRecipes.add(new IgnoreRecipe(inputFirst, inputSecond))
        ));
    }

    public static boolean inIgnore(ItemStack first, ItemStack second){
        return ignoreRecipes.stream().anyMatch(x ->
                (x.first instanceof IOreDictEntry ? (OreDictionary.getOreIDs(first).length == 0 ? false : OreDictionary.getOreName(OreDictionary.getOreIDs(first)[0]).equals(((IOreDictEntry) x.first).getName())) : ItemStack.areItemStacksEqual(first, RecipeAction.convert((IItemStack) x.first))) &&
                (x.second instanceof IOreDictEntry ? (OreDictionary.getOreIDs(second).length == 0 ? false : OreDictionary.getOreName(OreDictionary.getOreIDs(second)[0]).equals(((IOreDictEntry) x.second).getName())) : ItemStack.areItemStacksEqual(second, RecipeAction.convert((IItemStack) x.second))) ||
                (x.first instanceof IOreDictEntry ? (OreDictionary.getOreIDs(second).length == 0 ? false : OreDictionary.getOreName(OreDictionary.getOreIDs(second)[0]).equals(((IOreDictEntry) x.first).getName())) : ItemStack.areItemStacksEqual(second, RecipeAction.convert((IItemStack) x.first))) &&
                (x.second instanceof IOreDictEntry ? (OreDictionary.getOreIDs(first).length == 0 ? false : OreDictionary.getOreName(OreDictionary.getOreIDs(first)[0]).equals(((IOreDictEntry) x.second).getName())) : ItemStack.areItemStacksEqual(first, RecipeAction.convert((IItemStack) x.second)))
        );
    }

    public static class IgnoreRecipe {
        public IIngredient first;
        public IIngredient second;

        public IgnoreRecipe(IIngredient first, IIngredient second) {
            this.first = first;
            this.second = second;
        }
    }
}
