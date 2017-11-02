package ru.minebot.extreme_energy.gui.tablet;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent;
import ru.minebot.extreme_energy.init.ModSoundHandler;

import java.util.List;

public class ChaptersScene implements IScene {
    private float mouseX;
    private float mouseY;
    private Button[] buttons;
    private Label[] labels;

    public ChaptersScene(List<Chapter> chapters){
        buttons = new Button[6];
        labels = new Label[6];
        for (int i = 0; i < 6; i++){
            ResourceLocation[] icons = chapters.get(i).getIcons();
            int g = i;
            float x = 0.21f + 0.46f*(i%3);
            float y = i > 2 ? -0.55f : -0.13f;
            buttons[i] = new Button(x, y, 0.25f, 0.25f, icons[0], icons[1], icons[2], ModSoundHandler.tap0, TabletRender.soundVolume) {
                @Override
                public void action() {
                    TabletRender.setArticle(g, 0);
                }
            };
            labels[i] = new Label(x + 0.125f, y + 0.05f, chapters.get(i).getName(), Element.Align.CENTER, TabletRender.textColor);
        }
    }

    @Override
    public void draw(Tessellator tes, BufferBuilder buf, float mouseX, float mouseY){
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        for (Button button : buttons)
            button.draw(tes, buf, mouseX, mouseY);

        for (Label label : labels)
            label.draw(tes, buf, mouseX, mouseY);
    }

    @Override
    public void onMouseDown() {
        for (Button button : buttons)
            if (button.isHover(mouseX, mouseY))
                button.onMouseDown();
    }

    @Override
    public void onMouseMove() {

    }

    @Override
    public void onMouseUp() {
        for (Button button : buttons)
            if (button.isHover(mouseX, mouseY))
                button.onMouseUp();
    }

    @Override
    public void onWheelScrolled(int delta) {

    }

    @Override
    public void onKeyPressed() {

    }
}
