package ru.minebot.extreme_energy.modules;

public abstract class ModuleFunctional extends Module{
    public ModuleFunctional(String unlocName, String regName) {
        super(unlocName, regName, 1, false);
    }

    public abstract String[] getInfo();

    // Events
    public void firstUpdate(FuncArgs args){}

    public void putModule(FuncArgs args){}

    public void removeModule(FuncArgs args){}

    public void changeActive(FuncArgs args, boolean active){}

    public void changeRadius(FuncArgs args){}

    public void changeVoltage(FuncArgs args){}

    public void changeLink(FuncArgs args){}

    public void openInventory(FuncArgs args){}

    public void closeInventory(FuncArgs args){}

    public void loadWorld(FuncArgs args, boolean active){}
}

