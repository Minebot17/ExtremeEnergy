package ru.minebot.extreme_energy.gui.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import ru.minebot.extreme_energy.gui.slots.DontTakeSlot;
import ru.minebot.extreme_energy.gui.slots.ModuleSlot;
import ru.minebot.extreme_energy.gui.slots.SingleSlot;
import ru.minebot.extreme_energy.tile_entities.InventoryAi;

public class AiContainer extends BasicContainer {

    public AiContainer(IInventory player, InventoryAi te){
        super(player, te, 5);

        addSlotToContainer(new ModuleSlot(te, te, 0, 58, 16, 0));
        addSlotToContainer(new ModuleSlot(te, te, 1, 102, 16, 0));
        addSlotToContainer(new ModuleSlot(te, te, 2, 58, 40, 1));
        addSlotToContainer(new ModuleSlot(te, te, 3, 102, 40, 1));
        addSlotToContainer(new SingleSlot(te, 4, 80, 28));


        // Player Inventory, Slot 9-35, Slot IDs 9-35
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(player, x + y * 9 + 9, 8 + x * 18, 168 + y * 18));
            }
        }

        // Player Inventory, Slot 0-8, Slot IDs 36-44
        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(player, x, 8 + x * 18, 226));
        }

        int index = 0;
        for (int i = 32; i < inventorySlots.size(); i++)
            if (te.itemStack.equals(inventorySlots.get(i).getStack())) {
                index = i;
                break;
            }
        inventorySlots.set(index, new DontTakeSlot(player, index - 32, 8 + (index - 32) * 18, 226));
    }
}
