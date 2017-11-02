package ru.minebot.extreme_energy.gui.elements.switchButton;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import ru.minebot.extreme_energy.gui.elements.Button;

public abstract class SwitchButton extends Button {
    public ResourceLocation texture1;
    public ResourceLocation textureHover1;
    public ResourceLocation textureClick1;
    public boolean isOn;

    public SwitchButton(ResourceLocation off, ResourceLocation off_hover, ResourceLocation off_click,ResourceLocation on, ResourceLocation on_hover, ResourceLocation on_click, int posX, int posY, int sizeX, int sizeY, boolean isOn) {
        super(sizeX, sizeY, posX, posY, off, off_hover, off_click);
        texture1 = on;
        textureHover1 = on_hover;
        textureClick1 = on_click;
        this.isOn = isOn;
    }

    @Override
    public void draw(Minecraft mc, int mouseX, int mouseY){
        isHover = posX < mouseX && posX + sizeX > mouseX && posY < mouseY && posY + sizeY > mouseY;
        ResourceLocation tex = isMouseDown && isHover ? (isOn ? textureClick1 : textureClick) : (isHover ? (isOn ? textureHover1 : textureHover) : (isOn ? texture1 : texture));
        mc.getTextureManager().bindTexture(tex);
        drawModalRectWithCustomSizedTexture(posX, posY, 0, 0, sizeX, sizeY, sizeX, sizeY);
    }

    @Override
    public void mouseDown(){
        if (isHover){
            isMouseDown = true;
            playPressSound(Minecraft.getMinecraft().getSoundHandler());
            isOn = !isOn;
            onButtonClicked(isOn);
        }
    }

    @Override
    public void onButtonClicked() {}

    public abstract void onButtonClicked(boolean isOn);
}
