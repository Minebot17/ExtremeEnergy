package ru.minebot.extreme_energy.gui.tablet;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class Chapter {
    private String name;
    private ResourceLocation[] icons;
    private List<Article> articles;

    public Chapter(String name, ResourceLocation[] icons, List<Article> articles) {
        this.name = name;
        this.icons = icons;
        this.articles = articles;
    }

    @SideOnly(Side.CLIENT)
    public String getName() {
        return I18n.format(name);
    }
    public ResourceLocation[] getIcons() {
        return icons;
    }
    public List<Article> getArticles() {
        return articles;
    }
}
