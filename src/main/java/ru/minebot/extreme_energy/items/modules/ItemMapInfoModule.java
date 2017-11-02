package ru.minebot.extreme_energy.items.modules;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.gui.elements.moduleGui.IModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.PowerModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.StateModuleGui;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.ChipArgs;
import ru.minebot.extreme_energy.modules.IChip;
import ru.minebot.extreme_energy.modules.IInfo;
import ru.minebot.extreme_energy.modules.Module;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class ItemMapInfoModule extends Module implements IChip, IInfo {
    private static BlockFluidRenderer fluidRenderer;
    private Map map;
    private int updateFrequency;
    private int vboID;

    public ItemMapInfoModule() {
        super(Reference.ExtremeEnergyItems.MODULEINFOMAP.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULEINFOMAP.getRegistryName(), 1, false);
        updateFrequency = 5;
    }

    @Override
    public void renderScreen(ChipArgs args, Minecraft mc, Tessellator tes, BufferBuilder buf, ScaledResolution res) {
        if (map == null || args.energy < 1000 || !args.isModuleActive)
            return;

        if (vboID == 0)
            vboID = glGenBuffers();
        int x = res.getScaledWidth() / 3;
        int y = res.getScaledHeight() - (res.getScaledHeight()) / 4;
        float scale = 75f/map.radius;
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);
        glDepthMask(true);
        glPushMatrix();
        glScaled(-scale, -scale, -scale);
        glTranslated(-x/scale, -y/scale, 30);
        glRotated(-mc.player.rotationPitch - 15, 1, 0, 0);
        glRotated(mc.player.rotationYaw, 0, 1, 0);
        map.render();
        map.renderEffects(tes, tes.getBuffer());
        glPopMatrix();
        glDisable(GL_DEPTH_TEST);
    }

    @Override
    public void renderWorld(ChipArgs args, Minecraft mc, Tessellator tes, BufferBuilder buf, ScaledResolution res) {
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public int onImplantWork(ChipArgs args) {
        if (args.player.world.isRemote && args.energy > 1000) {
            Minecraft mc = Minecraft.getMinecraft();
            try {
                mc.player.getPosition();
            } catch (NullPointerException e) {
                return 0;
            }
            if (map != null)
                map.handleEffects();
            if (args.player.world.getTotalWorldTime() % updateFrequency == 0){
                if (fluidRenderer == null)
                    fluidRenderer = new BlockFluidRenderer(mc.getBlockColors());
                int mode = args.data.getInteger("mode");
                int radius = args.data.getInteger("radius");
                int update = args.data.getInteger("update");
                if (update == 0)
                    args.data.setInteger("update", 5);
                else
                    updateFrequency = update;
                map = new Map(Mode.values()[mode], radius, map != null ? map.lines : null);
            }
        }
        return 50;
    }

    @Override
    public IModuleGui[] getGui() {
        return new IModuleGui[]{
                new PowerModuleGui("Radius:", "radius", 30, 4),
                new StateModuleGui("Mode:", new String[]{ "Normal", "Flat", "Ore" }, "mode"),
                new PowerModuleGui("Update every:", "update", 300, 2)
        };
    }

    private class Map {
        private static final float linesSpeed = 0.1f;
        private static final float maxLinesOnSide = 6;
        public int radius;
        private HashMap<EnumFacing, List<Line>> lines;
        private Block[][][] matrix;
        private int vertexCount;
        private int vertexSize;
        private int colorSize;

        public Map(Mode mode, int radius, HashMap<EnumFacing, List<Line>> lines){
            this.radius = radius;
            if (lines == null) {
                lines = new HashMap<>();
                for (EnumFacing facing : EnumFacing.values())
                    if (facing.getAxis() != EnumFacing.Axis.Y)
                        lines.put(facing, new ArrayList<>());
            }
            this.lines = lines;
            matrix = mode.initMatrix(radius);

            for (int x = 0; x < matrix.length; x++)
                for (int y = 0; y < matrix[x].length; y++)
                    for (int z = 0; z < matrix[x][y].length; z++) {
                        if (matrix[x][y][z] != null) {
                            EnumFacing[] facings = EnumFacing.values();
                            for (int i = 0; i < facings.length; i++) {
                                int offset = facings[i].getAxisDirection().getOffset();
                                EnumFacing.Axis axis = facings[i].getAxis();
                                try {
                                    Block block = axis == EnumFacing.Axis.X ? matrix[x + offset][y][z] : axis == EnumFacing.Axis.Y ? matrix[x][y + offset][z] : matrix[x][y][z + offset];
                                    if (block != null && block.state.isOpaqueCube())
                                        facings[i] = null;
                                } catch (ArrayIndexOutOfBoundsException ignored) {
                                }
                            }

                            matrix[x][y][z].cut(facings);
                        }
                    }

            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            float[] data = getData();
            ByteBuffer buffer = BufferUtils.createByteBuffer(data.length*4);
            buffer.asFloatBuffer().put(data).flip();
            glBufferData(GL_ARRAY_BUFFER, buffer, GL_DYNAMIC_DRAW);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }

        private float[] getData(){
            List<Float> coords = new ArrayList<>();
            List<Float> color = new ArrayList<>();
            List<Float> uv = new ArrayList<>();
            for (int x = 0; x < matrix.length; x++)
                for (int y = 0; y < matrix[x].length; y++)
                    for (int z = 0; z < matrix[x][y].length; z++)
                        if (matrix[x][y][z] != null) {
                            IBlockState state = matrix[x][y][z].state;
                            IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
                            for (EnumFacing facings : matrix[x][y][z].toRender)
                                if (facings != null) {
                                    final List<BakedQuad> quads = model.getQuads(state, facings, 0);
                                    List<Float>[] data = getData(quads, state, x - matrix.length / 2, y - matrix[x].length / 2, z - matrix[x][y].length / 2);
                                    coords.addAll(data[0]);
                                    color.addAll(data[1]);
                                    uv.addAll(data[2]);
                                }
                            final List<BakedQuad> quads = model.getQuads(state, null, 0);
                            List<Float>[] data = getData(quads, state, x - matrix.length / 2, y - matrix[x].length / 2, z - matrix[x][y].length / 2);
                            coords.addAll(data[0]);
                            color.addAll(data[1]);
                            uv.addAll(data[2]);
                        }
            vertexCount = color.size()/3;
            vertexSize = coords.size();
            colorSize = color.size();
            float[] result = new float[coords.size()+color.size()+uv.size()];
            for (int i = 0; i < coords.size(); i++)
                result[i] = coords.get(i);
            for (int i = 0; i < color.size(); i++)
                result[i+coords.size()] = color.get(i);
            for (int i = 0; i < uv.size(); i++)
                result[i+color.size()+coords.size()] = uv.get(i);
            return result;
        }

        private List<Float>[] getData(List<BakedQuad> quads, IBlockState state, int x, int y, int z){
            Minecraft mc = Minecraft.getMinecraft();
            List<Float>[] result = new ArrayList[3];
            result[0] = new ArrayList<>();
            result[1] = new ArrayList<>();
            result[2] = new ArrayList<>();
            int[] offset = new int[]{x,y,z};
            int[][] vertexData = new int[quads.size()][28];
            boolean isOre = state.getBlock().getRegistryName().getResourcePath().contains("ore");
            for (int i = 0; i < quads.size(); i++) {
                vertexData[i] = quads.get(i).getVertexData();
                for (int j = 0; j < vertexData[i].length - 1; j += 7) {
                    for (int m = 0; m < 3; m++)
                        result[0].add(Float.intBitsToFloat(vertexData[i][j + m]) + offset[m]);
                    if (!isOre)
                        for (int m = 4; m < 6; m++)
                            result[2].add(Float.intBitsToFloat(vertexData[i][j + m]));
                    else {
                        result[2].add(0f);
                        result[2].add(0f);
                    }
                }

                if (isOre){
                    int level = state.getBlock().getHarvestLevel(state);
                    level = level > 4 ? 4 : level;
                    float color = 1 - level/4f;
                    float dif = 1f;
                    if (quads.get(i).shouldApplyDiffuseLighting())
                        dif = net.minecraftforge.client.model.pipeline.LightUtil.diffuseLight(quads.get(i).getFace());
                    for (int j = 0; j < 4; j++){
                        result[1].add(1f * dif);
                        result[1].add(color * dif);
                        result[1].add(color * dif);
                    }
                }
                else {
                    if (quads.get(i).hasTintIndex()) {
                        int k = mc.getBlockColors().colorMultiplier(state, mc.world, mc.player.getPosition().add(new BlockPos(x, y, z)), quads.get(i).getTintIndex());

                        if (EntityRenderer.anaglyphEnable)
                            k = TextureUtil.anaglyphColor(k);

                        float f = (float) (k >> 16 & 255) / 255.0F;
                        float f1 = (float) (k >> 8 & 255) / 255.0F;
                        float f2 = (float) (k & 255) / 255.0F;
                        if (quads.get(i).shouldApplyDiffuseLighting()) {
                            float diffuse = net.minecraftforge.client.model.pipeline.LightUtil.diffuseLight(quads.get(i).getFace());
                            f *= diffuse;
                            f1 *= diffuse;
                            f2 *= diffuse;
                        }
                        for (int j = 0; j < 4; j++) {
                            result[1].add(f);
                            result[1].add(f1);
                            result[1].add(f2);
                        }
                    } else if (quads.get(i).shouldApplyDiffuseLighting()) {
                        float diffuse = net.minecraftforge.client.model.pipeline.LightUtil.diffuseLight(quads.get(i).getFace());
                        for (int j = 0; j < 12; j++)
                            result[1].add(diffuse);
                    } else {
                        for (int j = 0; j < 12; j++)
                            result[1].add(1f);
                    }
                }
            }
            return result;
        }

        public void render(){
            Minecraft mc = Minecraft.getMinecraft();
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
            glEnableClientState( GL_VERTEX_ARRAY );
            glVertexPointer(3, GL_FLOAT, 0, 0);
            glEnableClientState(GL_COLOR_ARRAY);
            glColorPointer(3, GL_FLOAT, 0, vertexSize*4);
            glEnableClientState(GL_TEXTURE_COORD_ARRAY);
            glTexCoordPointer(2, GL_FLOAT, 0, (vertexSize+colorSize)*4);
            glDrawArrays(GL_QUADS, 0, vertexCount);
            glDisableClientState(GL_VERTEX_ARRAY);
            glDisableClientState(GL_COLOR_ARRAY);
            glDisableClientState(GL_TEXTURE_COORD_ARRAY);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        }

        public void renderEffects(Tessellator tes, BufferBuilder buf){
            Minecraft mc = Minecraft.getMinecraft();
            glDisable(GL_CULL_FACE);
            GlStateManager.disableTexture2D();
            buf.begin(GL_POLYGON, DefaultVertexFormats.POSITION_COLOR);
            buf.pos(-radius, -radius-1, -radius).color(0.17f, 0.37f, 1f, 0.5f).endVertex();
            buf.pos(-radius, -radius-1, radius).color(0.17f, 0.37f, 1f, 0.5f).endVertex();
            buf.pos(radius, -radius-1, radius).color(0.17f, 0.37f, 1f, 0.5f).endVertex();
            buf.pos(radius, -radius-1, -radius).color(0.17f, 0.37f, 1f, 0.5f).endVertex();
            tes.draw();
            glEnable(GL_CULL_FACE);
            GlStateManager.enableTexture2D();

            mc.getTextureManager().bindTexture(new ResourceLocation("meem:textures/other/cursor.png"));
            glPushMatrix();
            glRotated(-mc.player.rotationYaw, 0, 1, 0);
            buf.begin(GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
            buf.pos(0.5f, 1, 0).tex(0, 0).endVertex();
            buf.pos(0.5f, 0, 0).tex(0, 1).endVertex();
            buf.pos(-0.5f, 0, 0).tex(1, 1).endVertex();
            buf.pos(-0.5f, 1, 0).tex(1, 0).endVertex();
            tes.draw();
            glPopMatrix();

            GlStateManager.disableTexture2D();
            for (EnumFacing enumFacing : EnumFacing.values()) {
                if (!lines.containsKey(enumFacing))
                    continue;

                List<Line> lines = this.lines.get(enumFacing);
                for (Line line : lines)
                    line.render(tes, buf);
            }
            GlStateManager.enableTexture2D();
        }

        public void handleEffects(){
            for (EnumFacing enumFacing : EnumFacing.values()){
                if (!lines.containsKey(enumFacing))
                    continue;

                List<Line> lines = this.lines.get(enumFacing);
                List<Line> toRemove = new ArrayList<>();
                for (Line line : lines){
                    line.update();
                    if (line.isDead())
                        toRemove.add(line);
                }
                lines.removeAll(toRemove);

                if (lines.size() < maxLinesOnSide && ModUtils.random.nextFloat() < 0.05f)
                    lines.add(new Line(enumFacing, 2*radius*(ModUtils.random.nextFloat() - 0.5f), -radius, radius/(ModUtils.random.nextFloat()+0.5f*5), 0.5f, 1));
            }
        }

        private class Line {
            private final float POSY;
            private float posX;
            private float posY;
            private float height;
            private float width;
            private float zOffset;
            private float angle;

            public Line(EnumFacing side, float posX, float posY, float height, float width, float zOffset) {
                this.angle = side.getHorizontalAngle();
                this.posX = posX;
                this.posY = posY;
                this.height = height;
                this.width = width/2;
                this.zOffset = zOffset;
                this.POSY = posY;
            }

            public void render(Tessellator tes, BufferBuilder buf){
                float downY = posY - POSY > height ? posY - height : POSY;
                glPushMatrix();
                glRotated(angle, 0, 1, 0);
                buf.begin(GL_POLYGON, DefaultVertexFormats.POSITION_COLOR);
                buf.pos(posX+width, posY, zOffset+radius).color(0.17f, 0.37f, 1f, 0.5f).endVertex();
                buf.pos(posX+width, downY, zOffset+radius).color(0.17f, 0.37f, 1f, 0.5f).endVertex();
                buf.pos(posX-width, downY, zOffset+radius).color(0.17f, 0.37f, 1f, 0.5f).endVertex();
                buf.pos(posX-width, posY, zOffset+radius).color(0.17f, 0.37f, 1f, 0.5f).endVertex();
                tes.draw();
                glPopMatrix();
            }

            public void update(){
                posY += linesSpeed;
            }

            public boolean isDead(){
                return posY >= radius;
            }
        }
    }

    private static class Block {
        IBlockState state;
        EnumFacing[] toRender;

        public Block(IBlockState state){
            this.state = state;
        }

        public void cut(EnumFacing[] toRender){
            this.toRender = toRender;
        }

    }

    private enum Mode {
        NORMAL(args -> true),
        FLAT(args -> Math.abs(args.y - args.radius) < 3),
        ORES(args -> args.state.getBlock().getRegistryName().getResourcePath().contains("ore"));

        private Predicate<Args> checkFunc;

        Mode(Predicate<Args> checkFunc){
            this.checkFunc = checkFunc;
        }

        public Block[][][] initMatrix(int radius){
            Minecraft mc = Minecraft.getMinecraft();
            BlockPos pos = mc.player.getPosition();
            Block[][][] result = new Block[radius*2][radius*2][radius*2];
            for (int x = 0; x < radius*2; x++)
                for (int y = 0; y < radius*2; y++)
                    for (int z = 0; z < radius*2; z++){
                        IBlockState state = mc.player.world.getBlockState(new BlockPos(x-radius+pos.getX(), y-radius+pos.getY(), z-radius+pos.getZ()));
                        if (state.getBlock() != Blocks.AIR && !(state.getBlock() instanceof BlockLiquid))
                            result[x][y][z] = checkFunc.test(new Args(x, y, z, state, radius)) ? new Block(state) : null;
                    }
            return result;
        }

        private class Args {
            public int x;
            public int y;
            public int z;
            IBlockState state;
            int radius;

            public Args(int x, int y, int z, IBlockState state, int radius) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.state = state;
                this.radius = radius;
            }
        }
    }
}
