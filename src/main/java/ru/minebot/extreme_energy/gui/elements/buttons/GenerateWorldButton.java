package ru.minebot.extreme_energy.gui.elements.buttons;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.math.BlockPos;
import ru.minebot.extreme_energy.energy.IFrequencyHandler;

import java.util.Random;

public class GenerateWorldButton extends GenerateButton{
    public GenerateWorldButton(IFrequencyHandler handler, GuiTextField field, int posX, int posY) {
        super(handler, new BlockPos(0,0,0), field, posX, posY);
    }

    @Override
    public void onButtonClicked() {
        Random rnd = new Random();
        int frequency = rnd.nextInt(999999999);
        handler.setFrequency(frequency);
        field.setText(frequency+"");
    }
}
