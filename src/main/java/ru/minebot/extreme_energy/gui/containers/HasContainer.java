package ru.minebot.extreme_energy.gui.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import ru.minebot.extreme_energy.gui.slots.ModuleSlot;
import ru.minebot.extreme_energy.tile_entities.TileEntityHAS;

public class HasContainer extends BasicContainer {
    public HasContainer(EntityPlayer player, IInventory inv, TileEntityHAS entity) {
        super(inv, entity, 3);

        this.addSlotToContainer(new Slot(entity, 0, 47, 33));
        this.addSlotToContainer(new SlotFurnaceOutput(player, entity, 1, 107, 33));

        addSlotToContainer(new ModuleSlot(entity, entity, 2, 181, 4));

        // Player Inventory, Slot 9-35, Slot IDs 9-35
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(inv, x + y * 9 + 9, 8 + x * 18, 105 + y * 18));
            }
        }

        // Player Inventory, Slot 0-8, Slot IDs 36-44
        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(inv, x, 8 + x * 18, 163));
        }
    }
}
