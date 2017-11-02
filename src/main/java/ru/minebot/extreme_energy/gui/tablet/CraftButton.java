package ru.minebot.extreme_energy.gui.tablet;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.minebot.extreme_energy.init.ModSoundHandler;

public abstract class CraftButton extends Button {
    protected boolean left;

    public CraftButton(float posX, float posY, boolean left) {
        super(posX, posY, 0.1f, 0.07f,
                new ResourceLocation("meem:tablet/textures/sidebutton_normal.png"),
                new ResourceLocation("meem:tablet/textures/sidebutton_hover.png"),
                new ResourceLocation("meem:tablet/textures/sidebutton_click.png"),
                ModSoundHandler.tap1, TabletRender.soundVolume);
        this.left = left;
    }

    @Override
    public void draw(Tessellator tes, BufferBuilder buf, float mouseX, float mouseY) {
        boolean isHover = isHover(mouseX, mouseY);
        ResourceLocation tex = isMouseDown && isHover ? textureClick : (isHover ? textureHover : texture);
        mc.getTextureManager().bindTexture(tex);
        buf.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
        buf.pos(posX, posY, 0).tex(left ? 0 : 1, 0).endVertex();
        buf.pos(posX, posY-sizeY, 0).tex(left ? 0 : 1, 1).endVertex();
        buf.pos(posX+sizeX, posY-sizeY, 0).tex(left ? 1 : 0, 1).endVertex();
        buf.pos(posX+sizeX, posY, 0).tex(left ? 1 : 0, 0).endVertex();
        tes.draw();
    }
}
