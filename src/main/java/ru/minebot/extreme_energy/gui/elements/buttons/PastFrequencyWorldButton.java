package ru.minebot.extreme_energy.gui.elements.buttons;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.math.BlockPos;
import ru.minebot.extreme_energy.energy.IFrequencyHandler;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;

public class PastFrequencyWorldButton extends PastFrequencyButton{
    public PastFrequencyWorldButton(IFrequencyHandler handler, GuiTextField field, int posX, int posY) {
        super(handler, new BlockPos(0, 0, 0), field, posX, posY);
    }

    @Override
    public void onButtonClicked() {
        try {
            String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null).getTransferData(DataFlavor.stringFlavor);
            if (isValid(data)){
                int frequency = Integer.parseInt(data);
                handler.setFrequency(frequency);
                field.setText(frequency+"");
            }
            else
                throw new Exception("Invalid string format");
        }
        catch(Exception e){
            System.out.println("Insert failed");
        }
    }
}
