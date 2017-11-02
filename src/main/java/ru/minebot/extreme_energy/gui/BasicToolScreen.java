package ru.minebot.extreme_energy.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import ru.minebot.extreme_energy.energy.IFrequencyHandler;
import ru.minebot.extreme_energy.gui.elements.ProgressBar;
import ru.minebot.extreme_energy.gui.elements.FrequencyModule;
import ru.minebot.extreme_energy.gui.elements.switchButton.SwitchButton;
import ru.minebot.extreme_energy.gui.elements.switchButton.SwitchButtonBlue;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketToolData;

import java.io.IOException;

public class BasicToolScreen extends GuiScreen{
    protected Minecraft mc;
    protected FontRenderer fontRenderer;
    protected int guiLeft;
    protected int guiTop;
    protected NBTTagCompound data;
    protected int color;

    protected int heightScreen;
    protected String title;
    protected int maxEnergy;
    protected boolean drawImpGui;

    protected ProgressBar bar;
    protected SwitchButton powImplant;
    protected SwitchButton powField;
    protected SwitchButton powCap;
    protected FrequencyModule frequencyModule;

    public BasicToolScreen(NBTTagCompound data, int heightScreen, String tile, int maxEnergy, boolean drawImpGui){
        this.data = data;
        this.heightScreen = heightScreen;
        this.title = tile;
        this.maxEnergy = maxEnergy;
        this.drawImpGui = drawImpGui;
        color = 9691135;
    }

    @Override
    public void initGui() {
        mc = Minecraft.getMinecraft();
        fontRenderer = mc.fontRenderer;
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
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        data = Minecraft.getMinecraft().player.inventory.getCurrentItem().getTagCompound().getCompoundTag("meemCategory");

        ScaledResolution sr = new ScaledResolution(mc);
        guiLeft = (sr.getScaledWidth() - 256)/2;
        guiTop = (sr.getScaledHeight() - 256)/2;

        GlStateManager.translate((float)guiLeft, (float)guiTop, 0.0F);
        mouseX -= guiLeft;
        mouseY -= guiTop;

        if (drawImpGui)
            mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/gui/implantsgui.png"));
        this.drawTexturedModalRect(0, 0, 0, 0, 256, heightScreen);

        powImplant.draw(mc, mouseX, mouseY);
        powField.draw(mc, mouseX, mouseY);
        powCap.draw(mc, mouseX, mouseY);
        frequencyModule.draw(mc, mouseX, mouseY);

        fontRenderer.drawString(title, 128 - fontRenderer.getStringWidth(title)/2, 10, color);
        fontRenderer.drawString("Status: ", 12, 27, color);
        fontRenderer.drawString("Powered by implant: ", 12, 60, color);
        fontRenderer.drawString("Powered by field: ", 12, 78, color);
        fontRenderer.drawString("Powered by capacitor: ", 12, 96, color);
        fontRenderer.drawString("Frequency: ", 12, 117, color);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        powImplant.mouseDown();
        powField.mouseDown();
        powCap.mouseDown();
        frequencyModule.mouseDown(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state){
        powImplant.mouseUp();
        powField.mouseUp();
        powCap.mouseUp();
        frequencyModule.mouseUp();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        frequencyModule.keyTyped(typedChar, keyCode);
    }

    @Override
    public void updateScreen() {
        frequencyModule.update();
    }

    protected void markDirty(){
        NetworkWrapper.instance.sendToServer(new PacketToolData(data));
    }
}
