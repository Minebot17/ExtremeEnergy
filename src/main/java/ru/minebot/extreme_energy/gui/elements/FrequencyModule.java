package ru.minebot.extreme_energy.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import ru.minebot.extreme_energy.energy.IFrequencyHandler;
import ru.minebot.extreme_energy.gui.elements.buttons.CopyButton;
import ru.minebot.extreme_energy.gui.elements.buttons.GenerateWorldButton;
import ru.minebot.extreme_energy.gui.elements.buttons.PastFrequencyWorldButton;

public class FrequencyModule extends Gui {
    protected GuiTextField textField;
    protected Button[] buttons;
    protected IFrequencyHandler handler;

    public FrequencyModule(FontRenderer font, IFrequencyHandler handler, int posX, int posY){
        this.handler = handler;
        textField = new GuiTextField(0, font, posX, posY, 65,13);
        textField.setMaxStringLength(9);
        textField.setText(handler.getFrequency()+"");
        textField.setFocused(true);
        buttons = new Button[]{
                new CopyButton(handler, posX + 68, posY - 1),
                new PastFrequencyWorldButton(handler, textField, posX + 68, posY + 7),
                new GenerateWorldButton(handler, textField, posX + 77, posY + 3)
        };
    }

    public void draw(Minecraft mc, int mouseX, int mouseY){
        textField.drawTextBox();
        for (int i = 0; i < 3; i++)
            buttons[i].draw(mc, mouseX, mouseY);
    }

    public void mouseDown(int mouseX, int mouseY, int mouseButton){
        textField.mouseClicked(mouseX, mouseY, mouseButton);
        for (int i = 0; i < 3; i++)
            buttons[i].mouseDown();
    }

    public void mouseUp(){
        for (int i = 0; i < 3; i++)
            buttons[i].mouseUp();
    }

    public void update(){
        textField.updateCursorCounter();
    }

    public void keyTyped(char typedChar, int keyCode){
        if ((typedChar >= '0' && typedChar <= '9') || Keyboard.KEY_BACK == keyCode || Keyboard.KEY_DELETE == keyCode){
            textField.textboxKeyTyped(typedChar, keyCode);
            if (textField.isFocused()) {
                int frequency = textField.getText().equals("") ? 0 : Integer.parseInt(textField.getText());
                handler.setFrequency(frequency);
            }
        }
    }
}
