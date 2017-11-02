package ru.minebot.extreme_energy.gui.elements.moduleGui;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import ru.minebot.extreme_energy.other.ImplantData;

public interface IModuleGui {
    void initGui(Minecraft mc, ImplantData data, NBTTagCompound values);
    void draw(ModuleGuiArgs args);
    default boolean onMouseDown(ModuleGuiArgs args){return false;}
    default boolean onMouseUp(ModuleGuiArgs args){return false;}
    default boolean onMouseMove(ModuleGuiArgs args){return false;}
    default boolean keyTyped(ImplantData data, NBTTagCompound values, int keyCode){return false;}

    int getHeight();
}
