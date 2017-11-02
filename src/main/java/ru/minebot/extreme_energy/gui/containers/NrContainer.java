package ru.minebot.extreme_energy.gui.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import ru.minebot.extreme_energy.gui.slots.ModuleSlot;
import ru.minebot.extreme_energy.gui.slots.SingleSlot;
import ru.minebot.extreme_energy.gui.slots.ValidSlot;
import ru.minebot.extreme_energy.tile_entities.TileEntityNR;

public class NrContainer extends BasicContainer {
    public NrContainer(IInventory player, TileEntityNR te) {
        super(player, te, 6);

        addSlotToContainer(new ValidSlot(te, 1, 8, 29));
        addSlotToContainer(new ValidSlot(te, 0, 8, 49));
        addSlotToContainer(new SingleSlot(te, 2, 8, 69));

        this.addSlotToContainer(new ModuleSlot(te, te, 3, 181, 4));
        this.addSlotToContainer(new ModuleSlot(te, te, 4, 181, 22));
        this.addSlotToContainer(new ModuleSlot(te, te, 5, 181, 40));

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
