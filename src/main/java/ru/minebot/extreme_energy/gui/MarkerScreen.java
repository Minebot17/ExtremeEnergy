package ru.minebot.extreme_energy.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;
import ru.minebot.extreme_energy.gui.elements.ColorPicker;
import ru.minebot.extreme_energy.items.modules.ItemPathInfoModule;

import java.io.IOException;

public class MarkerScreen extends GuiScreen {
    protected int guiLeft;
    protected int guiTop;
    protected String primaryName;
    protected GuiTextField nameField;
    protected GuiTextField coordsField;
    protected GuiTextField colorField;
    protected ColorPicker colorPicker;
    protected GuiButton[] buttons;
    protected boolean toCreate;
    protected ItemStack stack;
    protected ItemPathInfoModule.Mark mark;
    protected int color;
    protected BlockPos pos;

    public MarkerScreen(ItemStack stack, ItemPathInfoModule.Mark mark){
        primaryName = mark.name;
        this.stack = stack;
        this.mark = mark;
        toCreate = false;
    }

    public MarkerScreen(ItemStack stack, BlockPos pos){
        this.pos = pos;
        this.stack = stack;
        toCreate = true;
    }

    public void initGui() {
        this.color = 0xffffff;
        this.nameField = new GuiTextField(0, fontRenderer, 5, 20, 151, 20);
        this.nameField.setFocused(true);
        this.coordsField = new GuiTextField(1, fontRenderer, 5, 60, 151, 20);
        this.colorField = new GuiTextField(2, fontRenderer, 5, 100, 151, 20);
        this.colorPicker = new ColorPicker(13, 125, 130, new int[][]{
                new int[]{ 0x000000, 0xeeeeee, 0xff0000, 0x00ff00, 0x0000ff },
                new int[]{ 0xf37736, 0xfdf498, 0x7c501a, 0xff77aa, 0x77aaff }
        }) {
            @Override
            public void onButtonClicked(int color) {
                colorField.setText("#" + Integer.toHexString(color));
            }
        };

        if (toCreate){
            nameField.setText("");
            coordsField.setText(pos.getX()+","+pos.getY()+","+pos.getZ());
            colorField.setText("#ffffff");
            buttons = new GuiButton[]{ new GuiButton(4, 5, colorPicker.getHeight()+130, 150, 20, "Create") };
        }
        else {
            nameField.setText(mark.name);
            coordsField.setText(mark.pos.getX()+","+mark.pos.getY()+","+mark.pos.getZ());
            colorField.setText("#"+Integer.toHexString(mark.color));
            buttons = new GuiButton[]{
                    new GuiButton(4, 5, colorPicker.getHeight()+130, 75, 20, "Change"),
                    new GuiButton(5, 80, colorPicker.getHeight()+130, 75, 20, "Remove")
            };
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        guiLeft = (width - 161)/2;
        guiTop = (height - 250)/2;

        GlStateManager.translate(guiLeft, guiTop, 0);
        mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/gui/markergui.png"));
        drawTexturedModalRect(0, 0, 2, 2, 161, 250);

        nameField.drawTextBox();
        colorField.drawTextBox();
        coordsField.drawTextBox();
        colorPicker.draw(mc, mouseX-guiLeft, mouseY-guiTop);
        for (GuiButton button : buttons)
            button.drawButton(mc, mouseX-guiLeft, mouseY-guiTop, mc.getRenderPartialTicks());

        fontRenderer.drawString("Name:", 5, 10, color);
        fontRenderer.drawString("Coordinates:", 5, 50, color);
        fontRenderer.drawString("Color:", 5, 90, color);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        mouseX -= guiLeft;
        mouseY -= guiTop;
        nameField.mouseClicked(mouseX, mouseY, mouseButton);
        coordsField.mouseClicked(mouseX, mouseY, mouseButton);
        colorField.mouseClicked(mouseX, mouseY, mouseButton);
        colorPicker.mouseDown();

        if (toCreate){
            if (buttons[0].mousePressed(mc, mouseX, mouseY) && isValid()) {
                ItemPathInfoModule.createMark(stack, nameField.getText(), getPosFromField(), Integer.parseInt(colorField.getText().substring(1), 16));
                closeGui();
            }
        }
        else {
            if (buttons[1].mousePressed(mc, mouseX, mouseY)){
                ItemPathInfoModule.removeMark(stack, primaryName);
                closeGui();
            }
            else if (buttons[0].mousePressed(mc, mouseX, mouseY) && isValid()){
                ItemPathInfoModule.changeMark(stack, primaryName, nameField.getText(), getPosFromField(), Integer.parseInt(colorField.getText().substring(1), 16));
                closeGui();
            }
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        nameField.updateCursorCounter();
        coordsField.updateCursorCounter();
        colorField.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        nameField.textboxKeyTyped(typedChar, keyCode);
        colorField.textboxKeyTyped(typedChar, keyCode);
        if ((typedChar >= '0' && typedChar <= '9') || Keyboard.KEY_BACK == keyCode || Keyboard.KEY_DELETE == keyCode || typedChar == ',' || typedChar == '-')
            coordsField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    private boolean isValid(){
        if (nameField.getText().equals(""))
            return sendMessage("nameEmpty");

        if (toCreate && ItemPathInfoModule.existMark(stack, nameField.getText()))
            return sendMessage("nameExist");

        String[] splitted = coordsField.getText().split(",");
        if (splitted.length != 3)
            return sendMessage("incorrectCoords");
        try {
            for (String str : splitted) {
                int i = Integer.parseInt(str);
            }
        }
        catch (NumberFormatException e){ return sendMessage("incorrectCoords"); }

        try {
            int i = Integer.parseInt(colorField.getText().substring(1), 16);
        }
        catch (NumberFormatException e){ return sendMessage("incorrectColor"); }

        return true;
    }

    private boolean sendMessage(String text){
        mc.player.sendChatMessage(I18n.format("markError." + text));
        return false;
    }

    private BlockPos getPosFromField(){
        String[] splitted = coordsField.getText().split(",");
        return new BlockPos(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]));
    }

    private void closeGui(){
        this.mc.displayGuiScreen((GuiScreen)null);

        if (this.mc.currentScreen == null)
        {
            this.mc.setIngameFocus();
        }
    }
}
