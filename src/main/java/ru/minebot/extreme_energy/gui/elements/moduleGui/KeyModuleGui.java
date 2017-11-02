package ru.minebot.extreme_energy.gui.elements.moduleGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Keyboard;
import ru.minebot.extreme_energy.other.ImplantData;

public class KeyModuleGui extends Gui implements IModuleGui {
    protected String text;
    protected String tagKey;
    protected GuiButton button;
    protected int color;
    protected boolean isDirty;
    protected boolean waitKeyTaped;

    public KeyModuleGui(String text, String tagKey){
        this.text = text;
        this.tagKey = tagKey;
        this.color = 9691135;
    }

    @Override
    public void initGui(Minecraft mc, ImplantData data, NBTTagCompound values) {
        String toButton = values.getInteger(tagKey) == 0 ? "No value" : Keyboard.getKeyName(values.getInteger(tagKey));
        button = new GuiButton(0, mc.fontRenderer.getStringWidth(text) + 5, 0, toButton);
        button.setWidth(mc.fontRenderer.getStringWidth(toButton) + 5);
    }

    @Override
    public void draw(ModuleGuiArgs args) {
        args.font.drawString(text, 2, 6, color);
        button.drawButton(args.mc, args.mouseX, args.mouseY, Minecraft.getMinecraft().getRenderPartialTicks());
    }

    @Override
    public boolean onMouseDown(ModuleGuiArgs args) {
        if (button.mousePressed(args.mc, args.mouseX, args.mouseY)){
            button.displayString = "Press any button";
            waitKeyTaped = true;
        }
        return isDirty;
    }

    @Override
    public boolean keyTyped(ImplantData data, NBTTagCompound values, int keyCode){
        if (waitKeyTaped){
            waitKeyTaped = false;
            button.displayString = Keyboard.getKeyName(keyCode);
            button.setWidth(Minecraft.getMinecraft().fontRenderer.getStringWidth(Keyboard.getKeyName(keyCode)) + 5);
            values.setInteger(tagKey, keyCode);

            return true;
        }
        else
            return false;
    }

    @Override
    public int getHeight() {
        return 24;
    }
}
