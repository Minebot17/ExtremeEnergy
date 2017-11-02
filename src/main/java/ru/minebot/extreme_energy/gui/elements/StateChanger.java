package ru.minebot.extreme_energy.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;

public abstract class StateChanger extends Gui {
    protected Minecraft mc;
    protected FontRenderer font;
    protected GuiButton button;
    protected String[] states;
    protected String text;

    protected int color;
    protected int state;
    protected int mouseX;
    protected int mouseY;
    protected int posX;
    protected int posY;

    public StateChanger(Minecraft mc, String text, String[] states, int state, int color){
        this(mc, text, states, state, color, 0 ,0);
    }

    public StateChanger(Minecraft mc, String text, String[] states, int state, int color, int posX, int posY){
        this.posX = posX;
        this.posY = posY;
        this.mc = mc;
        this.font = mc.fontRenderer;
        this.color = color;
        this.text = text;
        this.states = states;
        this.state = state;
        button = new GuiButton(0, font.getStringWidth(text) + 3 + posX, posY, font.getStringWidth(states[state]) + 10, 20, states[state]);
    }

    public void draw(int mouseX, int mouseY){
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        mc.fontRenderer.drawString(text, 3 + posX, 5 + posY, color);
        button.drawButton(mc, mouseX, mouseY, mc.getRenderPartialTicks());
    }

    public void mouseDown(){
        if (button.mousePressed(mc, mouseX, mouseY)){
            button.playPressSound(mc.getSoundHandler());
            state++;
            if (state >= states.length)
                state = 0;
            button.setWidth(font.getStringWidth(states[state] + 10));
            button.displayString = states[state];
            onMouseClick(state);
        }
    }

    public abstract void onMouseClick(int state);
}
