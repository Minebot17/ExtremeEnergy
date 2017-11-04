package ru.minebot.extreme_energy.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import ru.minebot.extreme_energy.gui.containers.HpaContainer;
import ru.minebot.extreme_energy.tile_entities.TileEntityHPA;

public class HpaGui extends ReceiverGui<TileEntityHPA> {

    public HpaGui(EntityPlayer player, IInventory playerInv, final TileEntityHPA te) {
        super(te, new HpaContainer(player, playerInv, te), "meem:textures/gui/hpagui.png", 176, 187, 1);
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        if (te.isInvalid())
            te = (TileEntityHPA) te.getWorld().getTileEntity(te.getPos());
        if (te.isWork){
            this.drawTexturedModalRect(69 + guiLeft, 32 + guiTop, 176, 0, te.workPhase, 18); // 30 phase
        }
    }

    @Override
    protected void markDirty() {

    }
}
