package ru.minebot.extreme_energy.gui.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import ru.minebot.extreme_energy.gui.slots.ModuleSlot;
import ru.minebot.extreme_energy.gui.slots.SingleSlot;
import ru.minebot.extreme_energy.tile_entities.TileEntityHVG;

public class HvgContainer extends BasicContainer{

    public HvgContainer(IInventory player, TileEntityHVG entity){
        super(player, entity, 6);

        this.addSlotToContainer(new SingleSlot(entity, 0, 8, 68));

        this.addSlotToContainer(new ModuleSlot(entity, entity, 1, 181, 4));
        this.addSlotToContainer(new ModuleSlot(entity, entity, 2, 181, 22));
        this.addSlotToContainer(new ModuleSlot(entity, entity, 3, 181, 40));
        this.addSlotToContainer(new ModuleSlot(entity, entity, 4, 181, 58));
        this.addSlotToContainer(new ModuleSlot(entity, entity, 5, 181, 76));

        // Player Inventory, Slot 9-35, Slot IDs 9-35
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(player, x + y * 9 + 9, 8 + x * 18, 88 + y * 18));
            }
        }

        // Player Inventory, Slot 0-8, Slot IDs 36-44
        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(player, x, 8 + x * 18, 146));
        }
    }
}
