package ru.minebot.extreme_energy.gui;

import net.minecraft.inventory.IInventory;
import ru.minebot.extreme_energy.gui.containers.FgContainer;
import ru.minebot.extreme_energy.gui.elements.ProgressBar;
import ru.minebot.extreme_energy.tile_entities.TileEntityFG;

public class FgGui extends BasicGuiContainer<TileEntityFG> {

    protected ProgressBar bar;

    public FgGui(IInventory player, TileEntityFG te){
        super(te, new FgContainer(player, te), "meem:textures/gui/fggui.png", 176, 151, STANDART_COLOR, 2);
    }

    @Override
    public void initGui(){
        super.initGui();
        bar = new ProgressBar(ProgressBar.Type.RF);
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        if (te.isInvalid())
            te = (TileEntityFG) te.getWorld().getTileEntity(te.getPos());
        if (te.isBurn){
            drawTexturedModalRect(58 + guiLeft, 26 + guiTop + te.burnPhase, 176, te.burnPhase, 14, 14 - te.burnPhase);
            drawTexturedModalRect(105 + guiLeft, 26 + guiTop + te.burnPhase, 176, te.burnPhase, 14, 14 - te.burnPhase);
        }
    }

    @Override
    protected void draw(int mouseX, int mouseY){
        bar.draw(mc, 26, 50, mouseX, mouseY, te.getEnergyStored(), te.getMaxEnergyStored());
    }

    @Override
    protected void markDirty() {

    }
}
