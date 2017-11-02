package ru.minebot.extreme_energy.gui.elements.sideButtonsModule;

import net.minecraft.util.ResourceLocation;
import ru.minebot.extreme_energy.gui.elements.Button;

public abstract class RightButtonBlue extends Button{
    public RightButtonBlue(int posX, int posY) {
        super(9, 16, posX, posY,
            new ResourceLocation("meem:textures/gui/buttons/left_right_blue/button_right.png"),
            new ResourceLocation("meem:textures/gui/buttons/left_right_blue/button_right_hover.png"),
            new ResourceLocation("meem:textures/gui/buttons/left_right_blue/button_right_click.png")
        );
    }
}
