package ru.minebot.extreme_energy.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import ru.minebot.extreme_energy.gui.elements.FrameOfModules;
import ru.minebot.extreme_energy.modules.Module;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.other.IModuleProvider;

public abstract class BasicGuiContainer<T extends TileEntity> extends GuiContainer {
    public static final int STANDART_COLOR = 4210752;
    public static final int BLUE_COLOR = 9691135;

    private ResourceLocation background;
    private int moduleCount;
    private FrameOfModules frame;
    protected T te;
    protected int color;

    public BasicGuiContainer(T te, Container container, String background, int sizeX, int sizeY){
        this(te, container, background, sizeX, sizeY, STANDART_COLOR, 0);
    }

    public BasicGuiContainer(T te, Container container, String background, int sizeX, int sizeY, int color){
        this(te, container, background, sizeX, sizeY, color, 0);
    }

    public BasicGuiContainer(T te, Container container, String background, int xSize, int ySize, int color, int moduleCount){
        super(container);
        this.te = te;
        this.background = new ResourceLocation(background);
        this.color = color;
        this.moduleCount = moduleCount;

        this.xSize = moduleCount == 0 ? xSize : xSize+20;
        this.ySize = ySize;
    }

    @Override
    public void initGui(){
        super.initGui();
        if (moduleCount != 0)
            frame = new FrameOfModules(moduleCount, Module.getLocalizedNames(((IModuleProvider)te).getAcceptedModules()));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(background);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, moduleCount == 0 ? xSize : xSize-20, this.ySize);
        drawBackground(partialTicks, mouseX, mouseY);
        frame.draw(mc, xSize + guiLeft - 19, guiTop, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
        mouseX -= guiLeft;
        mouseY -= guiTop;
        draw(mouseX, mouseY);
        fontRenderer.drawString(te.getBlockType().getLocalizedName(), (moduleCount == 0 ? xSize/2 : (xSize-20)/2) - this.fontRenderer.getStringWidth(te.getBlockType().getLocalizedName()) / 2, 5, color);
        renderHoveredToolTip(mouseX, mouseY);
    }

    protected void drawBackground(float partialTicks, int mouseX, int mouseY){}
    protected abstract void draw(int mouseX, int mouseY);

    protected abstract void markDirty();
}
