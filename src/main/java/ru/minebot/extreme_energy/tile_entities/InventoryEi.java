package ru.minebot.extreme_energy.tile_entities;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import ru.minebot.extreme_energy.init.ModItems;

public class InventoryEi extends InventoryBi {

    public InventoryEi(ItemStack implant){
        super(implant, 7,new Item[] {
                ModItems.electricalModule,
                ModItems.strangeModule,
                ModItems.lifeModule,
                ModItems.aggressiveModule,
                ModItems.luckModule,
                ModItems.teleportModule,
                ModItems.plantModule,
                ModItems.waterModule,
                ModItems.lavaModule,
                ModItems.windModule,
                ModItems.shieldModule,
                ModItems.lightningModule,
                ModItems.hungerEnergyModule,
                ModItems.motionEnergyModule,
                ModItems.chunkEnergyModule,
                ModItems.sunEnergyModule,
                ModItems.lavaEnergyModule,
                ModItems.nuclearEnergyModule,
                ModItems.linksInfoModule,
                ModItems.entityInfoModule,
                ModItems.mapInfoModule,
                ModItems.pathInfoModule
        });
    }
}
