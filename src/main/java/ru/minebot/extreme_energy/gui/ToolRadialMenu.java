package ru.minebot.extreme_energy.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

public class ToolRadialMenu extends Gui{
    protected final RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
    private int selected = -1;
    public int itemSelect = -1;
    public int lastX = 0;
    public int lastY = 0;

    public void draw(Minecraft mc){
        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        float toDec = (float) Math.PI/180f;
        int mouseX = Mouse.getX() - mc.displayWidth/2;
        int mouseY = Mouse.getY() - mc.displayHeight/2;

        if (dist(mouseX, mouseY) > 200)
            Mouse.setCursorPosition(lastX + mc.displayWidth/2, lastY + mc.displayHeight/2);
        lastX = mouseX;
        lastY = mouseY;

        for (int i = 0; i < 6; i++){
            GL11.glRotatef(60, 0,0,1);
            if (dist(mouseX, mouseY) > 100 && dist(mouseX, mouseY) < 220){
                float angle = (float)Math.atan2(mouseX, mouseY);
                int angleDec = (int)((angle/toDec+330f)%360f);
                selected = (int)(angleDec/60f);
            }
            else
                selected = -1;

            int offset;
            if (i == selected) {
                mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/gui/implantshud/segmentactive.png"));
                offset = -110;
            }
            else {
                mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/gui/implantshud/segment.png"));
                offset = -100;
            }
            drawModalRectWithCustomSizedTexture(-49, offset, 0, 0, 99, 47, 99, 47);
        }

        for (int i = 1; i < 6; i++){
            int itemAngle = (i * 60 + 270)%360;
            if (selected != -1)
                itemSelect = (selected + 1)%6;
            else
                itemSelect = -1;

            String[] names = new String[]{
                    "pickaxe",
                    "shovel",
                    "axe",
                    "hoe",
                    "shears"
            };
            GL11.glPushMatrix();
            GL11.glTranslatef((float) Math.cos(itemAngle*toDec) * (i == itemSelect ? 100f : 80f), (float) Math.sin(itemAngle*toDec) * (i == itemSelect ? 100f : 80f), 0);
            mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/items/tools/energy_"+names[i-1]+".png"));
            drawModalRectWithCustomSizedTexture(-8, -8,0,0, 16, 16, 16,16);
            GL11.glColor3f(1, 1, 1);
            GL11.glPopMatrix();
        }
        mc.fontRenderer.drawString("Auto", -10, itemSelect == 0 ? -95 : -83, 9691135);

        mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/gui/implantshud/cursor.png"));
        drawModalRectWithCustomSizedTexture(mouseX/2 - 4, -mouseY/2 - 4,0,0, 8, 8, 8,8);

        GL11.glDisable(GL_BLEND);
        GlStateManager.enableAlpha();
    }

    private float dist(int x, int y){
        return (float)Math.sqrt(x*x+y*y);
    }
}
