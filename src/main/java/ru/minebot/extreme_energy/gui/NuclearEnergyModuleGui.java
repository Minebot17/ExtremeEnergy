package ru.minebot.extreme_energy.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import ru.minebot.extreme_energy.gui.containers.NuclearEnergyModuleContainer;

public class NuclearEnergyModuleGui extends GuiContainer {

    public NuclearEnergyModuleGui(IInventory player, ItemStack stack) {
        super(new NuclearEnergyModuleContainer(player, stack));
        xSize = 176;
        ySize = 192;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/gui/nuclearenergymodulegui.png"));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        renderHoveredToolTip(mouseX, mouseY);
    }
}
