package ru.minebot.extreme_energy.gui.elements.sideButtonsModule;

import net.minecraft.util.ResourceLocation;

public abstract class SideButtonModuleBlue extends SideButtonsModule {
    public SideButtonModuleBlue(int startValue, int posX, int posY) {
        super(
                new ResourceLocation("meem:textures/gui/buttons/left_right_blue/button_left.png"),
                new ResourceLocation("meem:textures/gui/buttons/left_right_blue/button_left_hover.png"),
                new ResourceLocation("meem:textures/gui/buttons/left_right_blue/button_left_click.png"),
                new ResourceLocation("meem:textures/gui/buttons/left_right_blue/button_right.png"),
                new ResourceLocation("meem:textures/gui/buttons/left_right_blue/button_right_hover.png"),
                new ResourceLocation("meem:textures/gui/buttons/left_right_blue/button_right_click.png"),
                9691135, startValue, posX, posY, 10);
    }
}
