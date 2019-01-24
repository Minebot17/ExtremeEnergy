package ru.minebot.extreme_energy.gui.elements.switchButton;

import net.minecraft.util.ResourceLocation;

public abstract class SwitchButtonGrey extends SwitchButton {
    public SwitchButtonGrey(int posX, int posY, boolean isOn) {
        super(
                new ResourceLocation("meem:textures/gui/buttons/switch_grey/off.png"),
                new ResourceLocation("meem:textures/gui/buttons/switch_grey/off_hover.png"),
                new ResourceLocation("meem:textures/gui/buttons/switch_grey/off_click.png"),
                new ResourceLocation("meem:textures/gui/buttons/switch_grey/on.png"),
                new ResourceLocation("meem:textures/gui/buttons/switch_grey/on_hover.png"),
                new ResourceLocation("meem:textures/gui/buttons/switch_grey/on_click.png"),
                posX, posY, 17, 16, isOn);
    }
}
