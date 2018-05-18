package ru.minebot.extreme_energy.gui.tablet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.Language;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;

import java.io.*;
import java.util.*;

@SideOnly(Side.CLIENT)
public class TabletRender {
    public static final float deltaForMove = 1;
    public static final float soundVolume = 0.2f;
    public static final float textWidth = 1.14f;
    public static final int textColor = 0x262626;
    public static final float leftX = 0.38f;
    public static final float centerX = 0.95f;
    public static final float rightX = 1.51f;

    private static Language language;
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static List<Chapter> chapters;
    private static boolean isUngrabed;
    private static List<List<HashMap<String, String>>> langMap;
    protected static IScene scene;
    private static Stack<Integer[]> articleBuffer;
    private static Integer[] lastArticle;
    protected static float pitch;
    protected static float mouseY;

    @SideOnly(Side.CLIENT)
    public static void init(){
        Minecraft.getMinecraft().gameSettings.fboEnable = true;
        boolean small = mc.fontRenderer.getUnicodeFlag();
        Label.textSize = small ? 0.0052f : 0.0038f;
        language = mc.getLanguageManager().getCurrentLanguage();
        chapters = new ArrayList<>();
        langMap = new ArrayList<>();
        for (int i = 0; i < 6; i++){
            langMap.add(new ArrayList<>());
            String chapterName = I18n.format("tablet.chapter" + i);
            ResourceLocation[] chapterIcons = new ResourceLocation[]{
                    new ResourceLocation("meem:tablet/c" + i + "/icon_normal.png"),
                    new ResourceLocation("meem:tablet/c" + i + "/icon_hover.png"),
                    new ResourceLocation("meem:tablet/c" + i + "/icon_click.png")
            };
            List<Article> articles = new ArrayList<>();
            int j = 0;
            while(true){
                try{
                    List<String> lines = ModUtils.getLines(language, i, j);
                    String name = lines.get(0);
                    String title = lines.get(1);
                    langMap.get(i).add(ModUtils.getLangMap(lines));
                    IResource resource = mc.getResourceManager().getResource(new ResourceLocation("meem:tablet/c" + i + "/a" + j + ".json"));
                    InputStreamReader reader = new InputStreamReader(resource.getInputStream());
                    JsonParser parser = new JsonParser();
                    JsonObject json = parser.parse(reader).getAsJsonObject();
                    JsonArray array = json.getAsJsonArray("elements");
                    String key = "";
                    if(json.has("key"))
                        key = json.get("key").getAsString().toLowerCase();
                    List<Element> elements = new ArrayList<>();
                    for (JsonElement obj : array) {
                        try {
                            elements.add(Element.getElement((JsonObject) obj, i, j));
                        } catch (Exception e) {
                            System.out.println("Error in " + i + " chapter " + j + " article!");
                            e.printStackTrace();
                        }
                    }

                    Element[] a = new Element[elements.size()];
                    articles.add(new Article(name, title, elements.toArray(a), key));
                    j++;
                }
                catch (IOException e){
                    break;
                }
            }
            chapters.add(new Chapter(chapterName, chapterIcons, articles));
        }
        scene = new ChaptersScene(chapters);
    }

    @SideOnly(Side.CLIENT)
    public static void setArticle(int chapter, int article){
        if (chapter == -1)
            scene = new ChaptersScene(chapters);
        else {
            scene = new ArticlesScene(chapters.get(chapter).getArticles(), chapters.get(chapter).getName(), chapter, article);
            articleBuffer = new Stack<>();
            lastArticle = new Integer[]{chapter,article};
        }
    }

    @SideOnly(Side.CLIENT)
    public static void setArticle(String key){
        for (int i = 0; i < chapters.size(); i++)
            for (int j= 0; j < chapters.get(i).getArticles().size(); j++)
                if (chapters.get(i).getArticles().get(j).getKey().contains(key.toLowerCase())) {
                    scene = new ArticlesScene(chapters.get(i).getArticles(), chapters.get(i).getName(), i, j);
                    articleBuffer.push(lastArticle);
                    lastArticle = new Integer[]{i,j};
                    break;
                }
    }

    @SideOnly(Side.CLIENT)
    public static void returnArticle(){
        if (articleBuffer.size() != 0) {
            Integer[] index = articleBuffer.pop();
            scene = new ArticlesScene(chapters.get(index[0]).getArticles(), chapters.get(index[0]).getName(), index[0], index[1]);
            lastArticle = index;
        }
    }

    public static HashMap<String, String> getLangMap(int chapter, int article){
        return langMap.get(chapter).get(article);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderItem(RenderSpecificHandEvent e){
        if (e.getItemStack().getItem() == ModItems.tablet && e.getHand() == EnumHand.MAIN_HAND){
            e.setCanceled(true);

            Tessellator tes = Tessellator.getInstance();
            ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

            glDisable(GL_DEPTH_TEST);
            RenderHelper.enableGUIStandardItemLighting();
            pitch = e.getInterpolatedPitch();
            if (e.getInterpolatedPitch() < 59)
                glRotated(e.getInterpolatedPitch() - 60, 1, 0, 0);
            renderArms(); // TODO: fix resolution.getScaleFactor() == 1 size
            glScalef(resolution.getScaleFactor(), resolution.getScaleFactor(), 1);
            drawTablet(tes, resolution);

            drawElements(resolution);

            if (Mouse.getY() > Display.getHeight() - Display.getHeight()/10 || mc.currentScreen != null) {
                if (isUngrabed) {
                    if (mc.currentScreen == null)
                        mc.setIngameFocus();
                    isUngrabed = false;
                }
                return;
            }

            if (!isUngrabed && e.getInterpolatedPitch() > 59) {
                mc.setIngameNotInFocus();
                Mouse.setCursorPosition(Display.getWidth()/2, Display.getHeight() - Display.getHeight()/10);
                isUngrabed = true;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void drawTablet(Tessellator tes, ScaledResolution resolution){
        BufferBuilder buf = tes.getBuffer();
        glPushMatrix();
        GlStateManager.disableTexture2D();
        double frameSize = 1.95D;

        // Screen
        buf.begin(GL_POLYGON, DefaultVertexFormats.POSITION);
        buf.pos(-0.8D, -0.45D, -2D).endVertex();
        buf.pos(0.8D, -0.45D, -2D).endVertex();
        buf.pos(0.8D, 0.45D, -2D).endVertex();
        buf.pos(-0.8D, 0.45D, -2D).endVertex();
        tes.draw();

        //glColor3f(1, 0, 0);
        // Side in
        buf.begin(GL_POLYGON, DefaultVertexFormats.POSITION_COLOR);
        buf.pos(-0.8D, -0.45D, -2D).color(0.6f, 0.6f, 0.6f, 1f).endVertex();
        buf.pos(-0.8D, 0.45D, -2D).color(0.6f, 0.6f, 0.6f, 1f).endVertex();
        buf.pos(-0.8D, 0.45D, -frameSize).color(0.3f, 0.3f, 0.3f, 1f).endVertex();
        buf.pos(-0.8D, -0.45D, -frameSize).color(0.3f, 0.3f, 0.3f, 1f).endVertex();
        tes.draw();

        buf.begin(GL_POLYGON, DefaultVertexFormats.POSITION_COLOR);
        buf.pos(0.8D, -0.45D, -frameSize).color(0.3f, 0.3f, 0.3f, 1f).endVertex();
        buf.pos(0.8D, 0.45D, -frameSize).color(0.3f, 0.3f, 0.3f, 1f).endVertex();
        buf.pos(0.8D, 0.45D, -2D).color(0.6f, 0.6f, 0.6f, 1f).endVertex();
        buf.pos(0.8D, -0.45D, -2D).color(0.6f, 0.6f, 0.6f, 1f).endVertex();
        tes.draw();

        buf.begin(GL_POLYGON, DefaultVertexFormats.POSITION_COLOR);
        buf.pos(0.8D, 0.45D, -frameSize).color(0.3f, 0.3f, 0.3f, 1f).endVertex();
        buf.pos(-0.8D, 0.45D, -frameSize).color(0.3f, 0.3f, 0.3f, 1f).endVertex();
        buf.pos(-0.8D, 0.45D, -2D).color(0.6f, 0.6f, 0.6f, 1f).endVertex();
        buf.pos(0.8D, 0.45D, -2D).color(0.6f, 0.6f, 0.6f, 1f).endVertex();
        tes.draw();

        buf.begin(GL_POLYGON, DefaultVertexFormats.POSITION_COLOR);
        buf.pos(0.8D, -0.45D, -2D).color(0.6f, 0.6f, 0.6f, 1f).endVertex();
        buf.pos(-0.8D, -0.45D, -2D).color(0.6f, 0.6f, 0.6f, 1f).endVertex();
        buf.pos(-0.8D, -0.45D, -frameSize).color(0.3f, 0.3f, 0.3f, 1f).endVertex();
        buf.pos(0.8D, -0.45D, -frameSize).color(0.3f, 0.3f, 0.3f, 1f).endVertex();
        tes.draw();
        GlStateManager.enableTexture2D();

        // Forward
        mc.getTextureManager().bindTexture(new ResourceLocation("meem:tablet/textures/leftframe.png"));
        buf.begin(GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
        buf.pos(-0.8D, -0.45D, -frameSize).tex(1, 1).endVertex();
        buf.pos(-0.8D, 0.45D, -frameSize).tex(1, 0).endVertex();
        buf.pos(-0.85D, 0.45D, -frameSize).tex(0, 0).endVertex();
        buf.pos(-0.85D, -0.45D, -frameSize).tex(0, 1).endVertex();
        tes.draw();

        mc.getTextureManager().bindTexture(new ResourceLocation("meem:tablet/textures/rightframe.png"));
        buf.begin(GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
        buf.pos(0.8D, -0.45D, -frameSize).tex(0, 1).endVertex();
        buf.pos(0.85D, -0.45D, -frameSize).tex(1, 1).endVertex();
        buf.pos(0.85D, 0.45D, -frameSize).tex(1, 0).endVertex();
        buf.pos(0.8D, 0.45D, -frameSize).tex(0, 0).endVertex();
        tes.draw();

        mc.getTextureManager().bindTexture(new ResourceLocation("meem:tablet/textures/sideframe.png"));
        buf.begin(GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
        buf.pos(-0.85D, 0.5D, -frameSize).tex(0, 0).endVertex();
        buf.pos(-0.85D, 0.45D, -frameSize).tex(0, 1).endVertex();
        buf.pos(0.85D, 0.45D, -frameSize).tex(1, 1).endVertex();
        buf.pos(0.85D, 0.5D, -frameSize).tex(1, 0).endVertex();
        tes.draw();

        buf.begin(GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
        buf.pos(-0.85D, -0.45D, -frameSize).tex(0, 1).endVertex();
        buf.pos(-0.85D, -0.5D, -frameSize).tex(0, 0).endVertex();
        buf.pos(0.85D, -0.5D, -frameSize).tex(1, 0).endVertex();
        buf.pos(0.85D, -0.45D, -frameSize).tex(1, 1).endVertex();
        tes.draw();

        glPopMatrix();
    }

    @SideOnly(Side.CLIENT)
    private void renderArms(){
        Render<AbstractClientPlayer> R = mc.getRenderManager().getEntityRenderObject(mc.player);
        RenderPlayer render = (RenderPlayer)R;
        glPushMatrix();
        glTranslated(0, -0.8f, -0.5f);
        glRotatef(-35, 1,0,0);
        mc.getTextureManager().bindTexture(mc.player.getLocationSkin());
        render.renderRightArm(mc.player);
        render.renderLeftArm(mc.player);
        glPopMatrix();
    }

    @SideOnly(Side.CLIENT)
    private void drawElements(ScaledResolution resolution){
        float x = (1.4f * (2f*Mouse.getX() - Display.getWidth()))/(Display.getHeight()*resolution.getScaleFactor()) + 0.8f;
        float y = 2.8f*(Display.getHeight() - Mouse.getY() - 0.36f*Display.getHeight()/resolution.getScaleFactor())/(Display.getHeight()*resolution.getScaleFactor());
        mouseY = y;

        glTranslated(-0.8f, 0.45f, -1.999);
        scene.draw(Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), x, -y);
        glTranslated(0.8f, -0.45f, 1.999);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void keyEvent(InputEvent.KeyInputEvent e){
        if (isUngrabed){
            scene.onKeyPressed();
        }
    }

    private int lastPosX;
    private int lastPosY;
    private boolean isMouseDown;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void mouse(MouseEvent e){
        if (isUngrabed){
            e.setCanceled(true);
            if (Mouse.getEventDWheel() != 0)
                scene.onWheelScrolled(Mouse.getDWheel());

            if (Mouse.isButtonDown(0) && !isMouseDown){
                scene.onMouseDown();
                isMouseDown = true;
                lastPosX = Mouse.getX();
                lastPosY = Mouse.getY();
                mc.setIngameNotInFocus();
                Mouse.setCursorPosition(lastPosX, lastPosY);
            }
            else if (!Mouse.isButtonDown(0) && isMouseDown){
                scene.onMouseUp();
                isMouseDown = false;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent e){
        if (mc.getLanguageManager().getCurrentLanguage() != language)
            init();

        if (isUngrabed && mc.player.inventory.getCurrentItem().getItem() != ModItems.tablet){
            if (mc.currentScreen == null)
                mc.setIngameFocus();
            isUngrabed = false;
        }

        if (isUngrabed && isMouseDown){
            if (Math.sqrt(Math.abs(lastPosX - Mouse.getX()) + Math.abs(lastPosY - Mouse.getY())) > deltaForMove){
                scene.onMouseMove();
                lastPosX = Mouse.getX();
                lastPosY = Mouse.getY();
            }
        }
    }
}
