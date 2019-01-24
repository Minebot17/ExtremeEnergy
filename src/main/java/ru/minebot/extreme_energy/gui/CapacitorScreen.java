package ru.minebot.extreme_energy.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import ru.minebot.extreme_energy.energy.IFrequencyHandler;
import ru.minebot.extreme_energy.gui.elements.FrequencyModule;
import ru.minebot.extreme_energy.gui.elements.ProgressBar;
import ru.minebot.extreme_energy.gui.elements.switchButton.SwitchButton;
import ru.minebot.extreme_energy.gui.elements.switchButton.SwitchButtonGrey;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.capacitors.Capacitor;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketFrequencyItem;
import ru.minebot.extreme_energy.network.packages.PacketSwitchCapacitor;

import java.io.IOException;

public class CapacitorScreen extends GuiScreen {

    protected int guiLeft;
    protected int guiTop;
    protected SwitchButtonGrey switchButton;
    protected FrequencyModule module;
    protected ProgressBar bar;
    protected NBTTagCompound tag;
    protected int maxEnergy;

    public CapacitorScreen(ItemStack stack) {
        this.tag = ModUtils.getNotNullCategory(stack);
        maxEnergy = ((Capacitor)stack.getItem()).getMaxEnergyStored(stack);
    }

    @Override
    public void initGui() {
        switchButton = new SwitchButtonGrey(5, 6, tag.getBoolean("state")) {
            @Override
            public void onButtonClicked(boolean isOn) {
                tag.setBoolean("state", isOn);
                NetworkWrapper.instance.sendToServer(new PacketSwitchCapacitor(isOn));
            }
        };
        bar = new ProgressBar(ProgressBar.Type.RF);
        module = new FrequencyModule(fontRenderer, new IFrequencyHandler() {
            @Override
            public int getFrequency() {
                return tag.getInteger("frequency");
            }

            @Override
            public void setFrequency(int frequency) {
                tag.setInteger("frequency", frequency);
                NetworkWrapper.instance.sendToServer(new PacketFrequencyItem(frequency));
            }
        }, 24, 8);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        guiLeft = (width - 150)/2;
        guiTop = (height - 46)/2;

        GlStateManager.translate(guiLeft, guiTop, 0);
        mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/gui/capgui.png"));
        drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 150, 46, 150, 46);

        module.draw(mc, mouseX - guiLeft, mouseY - guiTop);
        switchButton.draw(mc, mouseX - guiLeft, mouseY - guiTop);
        bar.draw(mc, 5, 25, mouseX - guiLeft, mouseY - guiTop, tag.getInteger("Energy"), maxEnergy);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        module.mouseDown(mouseX - guiLeft, mouseY - guiTop, mouseButton);
        switchButton.mouseDown();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        module.mouseUp();
        switchButton.mouseUp();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        module.update();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        module.keyTyped(typedChar, keyCode);
        if (Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode() == keyCode || keyCode == Keyboard.KEY_ESCAPE){
            this.mc.displayGuiScreen((GuiScreen)null);

            if (this.mc.currentScreen == null)
                this.mc.setIngameFocus();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
