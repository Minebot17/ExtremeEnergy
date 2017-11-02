package ru.minebot.extreme_energy.gui.elements.moduleGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;
import ru.minebot.extreme_energy.gui.elements.StateChanger;
import ru.minebot.extreme_energy.other.ImplantData;

public class StateModuleGui extends Gui implements IModuleGui {
    protected String text;
    protected String[] states;
    protected String key;
    protected StateChanger stateGui;
    protected boolean isDirty;

    public StateModuleGui(String text, String[] states){
        this(text, states, "stateIndex");
    }

    public StateModuleGui(String text, String[] states, String key){
        this.text = text;
        this.states = states;
        this.key = key;
    }

    @Override
    public void initGui(Minecraft mc, ImplantData data, NBTTagCompound values) {
        stateGui = new StateChanger(mc, text, states, values.getInteger(key), 9691135) {
            @Override
            public void onMouseClick(int state) {
                values.setInteger(key, state);
                isDirty = true;
            }
        };
    }

    @Override
    public void draw(ModuleGuiArgs args) {
        stateGui.draw(args.mouseX, args.mouseY);
    }

    @Override
    public boolean onMouseDown(ModuleGuiArgs args) {
        stateGui.mouseDown();
        return isDirty;
    }

    @Override
    public int getHeight() {
        return 18;
    }
}
