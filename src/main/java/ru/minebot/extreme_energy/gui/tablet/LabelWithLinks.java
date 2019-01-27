package ru.minebot.extreme_energy.gui.tablet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import ru.minebot.extreme_energy.init.ModSoundHandler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class LabelWithLinks extends Label implements IClickable {
    protected List<Region> regions = new ArrayList<>();
    protected int hoverRegion;

    public LabelWithLinks(float posX, float posY, String[] text, Align align, TextFormatting format, float width, int color) {
        super(posX, posY);
        this.format = format;
        this.align = align;
        this.width = width;
        this.color = color;

        List<String> translated = new ArrayList<>();
        for (String key : text)
            translated.add(I18n.format(key));

        List<String> toRender = new ArrayList<>();
        if (width != 0){
            for(String line : translated) {
                String[] str = line.split(" ");
                List<String> words = new ArrayList<>();
                words.addAll(Arrays.asList(str));

                List<String> toRegion = new ArrayList<>();
                List<String> links = new ArrayList<>();
                for (int i = 0; i < words.size(); i++)
                    if (words.get(i).contains("{\"") && words.get(i).contains("}")){
                        String[] buffer = words.get(i).split("\"");
                        String toAdd = null;
                        if (buffer[2].indexOf("}") != buffer[2].length()-1)
                            toAdd = buffer[2].split("}")[1];
                        String toSet = buffer[2].substring(0, buffer[2].indexOf("}"));
                        words.set(i, toSet + "j5j");
                        if (toAdd != null)
                            words.add(i+1, toAdd);
                        toRegion.add(toSet);
                        links.add(buffer[1]);
                    }

                List<String> result = new ArrayList<>();
                result.add(words.get(0));
                int j = 0;
                for (int i = 1; i < words.size(); i++) {
                    if ((float)font.getStringWidth(result.get(j) + words.get(i))/(1f/textSize) < width) {
                        result.set(j, result.get(j) + (result.get(j).length() == 0 || words.get(i).equals(".") || words.get(i).equals(",") || words.get(i).equals(":") || words.get(i).equals(";")  ? "" : " ") + words.get(i));
                    }
                    else {
                        result.add(words.get(i));
                        j++;
                    }
                }

                for (int i = 0; i < toRegion.size(); i++)
                    for (int h = 0; h < result.size(); h++)
                        if (result.get(h).contains(toRegion.get(i) + "j5j")){
                            int preIndex = result.get(h).indexOf(toRegion.get(i) + "j5j")-1;
                            if (preIndex == -1){
                                if (h != 0 && result.get(h - 1).charAt(result.get(h - 1).length()-1) == ('-' | '_'))
                                    continue;
                            }
                            else if (result.get(h).charAt(preIndex) != ' ')
                                continue;

                            String[] splitted = result.get(h).split(toRegion.get(i) + "j5j");
                            if (splitted.length == 0)
                                splitted = new String[]{""};
                            String toFind = splitted[0]+toRegion.get(i)+"j5j"+(splitted.length == 1 ? "" : splitted[1]);
                            int toY = -1;
                            for (int k = 0; k < result.size(); k++)
                                if (result.get(k).contains(toFind))
                                    toY = k + toRender.size();
                            toRegion.set(i, toRegion.get(i).replace('_', ' '));
                            result.set(h, splitted[0] + TextFormatting.BLUE + toRegion.get(i) + TextFormatting.RESET + (splitted.length == 1 ? "" : splitted[1]));
                            regions.add(new Region(h, links.get(i), i, toY, splitted[0]) {
                                @Override
                                public boolean isHover(float mouseX, float mouseY) {
                                    float x = posX + font.getStringWidth(textBefore) * textSize;
                                    float y = posY - offsetY*oneCharHeight;
                                    float width = font.getStringWidth(toRegion.get(offsetX)) * textSize;
                                    return mouseX > x && mouseX < x + width && mouseY < y && mouseY > y - oneCharHeight;
                                }
                            });
                        }

                toRender.addAll(result);
            }
        }
        else
            toRender.addAll(Arrays.asList(text));
        this.text = toRender;
        this.height = toRender.size() * oneCharHeight;
    }

    protected LabelWithLinks(LabelWithLinks label, int begin, int size){
        super(label.posX, label.posY);
        this.format = label.format;
        this.align = label.align;
        this.color = label.color;
        List<Region> regions = new ArrayList<>();
        List<Region> buffer = label.regions;
        for (Region region : buffer)
            if (region.line >= begin && region.line < begin+size)
                regions.add(region);
        this.regions = regions;
        this.width = label.width;
        this.text = label.text.subList(begin, begin + size);
        this.height = size * oneCharHeight;
    }

    public LabelWithLinks splitLabel(int begin, int size){
        return new LabelWithLinks(this, begin, size);
    }

    public LabelWithLinks splitLabel(int begin){
        return new LabelWithLinks(this, begin, text.size() - begin);
    }

    @Override
    public void onMouseDown() {
        regions.get(hoverRegion).click();
    }

    @Override
    public void onMouseUp() {

    }

    @Override
    public void onMouseMove() {

    }

    @Override
    public boolean isHover(float mouseX, float mouseY) {
        hoverRegion = -1;
        for (int i = 0; i < regions.size(); i++)
            if (regions.get(i).isHover(mouseX, mouseY))
                hoverRegion = i;
        return hoverRegion != -1;
    }

    private abstract class Region {
        public int line;
        private String link;
        public int offsetX;
        public int offsetY;
        public String textBefore;

        public Region(int line, String link, int offsetX, int offsetY, String textBefore){
            this.line = line;
            this.link = link;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.textBefore = textBefore;
        }

        public abstract boolean isHover(float mouseX, float mouseY);
        public void click(){
            mc.player.playSound(ModSoundHandler.tap0, TabletRender.soundVolume, 1);

            if (link.contains("http")){
                try {
                    URI uri = new URI(link);
                    java.awt.Desktop.getDesktop().browse(uri);

                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
            else if (link.contains("/")) {
                String[] splited = link.split("/");
                int chapter = Integer.parseInt(splited[0].substring(1));
                int article = Integer.parseInt(splited[1].substring(1));
                TabletRender.setArticle(chapter, article);
            }
            else
                TabletRender.setArticle(link);
        }
    }

    public static Element loadFromJson(JsonObject json, int chapter, int article){
        HashMap<String, String> langMap = TabletRender.getLangMap(chapter, article);
        String[] text;
        if (json.get("text").isJsonArray()){
            JsonArray array = json.get("text").getAsJsonArray();
            text = new String[array.size()];
            for (int i = 0; i < array.size(); i++)
                text[i] = array.get(i).getAsString();
        }
        else
            text = new String[]{json.get("text").getAsString()};

        for (int i = 0; i < text.length; i++)
            text[i] = langMap.get(text[i]);

        float width = !json.has("width") ? TabletRender.textWidth : json.get("width").getAsFloat();

        String alignStr = json.has("align") ? json.get("align").getAsString() : null;
        Align align = alignStr == null ? Align.LEFT : alignStr.equals("l") ? Align.LEFT : alignStr.equals("c") ? Align.CENTER : Align.RIGHT;

        String formatStr = json.has("format") ? json.get("format").getAsString() : null;
        TextFormatting format = formatStr == null ? null :
                formatStr.equals("underline") ? TextFormatting.UNDERLINE :
                        formatStr.equals("line") ? TextFormatting.OBFUSCATED :
                                formatStr.equals("italic") ? TextFormatting.ITALIC :
                                        TextFormatting.BOLD;

        String colorStr = json.has("color") ? json.get("color").getAsString() : null;
        int color = colorStr == null ? TabletRender.textColor : Integer.parseInt(colorStr);
        return new LabelWithLinks(align == Align.LEFT ? TabletRender.leftX : align == Align.CENTER ? TabletRender.centerX : TabletRender.rightX, 0, text, align, format, width, color);
    }
}
