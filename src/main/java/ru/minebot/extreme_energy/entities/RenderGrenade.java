package ru.minebot.extreme_energy.entities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import static org.lwjgl.opengl.GL11.*;


import javax.annotation.Nullable;

public class RenderGrenade extends Render<EntityGrenade> {


    public RenderGrenade(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityGrenade entity, double x, double y, double z, float entityYaw, float partialTicks) {
        bindEntityTexture(entity);
        Minecraft mc = Minecraft.getMinecraft();
        Tessellator tes = Tessellator.getInstance();
        BufferBuilder buf = tes.getBuffer();

        if (!entity.isDead) {
            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableBlend();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.pushMatrix();

            Vec3d from = entity.getPositionVector();
            Vec3d to = mc.player.getPositionEyes(partialTicks);
            double length2 = Math.sqrt(Math.pow(to.x - from.x, 2) + Math.pow(to.z - from.z, 2));
            Vec3d delta = to.subtract(from);

            glTranslated(x, y, z);
            glRotatef((float)(-Math.atan2(delta.z, delta.x) / Math.PI * 180), 0, 1, 0);
            glRotatef((float)(Math.atan2(delta.y, length2) / Math.PI * 180), 0, 0, 1);

            buf.begin(GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
            buf.pos(0, 0.16f, 0.16f).tex(0, 0).endVertex();
            buf.pos(0, -0.16f, 0.16f).tex(0, 1).endVertex();
            buf.pos(0, -0.16f, -0.16f).tex(1, 1).endVertex();
            buf.pos(0, 0.16f, -0.16f).tex(1, 0).endVertex();
            tes.draw();

            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityGrenade entity) {
        return entity.age >= 100 ? new ResourceLocation("meem:textures/other/grenade.png") : new ResourceLocation("meem:textures/items/grenade.png");
    }
}
