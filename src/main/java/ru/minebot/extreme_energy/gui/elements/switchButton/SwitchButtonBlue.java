package ru.minebot.extreme_energy.gui.elements.switchButton;

import net.minecraft.util.ResourceLocation;

public abstract class SwitchButtonBlue extends SwitchButton {
    public SwitchButtonBlue(int posX, int posY, boolean isOn) {
        super(
                new ResourceLocation("meem:textures/gui/buttons/switch_blue/off.png"),
                new ResourceLocation("meem:textures/gui/buttons/switch_blue/off_hover.png"),
                new ResourceLocation("meem:textures/gui/buttons/switch_blue/off_click.png"),
                new ResourceLocation("meem:textures/gui/buttons/switch_blue/on.png"),
                new ResourceLocation("meem:textures/gui/buttons/switch_blue/on_hover.png"),
                new ResourceLocation("meem:textures/gui/buttons/switch_blue/on_click.png"),
                posX, posY, 17, 16, isOn);
    }
}
