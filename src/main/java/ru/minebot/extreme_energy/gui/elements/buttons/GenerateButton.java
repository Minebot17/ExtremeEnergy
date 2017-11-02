package ru.minebot.extreme_energy.gui.elements.buttons;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import ru.minebot.extreme_energy.energy.IFrequencyHandler;
import ru.minebot.extreme_energy.gui.elements.Button;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketFrequency;

import java.util.Random;

public class GenerateButton extends Button{
    protected GuiTextField field;
    protected BlockPos pos;
    protected IFrequencyHandler handler;

    public GenerateButton(IFrequencyHandler handler, BlockPos pos, GuiTextField field, int posX, int posY) {
        super(9, 8, posX, posY,
                new ResourceLocation("meem:textures/gui/buttons/copy_past_refresh/refresh.png"),
                new ResourceLocation("meem:textures/gui/buttons/copy_past_refresh/refresh_hover.png")
        );
        this.pos = pos;
        this.handler = handler;
        this.field = field;
    }

    @Override
    public void onButtonClicked() {
        Random rnd = new Random();
        int frequency = rnd.nextInt(999999999);
        NetworkWrapper.instance.sendToServer(new PacketFrequency(pos, frequency));
        handler.setFrequency(frequency);
        field.setText(frequency+"");
    }
}
