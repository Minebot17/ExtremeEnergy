package ru.minebot.extreme_energy.gui.tablet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.util.glu.GLU;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

public class ArticlesScene implements IScene {
    protected Slider leftSlider;
    protected Slider rightSlider;
    protected List<Element> buttonPages;
    protected List<Element> articlePages;
    protected List<Element> other;
    protected float height;
    protected float offsetButton;
    protected float offsetArticle;
    protected float mouseX;
    protected float mouseY;
    protected static int bit;

    public ArticlesScene(List<Article> articles, String chapterName, int chapter, int article){
        buttonPages = new ArrayList<>();
        articlePages = new ArrayList<>();
        other = new ArrayList<>();

        for (int i = 0; i < articles.size(); i++) {
            buttonPages.add(new ArticleButton(0, 0, chapter, i, articles.get(i).getName(), article == i));
        }

        float height = 0.05f;

        for (Element element : articles.get(article).getElements()){
            if (articlePages.size() == 0)
                articlePages.add(new Label(TabletRender.centerX, -0.01f, articles.get(article).getTitle(), Element.Align.CENTER, TextFormatting.BOLD, TabletRender.textColor));
            articlePages.add(element);
            height += element.getHeight();
        }
        this.height = height;

        other.add(new BackButton(0, -0.8f));
        other.add(new Label(0.2075f, -0.825f, chapterName, Element.Align.CENTER, TabletRender.textColor));
        other.add(new BackArticleButton(1.4805f, 0f));

        leftSlider = new StandartSlider(0.3f, 0, (int)Math.ceil(buttonPages.size()/8f), 0.9f, false);
        rightSlider = new StandartSlider(1.55f, 0, (int)Math.ceil(height/0.9f), 0.9f, true);
    }

    @Override
    public void draw(Tessellator tes, BufferBuilder buf, float mouseX, float mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        leftSlider.draw(tes, buf, mouseX, mouseY);
        rightSlider.draw(tes, buf, mouseX, mouseY);
        offsetButton = leftSlider.pageCount != 1 ? -(buttonPages.size()*0.1f-0.8f)/(leftSlider.height-leftSlider.height/leftSlider.pageCount)*leftSlider.thisPos : 0;
        offsetArticle = rightSlider.pageCount != 1 ? -(height-0.9f)/(rightSlider.height-rightSlider.height/rightSlider.pageCount)*rightSlider.thisPos : 0;

        bit = MinecraftForgeClient.reserveStencilBit();
        int flag = 1 << bit;

        glDisable(GL_TEXTURE_2D);
        glEnable(GL_STENCIL_TEST);
        glStencilFunc(GL_ALWAYS, flag, flag);
        glStencilOp(GL_ZERO, GL_ZERO, GL_REPLACE);
        glStencilMask(flag);
        glColorMask(false, false, false, false);
        glDepthMask(false);
        glClearStencil(0);
        glClear(GL_STENCIL_BUFFER_BIT);

        buf.begin(GL_POLYGON, DefaultVertexFormats.POSITION);
        buf.pos(0.3f, 0, 0).endVertex();
        buf.pos(0.3f, -0.9f, 0).endVertex();
        buf.pos(2, -0.9f, 0).endVertex();
        buf.pos(2, 0, 0).endVertex();
        tes.draw();

        buf.begin(GL_POLYGON, DefaultVertexFormats.POSITION);
        buf.pos(-2, 0, 0).endVertex();
        buf.pos(-2, -0.8f, 0).endVertex();
        buf.pos(0.3f, -0.8f, 0).endVertex();
        buf.pos(0.3f, 0, 0).endVertex();
        tes.draw();

        glEnable(GL_TEXTURE_2D);
        glStencilFunc(GL_EQUAL, flag, flag);
        glStencilMask(0);
        glColorMask(true, true, true, true);
        glDepthMask(true);

        glPushMatrix();
        glTranslatef(0, offsetButton, 0);
        for (int i = 0; i < buttonPages.size(); i++) {
            Element element = buttonPages.get(i);
            element.draw(tes, buf, mouseX, mouseY + i * 0.1f - offsetButton);
            glTranslatef(0, -element.getHeight(), 0);
        }
        glPopMatrix();

        glPushMatrix();
        glTranslatef(0, offsetArticle, 0);
        float height = 0;
        for (int i = 0; i < articlePages.size(); i++) {
            Element element = articlePages.get(i);
            element.draw(tes, buf, mouseX, mouseY + height - offsetArticle);
            glTranslatef(0, -element.getHeight(), 0);
            height += element.getHeight();
        }
        glTranslatef(0, height,0);
        height = 0;
        for (int i = 0; i < articlePages.size(); i++) {
            Element element = articlePages.get(i);
            element.postDraw(tes, buf, mouseX, mouseY + height - offsetArticle);
            glTranslatef(0, -element.getHeight(), 0);
            height += element.getHeight();
        }
        glPopMatrix();

        glDisable(GL_STENCIL_TEST);
        MinecraftForgeClient.releaseStencilBit(bit);

        RenderHelper.disableStandardItemLighting();
        glColor4f(1, 1, 1, 1);
        for(Element element : other)
            element.draw(tes, buf, mouseX, mouseY);
    }

    @Override
    public void onMouseDown() {
        if (mouseY < -0.8f && mouseY > -0.9f && mouseX > 0 && mouseX < 0.1f) {
            ((IClickable) other.get(0)).onMouseDown();
            return;
        }
        if (mouseX > 0 && mouseX < 1.6f && mouseY < 0 && mouseY > -0.9f) {
            if (leftSlider.isHover(mouseX, mouseY))
                leftSlider.onMouseDown();
            if (rightSlider.isHover(mouseX, mouseY))
                rightSlider.onMouseDown();

            for (Element element : other)
                if (element instanceof IClickable && ((IClickable) element).isHover(mouseX, mouseY))
                    ((IClickable) element).onMouseDown();

            mouseY -= offsetButton;
            for (int i = 0; i < buttonPages.size(); i++) {
                Element element = buttonPages.get(i);
                if (element instanceof IClickable && ((IClickable) element).isHover(mouseX, mouseY + i * 0.1f))
                    ((IClickable) element).onMouseDown();
            }

            mouseY -= offsetArticle - offsetButton;
            float height = 0;
            for (int i = 0; i < articlePages.size(); i++) {
                Element element = articlePages.get(i);
                if (element instanceof IClickable && ((IClickable) element).isHover(mouseX, mouseY + height))
                    ((IClickable) element).onMouseDown();
                height += element.getHeight();
            }
        }
    }

    @Override
    public void onMouseMove() {
        if (mouseX > 0 && mouseX < 1.6f && mouseY < 0 && mouseY > -0.9f) {
            if (leftSlider.isHover(mouseX, mouseY))
                leftSlider.onMouseMove();
            if (rightSlider.isHover(mouseX, mouseY))
                rightSlider.onMouseMove();

            for (Element element : other)
                if (element instanceof IClickable && ((IClickable) element).isHover(mouseX, mouseY))
                    ((IClickable) element).onMouseMove();

            mouseY -= offsetButton;
            for (int i = 0; i < buttonPages.size(); i++) {
                Element element = buttonPages.get(i);
                if (element instanceof IClickable && ((IClickable) element).isHover(mouseX, mouseY + i * 0.1f))
                    ((IClickable) element).onMouseMove();
            }

            mouseY -= offsetArticle - offsetButton;
            float height = 0;
            for (int i = 0; i < articlePages.size(); i++) {
                Element element = articlePages.get(i);
                if (element instanceof IClickable && ((IClickable) element).isHover(mouseX, mouseY + height))
                    ((IClickable) element).onMouseMove();
                height += element.getHeight();
            }
        }
    }

    @Override
    public void onMouseUp() {
        if (mouseY < -0.8f && mouseY > -0.9f && mouseX > 0 && mouseX < 0.1f) {
            ((IClickable) other.get(0)).onMouseUp();
            return;
        }
        if (mouseX > 0 && mouseX < 1.6f && mouseY < 0 && mouseY > -0.9f) {
            if (leftSlider.isHover(mouseX, mouseY))
                leftSlider.onMouseUp();
            if (rightSlider.isHover(mouseX, mouseY))
                rightSlider.onMouseUp();

            for (Element element : other)
                if (element instanceof IClickable && ((IClickable) element).isHover(mouseX, mouseY))
                    ((IClickable) element).onMouseUp();

            mouseY -= offsetButton;
            for (int i = 0; i < buttonPages.size(); i++) {
                Element element = buttonPages.get(i);
                if (element instanceof IClickable && ((IClickable) element).isHover(mouseX, mouseY + i * 0.1f))
                    ((IClickable) element).onMouseUp();
            }

            mouseY -= offsetArticle - offsetButton;
            float height = 0;
            for (int i = 0; i < articlePages.size(); i++) {
                Element element = articlePages.get(i);
                if (element instanceof IClickable && ((IClickable) element).isHover(mouseX, mouseY + height))
                    ((IClickable) element).onMouseUp();
                height += element.getHeight();
            }
        }
    }

    @Override
    public void onWheelScrolled(int delta) {
        if (mouseX > 0 && mouseX < 1.6f && mouseY < 0 && mouseY > -0.9f) {
            if (mouseX < 0.35f && mouseX > 0 && mouseY > -0.8f && mouseY < 0)
                leftSlider.onWheelScrolled(delta);
            else if (mouseX < 1.6f && mouseX > 0.35f && mouseY > -0.9f && mouseY < 0)
                rightSlider.onWheelScrolled(delta);
        }
    }

    @Override
    public void onKeyPressed() {
        for(Element element : other)
            if (element instanceof IKey)
                ((IKey) element).onKeyDown();

        for(Element element : buttonPages)
            if (element instanceof IKey)
                ((IKey) element).onKeyDown();

        for(Element element : articlePages)
            if (element instanceof IKey)
                ((IKey) element).onKeyDown();
    }

    public List<LabelWithLinks> splitLabels(List<LabelWithLinks> list, LabelWithLinks label, float yOffset){
        if (label.height > 0.9f - yOffset){
            int count = (int)((0.9f - yOffset)/Label.oneCharHeight);
            list.add(label.splitLabel(0, count));
            splitLabels(list, label.splitLabel(count), 0);
        }
        else
            list.add(label);
        return list;
    }
}
