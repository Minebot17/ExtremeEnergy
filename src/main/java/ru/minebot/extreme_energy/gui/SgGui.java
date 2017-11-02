package ru.minebot.extreme_energy.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import ru.minebot.extreme_energy.gui.containers.SgContainer;
import ru.minebot.extreme_energy.gui.elements.ProgressBar;
import ru.minebot.extreme_energy.gui.elements.sideButtonsModule.SideButtonModuleStandart;
import ru.minebot.extreme_energy.gui.elements.sideButtonsModule.SideButtonsModule;
import ru.minebot.extreme_energy.gui.tablet.Element;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.ItemChargeDetector;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketSG;
import ru.minebot.extreme_energy.tile_entities.TileEntitySG;

import java.io.IOException;

public class SgGui extends BasicGuiContainer<TileEntitySG> {

    protected ProgressBar barRF;
    protected ProgressBar barHeat;
    protected SideButtonsModule sideModule;

    public SgGui(IInventory player, TileEntitySG te) {
        super(te, new SgContainer(player, te), "meem:textures/gui/sggui.png", 176, 191, STANDART_COLOR, 4);
    }

    @Override
    public void initGui(){
        super.initGui();
        barRF = new ProgressBar(ProgressBar.Type.RF);
        barHeat = new ProgressBar(ProgressBar.Type.HEAT);
        sideModule = new SideButtonModuleStandart(te.getPower(), 60, 20) {
            @Override
            public int onValueChanged(boolean isLeft, int value) {
                int to = isShiftKeyDown() && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ? 1000 : isShiftKeyDown() ? 100 : 10;
                value += isLeft ? -to : to;
                if (value > te.getMaxPower())
                    value = te.getMaxPower();
                if (value < 0)
                    value = 0;
                te.setPower(value);
                markDirty();
                return value;
            }
        };
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        if (te.isInvalid())
            te = (TileEntitySG) te.getWorld().getTileEntity(te.getPos());
    }

    @Override
    protected void draw(int mouseX, int mouseY){
        ModUtils.drawString("Power", 90, 17, color, Element.Align.CENTER);
        fontRenderer.drawString("Charge: ", 7, 38, color);
        int index = ItemChargeDetector.getMessageIndexFromPos(te.getWorld(), new ChunkPos(te.getPos()));
        TextFormatting thisColor = ItemChargeDetector.getMessageColor(index);
        fontRenderer.drawString(thisColor+ I18n.format("chargeDetector."+ItemChargeDetector.getMessage(index)), fontRenderer.getStringWidth("Charge: ")+8, 38, color);

        GL11.glColor3f(1, 1, 1);
        sideModule.draw(mc, fontRenderer, mouseX, mouseY);
        barHeat.draw(mc, 27, 50, mouseX, mouseY, te.getHeat(), te.getMaxHeat());
        barRF.draw(mc, 27, 70, mouseX, mouseY, te.getEnergyStored(), te.getMaxEnergyStored());
    }

    @Override
    protected void markDirty() {
        NetworkWrapper.instance.sendToServer(new PacketSG(te.getPos()));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        sideModule.mouseDown();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        sideModule.mouseUp();
    }
}
