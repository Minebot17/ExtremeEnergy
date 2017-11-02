package ru.minebot.extreme_energy.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import ru.minebot.extreme_energy.gui.containers.EnergyBalancerContainer;
import ru.minebot.extreme_energy.gui.elements.Slider;
import ru.minebot.extreme_energy.gui.tablet.Element;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketSliderPos;
import ru.minebot.extreme_energy.tile_entities.InventoryEnergyBalancer;

import java.io.IOException;

public class EnergyBalancerGui extends GuiContainer {
    protected NBTTagCompound category;
    protected Slider slider;

    public EnergyBalancerGui(World world, IInventory player, ItemStack stack) {
        super(new EnergyBalancerContainer(player, new InventoryEnergyBalancer(world, stack)));
        this.category = ModUtils.getNotNullCategory(stack);
        xSize = 176;
        ySize = 193;
    }

    @Override
    public void initGui(){
        super.initGui();
        slider = new Slider(32, 66, 22) {
            @Override
            public void onSliderPosChanged(int sliderPos) {
                category.setInteger("sliderPos", sliderPos);
                NetworkWrapper.instance.sendToServer(new PacketSliderPos(sliderPos));
            }
        };
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/gui/energybalancergui.png"));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int sliderPos = category.getInteger("sliderPos");
        boolean done = ModUtils.getNotNullCategory(mc.player.inventory.getCurrentItem()).getBoolean("done");
        slider.draw(mc, sliderPos);
        ModUtils.drawString((int)((1f-Math.rint(10f * (sliderPos/21f))/10f) * 100f)+"%", 47, 83, 0xffffff, Element.Align.CENTER);
        ModUtils.drawString((int)(Math.rint(10f * (sliderPos/21f)) * 10f)+"%", 129, 83, 0xffffff, Element.Align.CENTER);
        ModUtils.drawString(done ? "Ready" : "Transfer", 88, 44, done ? 0x00ff00 : 0xffff00, Element.Align.CENTER);
        renderHoveredToolTip(mouseX - guiLeft, mouseY - guiTop);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        slider.mouseDown(mouseX-guiLeft, mouseY-guiTop);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        slider.mousePressed(mouseX-guiLeft, mouseY-guiTop);
    }
}
