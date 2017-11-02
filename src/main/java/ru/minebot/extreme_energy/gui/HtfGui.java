package ru.minebot.extreme_energy.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import ru.minebot.extreme_energy.gui.containers.HtfContainer;
import ru.minebot.extreme_energy.gui.elements.FrameOfModules;
import ru.minebot.extreme_energy.modules.Module;
import ru.minebot.extreme_energy.tile_entities.TileEntityHTF;

public class HtfGui extends ReceiverGui<TileEntityHTF> {

    public HtfGui(EntityPlayer player, IInventory playerInv, final TileEntityHTF te) {
        super(te, new HtfContainer(player, playerInv, te), "meem:textures/gui/htfgui.png",176, 187, 1);
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        if (te.isInvalid())
            te = (TileEntityHTF) te.getWorld().getTileEntity(te.getPos());
        if (te.isBurn){
            this.drawTexturedModalRect(70 + guiLeft, 33 + guiTop, 176, 0, te.burnPhase + 1, 16);
        }
    }

    @Override
    protected void markDirty() {

    }
}
