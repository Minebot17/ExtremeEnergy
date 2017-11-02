package ru.minebot.extreme_energy.gui;

import net.minecraft.inventory.IInventory;
import ru.minebot.extreme_energy.energy.IFrequencyHandler;
import ru.minebot.extreme_energy.gui.containers.FtrContainer;
import ru.minebot.extreme_energy.gui.elements.ProgressBar;
import ru.minebot.extreme_energy.gui.elements.FieldIndicator;
import ru.minebot.extreme_energy.gui.elements.FrequencyModule;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketFrequency;
import ru.minebot.extreme_energy.tile_entities.TileEntityFTR;

import java.io.IOException;

public class FtrGui extends BasicGuiContainer<TileEntityFTR> {

    protected ProgressBar energyBar;
    protected ProgressBar voltageBar;
    protected FrequencyModule frequencyModule;
    protected FieldIndicator indicator;

    public FtrGui(IInventory player, TileEntityFTR te){
        super(te, new FtrContainer(player, te), "meem:textures/gui/hvggui.png", 176, 170, STANDART_COLOR, 2);
    }

    @Override
    public void initGui(){
        super.initGui();
        energyBar = new ProgressBar(ProgressBar.Type.RF);
        voltageBar = new ProgressBar(ProgressBar.Type.VOLTAGE);
        frequencyModule = new FrequencyModule(fontRenderer, new IFrequencyHandler() {
            @Override
            public int getFrequency() {
                return te.getFrequency();
            }

            @Override
            public void setFrequency(int frequency) {
                te.setFrequency(frequency);
                markDirty();
            }
        }, fontRenderer.getStringWidth("Frequency:") + 11, 25);
        indicator = new FieldIndicator(7,6);
    }

    @Override
    protected void draw(int mouseX, int mouseY) {
        indicator.draw(mc, te.hasField());
        frequencyModule.draw(mc, mouseX, mouseY);

        fontRenderer.drawString("Frequency: ", 10, 27, color);

        voltageBar.draw(mc, 27, 50, mouseX, mouseY, te.getVoltage(), te.getMaxVoltage());
        energyBar.draw(mc, 27, 68, mouseX, mouseY, te.getEnergyStored(), te.getMaxEnergyStored());
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        frequencyModule.mouseDown(mouseX - guiLeft, mouseY - guiTop, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state){
        super.mouseReleased(mouseX, mouseY, state);
        frequencyModule.mouseUp();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        frequencyModule.keyTyped(typedChar, keyCode);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        frequencyModule.update();
    }

    @Override
    protected void markDirty(){
        NetworkWrapper.instance.sendToServer(new PacketFrequency(te.getPos(), te.getFrequency()));
    }
}
