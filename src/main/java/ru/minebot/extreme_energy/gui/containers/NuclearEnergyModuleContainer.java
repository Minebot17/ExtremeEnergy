package ru.minebot.extreme_energy.gui.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.minebot.extreme_energy.gui.slots.ValidSlot;
import ru.minebot.extreme_energy.tile_entities.InventoryNuclearEnergyModule;

public class NuclearEnergyModuleContainer extends BasicContainer {
    public NuclearEnergyModuleContainer(IInventory player, ItemStack stack){
        super(player, new InventoryNuclearEnergyModule(stack), 3);

        addSlotToContainer(new ValidSlot(te, 0, 80, 42));

        // Player Inventory, Slot 9-35, Slot IDs 9-35
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(player, x + y * 9 + 9, 8 + x * 18, 110 + y * 18));
            }
        }

        // Player Inventory, Slot 0-8, Slot IDs 36-44
        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(player, x, 8 + x * 18, 168));
        }
    }
}
