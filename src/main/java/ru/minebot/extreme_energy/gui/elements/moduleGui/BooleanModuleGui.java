package ru.minebot.extreme_energy.gui.elements.moduleGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;
import ru.minebot.extreme_energy.gui.elements.switchButton.SwitchButton;
import ru.minebot.extreme_energy.gui.elements.switchButton.SwitchButtonBlue;
import ru.minebot.extreme_energy.other.ImplantData;

public class BooleanModuleGui extends Gui implements IModuleGui {
    final Minecraft mc = Minecraft.getMinecraft();
    protected String dataKey;
    protected String text;
    protected SwitchButton button;
    protected boolean isDirty;

    public BooleanModuleGui(String text, String dataKey){
        this.text = text;
        this.dataKey = dataKey;
    }

    @Override
    public void initGui(Minecraft mc, ImplantData data, NBTTagCompound values) {
        button = new SwitchButtonBlue(mc.fontRenderer.getStringWidth(text) + 3, 0, values.getBoolean(dataKey)) {
            @Override
            public void onButtonClicked(boolean isOn) {
                values.setBoolean(dataKey, isOn);
                isDirty = true;
            }
        };
    }

    @Override
    public void draw(ModuleGuiArgs args) {
        mc.fontRenderer.drawString(text, 2, 5, 9691135);
        button.draw(mc, args.mouseX, args.mouseY);
    }

    @Override
    public boolean onMouseDown(ModuleGuiArgs args) {
        button.mouseDown();
        return isDirty;
    }

    @Override
    public boolean onMouseUp(ModuleGuiArgs args){
        button.mouseUp();
        return isDirty;
    }

    @Override
    public int getHeight() {
        return 25;
    }
}
