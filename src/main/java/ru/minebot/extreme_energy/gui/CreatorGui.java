package ru.minebot.extreme_energy.gui;

import net.minecraft.inventory.Container;
import ru.minebot.extreme_energy.energy.FieldCreatorStandart;
import ru.minebot.extreme_energy.energy.IFrequencyHandler;
import ru.minebot.extreme_energy.gui.elements.FrequencyModule;
import ru.minebot.extreme_energy.gui.elements.ProgressBar;
import ru.minebot.extreme_energy.gui.tablet.Element;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketFrequency;

import java.io.IOException;

public abstract class CreatorGui<T extends FieldCreatorStandart> extends BasicGuiContainer<T> {
    private FrequencyModule frequencyModule;
    private ProgressBar bar;
    private int bottom;

    public CreatorGui(T te, Container container, String background, int xSize, int ySize, int moduleCount) {
        super(te, container, background, xSize, ySize, STANDART_COLOR, moduleCount);
        this.bottom = ySize - 83;
    }

    @Override
    public void initGui() {
        super.initGui();
        frequencyModule = new FrequencyModule(fontRenderer, new IFrequencyHandler() {
            @Override
            public int getFrequency() {
                return te.getFrequency();
            }

            @Override
            public void setFrequency(int frequency) {
                te.setFrequency(frequency);
                NetworkWrapper.instance.sendToServer(new PacketFrequency(te.getPos(), frequency));
            }
        }, fontRenderer.getStringWidth("Frequency:") + 11, bottom - 33);
        bar = new ProgressBar(ProgressBar.Type.RF);
    }

    @Override
    protected void draw(int mouseX, int mouseY) {
        ModUtils.drawString("Frequency:", 10, bottom - 27, color, Element.Align.LEFT);
        frequencyModule.draw(mc, mouseX, mouseY);
        bar.draw(mc, 28, bottom - 17, mouseX, mouseY, te.getEnergyStored(), te.getMaxEnergyStored());
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        frequencyModule.mouseDown(mouseX-guiLeft, mouseY-guiTop, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
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
}
