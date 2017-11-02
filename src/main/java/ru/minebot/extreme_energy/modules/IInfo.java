package ru.minebot.extreme_energy.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;

public interface IInfo {
    void renderScreen(ChipArgs args, Minecraft mc, Tessellator tes, BufferBuilder buf, ScaledResolution res);
    void renderWorld(ChipArgs args, Minecraft mc, Tessellator tes, BufferBuilder buf, ScaledResolution res);
}
