package ru.minebot.extreme_energy.gui.elements.buttons;

import net.minecraft.util.ResourceLocation;
import ru.minebot.extreme_energy.energy.IFrequencyHandler;
import ru.minebot.extreme_energy.gui.elements.Button;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class CopyButton extends Button{
    private IFrequencyHandler handler;

    public CopyButton(IFrequencyHandler handler, int posX, int posY) {
        super(7, 7, posX, posY,
                new ResourceLocation("meem:textures/gui/buttons/copy_past_refresh/copy.png"),
                new ResourceLocation("meem:textures/gui/buttons/copy_past_refresh/copy_hover.png")
        );
        this.handler = handler;
    }

    @Override
    public void onButtonClicked() {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(handler.getFrequency() + ""), null);
    }
}
