package ru.minebot.extreme_energy.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import ru.minebot.extreme_energy.gui.containers.EiContainer;
import ru.minebot.extreme_energy.gui.elements.buttons.InfoButton;
import ru.minebot.extreme_energy.tile_entities.InventoryEi;

public class EiGui extends GuiContainer {
    protected InfoButton info;

    public EiGui(IInventory player, InventoryEi te){
        super(new EiContainer(player, te));

        this.xSize = 176;
        this.ySize = 242;

        info = new InfoButton(10, 139,"Modules: ", te.getAcceptedModules());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/gui/eigui.png"));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        info.draw(mc, mouseX - guiLeft, mouseY - guiTop);
        renderHoveredToolTip(mouseX - guiLeft, mouseY - guiTop);
    }
}
