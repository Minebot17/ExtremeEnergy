package ru.minebot.extreme_energy.gui.elements.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import ru.minebot.extreme_energy.gui.elements.Button;

import java.util.ArrayList;
import java.util.List;

public class InfoButton extends Button {
    protected List<String> list;

    public InfoButton(int posX, int posY, List<String> list){
        super(10, 10, posX, posY,
                new ResourceLocation("meem:textures/gui/moduleframe/info.png"),
                new ResourceLocation("meem:textures/gui/moduleframe/info_active.png")
        );
        this.list = list;
    }

    public InfoButton(int posX, int posY, String tile, Item[] list){
        super(10, 10, posX, posY,
                new ResourceLocation("meem:textures/gui/moduleframe/info.png"),
                new ResourceLocation("meem:textures/gui/moduleframe/info_active.png")
        );
        List<String> stringList = new ArrayList<>();
        stringList.add(tile);
        for (int i = 0; i < list.length; i++)
            stringList.add(list[i].getItemStackDisplayName(new ItemStack(list[i])));
        this.list = stringList;
    }

    public InfoButton(int sizeX, int sizeY, int posX, int posY, ResourceLocation texture, ResourceLocation textureHover, List<String> list) {
        super(sizeX, sizeY, posX, posY, texture, textureHover);
        this.list = list;
    }

    @Override
    public void draw(Minecraft mc, int mouseX, int mouseY){
        super.draw(mc, mouseX, mouseY);
        if (isHover)
            GuiUtils.drawHoveringText(list, mouseX, mouseY, mc.displayWidth, mc.displayHeight, -1, mc.fontRenderer);
    }

    @Override
    public void onButtonClicked() {

    }
}
