package ru.minebot.extreme_energy.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import ru.minebot.extreme_energy.gui.containers.HvgContainer;
import ru.minebot.extreme_energy.gui.elements.Button;
import ru.minebot.extreme_energy.gui.elements.FrameOfModules;
import ru.minebot.extreme_energy.gui.elements.sideButtonsModule.SideButtonModuleStandart;
import ru.minebot.extreme_energy.gui.elements.sideButtonsModule.SideButtonsModule;
import ru.minebot.extreme_energy.gui.tablet.Element;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.Module;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketFieldCreator;
import ru.minebot.extreme_energy.tile_entities.TileEntityHVG;

import java.io.IOException;

public class HvgGui extends CreatorGui<TileEntityHVG> {

    protected SideButtonsModule frequencySide;
    protected SideButtonsModule radiusSide;

    public HvgGui(IInventory playerInv, final TileEntityHVG te) {
        super(te, new HvgContainer(playerInv, te), "meem:textures/gui/hvggui.png", 176, 170, 5);
    }

    @Override
    public void initGui() {
        super.initGui();

        frequencySide = new SideButtonModuleStandart(te.getVoltage(), 101, 30) {
            @Override
            public int onValueChanged(boolean isLeft, int value) {
                int to = isShiftKeyDown() && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ? 1000 : isShiftKeyDown() ? 100 : 10;
                value += isLeft ? -to : to;
                if (value > te.getMaxVoltage())
                    value = te.getMaxVoltage();
                if (value < 10)
                    value = 10;
                te.setVoltage(value);
                NetworkWrapper.instance.sendToServer(new PacketFieldCreator(te.getPos(), te.getVoltage(), te.getRadius()));
                return value;
            }
        };

        radiusSide = new SideButtonModuleStandart(te.getRadius(), 17, 30) {
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
    protected void draw(int mouseX, int mouseY) {
        frequencySide.drawButtons(mc, mouseX, mouseY);
        radiusSide.draw(mc, fontRenderer, mouseX, mouseY);
        frequencySide.drawText(fontRenderer);
        ModUtils.drawString("Radius", 47, 27, color, Element.Align.CENTER);
        ModUtils.drawString("Voltage", 131, 27, color, Element.Align.CENTER);
        super.draw(mouseX, mouseY);
    }

    @Override
    protected void markDirty() {

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        frequencySide.mouseDown();
        radiusSide.mouseDown();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        frequencySide.mouseUp();
        radiusSide.mouseUp();
    }
}
