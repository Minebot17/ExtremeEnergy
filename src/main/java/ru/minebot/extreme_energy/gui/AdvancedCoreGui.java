package ru.minebot.extreme_energy.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import ru.minebot.extreme_energy.gui.containers.AdvancedCoreContainer;
import ru.minebot.extreme_energy.gui.elements.buttons.InfoButton;
import ru.minebot.extreme_energy.tile_entities.InventoryAdvancedCore;

public class AdvancedCoreGui extends GuiContainer {
    protected InfoButton info;

    public AdvancedCoreGui(IInventory playerInv, ItemStack core) {
        super(new AdvancedCoreContainer(playerInv, new InventoryAdvancedCore(core)));
        xSize = 176;
        ySize = 192;

        info = new InfoButton(10, 89,"Modules: ", new InventoryAdvancedCore(core).getAcceptedModules());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/gui/advancedcoregui.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        info.draw(mc, mouseX - guiLeft, mouseY - guiTop);
        renderHoveredToolTip(mouseX - guiLeft, mouseY - guiTop);
    }
}
