package ru.minebot.extreme_energy.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.capability.IImplant;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.gui.elements.sideButtonsModule.SideButtonModuleBlue;
import ru.minebot.extreme_energy.gui.elements.sideButtonsModule.SideButtonsModule;
import ru.minebot.extreme_energy.gui.elements.switchButton.SwitchButton;
import ru.minebot.extreme_energy.gui.elements.switchButton.SwitchButtonBlue;
import ru.minebot.extreme_energy.init.ModKeys;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.equipment.ItemEnergyArmor;
import ru.minebot.extreme_energy.items.equipment.ItemHeavyArmor;
import ru.minebot.extreme_energy.items.implants.Core;
import ru.minebot.extreme_energy.modules.IArmorCoreModule;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketClientImplantData;
import ru.minebot.extreme_energy.network.packages.PacketImplantData;
import ru.minebot.extreme_energy.other.ImplantData;

import java.io.IOException;
import java.util.List;

public class ArmorSettingsScreen extends GuiScreen {
    protected Minecraft mc;
    protected FontRenderer font;
    protected int guiLeft;
    protected int guiTop;
    protected NonNullList<ItemStack> inventory;
    protected ImplantData data;
    protected ItemStack coreStack;
    protected List<ItemStack> coreModules;
    protected int color;

    protected SideButtonsModule power;
    protected SwitchButton powCap;
    protected SwitchButton powImp;
    protected SwitchButton enableModules;

    public ArmorSettingsScreen(ImplantData data, NonNullList<ItemStack> inventory){
        this.data = data;
        this.inventory = inventory;
        this.coreStack = new ItemStack(data.core);
        this.coreModules = ModUtils.getCoreModules(data);
        this.color = 9691135;
        this.mc = Minecraft.getMinecraft();
        this.font = mc.fontRenderer;
    }

    @Override
    public void initGui() {
        int maxPower = ((Core)coreStack.getItem()).getMaxPower();
        if (data.core.getInteger("power") == 0)
            data.core.setInteger("power", 1);
        power = new SideButtonModuleBlue(data.core.getInteger("power"), 45, 56) {
            @Override
            public int onValueChanged(boolean isLeft, int value) {
                value += isLeft ? -1 : 1;
                if (value > maxPower)
                    value = maxPower;
                if (value < 1)
                    value = 1;
                data.core.setInteger("power", value);
                markDirty();
                return value;
            }
        };
        enableModules = new SwitchButtonBlue(font.getStringWidth("Enable modules: ")+10, 72, data.core.getBoolean("enableModules")) {
            @Override
            public void onButtonClicked(boolean isOn) {
                data.core.setBoolean("enableModules", isOn);
                markDirty();
            }
        };
        powCap = new SwitchButtonBlue(font.getStringWidth("Powered by capacitors: ")+10, 86, data.core.getBoolean("powCap")) {
            @Override
            public void onButtonClicked(boolean isOn) {
                data.core.setBoolean("powCap", isOn);
                markDirty();
            }
        };
        powImp = new SwitchButtonBlue(font.getStringWidth("Powered by implant: ")+10, 100, data.core.getBoolean("powImp")) {
            @Override
            public void onButtonClicked(boolean isOn) {
                data.core.setBoolean("powImp", isOn);
                markDirty();
            }
        };
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        guiLeft = (sr.getScaledWidth() - 256)/2;
        guiTop = (sr.getScaledHeight() - 150)/2;

        GlStateManager.translate((float)guiLeft, (float)guiTop, 0.0F);
        mouseX -= guiLeft;
        mouseY -= guiTop;

        mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/gui/implantsgui.png"));
        drawTexturedModalRect(0,0,0,0,256,150);

        power.draw(mc, font, mouseX, mouseY);
        enableModules.draw(mc, mouseX, mouseY);
        powCap.draw(mc, mouseX, mouseY);
        powImp.draw(mc, mouseX, mouseY);

        font.drawString("Armor settiongs", 128 - font.getStringWidth("Armor settiongs")/2, 10, color);
        font.drawString("You put " + scanArmor() + " pieces of customizable armor", 10, 25, color);
        font.drawString("Core: " + coreStack.getDisplayName(), 10, 45, color);
        font.drawString("Power: ", 10, 60, color);
        font.drawString("Enable modules: ", 10, 75, color);
        font.drawString("Powered by capacitors: ", 10, 90, color);
        font.drawString("Powered by implant: ", 10, 105, color);
        font.drawString("Energy consumption: " + getEnergyLost() + "RF per HP", 10, 120, color);
        String m = "Modules: ";
        for (int i = 0; i < coreModules.size(); i++)
            m += coreModules.get(i).getDisplayName() + ((i+1) == coreModules.size() ? "" : (i == 1 ? ", \n" : ", "));
        font.drawString(m, 10, 135, color);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        power.mouseDown();
        enableModules.mouseDown();
        powCap.mouseDown();
        powImp.mouseDown();
    }


    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state){
        power.mouseUp();
        enableModules.mouseUp();
        powCap.mouseUp();
        powImp.mouseUp();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode() == keyCode || keyCode == ModKeys.openArmorSettings.getKeyCode()){
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

    protected void markDirty(){
        mc.player.getCapability(ImplantProvider.IMPLANT, null).setImplant(data);
        NetworkWrapper.instance.sendToServer(new PacketClientImplantData(true));
    }

    protected int scanArmor(){
        int result = 0;
        for (int i = 0; i < inventory.size(); i++)
            if (inventory.get(i).getItem() instanceof ItemEnergyArmor || inventory.get(i).getItem() instanceof ItemHeavyArmor)
                result++;
        return result;
    }

    protected int getEnergyLost(){
        int result = 0;
        int power = data.core.getInteger("power");
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getItem() instanceof ItemEnergyArmor)
                result += 175 * power;
            else if (inventory.get(i).getItem() instanceof ItemHeavyArmor)
                result += 250 * power;
        }
        for (int i = 0; i < coreModules.size(); i++)
            result += ((IArmorCoreModule)coreModules.get(i).getItem()).getEnergy(power);
        return result;
    }
}
