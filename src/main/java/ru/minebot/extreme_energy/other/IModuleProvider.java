package ru.minebot.extreme_energy.other;

import net.minecraft.item.Item;

public interface IModuleProvider {
    Item[] getAcceptedModules();

    default boolean isAccepted(Item item){
        Item[] items = getAcceptedModules();
        for (Item item1 : items)
            if (item1.equals(item))
                return true;
        return false;
    }

    void updateValueModules();
}
