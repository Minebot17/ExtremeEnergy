package ru.minebot.extreme_energy.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import ru.minebot.extreme_energy.gui.containers.FcContainer;
import ru.minebot.extreme_energy.gui.elements.*;
import ru.minebot.extreme_energy.gui.elements.sideButtonsModule.SideButtonModuleStandart;
import ru.minebot.extreme_energy.gui.elements.sideButtonsModule.SideButtonsModule;
import ru.minebot.extreme_energy.gui.tablet.Element;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.Module;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketFieldCreator;
import ru.minebot.extreme_energy.network.packages.PacketPublicPrivate;
import ru.minebot.extreme_energy.tile_entities.TileEntityFC;

import java.io.IOException;
import java.util.ArrayList;

public class FcGui extends ReceiverGui<TileEntityFC> {

    private PrivateFrame framePrivate;
    private SideButtonsModule radiusModule;

    public FcGui(IInventory playerInv, final TileEntityFC te) {
        super(te, new FcContainer(playerInv, te), "meem:textures/gui/fcgui.png", 176, 180, 3);
    }

    @Override
    public void initGui(){
        super.initGui();
        framePrivate = new PrivateFrame() {
            @Override
            public void onMouseClicked(boolean isPublic) {
                if (isPublic)
                    te.setPublic();
                else
                    te.setPrivate();
                NetworkWrapper.instance.sendToServer(new PacketPublicPrivate(te.getPos(), isPublic));
                ((FcContainer)inventorySlots).changeSlots(isPublic);
            }
        };

        radiusModule = new SideButtonModuleStandart(te.getRadius(), 17, 35) {
            @Override
            public int onValueChanged(boolean isLeft, int value) {
                int to = isShiftKeyDown() ? 10 : 1;
                value += isLeft ? -to : to;
                if (value > te.getMaxRadius())
                    value = te.getMaxRadius();
                if (value < 1)
                    value = 1;
                te.setRadius(value);
                NetworkWrapper.instance.sendToServer(new PacketFieldCreator(te.getPos(), te.getVoltage(), te.getRadius()));
                return value;
            }
        };
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        framePrivate.drawFrame(mc, guiLeft - 26, guiTop, te.isPublic());
    }

    @Override
    protected void draw(int mouseX, int mouseY) {
        radiusModule.draw(mc, fontRenderer, mouseX, mouseY);
        fontRenderer.drawString("Information:", 85, 20, color);
        String[] info = te.getModuleInfo();
        if (info != null)
            for (int i = 0; i < info.length; i++)
                fontRenderer.drawString(info[i], 85, 30 + i * 10, color);
        else {
            fontRenderer.drawString("No functional", 85, 30, color);
            fontRenderer.drawString("module", 85, 40, color);
        }

        ModUtils.drawString("Radius", 47, 28, color, Element.Align.CENTER);

        framePrivate.draw(mc, -26, 0, mouseX, mouseY, te.isPublic());
        super.draw(mouseX, mouseY);
    }

    @Override
    protected void markDirty() {

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        radiusModule.mouseDown();
        framePrivate.mouseDown();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        radiusModule.mouseUp();
    }
}
