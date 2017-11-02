package ru.minebot.extreme_energy.gui.tablet;

import net.minecraft.util.ResourceLocation;
import ru.minebot.extreme_energy.init.ModSoundHandler;

public class BackArticleButton extends Button {
    public BackArticleButton(float posX, float posY) {
        super(posX, posY, 0.07f, 0.07f,
                new ResourceLocation("meem:tablet/textures/backarticlebutton_normal.png"),
                new ResourceLocation("meem:tablet/textures/backarticlebutton_hover.png"),
                new ResourceLocation("meem:tablet/textures/backarticlebutton_click.png"),
                ModSoundHandler.tap1, TabletRender.soundVolume);
    }

    @Override
    public void action() {
        TabletRender.returnArticle();
    }
}
