package ru.minebot.extreme_energy.gui;

import net.minecraft.inventory.IInventory;
import org.lwjgl.input.Keyboard;
import ru.minebot.extreme_energy.gui.containers.EbContainer;
import ru.minebot.extreme_energy.gui.elements.ProgressBar;
import ru.minebot.extreme_energy.gui.elements.sideButtonsModule.SideButtonModuleStandart;
import ru.minebot.extreme_energy.gui.elements.sideButtonsModule.SideButtonsModule;
import ru.minebot.extreme_energy.gui.tablet.Element;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketEB;
import ru.minebot.extreme_energy.tile_entities.TileEntityEB;

import java.io.IOException;

public class EbGui extends BasicGuiContainer<TileEntityEB> {

    protected ProgressBar bar;
    protected SideButtonsModule sideModule;

    public EbGui(IInventory player, TileEntityEB te){
        super(te, new EbContainer(player, te), "meem:textures/gui/ebgui.png", 176, 146, BasicGuiContainer.STANDART_COLOR, 2);
    }

    @Override
    public void initGui(){
        super.initGui();
        sideModule = new SideButtonModuleStandart(te.getExtract(), 56, 24) {
            @Override
            public int onValueChanged(boolean isLeft, int value) {
                int to = isShiftKeyDown() && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ? 100 : isShiftKeyDown() ? 10 : 1;
                value += isLeft ? -to : to;
                if (value > te.getMaxExtract())
                    value = te.getMaxExtract();
                if (value < 1)
                    value = 1;
                te.setExtract(value);
                markDirty();
                return value;
            }
        };
        bar = new ProgressBar(ProgressBar.Type.RF);
    }

    @Override
    protected void draw(int mouseX, int mouseY) {
        sideModule.draw(mc, fontRenderer, mouseX, mouseY);

        ModUtils.drawString("Output", 86, 19, color, Element.Align.CENTER);

        bar.draw(mc, 27, 45, mouseX, mouseY, te.getEnergyStored(), te.getMaxEnergyStored());
    }

    @Override
    protected void markDirty() {
        NetworkWrapper.instance.sendToServer(new PacketEB(te.getPos()));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        sideModule.mouseDown();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state){
        super.mouseReleased(mouseX, mouseY, state);
        sideModule.mouseUp();
    }
}
