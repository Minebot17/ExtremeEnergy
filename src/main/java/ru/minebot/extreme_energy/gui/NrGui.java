package ru.minebot.extreme_energy.gui;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.IInventory;
import org.lwjgl.opengl.GL11;
import ru.minebot.extreme_energy.gui.containers.NrContainer;
import ru.minebot.extreme_energy.gui.elements.ProgressBar;
import ru.minebot.extreme_energy.items.ItemNuclearFuel;
import ru.minebot.extreme_energy.tile_entities.TileEntityNR;

public class NrGui extends BasicGuiContainer<TileEntityNR> {

    protected ProgressBar barRF;
    protected ProgressBar barHeat;
    protected ProgressBar barFuel;

    public NrGui(IInventory player, TileEntityNR te) {
        super(te, new NrContainer(player, te), "meem:textures/gui/nrgui.png", 176, 171, BasicGuiContainer.STANDART_COLOR, 3);
    }

    @Override
    public void initGui(){
        super.initGui();
        barRF = new ProgressBar(ProgressBar.Type.RF);
        barHeat = new ProgressBar(ProgressBar.Type.HEAT);
        barFuel = new ProgressBar(ProgressBar.Type.NUCLEAR_FUEL);
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        if (te.isInvalid())
            te = (TileEntityNR) te.getWorld().getTileEntity(te.getPos());
    }

    @Override
    protected void draw(int mouseX, int mouseY) {
        barFuel.draw(mc, 27, 30, mouseX, mouseY, te.getFuel(), ItemNuclearFuel.workTime);
        barHeat.draw(mc, 27, 50, mouseX, mouseY, te.getHeat(), te.getMaxHeat());
        barRF.draw(mc, 27, 70, mouseX, mouseY, te.getEnergyStored(), te.getMaxEnergyStored());
    }

    @Override
    protected void markDirty() {

    }
}
