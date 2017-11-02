package ru.minebot.extreme_energy.gui.elements.sideButtonsModule;

import net.minecraft.util.ResourceLocation;
import ru.minebot.extreme_energy.gui.elements.Button;

public abstract class LeftButtonBlue extends Button{
    public LeftButtonBlue(int posX, int posY) {
        super(9, 16, posX, posY,
                new ResourceLocation("meem:textures/gui/buttons/left_right_blue/button_left.png"),
                new ResourceLocation("meem:textures/gui/buttons/left_right_blue/button_left_hover.png"),
                new ResourceLocation("meem:textures/gui/buttons/left_right_blue/button_left_click.png")
        );
    }
}
