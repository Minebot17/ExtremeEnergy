package ru.minebot.extreme_energy.gui;

import net.minecraft.inventory.IInventory;
import ru.minebot.extreme_energy.energy.IFrequencyHandler;
import ru.minebot.extreme_energy.gui.containers.FtContainer;
import ru.minebot.extreme_energy.gui.elements.ProgressBar;
import ru.minebot.extreme_energy.gui.elements.FieldIndicator;
import ru.minebot.extreme_energy.gui.elements.FrequencyModule;
import ru.minebot.extreme_energy.gui.elements.sideButtonsModule.SideButtonModuleStandart;
import ru.minebot.extreme_energy.gui.elements.sideButtonsModule.SideButtonsModule;
import ru.minebot.extreme_energy.gui.tablet.Element;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketDoubleFrequency;
import ru.minebot.extreme_energy.network.packages.PacketTileRadius;
import ru.minebot.extreme_energy.tile_entities.TileEntityFT;

import java.io.IOException;

public class FtGui extends BasicGuiContainer<TileEntityFT> {

    protected ProgressBar bar;
    protected FieldIndicator indicator;
    protected SideButtonsModule radiusModule;
    protected FrequencyModule creatorFrequency;
    protected FrequencyModule receiverFrequency;

    public FtGui(IInventory player, TileEntityFT te) {
        super(te, new FtContainer(player, te), "meem:textures/gui/ftgui.png", 176, 180, STANDART_COLOR, 3);
    }

    @Override
    public void initGui(){
        super.initGui();
        bar = new ProgressBar(ProgressBar.Type.VOLTAGE);
        indicator = new FieldIndicator(7, 6);
        radiusModule = new SideButtonModuleStandart(te.getRadius(), fontRenderer.getStringWidth("Radius:") + 11, 20) {
            @Override
            public int onValueChanged(boolean isLeft, int value) {
                int to = isShiftKeyDown() ? 10 : 1;
                value += isLeft ? -to : to;
                if (value > te.getMaxRadius())
                    value = te.getMaxRadius();
                if (value < 1)
                    value = 1;
                te.setRadius(value);
                markDirty();
                return value;
            }
        };
        creatorFrequency = new FrequencyModule(mc.fontRenderer, new IFrequencyHandler() {
            @Override
            public int getFrequency() {
                return te.getFrequency();
            }

            @Override
            public void setFrequency(int frequency) {
                te.setFrequency(frequency);
                markDirty();
            }
        }, fontRenderer.getStringWidth("Frequency:") + 11, 55);
        receiverFrequency = new FrequencyModule(mc.fontRenderer, new IFrequencyHandler() {
            @Override
            public int getFrequency() {
                return te.getFrequencyReceive();
            }

            @Override
            public void setFrequency(int frequency) {
                te.setFrequencyReceive(frequency);
                markDirty();
            }
        }, fontRenderer.getStringWidth("Convert to:") + 15, 40);
    }

    @Override
    protected void draw(int mouseX, int mouseY) {
        indicator.draw(mc, te.hasField());
        radiusModule.draw(mc, mc.fontRenderer, mouseX, mouseY);
        creatorFrequency.draw(mc, mouseX, mouseY);
        receiverFrequency.draw(mc, mouseX, mouseY);

        ModUtils.drawString("Radius:", 10, 28, color, Element.Align.LEFT);
        ModUtils.drawString("Frequency: ", 10, 47, color, Element.Align.LEFT);
        ModUtils.drawString("Convert to: ", 10, 61, color, Element.Align.LEFT);
        bar.draw(mc, 7, 71, mouseX, mouseY, te.getVoltageReceive(), te.getMaxVoltageReceive());
    }

    @Override
    protected void markDirty() {
        NetworkWrapper.instance.sendToServer(new PacketDoubleFrequency(te.getPos()));
        NetworkWrapper.instance.sendToServer(new PacketTileRadius(te.getPos()));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        mouseX -= guiLeft;
        mouseY -= guiTop;
        radiusModule.mouseDown();
        creatorFrequency.mouseDown(mouseX, mouseY, mouseButton);
        receiverFrequency.mouseDown(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        radiusModule.mouseUp();
        creatorFrequency.mouseUp();
        receiverFrequency.mouseUp();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        creatorFrequency.keyTyped(typedChar, keyCode);
        receiverFrequency.keyTyped(typedChar, keyCode);
    }

    @Override
    public void updateScreen(){
        super.updateScreen();
        creatorFrequency.update();
        receiverFrequency.update();
    }
}
