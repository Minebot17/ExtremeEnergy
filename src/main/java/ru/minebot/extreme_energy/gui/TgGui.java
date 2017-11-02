package ru.minebot.extreme_energy.gui;

import net.minecraft.inventory.IInventory;
import ru.minebot.extreme_energy.gui.containers.TgContainer;
import ru.minebot.extreme_energy.gui.elements.ProgressBar;
import ru.minebot.extreme_energy.gui.elements.LavaBar;
import ru.minebot.extreme_energy.gui.elements.LavaBarStandart;
import ru.minebot.extreme_energy.tile_entities.TileEntityTG;

public class TgGui extends BasicGuiContainer<TileEntityTG> {

    protected ProgressBar bar;
    protected LavaBar fluidBar;

    public TgGui(IInventory player, TileEntityTG te){
        super(te, new TgContainer(player, te), "meem:textures/gui/tggui.png", 176, 140, STANDART_COLOR, 2);
    }

    @Override
    public void initGui(){
        super.initGui();
        bar = new ProgressBar(ProgressBar.Type.RF);
        fluidBar = new LavaBarStandart();
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        if (te.isInvalid())
            te = (TileEntityTG) te.getWorld().getTileEntity(te.getPos());
    }

    @Override
    protected void draw(int mouseX, int mouseY){
        fluidBar.draw(mc, 27, 18, mouseX, mouseY, te.milliBuckets, te.maxMilliBuckets);
        bar.draw(mc, 26, 39, mouseX, mouseY, te.getEnergyStored(), te.getMaxEnergyStored());
    }

    @Override
    protected void markDirty() {

    }
}
