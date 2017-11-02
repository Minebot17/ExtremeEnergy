package ru.minebot.extreme_energy.tile_entities;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ru.minebot.extreme_energy.init.ModItems;

public class InventoryAi extends InventoryBi {

    public InventoryAi(ItemStack implant){
        super(implant, 5,new Item[] {
                ModItems.electricalModule,
                ModItems.strangeModule,
                ModItems.lifeModule,
                ModItems.aggressiveModule,
                ModItems.luckModule,
                ModItems.teleportModule,
                ModItems.plantModule,
                ModItems.waterModule,
                ModItems.lavaModule,
                ModItems.hungerEnergyModule,
                ModItems.motionEnergyModule,
                ModItems.chunkEnergyModule,
                ModItems.sunEnergyModule,
                ModItems.linksInfoModule,
                ModItems.entityInfoModule,
                ModItems.mapInfoModule
        });
    }
}
