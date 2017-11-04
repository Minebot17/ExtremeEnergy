package ru.minebot.extreme_energy.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import ru.minebot.extreme_energy.gui.containers.HasContainer;
import ru.minebot.extreme_energy.tile_entities.TileEntityHAS;

public class HasGui extends ReceiverGui<TileEntityHAS>{
    public HasGui(EntityPlayer player, IInventory playerInv, final TileEntityHAS te) {
        super(te, new HasContainer(player, playerInv, te), "meem:textures/gui/hasgui.png", 176, 187, 1);
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        if (te.isInvalid())
            te = (TileEntityHAS) te.getWorld().getTileEntity(te.getPos());
        if (te.isWork){
            this.drawTexturedModalRect(72 + guiLeft, 30 + guiTop, 176, 0, te.workPhase, 23); // 22 phase
        }
    }

    @Override
    protected void markDirty() {

    }
}
