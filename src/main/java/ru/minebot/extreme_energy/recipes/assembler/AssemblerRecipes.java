package ru.minebot.extreme_energy.recipes.assembler;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import ru.minebot.extreme_energy.init.ModBlocks;
import ru.minebot.extreme_energy.init.ModItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class AssemblerRecipes {

    public static List<FullRecipeAssembler> recipesList;
    protected static ArrayList<RecipeAssemblerString> possibleOres;
    protected static ArrayList<RecipeAssembler> halfDictionary;
    protected static HashMap<DoubleItem, ItemStack> recipes;
    protected static HashMap<DoubleItem, Integer> energy;

    public static void init() throws Exception {
        recipes = new HashMap<DoubleItem, ItemStack>();
        halfDictionary = new ArrayList<RecipeAssembler>();
        possibleOres = new ArrayList<RecipeAssemblerString>();
        energy = new HashMap<DoubleItem, Integer>();
        recipesList = new ArrayList<FullRecipeAssembler>();

        halfDictionaryRecipes();
        halfDictionaryToRecipes();
        notOreDictionaryRecipes();
        possibleRecipes();
        possibleOresToRecipes();
        constructRecipes();
    }

    // Recipes to halfDictionary
    protected static void halfDictionaryRecipes(){
        halfDictionary.add(new RecipeAssembler("stickIron", ModItems.crystal, new ItemStack(ModItems.electricCarver), 50000));
        halfDictionary.add(new RecipeAssembler("ingotCopper", ModItems.electricCarver, new ItemStack(ModItems.antenna), 5000));
        halfDictionary.add(new RecipeAssembler("blockIron", Items.STICK, new ItemStack(ModItems.toughIronRod), 20000));
        halfDictionary.add(new RecipeAssembler("ingotIron", Items.STICK, new ItemStack(ModItems.ironRod), 2000));
        halfDictionary.add(new RecipeAssembler("ingotIron", ModItems.electricCarver, new ItemStack(ModItems.case_), 5000));
        halfDictionary.add(new RecipeAssembler("ingotIron", ModItems.copperWires, new ItemStack(ModItems.electricalBoard), 5000));
        halfDictionary.add(new RecipeAssembler("gemDiamond", ModItems.processor, new ItemStack(ModItems.forceFieldComponent), 1000000));
        halfDictionary.add(new RecipeAssembler("dustSteel", ModItems.ironCylinder, new ItemStack(ModItems.heatModule), 50000));
    }

    // Recipes to recipes with method 'putRecipe'
    protected static void notOreDictionaryRecipes(){
        putRecipe(ModItems.ironBlank, ModItems.electricCarver, new ItemStack(ModItems.ironCylinder), 2500);
        putRecipe(ModItems.copperBlank, ModItems.electricCarver, new ItemStack(ModItems.copperWires), 2500);
        putRecipe(ModItems.ironCylinder, ModItems.copperWires, new ItemStack(ModItems.voltageModule), 50000);
        putRecipe(ModItems.case_, ModItems.antenna, new ItemStack(ModItems.radiusModule), 50000);
        putRecipe(ModItems.smallCrystal, ModItems.case_, new ItemStack(ModItems.processor), 5000);
        putRecipe(ModItems.electricalBoard, ModItems.processor, new ItemStack(ModItems.lossReduceModule), 50000);
        putRecipe(ModItems.heavyBlank, ModItems.electricCarver, new ItemStack(ModItems.heavyPlate), 25000);
        putRecipe(ModItems.ironCylinder, ModItems.electricCarver, new ItemStack(ModItems.capsule), 5000);
        putRecipe(ModItems.capacityModule, Item.getItemFromBlock(ModBlocks.cable), new ItemStack(ModItems.conductionModule), 15000);
    }

    // Recipes to possibleOres (or full oreDictionary)
    protected static void possibleRecipes(){
        possibleOres.add(new RecipeAssemblerString("ingotIron", "ingotIron", new ItemStack(ModItems.ironBlank), 10000));
        possibleOres.add(new RecipeAssemblerString("ingotCopper", "ingotCopper", new ItemStack(ModItems.copperBlank), 10000));
        possibleOres.add(new RecipeAssemblerString("ingotSteel", "ingotSteel", new ItemStack(ModItems.heavyBlank), 10000));
        possibleOres.add(new RecipeAssemblerString("blockRedstone", "ingotIron", new ItemStack(ModItems.capacityModule), 10000));

    }

    protected static void halfDictionaryToRecipes() throws Exception {
        for (int i = 0; i < halfDictionary.size(); i++)
            if (OreDictionary.doesOreNameExist(halfDictionary.get(i).getNameFirst())){
                NonNullList<ItemStack> items = OreDictionary.getOres(halfDictionary.get(i).getNameFirst());
                ItemStack out = halfDictionary.get(i).getStack();
                if (items.size() == 0 || out == null)
                    throw new Exception("Invalid assembler recipes");
                for (int j = 0; j < items.size(); j++)
                    putRecipe(items.get(j).getItem(), halfDictionary.get(i).getItemSecond(), out, halfDictionary.get(i).getEnergy());
            }
    }

    protected static void possibleOresToRecipes() throws Exception {
        for (int i = 0; i < possibleOres.size(); i++)
            if (OreDictionary.doesOreNameExist(possibleOres.get(i).getNameFirst()) && OreDictionary.doesOreNameExist(possibleOres.get(i).getNameSecond())){
                NonNullList<ItemStack> items1 = OreDictionary.getOres(possibleOres.get(i).getNameFirst());
                NonNullList<ItemStack> items2 = OreDictionary.getOres(possibleOres.get(i).getNameSecond());
                ItemStack out = possibleOres.get(i).getStack();
                if (items1.size() == 0 || items1.size() == 0 || out == null)
                    throw new Exception("Invalid assembler recipes");
                for (int n = 0; n < items1.size(); n++)
                    for (int m = 0; m < items1.size(); m++)
                        putRecipe(items1.get(n).getItem(), items2.get(m).getItem(), out, possibleOres.get(i).getEnergy());
            }
    }

    protected static void constructRecipes(){
        Object[] items = recipes.keySet().toArray();
        for (int i = 0; i < items.length; i++){
            recipesList.add(new FullRecipeAssembler((DoubleItem) items[i], recipes.get(items[i]), energy.get(items[i])));
        }
    }

    protected static void putRecipe(Item item1, Item item2, ItemStack stack, int energy_){
        recipes.put(new DoubleItem(item1, item2), stack);
        energy.put(new DoubleItem(item1, item2), energy_);
    }

    // Public methods
    public static boolean hasRecipe(Item item1, Item item2){
        return recipes.containsKey(new DoubleItem(item1, item2));
    }

    public static ItemStack getOut(Item item1, Item item2){
        ItemStack stack = recipes.get(new DoubleItem(item1, item2));
        if (stack == null)
            stack = recipes.get(new DoubleItem(item2, item1));
        return stack != null ? stack : ItemStack.EMPTY;
    }

    public static int getEnergy(Item item1, Item item2){
        try {
            return hasRecipe(item1, item2) ? energy.get(new DoubleItem(item1, item2)) : energy.get(new DoubleItem(item2, item1));
        }
        catch (NullPointerException e){ return 0; }
    }

    public static List<List<ItemStack>> getElements(ItemStack stack){
        List<List<ItemStack>> result = new ArrayList<>();
        for(FullRecipeAssembler recipe : recipesList){
            if (recipe.output.getItem() == stack.getItem()) {
                List<ItemStack> list = new ArrayList<>();
                list.add(new ItemStack(recipe.getInput().getItemFirst()));
                list.add(new ItemStack(recipe.getInput().getItemSecond()));
                result.add(list);
            }
        }
        return result;
    }
}
