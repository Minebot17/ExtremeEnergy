package ru.minebot.extreme_energy.recipes.managers;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.integration.crafttweaker.SawmillRegister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SawmillRecipes {

    public static List<FullRecipeSawmill> recipesList = new ArrayList<>();
    public static ArrayList<RecipeSawmillString> possibleOres = new ArrayList<>();
    public static ArrayList<RecipeSawmill> ores = new ArrayList<>();
    protected static HashMap<Item, ItemStack> recipes = new HashMap<>();
    protected static HashMap<Item, Integer> energy = new HashMap<>();

    public static void init() throws Exception {
        OreDictionaryRecipes();
        oresToRecipes();
        NotOreDictionaryRecipes();
        PossibleRecipes();
        possibleOresToRecipes();
        constructRecipes();
    }

    // Recipes to ores
    protected static void OreDictionaryRecipes(){

    }

    // Recipes to recipes with method 'putRecipe'
    protected static void NotOreDictionaryRecipes(){
        putRecipe(Item.getItemFromBlock(Blocks.LOG), new ItemStack(Blocks.PLANKS, 8), 3000);
        putRecipe(Item.getItemFromBlock(Blocks.CHEST), new ItemStack(Blocks.PLANKS, 4), 4000);
        putRecipe(Item.getItemFromBlock(Blocks.TRAPPED_CHEST), new ItemStack(Blocks.PLANKS, 4), 4000);
        putRecipe(Item.getItemFromBlock(Blocks.CRAFTING_TABLE), new ItemStack(Blocks.PLANKS, 2), 4000);
        putRecipe(Item.getItemFromBlock(Blocks.WOODEN_BUTTON), new ItemStack(Blocks.PLANKS, 1), 2000);
        putRecipe(Item.getItemFromBlock(Blocks.WOODEN_PRESSURE_PLATE), new ItemStack(Blocks.PLANKS, 1), 4000);
        putRecipe(Item.getItemFromBlock(Blocks.TRAPDOOR), new ItemStack(Blocks.PLANKS, 1), 4000);
        putRecipe(Items.BOWL, new ItemStack(Blocks.PLANKS, 1), 2000);
        putRecipe(Items.SIGN, new ItemStack(Blocks.PLANKS, 2), 4000);

        putRecipe(Item.getItemFromBlock(Blocks.OAK_FENCE), new ItemStack(Blocks.PLANKS, 1), 4000);
        putRecipe(Item.getItemFromBlock(Blocks.SPRUCE_FENCE), new ItemStack(Blocks.PLANKS, 1), 4000);
        putRecipe(Item.getItemFromBlock(Blocks.BIRCH_FENCE), new ItemStack(Blocks.PLANKS, 1), 4000);
        putRecipe(Item.getItemFromBlock(Blocks.JUNGLE_FENCE), new ItemStack(Blocks.PLANKS, 1), 4000);
        putRecipe(Item.getItemFromBlock(Blocks.ACACIA_FENCE), new ItemStack(Blocks.PLANKS, 1), 4000);
        putRecipe(Item.getItemFromBlock(Blocks.DARK_OAK_FENCE), new ItemStack(Blocks.PLANKS, 1), 4000);

        putRecipe(Item.getItemFromBlock(Blocks.OAK_FENCE_GATE), new ItemStack(Blocks.PLANKS, 1), 4000);
        putRecipe(Item.getItemFromBlock(Blocks.SPRUCE_FENCE_GATE), new ItemStack(Blocks.PLANKS, 1), 4000);
        putRecipe(Item.getItemFromBlock(Blocks.BIRCH_FENCE_GATE), new ItemStack(Blocks.PLANKS, 1), 4000);
        putRecipe(Item.getItemFromBlock(Blocks.JUNGLE_FENCE_GATE), new ItemStack(Blocks.PLANKS, 1), 4000);
        putRecipe(Item.getItemFromBlock(Blocks.ACACIA_FENCE_GATE), new ItemStack(Blocks.PLANKS, 1), 4000);
        putRecipe(Item.getItemFromBlock(Blocks.DARK_OAK_FENCE_GATE), new ItemStack(Blocks.PLANKS, 1), 4000);

        putRecipe(Item.getItemFromBlock(Blocks.OAK_STAIRS), new ItemStack(Blocks.PLANKS, 1), 4000);
        putRecipe(Item.getItemFromBlock(Blocks.SPRUCE_STAIRS), new ItemStack(Blocks.PLANKS, 1), 4000);
        putRecipe(Item.getItemFromBlock(Blocks.BIRCH_STAIRS), new ItemStack(Blocks.PLANKS, 1), 4000);
        putRecipe(Item.getItemFromBlock(Blocks.JUNGLE_STAIRS), new ItemStack(Blocks.PLANKS, 1), 4000);
        putRecipe(Item.getItemFromBlock(Blocks.ACACIA_STAIRS), new ItemStack(Blocks.PLANKS, 1), 4000);
        putRecipe(Item.getItemFromBlock(Blocks.DARK_OAK_STAIRS), new ItemStack(Blocks.PLANKS, 1), 4000);

        putRecipe(Items.BOAT, new ItemStack(Blocks.PLANKS, 2), 4000);
        putRecipe(Items.SPRUCE_BOAT, new ItemStack(Blocks.PLANKS, 2), 4000);
        putRecipe(Items.BIRCH_BOAT, new ItemStack(Blocks.PLANKS, 2), 4000);
        putRecipe(Items.JUNGLE_BOAT, new ItemStack(Blocks.PLANKS, 2), 4000);
        putRecipe(Items.ACACIA_BOAT, new ItemStack(Blocks.PLANKS, 2), 4000);
        putRecipe(Items.DARK_OAK_BOAT, new ItemStack(Blocks.PLANKS, 2), 4000);

        putRecipe(Items.WOODEN_SWORD, new ItemStack(Blocks.PLANKS, 1), 4000);
        putRecipe(Items.WOODEN_SHOVEL, new ItemStack(Blocks.PLANKS, 1), 4000);
        putRecipe(Items.WOODEN_PICKAXE, new ItemStack(Blocks.PLANKS, 1), 4000);
        putRecipe(Items.WOODEN_AXE, new ItemStack(Blocks.PLANKS, 1), 4000);
        putRecipe(Items.WOODEN_HOE, new ItemStack(Blocks.PLANKS, 1), 4000);

        putRecipe(Items.LEATHER_HELMET, new ItemStack(Items.LEATHER, 2), 4000);
        putRecipe(Items.LEATHER_CHESTPLATE, new ItemStack(Items.LEATHER, 4), 4000);
        putRecipe(Items.LEATHER_LEGGINGS, new ItemStack(Items.LEATHER, 3), 4000);
        putRecipe(Items.LEATHER_BOOTS, new ItemStack(Items.LEATHER, 2), 4000);

        putRecipe(Item.getItemFromBlock(Blocks.BOOKSHELF), new ItemStack(Blocks.PLANKS, 3), 4000);
        putRecipe(Item.getItemFromBlock(Blocks.JUKEBOX), new ItemStack(Blocks.PLANKS, 4), 4000);
        putRecipe(Item.getItemFromBlock(Blocks.NOTEBLOCK), new ItemStack(Blocks.PLANKS, 4), 4000);

        putRecipe(Item.getItemFromBlock(Blocks.MELON_BLOCK), new ItemStack(Items.MELON, 9), 2000);
        putRecipe(Item.getItemFromBlock(Blocks.LEVER), new ItemStack(Blocks.COBBLESTONE, 1), 2000);
        putRecipe(Item.getItemFromBlock(Blocks.REDSTONE_TORCH), new ItemStack(Items.REDSTONE, 1), 2000);
        putRecipe(Items.PAINTING, new ItemStack(Blocks.WOOL, 1), 2000);
        putRecipe(Items.ITEM_FRAME, new ItemStack(Items.LEATHER, 1), 2000);
    }

    // Recipes to possibleOres
    protected static void PossibleRecipes(){

    }

    protected static void oresToRecipes() throws Exception {
        for (int i = 0; i < ores.size(); i++)
            if (OreDictionary.doesOreNameExist(ores.get(i).getName())){
                NonNullList<ItemStack> items = OreDictionary.getOres(ores.get(i).getName());
                if (items.size() == 0)
                    throw new Exception("Invalid sawmill recipes");
                for (int j = 0; j < items.size(); j++) {
                    recipes.put(items.get(j).getItem(), ores.get(i).getStack());
                    energy.put(items.get(j).getItem(), ores.get(i).getEnergy());
                }
            }
    }

    protected static void possibleOresToRecipes() throws Exception {
        for (int i = 0; i < possibleOres.size(); i++)
            if (OreDictionary.doesOreNameExist(possibleOres.get(i).getNameFirst()) && OreDictionary.doesOreNameExist(possibleOres.get(i).getNameSecond())){
                NonNullList<ItemStack> items = OreDictionary.getOres(possibleOres.get(i).getNameFirst());
                ItemStack out = OreDictionary.getOres(possibleOres.get(i).getNameSecond()).get(0);
                out.setCount(possibleOres.get(i).getCount());
                if (items.size() == 0 || out == null)
                    throw new Exception("Invalid sawmill recipes");
                for (int j = 0; j < items.size(); j++) {
                    recipes.put(items.get(j).getItem(), out);
                    energy.put(items.get(j).getItem(), possibleOres.get(i).getEnergy());
                }
            }
    }

    public static void putRecipe(Item item, ItemStack stack, int energy_){
        if (ExtremeEnergy.craftTweakerActive && SawmillRegister.inIgnore(new ItemStack(item)))
            return;
        recipes.put(item, stack);
        energy.put(item, energy_);
    }

    protected static void constructRecipes(){
        Object[] items = recipes.keySet().toArray();
        for (int i = 0; i < items.length; i++){
            recipesList.add(new FullRecipeSawmill((Item)items[i], recipes.get(items[i]), energy.get(items[i])));
        }
    }

    // Public methods
    public static boolean hasRecipe(Item item){
        return recipes.containsKey(item);
    }

    public static ItemStack getOut(Item item){
        ItemStack stack = recipes.get(item);
        return stack != null ? stack : ItemStack.EMPTY;
    }

    public static int getEnergy(Item item){
        try {
            return energy.get(item);
        }
        catch (NullPointerException e){ return 0; }
    }

    public static List<List<ItemStack>> getElements(ItemStack stack){
        List<List<ItemStack>> result = new ArrayList<>();
        for(FullRecipeSawmill recipe : recipesList){
            if (recipe.output.getItem() == stack.getItem()) {
                List<ItemStack> list = new ArrayList<>();
                list.add(recipe.getInput());
                result.add(list);
            }
        }
        return result;
    }

    public static int getCount(Item item){
        for(FullRecipeSawmill recipe : recipesList){
            if (recipe.input == item)
                return recipe.getOutput().getCount();
        }
        return -1;
    }

    private static class RecipeSawmillString {
        private String nameFirst;
        private String nameSecond;
        private int count;
        private int energy;

        public RecipeSawmillString(String nameFirst, String nameSecond, int energy){
            this(nameFirst, nameSecond, energy, 1);
        }

        public RecipeSawmillString(String nameFirst, String nameSecond, int energy, int count){
            this.nameFirst = nameFirst;
            this.nameSecond = nameSecond;
            this.energy = energy;
            this.count = count;
        }

        public String getNameFirst(){ return nameFirst; }
        public String getNameSecond(){ return nameSecond; }
        public int getEnergy(){ return energy; }
        public int getCount(){ return count; }
    }

    public static class RecipeSawmill {
        private String name;
        private ItemStack stack;
        private int energy;

        public RecipeSawmill(String name, ItemStack stack, int energy){
            this.name = name;
            this.stack = stack;
            this.energy = energy;
        }

        public String getName(){ return name; }
        public ItemStack getStack(){ return stack; }
        public int getEnergy(){ return energy; }
    }

    public static class FullRecipeSawmill {
        protected Item input;
        protected ItemStack output;
        protected int energy;

        public FullRecipeSawmill(Item input, ItemStack output, int energy){
            this.input = input;
            this.output = output;
            this.energy = energy;
        }

        public ItemStack getInput(){
            return new ItemStack(input);
        }
        public ItemStack getOutput(){
            return output;
        }
        public int getEnergy(){
            return energy;
        }
    }
}
