package ru.minebot.extreme_energy.gui.tablet;

import com.google.gson.JsonObject;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.crash.CrashReport;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec2f;
import static org.lwjgl.opengl.GL11.*;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import ru.minebot.extreme_energy.recipes.managers.AssemblerRecipes;
import ru.minebot.extreme_energy.recipes.managers.CrusherRecipes;

import java.util.*;

public class Craft extends Element implements IClickable{
    protected Type type;
    protected Align align;
    protected ItemStack output;

    protected Label pages;
    protected Button leftButton;
    protected Button rightButton;
    protected List<List<ItemStack>> crafts;
    protected int thisCraft;
    protected float mouseX;
    protected float mouseY;
    protected boolean isLeftHover;
    protected boolean isRightHover;
    protected int slotHover;
    protected ItemStack stackHover;
    protected boolean animation;
    protected long thisTime;

    public Craft(float posX, float posY, Type type, Align align, ItemStack output) {
        super(posX, posY);
        this.type = type;
        this.align = align;
        this.output = output;
        this.animation = true;

        float labelWidth = font.getStringWidth("999/999")/(1f/Label.textSize);
        float posXLabel = align == Align.LEFT ? posX + 0.1f + labelWidth/2 : align == Align.CENTER ? posX : posX - 0.1f - labelWidth/2f;
        float posXLeft = align == Align.LEFT ? posX : align == Align.CENTER ? posX - labelWidth/2 - 0.1f : posX - labelWidth - 0.2f;
        float posXRight = align == Align.LEFT ? posX + 0.1f + labelWidth : align == Align.CENTER ? posX + labelWidth/2 : posX - 0.1f;
        crafts = type.getItems(output);
        pages = new NotLangLabel(posXLabel, -getHeight() + 0.06f, "999/999", Align.CENTER, null, TabletRender.textColor);
        leftButton = new CraftButton(posXLeft, -getHeight() + 0.08f, true) {
            @Override
            public void action() {
                setPage(thisCraft - 1);
            }
        };
        rightButton = new CraftButton(posXRight, -getHeight() + 0.08f, false) {
            @Override
            public void action() {
                setPage(thisCraft + 1);
            }
        };
        setPage(0);
    }

    @Override
    public void draw(Tessellator tes, BufferBuilder buf, float mouseX, float mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.isLeftHover = isHoverButton(true);
        this.isRightHover = isHoverButton(false);
        this.slotHover = isHoverSlot();

        float localX = align == Align.LEFT ? posX : align == Align.CENTER ? posX - type.width/2f : posX - type.width;
        glColor3f(1, 1, 1);
        mc.getTextureManager().bindTexture(type.texture);
        buf.begin(GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
        buf.pos(localX, posY, 0).tex(0, 0).endVertex();
        buf.pos(localX, posY - type.height, 0).tex(0, 1).endVertex();
        buf.pos(localX + type.width, posY - type.height, 0).tex(1, 1).endVertex();
        buf.pos(localX + type.width, posY, 0).tex(1, 0).endVertex();
        tes.draw();

        if (crafts.size() != 1) {
            leftButton.draw(tes, buf, mouseX, mouseY);
            rightButton.draw(tes, buf, mouseX, mouseY);
        }

        glPushMatrix();
        Vec2f[] result = type.result;
        Vec2f[] poses = type.slots;
        List<ItemStack> toRender = crafts.get(thisCraft);
        for (int i = 0; i < toRender.size(); i++) {
            toRender.get(i).setItemDamage(0);
            renderItem(poses[i].x + localX, poses[i].y, toRender.get(i));
        }

        int c1;
        int c2 = 0;
        if (output.getCount() != 1) {
            c1 = output.getCount() / 2;
            c2 = output.getCount() - c1;
        } else
            c1 = 1;

        for (int i = 0; i < result.length; i++) {
            ItemStack stack = output.copy();
            if (type.result.length != 1)
                stack.setCount(i == 0 ? c1 : c2);
            if (stack.getCount() != 0)
                renderItem(result[i].x + localX, result[i].y, stack);
        }
        glPopMatrix();

        if (crafts.size() != 1)
            pages.draw(tes, buf, mouseX, mouseY);

        if (crafts.size() != 1 && animation && mc.world.getTotalWorldTime()%30==0 && thisTime != mc.world.getTotalWorldTime()){
            thisTime = mc.world.getTotalWorldTime();
            int index = thisCraft + 1;
            if (index >= crafts.size())
                index = 0;
            setPage(index);
        }
    }

    @Override
    public void postDraw(Tessellator tes, BufferBuilder buf, float mouseX, float mouseY){
        if (slotHover != -1){
            if (type.slots.length > slotHover) {
                try {
                    stackHover = crafts.get(thisCraft).get(slotHover);
                }
                catch (IndexOutOfBoundsException e){ stackHover = ItemStack.EMPTY; }
            }
            else
                stackHover = output;

            if (!stackHover.isEmpty() && TabletRender.mouseY > 0 && TabletRender.mouseY < 0.9f){
                glDisable(GL_DEPTH_TEST);
                drawHoveringText(mouseX - 0.035f, mouseY - 0.085f, stackHover);
                glEnable(GL_DEPTH_TEST);
            }
        }
    }

    @Override
    public void onMouseDown() {
        if (crafts.size() != 1) {
            if (isLeftHover) {
                animation = false;
                leftButton.onMouseDown();
            }
            if (isRightHover) {
                animation = false;
                rightButton.onMouseDown();
            }
        }
        if (slotHover != -1 && !stackHover.isEmpty() && stackHover.getItem() != output.getItem()){
            TabletRender.setArticle(stackHover.getItem().getRegistryName().toString().substring(5));
        }
    }

    @Override
    public void onMouseUp() {
        if (crafts.size() != 1) {
            if (isLeftHover)
                leftButton.onMouseUp();
            if (isRightHover)
                rightButton.onMouseUp();
        }
    }

    @Override
    public void onMouseMove() {
        if (crafts.size() != 1) {
            if (isLeftHover)
                leftButton.onMouseMove();
            if (isRightHover)
                rightButton.onMouseMove();
        }
    }

    @Override
    public boolean isHover(float mouseX, float mouseY) {
        float localX = align == Align.LEFT ? posX : align == Align.CENTER ? posX - type.width/2f : posX - type.width;
        return mouseX > localX && mouseX < localX + type.width && mouseY < posY && mouseY > posY - getHeight();
    }

    @Override
    public float getHeight() {
        return type.height + (crafts.size() != 1 ? 0.1f : 0);
    }

    private boolean isHoverButton(boolean left){
        float labelWidth = font.getStringWidth("999/999")/(1f/Label.textSize);
        float posXLeft = align == Align.LEFT ? posX : align == Align.CENTER ? posX - labelWidth/2 - 0.1f : posX - labelWidth - 0.2f;
        float posXRight = align == Align.LEFT ? posX + 0.1f + labelWidth : align == Align.CENTER ? posX + labelWidth/2 : posX - 0.1f;
        float localX = left ? posXLeft : posXRight;
        float localY = -getHeight() + 0.08f;

        return mouseX > localX && mouseX < localX + 0.1f && mouseY < localY && mouseY > localY - 0.07f;
    }

    private int isHoverSlot(){
        int i = 0;
        float localX = align == Align.LEFT ? posX : align == Align.CENTER ? posX - type.width/2f : posX - type.width;
        while (i < type.slots.length) {
            float x = type.slots[i].x + localX;
            float y = type.slots[i].y + posY;
            if (mouseX > x && mouseX < x + 0.08f && mouseY < y && mouseY > y - 0.08f)
                return i;
            i++;
        }

        int c1 = 0;
        int c2 = 0;
        if (output.getCount() != 1) {
            c1 = output.getCount() / 2;
            c2 = output.getCount() - c1;
        } else
            c1 = 1;

        for (int j = 0; j < type.result.length; j++) {
            float x = type.result[j].x + localX;
            float y = type.result[j].y + posY;
            if ((j == 0 ? c1 != 0 : c2 != 0) && mouseX > x && mouseX < x + 0.08f && mouseY < y && mouseY > y - 0.08f)
                return i;
            i++;
        }
        return -1;
    }

    private void setPage(int index){
        if (index >= 0 && index < crafts.size()){
            thisCraft = index;
            pages.text = Collections.singletonList(index + 1 + "/" + crafts.size());

            if (type == Type.CRUSHER) {
                int count = CrusherRecipes.getCount(crafts.get(thisCraft).get(0));
                output.setCount(count);
            }
            else if (type == Type.WORKBENCH){
                for(IRecipe recipe : CraftingManager.REGISTRY) {
                    boolean yes = false;
                    if (recipe instanceof ShapedRecipes && Arrays.asList(((ShapedRecipes) recipe).recipeItems).equals(crafts.get(thisCraft)))
                        yes = true;
                    else if (recipe instanceof ShapelessRecipes && ((ShapelessRecipes) recipe).recipeItems.equals(crafts.get(thisCraft)))
                        yes = true;
                    if (recipe.getRecipeOutput().getItem() == output.getItem() && yes)
                        output.setCount(recipe.getRecipeOutput().getCount());
                }
            }
        }
    }

    private void renderItem(float x, float y, ItemStack item){
        glPushMatrix();
        glTranslated(0, 0, -0.00001f);
        glScalef(0.005f, -0.005f, 0.00001f);
        glEnable(GL_DEPTH_TEST);
        mc.getRenderItem().renderItemIntoGUI(item, (int)(x*200), -(int)(y*200));
        mc.getRenderItem().renderItemOverlays(font, item, (int)(x*200), -(int)(y*200));
        glDisable(GL_DEPTH_TEST);
        glPopMatrix();
    }

    private void drawHoveringText(float x, float y, ItemStack item){
        glDisable(GL_STENCIL_TEST);
        List<String> tooltip = item.getTooltip(mc.player, ITooltipFlag.TooltipFlags.NORMAL);
        try {
            List<ItemStack> craft = AssemblerRecipes.getElements(item).get(0);
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)){
                tooltip.add(TextFormatting.YELLOW + craft.get(0).getDisplayName());
                tooltip.add(TextFormatting.YELLOW + craft.get(1).getDisplayName());
            }
            else
                tooltip.add(TextFormatting.GRAY + I18n.format("tablet.craft.tooltip"));
        }
        catch (IndexOutOfBoundsException ignored){}

        glPushMatrix();
        glTranslatef(0, 0, -0.001f);
        glScalef(0.004f, -0.004f, 0.00001f);
        GuiUtils.drawHoveringText(item, tooltip, (int)(x*250), -(int)(y*250), Display.getWidth(), Display.getHeight(), 200, font);
        glPopMatrix();
        glEnable(GL_STENCIL_TEST);
    }

    public static Element loadFromJson(JsonObject json, int chapter, int article){
        String str1 = json.get("style").getAsString();
        Type type = str1.equals("w") ? Type.WORKBENCH : str1.equals("f") ? Type.FURNACE : str1.equals("a") ? Type.ASSEMBLER : Type.CRUSHER;
        Align align = Align.CENTER;
        if (json.has("align"))
            align = json.get("align").getAsString().equals("l") ? Align.LEFT : json.get("align").getAsString().equals("r") ? Align.RIGHT : Align.CENTER;
        ItemStack stack = ItemStack.EMPTY;
        try {
            Item item = Item.REGISTRY.getObject(new ResourceLocation("meem:" + json.get("item").getAsString().toLowerCase()));
            if (item == null)
                throw new NullPointerException("Output is invalid");
            stack = new ItemStack(item);
        }
        catch (NullPointerException e){ mc.crashed(new CrashReport("Output is invalid", e)); }
        return new Craft(align == Align.LEFT ? TabletRender.leftX : align == Align.CENTER ? TabletRender.centerX : TabletRender.rightX, 0, type, align, stack);
    }

    public enum Type {
        WORKBENCH(0, "meem:tablet/crafts/workbench.png", 0.58f, 0.27f, new Vec2f[]{
                new Vec2f(0.005f, -0.005f),
                new Vec2f(0.095f, -0.005f),
                new Vec2f(0.185f, -0.005f),
                new Vec2f(0.005f, -0.095f),
                new Vec2f(0.095f, -0.095f),
                new Vec2f(0.185f, -0.095f),
                new Vec2f(0.005f, -0.185f),
                new Vec2f(0.095f, -0.185f),
                new Vec2f(0.185f, -0.185f)
        }, new Vec2f(0.475f, -0.095f)),
        FURNACE(1, "meem:tablet/crafts/furnace.png", 0.41f, 0.13f, new Vec2f[]{new Vec2f(0.005f, -0.025f)}, new Vec2f(0.305f, -0.025f)),
        ASSEMBLER(2, "meem:tablet/crafts/assembler.png", 0.51f, 0.13f, new Vec2f[]{
                new Vec2f(0.005f, -0.025f),
                new Vec2f(0.105f, -0.025f)
        }, new Vec2f(0.405f, -0.025f)),
        CRUSHER(3, "meem:tablet/crafts/crusher.png", 0.41f, 0.265f, new Vec2f[]{new Vec2f(0.005f, -0.095f)},
                new Vec2f(0.305f, -0.025f),
                new Vec2f(0.305f, -0.16f)
        );

        private int index;
        public ResourceLocation texture;
        public float width;
        public float height;
        public Vec2f[] slots;
        public Vec2f[] result;

        Type(int index, String texture, float width, float height, Vec2f[] slots, Vec2f... result) {
            this.index = index;
            this.texture = new ResourceLocation(texture);
            this.width = width;
            this.height = height;
            this.slots = slots;
            this.result = result;
        }

        public List<List<ItemStack>> getItems(ItemStack result){
            if (index == 0){
                List<List<Ingredient>> list = new ArrayList<>();
                for(IRecipe recipe : CraftingManager.REGISTRY)
                    if (recipe.getRecipeOutput().getItem() == result.getItem())
                        list.add(recipe.getIngredients());

                List<List<ItemStack>> items = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    items.add(new ArrayList<>());
                    for (int j = 0; j < list.get(i).size(); j++) {
                        if (list.get(i).get(j).getMatchingStacks().length != 0)
                            items.get(i).add(list.get(i).get(j).getMatchingStacks()[0]);
                        else
                            items.get(i).add(ItemStack.EMPTY);
                    }
                }

                return items;
            }
            else if (index == 1){
                List<List<ItemStack>> list = new ArrayList<>();
                Map<ItemStack, ItemStack> map = FurnaceRecipes.instance().getSmeltingList();
                List<ItemStack> keys = new ArrayList(map.keySet());
                List<ItemStack> values = new ArrayList(map.values());
                for (int i = 0; i < map.size(); i++){
                    if (values.get(i).getItem() == result.getItem())
                        list.add(Collections.singletonList(keys.get(i)));
                }
                return list;
            }
            else if (index == 2)
                return AssemblerRecipes.getElements(result);
            else if (index == 3) {
                return CrusherRecipes.getElements(result);
            }
            return null;
        }
    }
}
