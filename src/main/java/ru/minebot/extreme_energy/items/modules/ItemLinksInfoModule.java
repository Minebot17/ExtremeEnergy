package ru.minebot.extreme_energy.items.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import static org.lwjgl.opengl.GL11.*;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.energy.IFieldCreatorEnergy;
import ru.minebot.extreme_energy.energy.IFieldReceiverEnergy;
import ru.minebot.extreme_energy.gui.elements.moduleGui.BooleanModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.IModuleGui;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.ChipArgs;
import ru.minebot.extreme_energy.modules.IChip;
import ru.minebot.extreme_energy.modules.IInfo;
import ru.minebot.extreme_energy.modules.Module;

import java.util.ArrayList;
import java.util.List;


public class ItemLinksInfoModule extends Module implements IChip, IInfo{
    private List<Vertex> vertices = new ArrayList<>();
    private List<float[]> colors = new ArrayList<>();

    private boolean renderLinks;
    private boolean renderFields;

    public ItemLinksInfoModule() {
        super(Reference.ExtremeEnergyItems.MODULEINFOLINKS.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULEINFOLINKS.getRegistryName(), 1, false);
    }


    @Override
    public void renderScreen(ChipArgs args, Minecraft mc, Tessellator tes, BufferBuilder buf, ScaledResolution res) {

    }

    @Override
    public void renderWorld(ChipArgs args, Minecraft mc, Tessellator tes, BufferBuilder buf, ScaledResolution res) {
        if (vertices.size() == 0 || args.energy < 1000)
            return;

        if (renderLinks || renderFields) {
            glLineWidth(6);
            GlStateManager.disableTexture2D();
            glEnable(GL_BLEND);
            glDisable(GL_DEPTH_TEST);
            for (int i = 0; i < vertices.size(); i++) {
                float[] color = colors.get(i);
                if (renderLinks)
                    renderVertices(vertices.get(i), mc.player.getPositionEyes(mc.getRenderPartialTicks()), tes, buf, color[0], color[1], color[2]);
                if (renderFields) {
                    glEnable(GL_DEPTH_TEST);
                    renderFields(mc.world, vertices.get(i), tes, buf, color[0], color[1], color[2]);
                    glDisable(GL_DEPTH_TEST);
                }
            }
            glEnable(GL_DEPTH_TEST);
            glDisable(GL_BLEND);
            GlStateManager.enableTexture2D();
        }
    }

    private void renderVertices(Vertex vertex, Vec3d eyes, Tessellator tes, BufferBuilder buf, float r, float g, float b){
        Vec3d from = new Vec3d(vertex.pos.getX()+0.5f, vertex.pos.getY()+0.5f, vertex.pos.getZ()+0.5f);
        double length = Math.sqrt(Math.pow(eyes.x - from.x, 2) + Math.pow(eyes.z - from.z, 2));
        Vec3d delta = eyes.subtract(from);

        glPushMatrix();
        glTranslated(from.x, from.y, from.z);
        glRotatef((float)(-Math.atan2(delta.z, delta.x) / Math.PI * 180)+180, 0, 1, 0);
        glRotatef((float)(-Math.atan2(delta.y, length) / Math.PI * 180), 0, 0, 1);
        vertex.render(tes, buf, r, g, b);
        glPopMatrix();

        for (Vertex toVertex : vertex.links){
            Vec3d to = new Vec3d(toVertex.pos.getX()+0.5f, toVertex.pos.getY()+0.5f, toVertex.pos.getZ()+0.5f);
            buf.begin(GL_LINES, DefaultVertexFormats.POSITION_COLOR);
            buf.pos(from.x, from.y, from.z).color(r, g, b, 1).endVertex();
            buf.pos(to.x, to.y, to.z).color(r, g, b, 1).endVertex();
            tes.draw();
            renderVertices(toVertex, eyes, tes, buf, r, g, b);
        }

    }

    private void renderFields(World world, Vertex vertex, Tessellator tes, BufferBuilder buf, float r, float g, float b){
        if (world == null)
            return;

        Vec3d block = new Vec3d(vertex.pos.getX()+0.5f, vertex.pos.getY()+0.5f, vertex.pos.getZ()+0.5f);
        float radius = ((IFieldCreatorEnergy)world.getTileEntity(vertex.pos)).getRadius()+0.4f;
        glPushMatrix();
        glTranslated(block.x, block.y, block.z);
        float alpha = 0.75f;

        // UP
        buf.begin(GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
        buf.pos(+radius, +radius, +radius).color(r,g,b,alpha).endVertex();
        buf.pos(+radius, +radius, -radius).color(r,g,b,alpha).endVertex();
        buf.pos(-radius, +radius, -radius).color(r,g,b,alpha).endVertex();
        buf.pos(-radius, +radius, +radius).color(r,g,b,alpha).endVertex();
        tes.draw();

        // DOWN
        buf.begin(GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
        buf.pos(+radius, -radius, +radius).color(r,g,b,alpha).endVertex();
        buf.pos(+radius, -radius, -radius).color(r,g,b,alpha).endVertex();
        buf.pos(-radius, -radius, -radius).color(r,g,b,alpha).endVertex();
        buf.pos(-radius, -radius, +radius).color(r,g,b,alpha).endVertex();
        tes.draw();

        // SIDES
        buf.begin(GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        buf.pos(+radius, +radius, +radius).color(r,g,b,alpha).endVertex();
        buf.pos(+radius, -radius, +radius).color(r,g,b,alpha).endVertex();

        buf.pos(+radius, +radius, -radius).color(r,g,b,alpha).endVertex();
        buf.pos(+radius, -radius, -radius).color(r,g,b,alpha).endVertex();

        buf.pos(-radius, +radius, +radius).color(r,g,b,alpha).endVertex();
        buf.pos(-radius, -radius, +radius).color(r,g,b,alpha).endVertex();

        buf.pos(-radius, +radius, -radius).color(r,g,b,alpha).endVertex();
        buf.pos(-radius, -radius, -radius).color(r,g,b,alpha).endVertex();
        tes.draw();

        glPopMatrix();

        for (Vertex vertex1 : vertices)
            if (vertex1.type == VertexType.TRANSMITTER)
                renderFields(world, vertex1, tes, buf, r, g, b);
    }

    @Override
    public int onImplantWork(ChipArgs args) {
        if (args.energy > 1000 && args.player.world.isRemote && args.player.world.getTotalWorldTime()%20==0){
            renderLinks = args.data.getBoolean("links");
            renderFields = args.data.getBoolean("fields");
            vertices = new ArrayList<>();
            colors = new ArrayList<>();
            List<TileEntity> tes = ModUtils.radiusFilter(args.player.getPosition(), args.player.world.tickableTileEntities, 64);
            for (TileEntity te : tes)
                if (te instanceof IFieldCreatorEnergy && !(te instanceof IFieldReceiverEnergy) && ((IFieldCreatorEnergy) te).isActive()){
                    vertices.add(new Vertex(te.getPos(), VertexType.CREATOR, getVertexes(args.player.world, te.getPos(), new ArrayList<>())));

                    int color = (int)((((IFieldCreatorEnergy) te).getFrequency()/1000000000f)*16777216);
                    float r = ((color & 0xFF0000) >> 16) / 255f;
                    float g = ((color & 0xFF00) >> 8) / 255f;
                    float b = (color & 0xFF) / 255f;
                    colors.add(new float[]{ r, g, b });
                }
        }
        return 6;
    }

    private List<Vertex> getVertexes(World world, BlockPos pos, List<Vertex> result){
        TileEntity te = world.getTileEntity(pos);
        List<BlockPos> links = ((IFieldCreatorEnergy) te).getLinks();
        for (BlockPos link : links){
            TileEntity newTes = world.getTileEntity(link);
            if (newTes instanceof IFieldCreatorEnergy){
                result.add(new Vertex(newTes.getPos(), VertexType.TRANSMITTER, getVertexes(world, newTes.getPos(), new ArrayList<>())));
            }
            else
                result.add(new Vertex(newTes.getPos(), VertexType.RECEIVER, new ArrayList<>()));
        }
        return result;
    }

    @Override
    public IModuleGui[] getGui() {
        return new IModuleGui[]{
                new BooleanModuleGui("Show fields:", "fields"),
                new BooleanModuleGui("Show links:", "links")
        };
    }

    @Override
    public int getTier() {
        return 0;
    }

    private class Vertex {
        public BlockPos pos;
        public VertexType type;
        public List<Vertex> links;

        public Vertex(BlockPos pos, VertexType type, List<Vertex> links){
            this.pos = pos;
            this.type = type;
            this.links = links;
        }

        public void render(Tessellator tes, BufferBuilder buf, float r, float g, float b){
            type.render.invoke(tes, buf, r, g, b);
        }
    }

    private enum VertexType {
        CREATOR((tes, buf, r, g, b) -> {
            glPushMatrix();
            glScaled(0.25f,0.25f,0.25f);
            buf.begin(GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
            buf.pos(0, 0, 0).color(r,g,b,1).endVertex();
            for (int i = 0; i <= 360; i += 6)
                buf.pos(0, Math.sin(Math.toRadians(i)), Math.cos(Math.toRadians(i))).color(r,g,b,1).endVertex();
            tes.draw();
            glPopMatrix();
        }),
        TRANSMITTER((tes, buf, r, g, b) -> {
            glPushMatrix();
            glScaled(0.25f,0.25f,0.25f);
            buf.begin(GL_TRIANGLES, DefaultVertexFormats.POSITION_COLOR);
            buf.pos(0, -1f, -1f).color(r,g,b,1).endVertex();
            buf.pos(0, -1f, 1f).color(r,g,b,1).endVertex();
            buf.pos(0, 1f, 0).color(r,g,b,1).endVertex();
            tes.draw();
            glPopMatrix();
        }),
        RECEIVER((tes, buf, r, g, b) -> {
            glPushMatrix();
            glScaled(0.25f,0.25f,0.25f);
            buf.begin(GL_POLYGON, DefaultVertexFormats.POSITION_COLOR);
            buf.pos(0, 1f, -1f).color(r,g,b,1).endVertex();
            buf.pos(0, -1f, -1f).color(r,g,b,1).endVertex();
            buf.pos(0, -1f, 1f).color(r,g,b,1).endVertex();
            buf.pos(0, 1f, 1f).color(r,g,b,1).endVertex();
            tes.draw();
            glPopMatrix();
        });

        public Action render;
        VertexType(Action render){
            this.render = render;
        }
    }

    private interface Action {
        void invoke(Tessellator tes, BufferBuilder buf, float r, float g, float b);
    }
}
