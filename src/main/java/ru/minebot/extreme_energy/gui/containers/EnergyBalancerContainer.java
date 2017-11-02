package ru.minebot.extreme_energy.gui.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ru.minebot.extreme_energy.gui.slots.DontTakeSlot;
import ru.minebot.extreme_energy.gui.slots.ValidSlot;
import ru.minebot.extreme_energy.tile_entities.InventoryEnergyBalancer;

public class EnergyBalancerContainer extends BasicContainer {
    public EnergyBalancerContainer(IInventory player, InventoryEnergyBalancer te){
        super(player, te, 2);

        addSlotToContainer(new ValidSlot(te, 0, 42, 36));
        addSlotToContainer(new ValidSlot(te, 1, 119, 36));

        // Player Inventory, Slot 9-35, Slot IDs 9-35
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(player, x + y * 9 + 9, 8 + x * 18, 111 + y * 18));
            }
        }

        // Player Inventory, Slot 0-8, Slot IDs 36-44
        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(player, x, 8 + x * 18, 169));
        }

        int index = 0;
        for (int i = 29; i < inventorySlots.size(); i++)
            if (te.itemStack.equals(inventorySlots.get(i).getStack())) {
                index = i;
                break;
            }
        inventorySlots.set(index, new DontTakeSlot(player, index - 29, 8 + (index - 29) * 18, 169));
    }
}
