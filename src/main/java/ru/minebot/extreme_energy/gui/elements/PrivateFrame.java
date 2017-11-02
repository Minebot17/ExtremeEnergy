package ru.minebot.extreme_energy.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.ArrayList;

public abstract class PrivateFrame extends Gui{
    private boolean[] isHover; // 0 - publicButton, 1 - privateButton, 2 - info

    private ResourceLocation frame;
    private ResourceLocation[] buttonPublic;
    private ResourceLocation[] buttonPrivate;
    private ResourceLocation[] info;

    public PrivateFrame(){
        frame = new ResourceLocation("meem:textures/gui/privateframe/cardframe.png");
        buttonPublic = new ResourceLocation[]{
                new ResourceLocation("meem:textures/gui/privateframe/public.png"),
                new ResourceLocation("meem:textures/gui/privateframe/public_hover.png"),
                new ResourceLocation("meem:textures/gui/privateframe/public_active.png")
        };
        buttonPrivate = new ResourceLocation[]{
                new ResourceLocation("meem:textures/gui/privateframe/private.png"),
                new ResourceLocation("meem:textures/gui/privateframe/private_hover.png"),
                new ResourceLocation("meem:textures/gui/privateframe/private_active.png")
        };
        info = new ResourceLocation[]{
                new ResourceLocation("meem:textures/gui/moduleframe/info.png"),
                new ResourceLocation("meem:textures/gui/moduleframe/info_active.png")
        };
        isHover = new boolean[]{false, false, false};
    }

    public void drawFrame(Minecraft mc, int x, int y, boolean isPublic){
        if (!isPublic) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(frame);
            drawModalRectWithCustomSizedTexture(x, y + 30, 0, 0, 24, 96, 24, 96);
        }
    }

    public void draw(Minecraft mc, int x, int y, int mouseX, int mouseY, boolean isPublic){
        isHover[0] = mouseX > x + 6 && mouseX < x + 19 && mouseY > y && mouseY < y + 13;
        isHover[1] = mouseX > x + 6 && mouseX < x + 19 && mouseY > y + 15 && mouseY < y + 28;
        isHover[2] = mouseX > x + 7 && mouseX < x + 17 && mouseY > y + (!isPublic ? 128 : 30) && mouseY < y + (!isPublic ? 138 : 40);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        mc.getTextureManager().bindTexture(isPublic ? buttonPublic[2] : (isHover[0] ? buttonPublic[1] : buttonPublic[0]));
        drawModalRectWithCustomSizedTexture(x + 6, y, 0, 0, 13, 13, 13, 13);
        mc.getTextureManager().bindTexture(!isPublic ? buttonPrivate[2] : (isHover[1] ? buttonPrivate[1] : buttonPrivate[0]));
        drawModalRectWithCustomSizedTexture(x + 6, y + 15, 0, 0, 13, 13, 13, 13);
        mc.getTextureManager().bindTexture(isHover[2] ? info[1] : info[0]);
        drawModalRectWithCustomSizedTexture(x + 7, y + (!isPublic ? 128 : 30), 0, 0, 10, 10, 10, 10);
        if (isHover[2]) {
            ArrayList<String> list = new ArrayList<String>();
            list.add("These buttons are for security settings.");
            list.add("The top button, that would be the field acted at all.");
            list.add("The bottom button, so that the field would act only on certain players, ");
            list.add("whose ID cards lie here.");
            GuiUtils.drawHoveringText(list, mouseX, mouseY, mc.displayWidth, mc.displayHeight, -1, mc.fontRenderer);
        }
    }

    public void mouseDown(){
        if (isHover[0] || isHover[1]) {
            playPressSound(Minecraft.getMinecraft().getSoundHandler());
            onMouseClicked(isHover[0]);
        }
    }

    public abstract void onMouseClicked(boolean isPublic);

    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
