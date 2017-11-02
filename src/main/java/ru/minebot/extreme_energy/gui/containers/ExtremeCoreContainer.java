package ru.minebot.extreme_energy.gui.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import ru.minebot.extreme_energy.gui.slots.DontTakeSlot;
import ru.minebot.extreme_energy.gui.slots.ModuleSlot;
import ru.minebot.extreme_energy.tile_entities.InventoryExtremeCore;

public class ExtremeCoreContainer extends BasicContainer{

    public ExtremeCoreContainer(IInventory player, InventoryExtremeCore te){
        super(player, te, 3);

        addSlotToContainer(new ModuleSlot(te, te, 0, 66, 26, 0));
        addSlotToContainer(new ModuleSlot(te, te, 1, 94, 26, 1));
        addSlotToContainer(new ModuleSlot(te, te, 2, 80, 49, 2));

        // Player Inventory, Slot 9-35, Slot IDs 9-35
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(player, x + y * 9 + 9, 8 + x * 18, 101 + y * 18));
            }
        }

        // Player Inventory, Slot 0-8, Slot IDs 36-44
        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(player, x, 8 + x * 18, 159));
        }

        int index = 0;
        for (int i = 30; i < inventorySlots.size(); i++)
            if (te.itemStack.equals(inventorySlots.get(i).getStack())) {
                index = i;
                break;
            }
        inventorySlots.set(index, new DontTakeSlot(player, index - 30, 8 + (index - 30) * 18, 159));
    }
}
