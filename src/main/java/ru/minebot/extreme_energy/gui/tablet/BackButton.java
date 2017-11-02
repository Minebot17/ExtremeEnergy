package ru.minebot.extreme_energy.gui.tablet;

import net.minecraft.util.ResourceLocation;
import ru.minebot.extreme_energy.init.ModSoundHandler;

public class BackButton extends Button {
    public BackButton(float posX, float posY) {
        super(posX, posY, 0.1f, 0.1f,
                new ResourceLocation("meem:tablet/textures/backbutton_normal.png"),
                new ResourceLocation("meem:tablet/textures/backbutton_hover.png"),
                new ResourceLocation("meem:tablet/textures/backbutton_click.png"),
                ModSoundHandler.tap0, TabletRender.soundVolume);
    }

    @Override
    public void action() {
        TabletRender.setArticle(-1, -1);
    }
}
