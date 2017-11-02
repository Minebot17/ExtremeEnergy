package ru.minebot.extreme_energy.recipes.crusher;

public class RecipeCrusherString {
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
