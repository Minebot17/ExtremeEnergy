package ru.minebot.extreme_energy.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.SoundEvents;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;

public abstract class ColorPicker extends Gui {
    protected int posX;
    protected int posY;
    protected int width;
    protected int height;
    protected int[][] colors;
    protected int[][][] colorsRGB;

    protected int countX;
    protected int countY;
    protected int widthFragment;
    protected Point hover;

    public ColorPicker(int posX, int posY, int width, int[][] colors) {
        this.posX = posX;
        this.posY = posY;
        this.colors = colors;
        this.countY = colors.length;
        this.countX = colors[0].length;
        this.widthFragment = Math.round((float)width/(float)countX);
        this.width = countX*widthFragment;
        this.height = countY*widthFragment;
        colorsRGB = new int[countY][countX][3];
        for (int y = 0; y < countY; y++)
            for (int x = 0; x < countX; x++){
                int hex = colors[y][x];
                int r = (hex & 0xFF0000) >> 16;
                int g = (hex & 0xFF00) >> 8;
                int b = (hex & 0xFF);
                colorsRGB[y][x][0] = r;
                colorsRGB[y][x][1] = g;
                colorsRGB[y][x][2] = b;
            }
    }

    public void draw(Minecraft mc, int mouseX, int mouseY){
        hover = posX < mouseX && posX + width > mouseX && posY < mouseY && posY + height > mouseY ? new Point((mouseX-posX)/widthFragment, (mouseY-posY)/widthFragment) : null;
        GlStateManager.resetColor();
        GlStateManager.disableTexture2D();
        Tessellator tes = Tessellator.getInstance();
        BufferBuilder buf = tes.getBuffer();
        for (int y = 0; y < countY; y++)
            for (int x = 0; x < countX; x++) {
                float r = colorsRGB[y][x][0]/255f;
                float g = colorsRGB[y][x][1]/255f;
                float b = colorsRGB[y][x][2]/255f;
                buf.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_COLOR);
                buf.pos(posX+x*widthFragment, posY+y*widthFragment, 0).color(r, g, b, 1f).endVertex();
                buf.pos(posX+x*widthFragment, posY+(y+1)*widthFragment, 0).color(r, g, b, 1f).endVertex();
                buf.pos(posX+(x+1)*widthFragment, posY+(y+1)*widthFragment, 0).color(r, g, b, 1f).endVertex();
                buf.pos(posX+(x+1)*widthFragment, posY+y*widthFragment, 0).color(r, g, b, 1f).endVertex();
                tes.draw();
            }
        GlStateManager.enableTexture2D();
    }

    public void mouseDown(){
        if (hover != null){
            playPressSound(Minecraft.getMinecraft().getSoundHandler());
            onButtonClicked(colors[hover.getY()][hover.getX()]);
        }
    }

    public int getHeight(){
        return height;
    }

    private void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public abstract void onButtonClicked(int color);
}
