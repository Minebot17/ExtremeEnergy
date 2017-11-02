package ru.minebot.extreme_energy.gui.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import ru.minebot.extreme_energy.gui.slots.CardSlot;
import ru.minebot.extreme_energy.gui.slots.ModuleSlot;
import ru.minebot.extreme_energy.tile_entities.TileEntityFC;

public class FcContainer extends BasicContainer{

    public FcContainer(IInventory player, TileEntityFC entity){
        super(player, entity, 8);
        int posX = entity.isPublic() ? 8000 : -22;
        this.addSlotToContainer(new CardSlot(entity, entity, 0, posX, 34));
        this.addSlotToContainer(new CardSlot(entity, entity, 1, posX, 52));
        this.addSlotToContainer(new CardSlot(entity, entity, 2, posX, 70));
        this.addSlotToContainer(new CardSlot(entity, entity, 3, posX, 88));
        this.addSlotToContainer(new CardSlot(entity, entity, 4, posX, 106));

        this.addSlotToContainer(new ModuleSlot(entity, entity, 5, 181, 4));
        this.addSlotToContainer(new ModuleSlot(entity, entity, 6, 181, 22));
        this.addSlotToContainer(new ModuleSlot(entity, entity, 7, 181, 40));

        // Player Inventory, Slot 9-35, Slot IDs 9-35
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(player, x + y * 9 + 9, 8 + x * 18, 98 + y * 18));
            }
        }

        // Player Inventory, Slot 0-8, Slot IDs 36-44
        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(player, x, 8 + x * 18, 156));
        }
    }

    public void changeSlots(boolean isPublic){
        for (int i = 0; i < 5; i++)
            inventorySlots.get(i).xPos = isPublic ? 8000 : -22;
    }
}
