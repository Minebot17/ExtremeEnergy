package ru.minebot.extreme_energy.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import ru.minebot.extreme_energy.energy.IFrequencyHandler;
import ru.minebot.extreme_energy.gui.containers.BasicContainer;
import ru.minebot.extreme_energy.gui.containers.SwordContainer;
import ru.minebot.extreme_energy.gui.elements.ProgressBar;
import ru.minebot.extreme_energy.gui.elements.FrequencyModule;
import ru.minebot.extreme_energy.gui.elements.buttons.InfoButton;
import ru.minebot.extreme_energy.gui.elements.sideButtonsModule.SideButtonModuleBlue;
import ru.minebot.extreme_energy.gui.elements.sideButtonsModule.SideButtonsModule;
import ru.minebot.extreme_energy.gui.elements.switchButton.SwitchButton;
import ru.minebot.extreme_energy.gui.elements.switchButton.SwitchButtonBlue;
import ru.minebot.extreme_energy.init.ModKeys;
import ru.minebot.extreme_energy.items.equipment.ItemEnergySword;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketToolData;
import ru.minebot.extreme_energy.tile_entities.InventorySword;

import java.io.IOException;

public class SwordGui extends GuiContainer {

    protected Minecraft mc;
    protected FontRenderer fontRenderer;
    protected NBTTagCompound data;
    protected int color;
    protected Item[] modules;

    protected ProgressBar bar;
    protected SwitchButton powImplant;
    protected SwitchButton powField;
    protected SwitchButton powCap;
    protected FrequencyModule frequencyModule;
    protected SideButtonsModule power;
    protected InfoButton info;

    public SwordGui(IInventory playerInv, ItemStack sword, NBTTagCompound data) {
        super(new SwordContainer(playerInv, new InventorySword(Minecraft.getMinecraft().world, sword)));
        mc = Minecraft.getMinecraft();
        fontRenderer = mc.fontRenderer;
        this.data = data;
        color = 9691135;
        xSize = 256;
        ySize = 256;
    }

    @Override
    public void initGui() {
        super.initGui();
        bar = new ProgressBar(ProgressBar.Type.RF);
        if (data.getInteger("power") == 0)
            data.setInteger("power", 1);
        powImplant = new SwitchButtonBlue(fontRenderer.getStringWidth("Powered by implant: ")+12, 57, data.getBoolean("powImplant")) {
            @Override
            public void onButtonClicked(boolean isOn) {
                data.setBoolean("powImplant", isOn);
                markDirty();
            }
        };
        powField = new SwitchButtonBlue(fontRenderer.getStringWidth("Powered by field: ")+12, 75, data.getBoolean("powField")) {
            @Override
            public void onButtonClicked(boolean isOn) {
                data.setBoolean("powField", isOn);
                markDirty();
            }
        };
        powCap = new SwitchButtonBlue(fontRenderer.getStringWidth("Powered by capacitor: ")+12, 93, data.getBoolean("powCap")) {
            @Override
            public void onButtonClicked(boolean isOn) {
                data.setBoolean("powCap", isOn);
                markDirty();
            }
        };
        frequencyModule = new FrequencyModule(mc.fontRenderer, new IFrequencyHandler() {
            @Override
            public int getFrequency() {
                return data.getInteger("frequency");
            }

            @Override
            public void setFrequency(int frequency) {
                data.setInteger("frequency", frequency);
                markDirty();
            }
        }, fontRenderer.getStringWidth("Frequency: ")+12, 115);
        if (data.getInteger("power") == 0)
            data.setInteger("power", 1);
        power = new SideButtonModuleBlue(data.getInteger("power"), fontRenderer.getStringWidth("Power: ")+12, 130) {
            @Override
            public int onValueChanged(boolean isLeft, int value) {
                value += isLeft ? -1 : 1;
                if (value > 6)
                    value = 6;
                if (value < 1)
                    value = 1;
                data.setInteger("power", value);
                markDirty();
                return value;
            }
        };
        info = new InfoButton(82, 150,"Modules: ", ((InventorySword)((BasicContainer)inventorySlots).te).getAcceptedModules());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/gui/energyswordgui.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        data = Minecraft.getMinecraft().player.inventory.getCurrentItem().getTagCompound().getCompoundTag("meemCategory");

        mouseX -= guiLeft;
        mouseY -= guiTop;
        powImplant.draw(mc, mouseX, mouseY);
        powField.draw(mc, mouseX, mouseY);
        powCap.draw(mc, mouseX, mouseY);
        frequencyModule.draw(mc, mouseX, mouseY);
        power.draw(mc, fontRenderer, mouseX, mouseY);

        fontRenderer.drawString("Energy Sword", 128 - fontRenderer.getStringWidth("Energy Sword")/2, 10, color);
        fontRenderer.drawString("Status: ", 12, 27, color);
        fontRenderer.drawString("Powered by implant: ", 12, 60, color);
        fontRenderer.drawString("Powered by field: ", 12, 78, color);
        fontRenderer.drawString("Powered by capacitor: ", 12, 96, color);
        fontRenderer.drawString("Frequency: ", 12, 117, color);
        fontRenderer.drawString("Power: ", 12, 133, color);
        fontRenderer.drawString("Module: ", 12, 150, color);
        bar.draw(mc, 12, 40, mouseX, mouseY, data.getInteger("energy"), ItemEnergySword.maxCap);
        info.draw(mc, mouseX, mouseY);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        power.mouseDown();
        powImplant.mouseDown();
        powField.mouseDown();
        powCap.mouseDown();
        frequencyModule.mouseDown(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state){
        super.mouseReleased(mouseX, mouseY, state);
        power.mouseUp();
        powImplant.mouseUp();
        powField.mouseUp();
        powCap.mouseUp();
        frequencyModule.mouseUp();
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void updateScreen() {
        frequencyModule.update();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        markDirty();
    }

    protected void markDirty(){
        NetworkWrapper.instance.sendToServer(new PacketToolData(data));
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_ESCAPE || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode) || Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode() == keyCode) {
            this.mc.player.closeScreen();
        }
    }
}
