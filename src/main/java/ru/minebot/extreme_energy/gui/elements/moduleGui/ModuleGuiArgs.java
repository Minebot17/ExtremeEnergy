package ru.minebot.extreme_energy.gui.elements.moduleGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.nbt.NBTTagCompound;
import ru.minebot.extreme_energy.other.ImplantData;

public class ModuleGuiArgs {
    public Minecraft mc;
    public FontRenderer font;
    public int mouseX;
    public int mouseY;
    public ImplantData data;
    public NBTTagCompound values;


    public ModuleGuiArgs(Minecraft mc, FontRenderer font, int mouseX, int mouseY, ImplantData data, NBTTagCompound values) {
        this.mc = mc;
        this.font = font;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.data = data;
        this.values = values;
    }
}
