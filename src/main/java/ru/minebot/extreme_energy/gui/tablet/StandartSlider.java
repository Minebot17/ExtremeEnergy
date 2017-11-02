package ru.minebot.extreme_energy.gui.tablet;

import net.minecraft.util.ResourceLocation;

import java.util.List;

public class StandartSlider extends Slider{
    public StandartSlider(float posX, float posY, int pageCount,float height, boolean mirrored) {
        super(posX, posY, pageCount,
                new ResourceLocation("meem:tablet/textures/edgeslider.png"),
                new ResourceLocation("meem:tablet/textures/centerslider.png"),
                new ResourceLocation("meem:tablet/textures/edgesliderbackground.png"),
                new ResourceLocation("meem:tablet/textures/centersliderbackground.png"),
                0.04f, 0.05f, 0.05f, height, mirrored);
    }
}
