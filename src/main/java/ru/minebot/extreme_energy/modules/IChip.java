package ru.minebot.extreme_energy.modules;

import net.minecraft.entity.player.EntityPlayer;
import ru.minebot.extreme_energy.gui.elements.moduleGui.IModuleGui;

public interface IChip extends IModuleTier {
    default void update(ChipArgs args){}
    default void changeActive(EntityPlayer player, boolean isOn){}

    int onImplantWork(ChipArgs args);
    IModuleGui[] getGui();
}
