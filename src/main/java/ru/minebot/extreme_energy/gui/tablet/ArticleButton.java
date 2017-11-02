package ru.minebot.extreme_energy.gui.tablet;

import net.minecraft.util.ResourceLocation;
import ru.minebot.extreme_energy.init.ModSoundHandler;

public class ArticleButton extends ButtonWithText {
    protected int chapter;
    protected int article;

    public ArticleButton(float posX, float posY, int chapter, int article, String text, boolean active) {
        super(posX, posY, 0.3f, 0.1f,
                new ResourceLocation(active ? "meem:tablet/textures/articlebutton_click.png" : "meem:tablet/textures/articlebutton_normal.png"),
                new ResourceLocation(active ? "meem:tablet/textures/articlebutton_click.png" : "meem:tablet/textures/articlebutton_hover.png"),
                new ResourceLocation("meem:tablet/textures/articlebutton_click.png"),
                ModSoundHandler.tap1, TabletRender.soundVolume, text, 0xffffff);
        this.chapter = chapter;
        this.article = article;
    }

    @Override
    public void action() {
        float pos = ((ArticlesScene)TabletRender.scene).leftSlider.thisPos;
        TabletRender.setArticle(chapter, article);
        ((ArticlesScene)TabletRender.scene).leftSlider.thisPos = pos;
    }
}
