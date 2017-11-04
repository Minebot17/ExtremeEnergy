package ru.minebot.extreme_energy.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import ru.minebot.extreme_energy.gui.containers.HpcContainer;
import ru.minebot.extreme_energy.tile_entities.TileEntityHPC;

public class HpcGui extends ReceiverGui<TileEntityHPC> {

    public HpcGui(EntityPlayer player, IInventory playerInv, final TileEntityHPC te) {
        super(te, new HpcContainer(player, playerInv, te), "meem:textures/gui/hpcgui.png",176, 199,1);
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        if (te.isInvalid())
            te = (TileEntityHPC) te.getWorld().getTileEntity(te.getPos());
        if (te.isWork){
            this.drawTexturedModalRect(69 + guiLeft, 25 + guiTop, 176, 0, te.workPhase + 1, 42); // 30 phase
        }
    }

    @Override
    protected void markDirty() {

    }
}
