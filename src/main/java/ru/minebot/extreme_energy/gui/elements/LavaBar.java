package ru.minebot.extreme_energy.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.ArrayList;

public class LavaBar extends Gui{
    protected ResourceLocation background;
    protected ResourceLocation bar;
    protected int width;
    protected int height;

    public LavaBar(ResourceLocation background, ResourceLocation bar, int width, int height){
        this.width = width;
        this.height = height;
        this.background = background;
        this.bar = bar;
    }

    public void draw(Minecraft mc, int posX, int posY, int mouseX, int mouseY, int value, int maxValue){
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(background);
        drawModalRectWithCustomSizedTexture(posX, posY, 0, 0, width, height, width, height);

        mc.getTextureManager().bindTexture(bar);
        drawTexturedModalRect(posX, posY, 0, 0, (int)((float)value/(float)maxValue * width), height);

        if (mouseX > posX && mouseX < posX + width && mouseY > posY && mouseY < posY + height) {
            ArrayList<String> list = new ArrayList<String>();
            list.add("Lava");
            list.add(value + "/" + maxValue + " mB");
            GuiUtils.drawHoveringText(list, mouseX, mouseY, mc.displayWidth, mc.displayHeight, -1, mc.fontRenderer);
        }
    }
}
