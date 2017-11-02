package ru.minebot.extreme_energy.init;

import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryRegisters {

    public static void register(){
        OreDictionary.registerOre("oreCopper", ModBlocks.copperOre);
        OreDictionary.registerOre("ingotCopper", ModItems.copperIngot);
        OreDictionary.registerOre("dustCopper", ModItems.copperDust);
        OreDictionary.registerOre("nuggetCopper", ModItems.copperNugget);
        OreDictionary.registerOre("blockCopper", ModBlocks.copper);
        OreDictionary.registerOre("dustGold", ModItems.goldDust);
        OreDictionary.registerOre("dustIron", ModItems.ironDust);
        OreDictionary.registerOre("dustCoal", ModItems.coalDust);
        OreDictionary.registerOre("stickIron", ModItems.ironRod);
        OreDictionary.registerOre("ingotSteel", ModItems.heavyIngot);
        OreDictionary.registerOre("dustSteel", ModItems.heavyDust);
        OreDictionary.registerOre("oreUranium", ModBlocks.uraniumOre);
        OreDictionary.registerOre("ingotUranium", ModItems.uranium);
        OreDictionary.registerOre("dustUranium", ModItems.uraniumDust);
    }
}
