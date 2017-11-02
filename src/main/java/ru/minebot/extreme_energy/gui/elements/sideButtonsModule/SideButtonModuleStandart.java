package ru.minebot.extreme_energy.gui.elements.sideButtonsModule;

import net.minecraft.util.ResourceLocation;

public abstract class SideButtonModuleStandart extends SideButtonsModule {
    public SideButtonModuleStandart(int startValue, int posX, int posY) {
        super(
                new ResourceLocation("meem:textures/gui/buttons/left_right/button_left.png"),
                new ResourceLocation("meem:textures/gui/buttons/left_right/button_left_hover.png"),
                new ResourceLocation("meem:textures/gui/buttons/left_right/button_left_click.png"),
                new ResourceLocation("meem:textures/gui/buttons/left_right/button_right.png"),
                new ResourceLocation("meem:textures/gui/buttons/left_right/button_right_hover.png"),
                new ResourceLocation("meem:textures/gui/buttons/left_right/button_right_click.png"),
                4210752, startValue, posX, posY, 21);
    }
}
