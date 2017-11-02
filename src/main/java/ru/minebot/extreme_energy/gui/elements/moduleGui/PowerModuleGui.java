package ru.minebot.extreme_energy.gui.elements.moduleGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Keyboard;
import ru.minebot.extreme_energy.gui.elements.moduleGui.IModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.ModuleGuiArgs;
import ru.minebot.extreme_energy.gui.elements.sideButtonsModule.SideButtonModuleBlue;
import ru.minebot.extreme_energy.gui.elements.sideButtonsModule.SideButtonsModule;
import ru.minebot.extreme_energy.other.ImplantData;

public class PowerModuleGui extends Gui implements IModuleGui {

    protected SideButtonsModule sideModule;
    protected boolean isDirty;
    protected String text;
    protected String dataKey;
    protected int maxValue;
    protected int minValue;

    public PowerModuleGui(){
        this("Power: ", "power", 4);
    }

    public PowerModuleGui(String text, String dataKey, int maxValue) {
        this(text, dataKey, maxValue, 1);
    }

    public PowerModuleGui(String text, String dataKey, int maxValue, int minValue) {
        this.text = text;
        this.dataKey = dataKey;
        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    @Override
    public void initGui(Minecraft mc, ImplantData data, NBTTagCompound values) {
        if (values.getInteger(dataKey) == 0)
            values.setInteger(dataKey, minValue);
        sideModule = new SideButtonModuleBlue(values.getInteger(dataKey), mc.fontRenderer.getStringWidth(text) + 4, 6) {
            @Override
            public int onValueChanged(boolean isLeft, int value) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
                    value += isLeft ? -100 : 100;
                else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                    value += isLeft ? -10 : 10;
                else
                    value += isLeft ? -1 : 1;
                if (value < minValue)
                    value = minValue;
                if (value > maxValue)
                    value = maxValue;
                values.setInteger(dataKey, value);
                isDirty = true;
                return value;
            }
        };
    }

    @Override
    public void draw(ModuleGuiArgs args) {
        args.font.drawString(text, 2, 10, 9691135);
        sideModule.draw(args.mc, args.font, args.mouseX, args.mouseY);
        isDirty = false;
    }

    @Override
    public boolean onMouseDown(ModuleGuiArgs args){
        sideModule.mouseDown();
        return isDirty;
    }

    @Override
    public boolean onMouseUp(ModuleGuiArgs args){
        sideModule.mouseUp();
        return false;
    }

    @Override
    public int getHeight() {
        return 24;
    }
}
