package ru.minebot.extreme_energy.gui.elements.buttons;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import ru.minebot.extreme_energy.energy.IFrequencyHandler;
import ru.minebot.extreme_energy.gui.elements.Button;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketFrequency;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;

public class PastFrequencyButton extends Button {
    protected GuiTextField field;
    protected BlockPos pos;
    protected IFrequencyHandler handler;

    public PastFrequencyButton(IFrequencyHandler handler, BlockPos pos, GuiTextField field, int posX, int posY) {
        super(7, 7, posX, posY,
                new ResourceLocation("meem:textures/gui/buttons/copy_past_refresh/past.png"),
                new ResourceLocation("meem:textures/gui/buttons/copy_past_refresh/past_hover.png")
        );
        this.pos = pos;
        this.handler = handler;
        this.field = field;
    }

    @Override
    public void onButtonClicked() {
        try {
            String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null).getTransferData(DataFlavor.stringFlavor);
            if (isValid(data)){
                int frequency = Integer.parseInt(data);
                NetworkWrapper.instance.sendToServer(new PacketFrequency(pos, frequency));
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

    protected boolean isValid(String str){
        if (str.length() <= 9) {
            for (int i = 0; i < str.length(); i++)
                if (!(str.charAt(i) >= '0' && str.charAt(i) <= '9'))
                    return false;
            return true;
        }
        return false;
    }
}
