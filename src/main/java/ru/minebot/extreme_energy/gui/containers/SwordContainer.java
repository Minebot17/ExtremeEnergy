package ru.minebot.extreme_energy.gui.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.minebot.extreme_energy.gui.slots.DontTakeSlot;
import ru.minebot.extreme_energy.gui.slots.ModuleSlot;
import ru.minebot.extreme_energy.tile_entities.InventorySword;

public class SwordContainer extends BasicContainer {

    public SwordContainer(IInventory player, InventorySword te){
        super(player, te, 1);

        addSlotToContainer(new ModuleSlot(te, te, 0, 61, 147));

        // Player Inventory, Slot 9-35, Slot IDs 9-35
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(player, x + y * 9 + 9, 48 + x * 18, 174 + y * 18));
            }
        }

        // Player Inventory, Slot 0-8, Slot IDs 36-44
        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(player, x, 48 + x * 18, 232));
        }

        int index = 0;
        for (int i = 28; i < inventorySlots.size(); i++)
            if (te.sword.equals(inventorySlots.get(i).getStack())) {
                index = i;
                break;
            }
        inventorySlots.set(index, new DontTakeSlot(player, index - 28, 48 + (index - 28) * 18, 232));
    }
}
