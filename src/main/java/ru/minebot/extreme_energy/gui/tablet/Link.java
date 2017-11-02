package ru.minebot.extreme_energy.gui.tablet;

import com.google.gson.JsonObject;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import ru.minebot.extreme_energy.init.ModSoundHandler;

import java.util.HashMap;

public abstract class Link extends Label implements IClickable{
    protected String url;
    protected SoundEvent sound;
    protected float volume;
    protected boolean isMouseDown;

    public Link(float posX, float posY, String text, String url, Align align, float width) {
        super(posX, posY, new String[]{TextFormatting.UNDERLINE+text}, align, width, 255);
        this.url = url;
        this.sound = ModSoundHandler.tap0;
    }

    @Override
    public void onMouseDown() {
        isMouseDown = true;
        mc.player.playSound(sound, volume, 1);
    }

    @Override
    public void onMouseUp() {
        if (isMouseDown){
            isMouseDown = false;
            action();
        }
    }

    @Override
    public void onMouseMove() {

    }

    public abstract void action();

    @Override
    public boolean isHover(float mouseX, float mouseY) {
        float width = (float)font.getStringWidth(text.get(0))/(1f/textSize);
        float localX = align == Align.RIGHT ? posX - width : posX - width/2f;
        return mouseX > localX && mouseX < localX + width && mouseY < posY && mouseY > posY - getHeight();
    }

    public static Element loadFromJson(JsonObject json, int chapter, int article){
        HashMap<String, String> langMap = TabletRender.getLangMap(chapter, article);
        String url = json.get("url").getAsString();
        String text = langMap.get(json.get("text").getAsString());
        float width = !json.has("width") ? TabletRender.textWidth : json.get("width").getAsFloat();

        String alignStr = json.has("align") ? json.get("align").getAsString() : null;
        Align align = alignStr == null ? Align.LEFT : alignStr.equals("l") ? Align.LEFT : alignStr.equals("c") ? Align.CENTER : Align.RIGHT;

        if (url.contains("http"))
            return new LinkNet(align == Align.LEFT ? TabletRender.leftX : align == Align.CENTER ? TabletRender.centerX : TabletRender.rightX, 0, text, url, align, width);
        else
            return new LinkArticle(align == Align.LEFT ? TabletRender.leftX : align == Align.CENTER ? TabletRender.centerX : TabletRender.rightX, 0, text, url, align, width);
    }
}
