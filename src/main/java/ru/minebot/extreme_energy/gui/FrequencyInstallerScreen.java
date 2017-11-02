package ru.minebot.extreme_energy.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import ru.minebot.extreme_energy.energy.IFrequencyHandler;
import ru.minebot.extreme_energy.gui.elements.FrequencyModule;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketFrequencyItem;

import java.io.IOException;

public class FrequencyInstallerScreen extends GuiScreen {

    protected int guiLeft;
    protected int guiTop;
    protected FrequencyModule module;
    protected NBTTagCompound tag;

    public FrequencyInstallerScreen(NBTTagCompound tag){
        this.tag = tag;
    }

    @Override
    public void initGui() {
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
        }, 4, 5);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        guiLeft = (width - 94)/2;
        guiTop = (height - 23)/2;

        GlStateManager.translate(guiLeft, guiTop, 0);
        mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/gui/figui.png"));
        drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 94, 23, 94, 23);

        module.draw(mc, mouseX - guiLeft, mouseY - guiTop);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        module.mouseDown(mouseX - guiLeft, mouseY - guiTop, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        module.mouseUp();
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
            {
                this.mc.setIngameFocus();
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
