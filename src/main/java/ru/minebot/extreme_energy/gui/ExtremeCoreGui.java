package ru.minebot.extreme_energy.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import ru.minebot.extreme_energy.gui.containers.ExtremeCoreContainer;
import ru.minebot.extreme_energy.gui.elements.buttons.InfoButton;
import ru.minebot.extreme_energy.tile_entities.InventoryExtremeCore;

public class ExtremeCoreGui extends GuiContainer {
    protected InfoButton info;

    public ExtremeCoreGui(IInventory playerInv, ItemStack core) {
        super(new ExtremeCoreContainer(playerInv, new InventoryExtremeCore(core)));
        xSize = 176;
        ySize = 183;

        info = new InfoButton(10, 80,"Modules: ", new InventoryExtremeCore(core).getAcceptedModules());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/gui/extremecoregui.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        info.draw(mc, mouseX - guiLeft, mouseY - guiTop);
        renderHoveredToolTip(mouseX - guiLeft, mouseY - guiTop);
    }
}
