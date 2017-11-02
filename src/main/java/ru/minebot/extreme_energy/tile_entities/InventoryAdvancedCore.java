package ru.minebot.extreme_energy.tile_entities;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ru.minebot.extreme_energy.init.ModItems;

public class InventoryAdvancedCore extends InventoryBasicCore {

    public InventoryAdvancedCore(ItemStack core) {
        super(core, 2,new Item[] {
                ModItems.electricalModule,
                ModItems.strangeModule,
                ModItems.lifeModule,
                ModItems.aggressiveModule,
                ModItems.teleportModule,
                ModItems.lavaModule,
                ModItems.securityModule
        });
    }
}
