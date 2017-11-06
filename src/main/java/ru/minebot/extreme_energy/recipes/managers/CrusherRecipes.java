package ru.minebot.extreme_energy.recipes.managers;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import ru.minebot.extreme_energy.init.ModItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CrusherRecipes {

    public static List<FullRecipeCrusher> recipesList;
    protected static ArrayList<RecipeCrusherString> possibleOres;
    protected static ArrayList<RecipeCrusher> ores;
    protected static HashMap<Item, ItemStack> recipes;
    protected static HashMap<Item, Integer> energy;

    public static void init() throws Exception {
        recipes = new HashMap<>();
        ores = new ArrayList<>();
        possibleOres = new ArrayList<>();
        energy = new HashMap<>();
        recipesList = new ArrayList<>();

        OreDictionaryRecipes();
        oresToRecipes();
        NotOreDictionaryRecipes();
        PossibleRecipes();
        possibleOresToRecipes();
        constructRecipes();
    }

    // Recipes to ores
    protected static void OreDictionaryRecipes(){
        ores.add(new RecipeCrusher("oreCopper", new ItemStack(ModItems.copperDust, 2), 10000));
        ores.add(new RecipeCrusher("ingotCopper", new ItemStack(ModItems.copperDust, 1), 5000));
        ores.add(new RecipeCrusher("oreIron", new ItemStack(ModItems.ironDust, 2), 10000));
        ores.add(new RecipeCrusher("ingotIron", new ItemStack(ModItems.ironDust, 1), 5000));
        ores.add(new RecipeCrusher("oreGold", new ItemStack(ModItems.goldDust, 2), 10000));
        ores.add(new RecipeCrusher("ingotGold", new ItemStack(ModItems.goldDust, 1), 5000));
        ores.add(new RecipeCrusher("oreCoal", new ItemStack(ModItems.coalDust, 4), 7000));
        ores.add(new RecipeCrusher("ingotSteel", new ItemStack(ModItems.heavyDust, 1), 10000));
        ores.add(new RecipeCrusher("oreDiamond", new ItemStack(Items.DIAMOND, 2), 10000));
        ores.add(new RecipeCrusher("oreEmerald", new ItemStack(Items.EMERALD, 2), 10000));
        ores.add(new RecipeCrusher("oreLapis", new ItemStack(Items.DYE, 16, 4), 10000));
        ores.add(new RecipeCrusher("oreRedstone", new ItemStack(Items.REDSTONE, 6), 10000));
        ores.add(new RecipeCrusher("oreQuartz", new ItemStack(Items.QUARTZ, 3), 10000));
        ores.add(new RecipeCrusher("stone", new ItemStack(Blocks.GRAVEL), 2000));
        ores.add(new RecipeCrusher("cobblestone", new ItemStack(Blocks.SAND), 2000));
        ores.add(new RecipeCrusher("glowstone", new ItemStack(Items.GLOWSTONE_DUST, 4), 2000));
        ores.add(new RecipeCrusher("stone", new ItemStack(Blocks.GRAVEL), 2000));
        ores.add(new RecipeCrusher("oreUranium", new ItemStack(ModItems.uraniumDust, 2), 10000));
        ores.add(new RecipeCrusher("ingotUranium", new ItemStack(ModItems.uraniumDust), 5000));
    }

    // Recipes to recipes with method 'putRecipe'
    protected static void NotOreDictionaryRecipes(){
        putRecipe(Items.COAL, new ItemStack(ModItems.coalDust, 1), 7000);
        putRecipe(ModItems.capsule, new ItemStack(ModItems.ironDust, 2), 4000);
        putRecipe(Item.getItemFromBlock(Blocks.GRAVEL), new ItemStack(Items.FLINT), 2000);
        putRecipe(Item.getItemFromBlock(Blocks.NETHERRACK), new ItemStack(Blocks.GRAVEL), 2000);
        putRecipe(Item.getItemFromBlock(Blocks.MAGMA), new ItemStack(Items.MAGMA_CREAM, 4), 2000);
        putRecipe(Items.BLAZE_ROD, new ItemStack(Items.BLAZE_POWDER, 4), 20000);
        putRecipe(Items.NETHER_STAR, new ItemStack(ModItems.californium, 2), 100000);
    }

    // Recipes to possibleOres
    protected static void PossibleRecipes(){
        possibleOres.add(new RecipeCrusherString("blockObsidian", "dustObsidian", 5000, 4));
        possibleOres.add(new RecipeCrusherString("ingotTin", "dustTin", 5000));
        possibleOres.add(new RecipeCrusherString("ingotLead", "dustLead", 5000));
        possibleOres.add(new RecipeCrusherString("ingotPlatinum", "dustPlatinum", 5000));
        possibleOres.add(new RecipeCrusherString("ingotInvar", "dustInvar", 5000));
        possibleOres.add(new RecipeCrusherString("ingotEnderium", "dustEnderium", 5000));
        possibleOres.add(new RecipeCrusherString("ingotSilver", "dustSilver", 5000));
        possibleOres.add(new RecipeCrusherString("ingotNickel", "dustNickel", 5000));
        possibleOres.add(new RecipeCrusherString("ingotElectrum", "dustElectrum", 5000));
        possibleOres.add(new RecipeCrusherString("ingotBronze", "dustBronze", 5000));

        possibleOres.add(new RecipeCrusherString("oreTin", "dustTin", 12000, 2));
        possibleOres.add(new RecipeCrusherString("oreSilver", "dustSilver", 12000, 2));
        possibleOres.add(new RecipeCrusherString("oreAluminum", "dustAluminum", 12000, 2));
        possibleOres.add(new RecipeCrusherString("oreLead", "dustLead", 12000, 2));
        possibleOres.add(new RecipeCrusherString("oreNickel", "dustNickel", 12000, 2));
        possibleOres.add(new RecipeCrusherString("orePlatinum", "dustPlatinum", 12000, 2));
        possibleOres.add(new RecipeCrusherString("oreIridium", "dustIridium", 12000, 2));
        possibleOres.add(new RecipeCrusherString("oreMithril", "dustMithril", 12000, 2));

        //AE 2
        possibleOres.add(new RecipeCrusherString("oreCertusQuartz", "crystalCertusQuartz", 12000, 2));
        possibleOres.add(new RecipeCrusherString("crystalCertusQuartz", "dustCertusQuartz", 5000));
        possibleOres.add(new RecipeCrusherString("crystalFluix", "dustFluix", 12000));
        possibleOres.add(new RecipeCrusherString("gemQuartz", "dustNetherQuartz", 12000));

        //Forestry
        possibleOres.add(new RecipeCrusherString("oreApatite", "gemApatite", 12000, 12));

        //Other
        possibleOres.add(new RecipeCrusherString("oreNiter", "dustNiter", 12000, 4));
        possibleOres.add(new RecipeCrusherString("oreSaltpeter", "dustNiter", 12000, 4));
        possibleOres.add(new RecipeCrusherString("oreSulfur", "dustSulfur", 12000, 6));
    }

    protected static void oresToRecipes() throws Exception {
        for (int i = 0; i < ores.size(); i++)
            if (OreDictionary.doesOreNameExist(ores.get(i).getName())){
                NonNullList<ItemStack> items = OreDictionary.getOres(ores.get(i).getName());
                if (items.size() == 0)
                    throw new Exception("Invalid crusher recipes");
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
                if (items.size() != 0)
                    break;

                ItemStack out = OreDictionary.getOres(possibleOres.get(i).getNameSecond()).get(0);
                out.setCount(possibleOres.get(i).getCount());
                if (items.size() == 0 || out == null)
                    throw new Exception("Invalid crusher recipes");
                for (int j = 0; j < items.size(); j++) {
                    recipes.put(items.get(j).getItem(), out);
                    energy.put(items.get(j).getItem(), possibleOres.get(i).getEnergy());
                }
            }
    }

    protected static void putRecipe(Item item, ItemStack stack, int energy_){
        recipes.put(item, stack);
        energy.put(item, energy_);
    }

    protected static void constructRecipes(){
        Object[] items = recipes.keySet().toArray();
        for (int i = 0; i < items.length; i++){
            recipesList.add(new FullRecipeCrusher((Item)items[i], recipes.get(items[i]), energy.get(items[i])));
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
        for(FullRecipeCrusher recipe : recipesList){
            if (recipe.output.getItem() == stack.getItem()) {
                List<ItemStack> list = new ArrayList<>();
                list.add(new ItemStack(recipe.getInput()));
                result.add(list);
            }
        }
        return result;
    }

    public static int getCount(Item item){
        for(FullRecipeCrusher recipe : recipesList){
            if (recipe.input == item)
                return recipe.getOutput().getCount();
        }
        return -1;
    }

    private static class RecipeCrusherString {
        private String nameFirst;
        private String nameSecond;
        private int count;
        private int energy;

        public RecipeCrusherString(String nameFirst, String nameSecond, int energy){
            this(nameFirst, nameSecond, energy, 1);
        }

        public RecipeCrusherString(String nameFirst, String nameSecond, int energy, int count){
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

    private static class RecipeCrusher {
        private String name;
        private ItemStack stack;
        private int energy;

        public RecipeCrusher(String name, ItemStack stack, int energy){
            this.name = name;
            this.stack = stack;
            this.energy = energy;
        }

        public String getName(){ return name; }
        public ItemStack getStack(){ return stack; }
        public int getEnergy(){ return energy; }
    }

    public static class FullRecipeCrusher {
        protected Item input;
        protected ItemStack output;
        protected int energy;

        public FullRecipeCrusher(Item input, ItemStack output, int energy){
            this.input = input;
            this.output = output;
            this.energy = energy;
        }

        public Item getInput(){
            return input;
        }

        public ItemStack getOutput(){
            return output;
        }

        public int getEnergy(){
            return energy;
        }
    }
}

