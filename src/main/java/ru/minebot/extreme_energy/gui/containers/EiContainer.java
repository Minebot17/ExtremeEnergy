package ru.minebot.extreme_energy.gui.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import ru.minebot.extreme_energy.gui.slots.DontTakeSlot;
import ru.minebot.extreme_energy.gui.slots.ModuleSlot;
import ru.minebot.extreme_energy.gui.slots.SingleSlot;
import ru.minebot.extreme_energy.tile_entities.InventoryEi;

public class EiContainer extends BasicContainer{

    public EiContainer(IInventory player, InventoryEi te){
        super(player, te, 7);

        addSlotToContainer(new ModuleSlot(te, te, 0, 58, 16, 0));
        addSlotToContainer(new ModuleSlot(te, te, 1, 58, 58, 0));
        addSlotToContainer(new ModuleSlot(te, te, 2, 80, 16, 1));
        addSlotToContainer(new ModuleSlot(te, te, 3, 80, 58, 1));
        addSlotToContainer(new ModuleSlot(te, te, 4, 102, 16, 2));
        addSlotToContainer(new ModuleSlot(te, te, 5, 102, 58, 2));
        addSlotToContainer(new SingleSlot(te, 6, 80, 37));

        // Player Inventory, Slot 9-35, Slot IDs 9-35
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(player, x + y * 9 + 9, 8 + x * 18, 160 + y * 18));
            }
        }

        // Player Inventory, Slot 0-8, Slot IDs 36-44
        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(player, x, 8 + x * 18, 218));
        }

        int index = 0;
        for (int i = 34; i < inventorySlots.size(); i++)
            if (te.itemStack.equals(inventorySlots.get(i).getStack())) {
                index = i;
                break;
            }
        inventorySlots.set(index, new DontTakeSlot(player, index - 34, 8 + (index - 34) * 18, 218));
    }
}
