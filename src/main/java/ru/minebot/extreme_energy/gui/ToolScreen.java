package ru.minebot.extreme_energy.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Keyboard;
import ru.minebot.extreme_energy.gui.elements.StateChanger;
import ru.minebot.extreme_energy.gui.elements.sideButtonsModule.SideButtonModuleBlue;
import ru.minebot.extreme_energy.gui.elements.sideButtonsModule.SideButtonsModule;
import ru.minebot.extreme_energy.gui.elements.switchButton.SwitchButton;
import ru.minebot.extreme_energy.gui.elements.switchButton.SwitchButtonBlue;
import ru.minebot.extreme_energy.init.ModKeys;
import ru.minebot.extreme_energy.items.equipment.ItemEnergyTool;

import java.io.IOException;

public class ToolScreen extends BasicToolScreen{

    protected StateChanger mode;
    protected StateChanger RBMMode;
    protected SwitchButton harvestLeaves;
    protected SideButtonsModule power;

    public ToolScreen(NBTTagCompound data){
        super(data, 215, "Energy Multi Tool", ItemEnergyTool.maxCap, true);
        this.data = data;
        color = 9691135;
    }

    @Override
    public void initGui() {
        super.initGui();
        mode = new StateChanger(mc, "Mode: ", new String[]{"Auto", "Pickaxe", "Shovel", "Axe", "Hoe", "Shears"}, data.getInteger("mode"), color, 9, 135) {
            @Override
            public void onMouseClick(int state) {
                data.setInteger("mode", state);
                markDirty();
            }
        };
        RBMMode = new StateChanger(mc, "RMB mode: ", new String[]{"Shovel", "Hoe"}, data.getInteger("rbmmode"), color, 9, 157) {
            @Override
            public void onMouseClick(int state) {
                data.setInteger("rbmmode", state);
                markDirty();
            }
        };
        harvestLeaves = new SwitchButtonBlue(fontRenderer.getStringWidth("Harvest leaves and plants: ")+12, 180, data.getBoolean("harvestLeaves")) {
            @Override
            public void onButtonClicked(boolean isOn) {
                data.setBoolean("harvestLeaves", isOn);
                markDirty();
            }
        };
        power = new SideButtonModuleBlue(data.getInteger("power"), fontRenderer.getStringWidth("Power: ")+12, 197) {
            @Override
            public int onValueChanged(boolean isLeft, int value) {
                value += isLeft ? -1 : 1;
                if (value > 3)
                    value = 3;
                if (value < 1)
                    value = 1;
                data.setInteger("power", value);
                markDirty();
                return value;
            }
        };
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        super.drawScreen(mouseX, mouseY, partialTicks);

        mouseX -= guiLeft;
        mouseY -= guiTop;
        mode.draw(mouseX, mouseY);
        RBMMode.draw(mouseX, mouseY);
        harvestLeaves.draw(mc, mouseX, mouseY);
        power.draw(mc, fontRenderer, mouseX, mouseY);

        fontRenderer.drawString("Harvest leaves and plants: ", 12, 184, color);
        fontRenderer.drawString("Power: ", 12, 200, color);
        bar.draw(mc, 12, 40, mouseX, mouseY, data.getInteger("energy"), maxEnergy);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        mode.mouseDown();
        RBMMode.mouseDown();
        harvestLeaves.mouseDown();
        power.mouseDown();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state){
        super.mouseReleased(mouseX, mouseY, state);
        harvestLeaves.mouseUp();
        power.mouseUp();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode() == keyCode || keyCode == Keyboard.KEY_ESCAPE){
            this.mc.displayGuiScreen((GuiScreen)null);

            if (this.mc.currentScreen == null)
            {
                this.mc.setIngameFocus();
            }
            return;
        }
    }
}
