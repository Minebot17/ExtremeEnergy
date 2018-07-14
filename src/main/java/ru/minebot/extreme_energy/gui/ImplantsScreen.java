package ru.minebot.extreme_energy.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.energy.IFrequencyHandler;
import ru.minebot.extreme_energy.gui.elements.Button;
import ru.minebot.extreme_energy.gui.elements.ProgressBar;
import ru.minebot.extreme_energy.gui.elements.buttons.*;
import ru.minebot.extreme_energy.gui.elements.moduleGui.IModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.ModuleGuiArgs;
import ru.minebot.extreme_energy.gui.elements.sideButtonsModule.LeftButtonBlue;
import ru.minebot.extreme_energy.gui.elements.sideButtonsModule.RightButtonBlue;
import ru.minebot.extreme_energy.gui.elements.switchButton.SwitchButton;
import ru.minebot.extreme_energy.gui.elements.switchButton.SwitchButtonBlue;
import ru.minebot.extreme_energy.gui.tablet.Element;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.init.ModKeys;
import ru.minebot.extreme_energy.items.implants.Implant;
import ru.minebot.extreme_energy.modules.IChip;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketClientImplantData;
import ru.minebot.extreme_energy.network.packages.PacketImplantData;
import ru.minebot.extreme_energy.network.packages.PacketModuleSync;
import ru.minebot.extreme_energy.network.packages.PacketNotifyModule;
import ru.minebot.extreme_energy.other.ImplantData;

import java.io.IOException;
import java.util.List;

public class ImplantsScreen extends GuiScreen {

    protected int guiTop;
    protected int guiLeft;
    protected int color;

    protected ImplantData implantData;

    // Elements
    protected SwitchButton activeImplantButton;
    protected SwitchButton showInfoButton;
    protected SwitchButton activeModule1;
    protected SwitchButton activeModule2;
    protected GuiTextField frequencyField;
    protected Button copyButton;
    protected Button pastButton;
    protected Button generateButton;
    protected ProgressBar powerBar;
    protected Button pageLeft;
    protected Button pageRight;
    protected IModuleGui[][] guis;

    // Values
    protected byte pageCount;
    protected byte page;
    protected boolean[] activeModules;
    protected int frequency_;
    protected int voltage;
    protected int maxVoltage;
    protected List<ItemStack> stacks;
    protected boolean drawModule1;
    protected boolean drawModule2;
    private int lastEnergy;
    private int state;
    private long lastTick;

    public ImplantsScreen(ImplantData implantData){
        this.implantData = implantData;
    }

    @Override
    public void initGui() {
        color = 9691135;
        maxVoltage = Implant.getMaxVoltage(implantData.type);
        stacks = ModUtils.getModules(implantData);

        page = implantData.implant.getByte("page");
        pageCount = (byte) (implantData.type + 1);
        activeModules = new boolean[pageCount * 2];
        boolean isOn = implantData.implant.getBoolean("isOn");
        boolean isShowInfo = implantData.implant.getBoolean("isShowInfo");
        byte[] modulesActive = implantData.implant.getByteArray("activesArray");
        for (int i = 0; i < modulesActive.length; i++) {
            activeModules[i] = modulesActive[i] != 0;
        }
        drawModule1 = stacks.size() > page * 2 - 2;
        drawModule2 = stacks.size() > page * 2 - 1;

        activeImplantButton = new SwitchButtonBlue(8, 8, isOn) {
            @Override
            public void onButtonClicked(boolean isOn) {
                implantData.implant.setBoolean("isOn", isOn);
                markDirty();
            }
        };
        showInfoButton = new SwitchButtonBlue(fontRenderer.getStringWidth("Show info on screen: ")+10, 106, isShowInfo) {
            @Override
            public void onButtonClicked(boolean isOn) {
                implantData.implant.setBoolean("isShowInfo", isOn);
                markDirty();
            }
        };
        activeModule1 = !drawModule1 ? null : new SwitchButtonBlue(8, 128, activeModules[page * 2 - 2]) {
            @Override
            public void onButtonClicked(boolean isOn) {
                int index = page * 2 - 2;
                activeModules[index] = isOn;
                implantData.implant.setByteArray("activesArray", boolToByte(activeModules));
                markDirty();
            }
        };
        activeModule2 = !drawModule2 ? null : new SwitchButtonBlue(131, 128, activeModules[page * 2 - 1]) {
            @Override
            public void onButtonClicked(boolean isOn) {
                int index = page * 2 - 1;
                activeModules[index] = isOn;
                implantData.implant.setByteArray("activesArray", boolToByte(activeModules));
                markDirty();
            }
        };
        IFrequencyHandler handlerF = new IFrequencyHandler() {
            @Override
            public int getFrequency() {
                return frequency_;
            }

            @Override
            public void setFrequency(int frequency) {
                frequency_ = frequency;
                implantData.implant.setInteger("frequency", frequency_);
                markDirty();
            }
        };
        frequencyField = new GuiTextField(0, fontRenderer, 70, 32, 65, 13);
        frequencyField.setMaxStringLength(9);
        frequencyField.setText(implantData.implant.getInteger("frequency")+"");
        frequencyField.setFocused(false);
        copyButton = new CopyButton(handlerF, 138, 30);
        pastButton = new PastFrequencyWorldButton(handlerF, frequencyField, 138, 40);
        generateButton = new GenerateWorldButton(handlerF, frequencyField, 148, 35);
        powerBar = new ProgressBar(ProgressBar.Type.RF);
        pageLeft = new LeftButtonBlue(105, 205) {
            @Override
            public void onButtonClicked() {
                page--;
                if (page < 1)
                    page = 1;
                implantData.implant.setByte("page", page);
                markDirty();
            }
        };
        pageRight = new RightButtonBlue(135, 205) {
            @Override
            public void onButtonClicked() {
                page++;
                if (page > pageCount)
                    page = pageCount;
                implantData.implant.setByte("page", page);
                markDirty();
            }
        };
        guis = new IModuleGui[6][0];
        for (int i = 0; i < stacks.size(); i++){
            ItemStack stack = stacks.get(i);
            guis[i] = stack.getItem() instanceof IChip ? ((IChip)stack.getItem()).getGui() : new IModuleGui[0];
        }
        voltage = implantData.implant.getInteger("voltage");
        for (int i = 0; i < stacks.size(); i++)
            for (int j = 0; j < guis[i].length; j++)
                guis[i][j].initGui(mc, implantData, ModUtils.getNotNullCategory(stacks.get(i)));
        markDirty();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        super.drawScreen(mouseX, mouseY, partialTicks);

        GL11.glEnable(GL11.GL_BLEND);
        guiLeft = (width - 256)/2;
        guiTop = (height - 256)/2;

        GlStateManager.translate((float)guiLeft, (float)guiTop, 0.0F);
        mouseX -= guiLeft;
        mouseY -= guiTop;

        mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/gui/implantsgui.png"));
        this.drawTexturedModalRect(0, 0, 0, 0, 256, 256);

        drawModule1 = stacks.size() > page * 2 - 2;
        drawModule2 = stacks.size() > page * 2 - 1;
        activeModule1 = !drawModule1 ? null : new SwitchButtonBlue(8, 128, activeModules[page * 2 - 2]) {
            @Override
            public void onButtonClicked(boolean isOn) {
                int index = page * 2 - 2;
                activeModules[index] = isOn;
                implantData.implant.setByteArray("activesArray", boolToByte(activeModules));
                NetworkWrapper.instance.sendToServer(new PacketNotifyModule(index, isOn));
                markDirty();
            }
        };
        activeModule2 = !drawModule2 ? null : new SwitchButtonBlue(131, 128, activeModules[page * 2 - 1]) {
            @Override
            public void onButtonClicked(boolean isOn) {
                int index = page * 2 - 1;
                activeModules[index] = isOn;
                implantData.implant.setByteArray("activesArray", boolToByte(activeModules));
                NetworkWrapper.instance.sendToServer(new PacketNotifyModule(index, isOn));
                markDirty();
            }
        };
        voltage = implantData.implant.getInteger("voltage");
        activeImplantButton.draw(mc, mouseX, mouseY);
        showInfoButton.draw(mc, mouseX, mouseY);
        if (activeModule1 != null)
            activeModule1.draw(mc, mouseX, mouseY);
        if (activeModule2 != null)
            activeModule2.draw(mc, mouseX, mouseY);
        frequencyField.drawTextBox();
        copyButton.draw(mc, mouseX, mouseY);
        pastButton.draw(mc, mouseX, mouseY);
        generateButton.draw(mc, mouseX, mouseY);
        pageLeft.draw(mc, mouseX, mouseY);
        pageRight.draw(mc, mouseX, mouseY);
        GlStateManager.translate(6,142,0);
        GlStateManager.pushMatrix();
        if (guis[page * 2 - 2] != null) {
            int yOffset = 0;
            for (int i = 0; i < guis[page * 2 - 2].length; i++) {
                guis[page * 2 - 2][i].draw(new ModuleGuiArgs(mc, fontRenderer, mouseX - 6, mouseY - 142 - yOffset, implantData, ModUtils.getNotNullCategory(stacks.get(page * 2 - 2))));
                yOffset += guis[page * 2 - 2][i].getHeight();
                GlStateManager.translate(0, guis[page * 2 - 2][i].getHeight(), 0);
            }
        }
        GlStateManager.popMatrix();
        GlStateManager.translate(123,0,0);
        GlStateManager.pushMatrix();
        if (guis[page * 2 - 1] != null) {
            int yOffset = 0;
            for (int i = 0; i < guis[page * 2 - 1].length; i++) {
                guis[page * 2 - 1][i].draw(new ModuleGuiArgs(mc, fontRenderer, mouseX - 129, mouseY - 142 - yOffset, implantData, ModUtils.getNotNullCategory(stacks.get(page * 2 - 1))));
                yOffset += guis[page * 2 - 1][i].getHeight();
                GlStateManager.translate(0, guis[page * 2 - 1][i].getHeight(), 0);
            }
        }
        GlStateManager.popMatrix();
        GlStateManager.translate(-129,-142,0);

        fontRenderer.drawString("Neuro Implant - " + (implantData.type == 0 ? "01" : implantData.type == 1 ? "05" : "10"), 30, 12, color);
        fontRenderer.drawString("Frequency: ", 10, 34, color);
        fontRenderer.drawString("Charging status: ", 10, 54, color);
        fontRenderer.drawString(state == 0 ? "Discharging" : state == 1 ? "Not charging" : (voltage == 0 ? "Charging" : voltage + " Volt"), fontRenderer.getStringWidth("Charging status: "+10), 54, state == 0 ? 0xFF0000 : state == 1 ? 0xFFFF00 : 0x00FF00);
        fontRenderer.drawString("Maximum voltage: " + maxVoltage, 10, 92, color);
        fontRenderer.drawString("Show info on screen: ", 10, 110, color);
        ModUtils.drawString(page + "/" + pageCount, 125, 213, color, Element.Align.CENTER);
        if (stacks.size() > page * 2 - 2)
            fontRenderer.drawString(stacks.get(page * 2 - 2).getDisplayName(), 33, 132, color);
        if (stacks.size() > page * 2 - 1)
            fontRenderer.drawString(stacks.get(page * 2 - 1).getDisplayName(), 153, 132, color);
        int energy = mc.player.getCapability(ImplantProvider.IMPLANT, null).getImplant().implant.getInteger("energy");
        powerBar.draw(mc,8, 70, mouseX, mouseY, energy, Implant.getMaxEnergy(implantData.type));

        if (mc.world.getTotalWorldTime()%20==0 && lastTick != mc.world.getTotalWorldTime()){
            state = lastEnergy > energy ? 0 : lastEnergy == energy ? 1 : 2;
            lastEnergy = energy;
            lastTick = mc.world.getTotalWorldTime();
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        activeImplantButton.mouseDown();
        showInfoButton.mouseDown();
        if (activeModule1 != null)
            activeModule1.mouseDown();
        if (activeModule2 != null)
            activeModule2.mouseDown();
        frequencyField.mouseClicked(mouseX - guiLeft, mouseY - guiTop, mouseButton);
        copyButton.mouseDown();
        pastButton.mouseDown();
        generateButton.mouseDown();
        pageLeft.mouseDown();
        pageRight.mouseDown();
        boolean g1 = false;
        boolean g2 = false;
        if (guis[page * 2 - 2].length != 0) {
            int yOffset = 0;
            for (int i = 0; i < guis[page * 2 - 2].length; i++) {
                boolean g1_ = guis[page * 2 - 2][i].onMouseDown(new ModuleGuiArgs(mc, fontRenderer, mouseX - guiLeft - 6, mouseY - guiTop - 142 - yOffset, implantData, ModUtils.getNotNullCategory(stacks.get(page * 2 - 2))));
                yOffset += guis[page * 2 - 2][i].getHeight();
                if (!g1 && g1_)
                    g1 = true;
            }
        }
        if (guis[page * 2 - 1].length != 0){
            int yOffset = 0;
            for (int i = 0; i < guis[page * 2 - 1].length; i++) {
                boolean g2_ = guis[page * 2 - 1][i].onMouseDown(new ModuleGuiArgs(mc, fontRenderer, mouseX - guiLeft - 129, mouseY - guiTop - 142 - yOffset, implantData, ModUtils.getNotNullCategory(stacks.get(page * 2 - 1))));
                yOffset += guis[page * 2 - 1][i].getHeight();
                if (!g2 && g2_)
                    g2 = true;
            }
        }
        if (g1 || g2)
            markDirty();
    }

    protected void mouseReleased(int mouseX, int mouseY, int state){
        super.mouseReleased(mouseX, mouseY, state);
        activeImplantButton.mouseUp();
        showInfoButton.mouseUp();
        if (activeModule1 != null)
            activeModule1.mouseUp();
        if (activeModule2 != null)
            activeModule2.mouseUp();
        copyButton.mouseUp();
        pastButton.mouseUp();
        generateButton.mouseUp();
        pageLeft.mouseUp();
        pageRight.mouseUp();
        boolean g1 = false;
        boolean g2 = false;
        if (guis[page * 2 - 2].length != 0) {
            int yOffset = 0;
            for (int i = 0; i < guis[page * 2 - 2].length; i++) {
                g1 = guis[page * 2 - 2][i].onMouseUp(new ModuleGuiArgs(mc, fontRenderer, mouseX - guiLeft - 6, mouseY - guiTop - 142 - yOffset, implantData, ModUtils.getNotNullCategory(stacks.get(page * 2 - 2))));
                yOffset += guis[page * 2 - 2][i].getHeight();
            }
        }
        if (guis[page * 2 - 1].length != 0){
            int yOffset = 0;
            for (int i = 0; i < guis[page * 2 - 1].length; i++) {
                g2 = guis[page * 2 - 1][i].onMouseUp(new ModuleGuiArgs(mc, fontRenderer, mouseX - guiLeft - 129, mouseY - guiTop - 142 - yOffset, implantData, ModUtils.getNotNullCategory(stacks.get(page * 2 - 1))));
                yOffset += guis[page * 2 - 1][i].getHeight();
            }
        }
        if (g1 || g2)
            markDirty();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if ((typedChar >= '0' && typedChar <= '9') || Keyboard.KEY_BACK == keyCode || Keyboard.KEY_DELETE == keyCode){
            frequencyField.textboxKeyTyped(typedChar, keyCode);
            if (frequencyField.isFocused()) {
                int frequency = frequencyField.getText().equals("") ? 0 : Integer.parseInt(frequencyField.getText());
                implantData.implant.setInteger("frequency", frequency);
                markDirty();
            }
        }
        if (Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode() == keyCode || keyCode == Keyboard.KEY_ESCAPE || keyCode == ModKeys.openImplantInterfaceKey.getKeyCode()){
            this.mc.displayGuiScreen((GuiScreen)null);

            if (this.mc.currentScreen == null)
            {
                this.mc.setIngameFocus();
            }
            return;
        }
        boolean g1 = false;
        boolean g2 = false;
        if (guis[page * 2 - 2].length != 0)
            for (int i = 0; i < guis[page * 2 - 2].length; i++)
                g1 = guis[page * 2 - 2][i].keyTyped(implantData, ModUtils.getNotNullCategory(stacks.get(page * 2 - 2)), keyCode);
        if (guis[page * 2 - 1].length != 0)
            for (int i = 0; i < guis[page * 2 - 1].length; i++)
                g2 = guis[page * 2 - 1][i].keyTyped(implantData, ModUtils.getNotNullCategory(stacks.get(page * 2 - 1)),keyCode);
        if (g1 || g2)
            markDirty();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        frequencyField.updateCursorCounter();
    }

    protected void markDirty(){
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < stacks.size(); ++i) {
            if (!stacks.get(i).isEmpty()) {
                NBTTagCompound stackTag = new NBTTagCompound();
                stacks.get(i).writeToNBT(stackTag);
                list.appendTag(stackTag);
            }
        }
        implantData.modules.setTag("Items", list);
        mc.player.getCapability(ImplantProvider.IMPLANT, null).setImplant(implantData);
        NetworkWrapper.instance.sendToServer(new PacketClientImplantData(false));
        NetworkWrapper.instance.sendToServer(new PacketModuleSync(true));
    }

    private byte[] boolToByte(boolean[] b){
        byte[] result = new byte[b.length];
        for (int i = 0; i < result.length; i++)
            result[i] = b[i] ? (byte) 1 : 0;
        return result;
    }


}
