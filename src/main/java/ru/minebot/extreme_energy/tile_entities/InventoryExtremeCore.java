package ru.minebot.extreme_energy.tile_entities;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ru.minebot.extreme_energy.init.ModItems;

public class InventoryExtremeCore extends InventoryBasicCore {

    public InventoryExtremeCore(ItemStack itemStack) {
        super(itemStack, 3,new Item[] {
                ModItems.electricalModule,
                ModItems.strangeModule,
                ModItems.lifeModule,
                ModItems.aggressiveModule,
                ModItems.teleportModule,
                ModItems.lavaModule,
                ModItems.windModule,
                ModItems.shieldModule,
                ModItems.lightningModule,
                ModItems.securityModule
        });
    }
}
