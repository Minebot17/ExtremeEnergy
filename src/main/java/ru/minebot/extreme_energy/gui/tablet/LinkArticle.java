package ru.minebot.extreme_energy.gui.tablet;

public class LinkArticle extends Link {
    protected int chapter;
    protected int article;

    public LinkArticle(float posX, float posY, String text, String url, Align align, float width) {
        super(posX, posY, text, url, align, width);
        String[] splited = url.split("/");
        chapter = Integer.parseInt(splited[0].substring(1));
        article = Integer.parseInt(splited[1].substring(1));
    }

    @Override
    public void action() {
        TabletRender.setArticle(chapter, article);
    }
}
