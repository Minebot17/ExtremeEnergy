package ru.minebot.extreme_energy.gui.tablet;

public class Article {
    private String name;
    private String title;
    private String key;
    private Element[] elements;

    public Article(String name, String title, Element[] elements, String key) {
        this.name = name;
        this.title = title;
        this.elements = elements;
        this.key = key;
    }

    public String getName() {
        return name;
    }
    public String getTitle() {
        return title;
    }
    public Element[] getElements() {
        return elements;
    }
    public String getKey() { return key; }
}
