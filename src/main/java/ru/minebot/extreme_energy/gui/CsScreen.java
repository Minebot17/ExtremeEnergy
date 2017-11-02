package ru.minebot.extreme_energy.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import ru.minebot.extreme_energy.gui.elements.StateChanger;
import ru.minebot.extreme_energy.gui.elements.sideButtonsModule.SideButtonModuleStandart;
import ru.minebot.extreme_energy.gui.elements.sideButtonsModule.SideButtonsModule;
import ru.minebot.extreme_energy.init.ModConfig;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketCS;
import ru.minebot.extreme_energy.tile_entities.TileEntityCS;

import java.io.IOException;

public class CsScreen extends GuiScreen {

    protected int guiLeft;
    protected int guiTop;
    protected TileEntityCS te;
    protected int color;
    protected StateChanger stateModule;
    protected SideButtonsModule sideModule;

    public CsScreen(TileEntityCS te){
        this.te = te;
        color = 4210752;
    }

    @Override
    public void initGui(){
        super.initGui();
        stateModule = new StateChanger(mc, "Mode: ", new String[]{"Off", "Normal", "Inverted", "Analog", "On"}, te.mode, color, 7, 14) {
            @Override
            public void onMouseClick(int state) {
                te.mode = state;
                markDirty();
            }
        };
        sideModule = new SideButtonModuleStandart(te.bound, fontRenderer.getStringWidth("Bound: ")+10,35) {
            @Override
            public int onValueChanged(boolean isLeft, int value) {
                int to = isShiftKeyDown() && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ? 1000000 : isShiftKeyDown() ? 100000 : 10000;
                value += isLeft ? -to : to;
                if (value > ModConfig.maxCapOfChunk)
                    value = ModConfig.maxCapOfChunk;
                if (value < 0)
                    value = 0;
                te.bound = value;
                markDirty();
                return value;
            }
        };
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        super.drawScreen(mouseX, mouseY, partialTicks);

        ScaledResolution sr = new ScaledResolution(mc);
        guiLeft = (sr.getScaledWidth() - 176)/2;
        guiTop = (sr.getScaledHeight() - 55)/2;

        GlStateManager.translate((float)guiLeft, (float)guiTop, 0.0F);
        mouseX -= guiLeft;
        mouseY -= guiTop;

        mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/gui/csgui.png"));
        drawTexturedModalRect(0, 0, 0, 0, 176, 55 );

        sideModule.drawButtons(mc, mouseX, mouseY);
        stateModule.draw(mouseX, mouseY);
        sideModule.drawText(fontRenderer);

        fontRenderer.drawString(te.getBlockType().getLocalizedName(), 88 - this.fontRenderer.getStringWidth(te.getBlockType().getLocalizedName()) / 2, 5, color);
        fontRenderer.drawString("Bound: ", 10, 38, color);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        stateModule.mouseDown();
        sideModule.mouseDown();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state){
        super.mouseReleased(mouseX, mouseY, state);
        sideModule.mouseUp();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException{
        super.keyTyped(typedChar, keyCode);
        if (Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode() == keyCode || keyCode == Keyboard.KEY_ESCAPE){
            this.mc.displayGuiScreen((GuiScreen)null);

            if (this.mc.currentScreen == null)
            {
                this.mc.setIngameFocus();
            }
        }
    }

    private void markDirty(){
        NetworkWrapper.instance.sendToServer(new PacketCS(te.getPos(), te.mode, te.bound));
    }
}
