package ru.minebot.extreme_energy.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import ru.minebot.extreme_energy.gui.containers.BasicCoreContainer;
import ru.minebot.extreme_energy.gui.elements.buttons.InfoButton;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.tile_entities.InventoryBasicCore;

public class BasicCoreGui extends GuiContainer{
    protected NBTTagCompound core;
    protected InfoButton info;

    public BasicCoreGui(IInventory playerInv, ItemStack core) {
        super(new BasicCoreContainer(playerInv, new InventoryBasicCore(core)));
        xSize = 176;
        ySize = 153;

        info = new InfoButton(10, 50,"Modules: ", new InventoryBasicCore(core).getAcceptedModules());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/gui/basiccoregui.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        info.draw(mc, mouseX - guiLeft, mouseY - guiTop);
        renderHoveredToolTip(mouseX - guiLeft, mouseY - guiTop);
    }
}
