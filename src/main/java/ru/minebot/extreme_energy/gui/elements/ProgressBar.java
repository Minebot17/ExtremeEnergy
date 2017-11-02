package ru.minebot.extreme_energy.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProgressBar extends Gui {

    protected Type type;

    public ProgressBar(Type type){
        this.type = type;
    }

    public void draw(Minecraft mc, int posX, int posY, int mouseX, int mouseY, int value, int maxValue){
        GlStateManager.disableLighting();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(type.background);
        drawModalRectWithCustomSizedTexture(posX, posY, 0, 0, 141, 14, 141, 14);

        mc.getTextureManager().bindTexture(type.bar);
        drawTexturedModalRect(posX + 14, posY + 1, 0, 0, (int)((float)value/(float)maxValue * 126f), 12);

        if (mouseX > posX && mouseX < posX + 141 && mouseY > posY && mouseY < posY + 12)
            GuiUtils.drawHoveringText(type.getTooltip(value, maxValue), mouseX, mouseY, mc.displayWidth, mc.displayHeight, -1, mc.fontRenderer);
    }

    public enum Type {
        VOLTAGE(0, "meem:textures/gui/progressbar/voltage_background.png", "meem:textures/gui/progressbar/voltage_bar.png"),
        RF(1, "meem:textures/gui/progressbar/energy_background.png", "meem:textures/gui/progressbar/energy_bar.png"),
        HEAT(2, "meem:textures/gui/progressbar/heat_background.png", "meem:textures/gui/progressbar/heat_bar.png"),
        NUCLEAR_FUEL(3, "meem:textures/gui/progressbar/nuclearfuel_background.png", "meem:textures/gui/progressbar/nuclearfuel_bar.png");

        private int index;
        public ResourceLocation background;
        public ResourceLocation bar;

        Type(int index, String background, String bar){
            this.index = index;
            this.background = new ResourceLocation(background);
            this.bar = new ResourceLocation(bar);
        }

        public List<String> getTooltip(int value, int maxValue){
            List<String> result = new ArrayList<>();
            if (index == 0){
                result.add("Voltage: " + value);
                result.add("Max Voltage: " + maxValue);
            }
            else if (index == 1){
                result.add(value + "/" + maxValue + " RF");
            }
            else if (index == 2){
                result.add("Temperature: " + value + " C°");
                result.add("Max Temperature: " + maxValue + " C°");
            }
            else if (index == 3){
                result.add(value/20 + "/" + maxValue/20 + " sec");
            }
            return result;
        }
    }
}
