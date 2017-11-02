package ru.minebot.extreme_energy.gui.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import ru.minebot.extreme_energy.gui.slots.ModuleSlot;
import ru.minebot.extreme_energy.gui.slots.SingleSlot;
import ru.minebot.extreme_energy.gui.slots.ValidSlot;
import ru.minebot.extreme_energy.tile_entities.TileEntitySG;

public class SgContainer extends BasicContainer {

    public SgContainer(IInventory player, TileEntitySG te) {
        super(player, te, 6);

        this.addSlotToContainer(new SingleSlot(te, 0, 8, 69));
        this.addSlotToContainer(new ValidSlot(te, 1, 8, 49));

        this.addSlotToContainer(new ModuleSlot(te, te, 2, 181, 4));
        this.addSlotToContainer(new ModuleSlot(te, te, 3, 181, 22));
        this.addSlotToContainer(new ModuleSlot(te, te, 4, 181, 40));
        this.addSlotToContainer(new ModuleSlot(te, te, 5, 181, 58));

        // Player Inventory, Slot 9-35, Slot IDs 9-35
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(player, x + y * 9 + 9, 8 + x * 18, 89 + y * 18));
            }
        }

        // Player Inventory, Slot 0-8, Slot IDs 36-44
        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(player, x, 8 + x * 18, 147));
        }
    }
}
