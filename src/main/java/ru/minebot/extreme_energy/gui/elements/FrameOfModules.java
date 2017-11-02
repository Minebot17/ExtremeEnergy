package ru.minebot.extreme_energy.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.ArrayList;

public class FrameOfModules extends Gui{
    private ResourceLocation top;
    private ResourceLocation bottom;
    private ResourceLocation left;
    private ResourceLocation right;
    private ResourceLocation center;
    private ResourceLocation info;
    private ResourceLocation info_active;
    private ArrayList<String> acceptedModules;

    private int count;

    public FrameOfModules(int count, ArrayList<String> acceptedModules){
        this.count = count;
        acceptedModules.add(0, "Permissible improvements:");
        this.acceptedModules = acceptedModules;

        top = new ResourceLocation("meem:textures/gui/moduleframe/top.png");
        bottom = new ResourceLocation("meem:textures/gui/moduleframe/bottom.png");
        left = new ResourceLocation("meem:textures/gui/moduleframe/left.png");
        right = new ResourceLocation("meem:textures/gui/moduleframe/right.png");
        center = new ResourceLocation("meem:textures/gui/moduleframe/center.png");
        info = new ResourceLocation("meem:textures/gui/moduleframe/info.png");
        info_active = new ResourceLocation("meem:textures/gui/moduleframe/info_active.png");
    }

    public void draw(Minecraft mc, int posX, int posY, int mouseX, int mouseY){
        mc.getTextureManager().bindTexture(top);
        drawModalRectWithCustomSizedTexture(posX, posY, 0, 0, 24, 3, 24, 3);

        for (int i = 0; i < count; i++){
            mc.getTextureManager().bindTexture(left);
            drawModalRectWithCustomSizedTexture(posX, posY + 3 + i * 18, 0, 0, 3, 18, 3, 18);
            mc.getTextureManager().bindTexture(center);
            drawModalRectWithCustomSizedTexture(posX + 3, posY + 3 + i * 18, 0, 0, 18, 18, 18, 18);
            mc.getTextureManager().bindTexture(right);
            drawModalRectWithCustomSizedTexture(posX + 21, posY + 3 + i * 18, 0, 0, 3, 18, 3, 18);
        }

        mc.getTextureManager().bindTexture(bottom);
        drawModalRectWithCustomSizedTexture(posX, posY + 3 + count * 18, 0, 0, 24, 3, 24, 3);

        boolean isHover = mouseX > posX + 7 && mouseX < posX + 17 && mouseY > posY + 8 + count * 18 && mouseY < posY + 18 + count * 18;
        mc.getTextureManager().bindTexture(isHover ? info_active : info);
        drawModalRectWithCustomSizedTexture(posX + 7, posY + 8 + count * 18, 0, 0, 10, 10, 10, 10);
        if (isHover)
            GuiUtils.drawHoveringText(acceptedModules, mouseX, mouseY, mc.displayWidth, mc.displayHeight, -1, mc.fontRenderer);
    }
}
