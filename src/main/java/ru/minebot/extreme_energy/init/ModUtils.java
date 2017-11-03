package ru.minebot.extreme_energy.init;

import cofh.redstoneflux.api.*;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.gui.tablet.Element;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketEntityMotion;
import ru.minebot.extreme_energy.network.packages.PacketLangMessage;
import ru.minebot.extreme_energy.network.packages.PacketPlaySound;
import ru.minebot.extreme_energy.network.packages.PacketSpawnParticle;
import ru.minebot.extreme_energy.other.ChargeSaveData;
import ru.minebot.extreme_energy.other.ImplantData;
import ru.minebot.extreme_energy.other.InfectedSaveData;
import ru.minebot.extreme_energy.tile_entities.EnergySender;


import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScaled;

public class ModUtils {

    public static Random random = new Random();
    public static HashMap<ChunkPos, Integer> clientChunkCharge = new HashMap<>();
    public static HashMap<Integer, HashMap<BlockPos, Integer>> shields = new HashMap<>();
    public static HashMap<Integer, HashMap<BlockPos, Integer>> shieldToAdd = new HashMap<>();
    public static BlockPos[] faces = new BlockPos[]{
            new BlockPos(0, 0, 1),
            new BlockPos(0, 1, 0),
            new BlockPos(1, 0, 0),
            new BlockPos(0, 0, -1),
            new BlockPos(0, -1, 0),
            new BlockPos(-1, 0, 0)
    };

    public static void updateNetwork(World world, BlockPos pos, List<BlockPos> checked){
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te instanceof EnergySender && !checked.contains(pos))
            ((EnergySender) te).calculateEnergyNetwork();
        else if (te == null && !checked.contains(pos) && world.getBlockState(pos).getBlock() == ModBlocks.cable){
            for (EnumFacing facing : EnumFacing.values()) {
                TileEntity te1 = world.getTileEntity(pos.offset(facing));
                if (te1 != null && !(te1 instanceof EnergySender) && te1 instanceof IEnergyProvider){
                    world.setBlockState(pos, ModBlocks.cableWithTile.getDefaultState());
                    break;
                }
            }
        }
        checked.add(pos);
        for (int i = 0; i < faces.length; i++) {
            BlockPos newPos = new BlockPos(pos.getX() + faces[i].getX(), pos.getY() + faces[i].getY(), pos.getZ() + faces[i].getZ());
            TileEntity newTe = world.getTileEntity(newPos);
            if (!checked.contains(newPos) && (world.getBlockState(newPos).getBlock() == ModBlocks.cable || (newTe != null && newTe instanceof EnergySender)))
                updateNetwork(world, newPos, checked);
        }
    }

    public static void createNuclearExplosion(World world, BlockPos pos, boolean drop){
        NetworkWrapper.instance.sendToAllAround(
                new PacketPlaySound(SoundEvent.REGISTRY.getIDForObject(SoundEvents.ENTITY_GENERIC_EXPLODE), pos.getX(), pos.getY(), pos.getZ()),
                new NetworkRegistry.TargetPoint(world.provider.getDimension(),pos.getX(), pos.getY(), pos.getZ(),32)
        );
        List<EntityLivingBase> entities = world.getEntities(EntityLivingBase.class, input -> input != null && input.getPosition().getDistance(pos.getX(), pos.getY(), pos.getZ()) < 32);
        for (EntityLivingBase entity : entities) {
            float power = 32f / (float) entity.getPosition().getDistance(pos.getX(), pos.getY(), pos.getZ());
            entity.attackEntityFrom(DamageSource.causeExplosionDamage(entity), (int)power);
            Vec3d vec = entity.getPositionVector().subtract(pos.getX(), pos.getY(), pos.getZ()).normalize().scale(power);
            entity.isAirBorne = true;
            entity.motionX += vec.x;
            entity.motionY += vec.y;
            entity.motionZ += vec.z;
            NetworkWrapper.instance.sendToAll(new PacketEntityMotion(entity));
        }
        for (int x = -32; x <= 32; x++)
            for (int y = -32; y <= 32; y++)
                for (int z = -32; z <= 32; z++){
                    BlockPos newPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    IBlockState state = world.getBlockState(newPos);
                    if (newPos.getDistance(pos.getX(), pos.getY(), pos.getZ()) < 32 + (ModUtils.random.nextFloat()-0.5f)*4f && state != Blocks.AIR.getDefaultState() && state.getBlockHardness(world, newPos) != -1){
                        world.getBlockState(newPos).getBlock().breakBlock(world, newPos, state);
                        world.setBlockToAir(newPos);
                        if (newPos.getDistance(pos.getX(), pos.getY(), pos.getZ()) > 26 && ModUtils.random.nextFloat() > 0.99f){
                            world.spawnEntity(new EntityItem(world, newPos.getX(), newPos.getY(), newPos.getZ(), new ItemStack(state.getBlock())));
                        }
                        NetworkWrapper.instance.sendToAllAround(
                                new PacketSpawnParticle(
                                        EnumParticleTypes.EXPLOSION_LARGE.getParticleID(),
                                        newPos.getX(), newPos.getY(), newPos.getZ(), 0, 0, 0, 1
                                ),
                                new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64)
                        );
                    }
                }

        InfectedSaveData data = InfectedSaveData.getOrCreateData(world);
        for (int i = -1; i < 1; i++)
            for (int j = -1; j < 1; j++)
                data.map.put(new ChunkPos(new ChunkPos(pos).x + i, new ChunkPos(pos).z + j), 72000); // 1 hour
        data.markDirty();

        if (drop){
            world.spawnEntity(new EntityItem(world, pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f, new ItemStack(ModItems.californium, ModUtils.random.nextInt(3)+1)));
        }
    }

    public static void addShieldToRemove(World world, BlockPos pos, int time){
        if (!shields.containsKey(world.provider.getDimension()))
            shields.put(world.provider.getDimension(), new HashMap<>());

        HashMap<BlockPos, Integer> map = shields.get(world.provider.getDimension());
        map.put(pos, time);
    }

    public static void addShieldToAdd(World world, BlockPos pos, int time){
        if (!shieldToAdd.containsKey(world.provider.getDimension()))
            shieldToAdd.put(world.provider.getDimension(), new HashMap<>());

        HashMap<BlockPos, Integer> map = shieldToAdd.get(world.provider.getDimension());
        map.put(pos, time);
    }

    public static boolean contains(int[] array, int value){
        for (int v : array)
            if (v == value)
                return true;
        return false;
    }

    public static boolean empty(int[] array){
        for (int v : array)
            if (v != 0)
                return false;
        return true;
    }

    public static void setState(World worldIn, BlockPos pos, IBlockState newState) {
        if (!worldIn.isRemote) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            IBlockState oldState = worldIn.getBlockState(pos);
            worldIn.setBlockState(pos, newState);
            worldIn.removeTileEntity(pos);

            if (tileentity != null) {
                tileentity.validate();
                worldIn.setTileEntity(pos, tileentity);
            }
            worldIn.notifyBlockUpdate(pos, oldState, newState, 0);
        }
    }

    public static float getAngle(Vec2f vec1, Vec2f vec2){
        return (float)Math.toDegrees(Math.atan2(vec2.y, vec2.x) - Math.atan2(vec1.y, vec1.x));
    }

    public static NetworkRegistry.TargetPoint getTargetPoint(EntityLivingBase entity, int range){
        return new NetworkRegistry.TargetPoint(0, entity.getPositionVector().x, entity.getPositionVector().y, entity.getPositionVector().z, range);
    }

    public static String getDescription(Item item){
        return I18n.format("description." + item.getUnlocalizedName().substring(5));
    }

    public static boolean inRadius(BlockPos center, BlockPos point, int radius) {
        return Math.abs(center.getX() - point.getX()) <= radius && Math.abs(center.getY() - point.getY()) <= radius && Math.abs(center.getZ() - point.getZ()) <= radius;
    }

    public static List<TileEntity> radiusFilter(BlockPos center, List<TileEntity> points, int radius) {
        List<TileEntity> result = new ArrayList<>();
        for (int i = 0; i < points.size(); i++)
            if (inRadius(center, points.get(i).getPos(), radius))
                result.add(points.get(i));
        return result;
    }

    public static List<EntityPlayer> radiusFilterPlayers(BlockPos center, List<EntityPlayer> points, int radius) {
        List<EntityPlayer> result = new ArrayList<>();
        for (int i = 0; i < points.size(); i++)
            if (inRadius(center, points.get(i).getPosition(), radius))
                result.add(points.get(i));
        return result;
    }

    public static List<Entity> radiusFilterEntities(BlockPos center, List<Entity> points, int radius) {
        List<Entity> result = new ArrayList<>();
        for (int i = 0; i < points.size(); i++)
            if (inRadius(center, points.get(i).getPosition(), radius))
                result.add(points.get(i));
        return result;
    }

    public static TileEntity getNearestTile(List<TileEntity> entities, BlockPos center) {
        List<BlockPos> poses = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++)
            poses.add(entities.get(i).getPos());
        return entities.get(getNearestPosIndex(poses, center));
    }

    public static Entity getNearestEntity(List<? extends Entity> entities, BlockPos center){
        List<BlockPos> poses = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++)
            poses.add(entities.get(i).getPosition());
        return entities.get(getNearestPosIndex(poses, center));
    }

    public static int getNearestPosIndex(List<BlockPos> poses, BlockPos center) {
        int index = -1;
        float dist = Integer.MAX_VALUE;
        for (int i = 0; i < poses.size(); i++) {
            float thisDist = (float) center.getDistance(poses.get(i).getX(), poses.get(i).getY(), poses.get(i).getZ());
            if (dist > thisDist) {
                dist = thisDist;
                index = i;
            }
        }
        return index;
    }

    public static BlockPos getNearestPos(List<BlockPos> poses, BlockPos center) {
        return poses.get(getNearestPosIndex(poses, center));
    }

    public static List<ItemStack> getModules(ImplantData data) {
        return getItems(data.modules);
    }

    public static List<ItemStack> getCoreModules(ImplantData data) {
        return getItems(data.core.getCompoundTag("tag").getCompoundTag(ExtremeEnergy.NBT_CATEGORY));
    }

    public static ImplantData setModules(ImplantData data, List<ItemStack> modules){
        data.modules.setTag("Items", setItems(new NBTTagList(), modules));
        return data;
    }

    public static ImplantData setCoreModules(ImplantData data, List<ItemStack> modules){
        data.core.getCompoundTag("tag").getCompoundTag(ExtremeEnergy.NBT_CATEGORY).setTag("Items", setItems(new NBTTagList(), modules));
        return data;
    }

    public static ItemStack getCore(ImplantData data) {
        return new ItemStack(data.core);
    }

    public static void sendModMessage(EntityPlayer player, String messageKey) {
        if (player.world.isRemote)
            player.sendMessage(new TextComponentString(I18n.format("message." + messageKey)));
        else
            NetworkWrapper.instance.sendTo(new PacketLangMessage("message", messageKey), (EntityPlayerMP) player);
    }

    public static boolean isModuleActive(ImplantData data, int moduleIndex) {
        byte[] actives = data.implant.getByteArray("activesArray");
        return actives[moduleIndex] != 0;
    }

    private static List<ItemStack> getItems(NBTTagCompound tag) {
        List<ItemStack> stacks = new ArrayList<>();
        NBTTagList list = tag.getTagList("Items", 10);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            stacks.add(new ItemStack(stackTag));
        }
        return stacks;
    }

    private static NBTTagList setItems(NBTTagList tag, List<ItemStack> items){
        for (int i = 0; i < items.size(); i++)
            tag.appendTag(items.get(i).writeToNBT(new NBTTagCompound()));
        return tag;
    }

    public static boolean containsModule(ImplantData data, Item module){
        List<ItemStack> list = getModules(data);
        for (ItemStack stack : list)
            if (stack.getItem() == module)
                return true;
        return false;
    }

    public static NBTTagCompound getNotNullTag(ItemStack stack) {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());
        return stack.getTagCompound();
    }

    public static NBTTagCompound getNotNullCategory(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (!stack.getTagCompound().hasKey(ExtremeEnergy.NBT_CATEGORY)) {
            stack.getTagCompound().setTag(ExtremeEnergy.NBT_CATEGORY, new NBTTagCompound());
        }
        return stack.getTagCompound().getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
    }

    public static int extractEnergyFromChunk(World world, ChunkPos pos, boolean simulate){
        ChargeSaveData data = ChargeSaveData.getOrCreateData(world);
        if (data.map.containsKey(pos)){
            Integer energy = data.map.get(pos);
            int result = Math.min(energy, (int)((float)energy/10000f));
            if (result != 0 && !simulate){
                energy -= result;
                data.map.put(pos, energy);
                data.markDirty();
            }
            return result;
        }
        else
            return 0;
    }

    public static int extractEnergyFromChunk(World world, ChunkPos pos, int maxExtract){
        ChargeSaveData data = ChargeSaveData.getOrCreateData(world);
        if (data.map.containsKey(pos)){
            Integer energy = data.map.get(pos);
            int result = Math.min(energy, maxExtract);
            if (result != 0){
                energy -= result;
                data.map.put(pos, energy);
                data.markDirty();
            }
            return result;
        }
        else
            return 0;
    }

    public static void addEnergyToChunk(World world, ChunkPos pos, int energy){
        ChargeSaveData data = ChargeSaveData.getOrCreateData(world);
        if (data.map.containsKey(pos)){
            int toEnergy = Math.min(ModConfig.maxCapOfChunk, energy + data.map.get(pos));
            data.map.put(pos, toEnergy);
            data.markDirty();
        }
    }

    public static void spawnParticles(World world, int type, float x, float y, float z, float xSpeed, float ySpeed, float zSpeed, int count){
        spawnParticles(world, type, x, y, z, xSpeed, ySpeed, zSpeed, count, 0, 0);
    }

    public static void spawnParticles(World world, int type, float x, float y, float z, float xSpeed, float ySpeed, float zSpeed, int count, float randomPos, float randomMotion){
        spawnParticles(world, type, x, y, z, xSpeed, ySpeed, zSpeed, count, randomPos, randomMotion, randomPos, randomMotion, randomPos, randomMotion);
    }

    public static void spawnParticles(World world, int type, float x, float y, float z, float xSpeed, float ySpeed, float zSpeed, int count, float randomXPos, float randomXMotion, float randomYPos, float randomYMotion, float randomZPos, float randomZMotion) {
        for (int i = 0; i < count; i++) {
            float xRandPos = (ModUtils.random.nextInt(2) == 0 ? -1 : 1) * ModUtils.random.nextFloat() * randomXPos;
            float yRandPos = (ModUtils.random.nextInt(2) == 0 ? -1 : 1) * ModUtils.random.nextFloat() * randomYPos;
            float zRandPos = (ModUtils.random.nextInt(2) == 0 ? -1 : 1) * ModUtils.random.nextFloat() * randomZPos;
            float xRandMotion = (ModUtils.random.nextInt(2) == 0 ? -1 : 1) * ModUtils.random.nextFloat() * randomXMotion;
            float yRandMotion = (ModUtils.random.nextInt(2) == 0 ? -1 : 1) * ModUtils.random.nextFloat() * randomYMotion;
            float zRandMotion = (ModUtils.random.nextInt(2) == 0 ? -1 : 1) * ModUtils.random.nextFloat() * randomZMotion;

            world.spawnParticle(
                    EnumParticleTypes.getParticleFromId(type),
                    x + xRandPos,
                    y + yRandPos,
                    z + zRandPos,
                    (xSpeed + xRandMotion) * xRandPos >= 0 ? 1 : -1,
                    (ySpeed + yRandMotion) * yRandPos >= 0 ? 1 : -1,
                    (zSpeed + zRandMotion) * zRandPos >= 0 ? 1 : -1
            );
        }
    }

    @SideOnly(Side.CLIENT)
    public static void drawString(String text, float x, float y, int color, Element.Align align){
        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        float localX = align == Element.Align.LEFT ? x : align == Element.Align.CENTER ? x - font.getStringWidth(text)/2f : x - font.getStringWidth(text);
        float localY = y - (float)font.FONT_HEIGHT/2f;
        font.drawString(text, localX, localY, color, false);
    }

    @SideOnly(Side.CLIENT)
    public static Entity getMouseOver(float partialTicks, float dist, boolean canBeCollided)
    {
        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = mc.getRenderViewEntity();
        Entity pointedEntity = null;

        if (entity != null)
        {
            if (mc.world != null)
            {
                mc.mcProfiler.startSection("pick");
                mc.pointedEntity = null;
                double d0 = dist;
                Vec3d vec3d = entity.getPositionEyes(partialTicks);
                boolean flag = false;
                double d1 = d0;

                if (mc.playerController.extendedReach())
                {
                    d1 = dist;
                    d0 = d1;
                }
                else
                {
                    if (d0 > dist)
                    {
                        flag = true;
                    }
                }

                Vec3d vec3d1 = entity.getLook(1.0F);
                Vec3d vec3d2 = vec3d.addVector(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0);
                Vec3d vec3d3 = null;
                float f = 1.0F;
                List<Entity> list = mc.world.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().grow(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0).expand(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
                {
                    public boolean apply(@Nullable Entity p_apply_1_)
                    {
                        return p_apply_1_ != null && (!canBeCollided || p_apply_1_.canBeCollidedWith());
                    }
                }));
                double d2 = d1;

                for (int j = 0; j < list.size(); ++j)
                {
                    Entity entity1 = (Entity)list.get(j);
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow((double)entity1.getCollisionBorderSize());
                    RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);

                    if (axisalignedbb.contains(vec3d))
                    {
                        if (d2 >= 0.0D)
                        {
                            pointedEntity = entity1;
                            vec3d3 = raytraceresult == null ? vec3d : raytraceresult.hitVec;
                            d2 = 0.0D;
                        }
                    }
                    else if (raytraceresult != null)
                    {
                        double d3 = vec3d.distanceTo(raytraceresult.hitVec);

                        if (d3 < d2 || d2 == 0.0D)
                        {
                            if (entity1.getLowestRidingEntity() == entity.getLowestRidingEntity() && !entity1.canRiderInteract())
                            {
                                if (d2 == 0.0D)
                                {
                                    pointedEntity = entity1;
                                    vec3d3 = raytraceresult.hitVec;
                                }
                            }
                            else
                            {
                                pointedEntity = entity1;
                                vec3d3 = raytraceresult.hitVec;
                                d2 = d3;
                            }
                        }
                    }
                }

                if (pointedEntity != null && flag && vec3d.distanceTo(vec3d3) > dist)
                {
                    return null;
                }

                if (pointedEntity != null && d2 < d1)
                {
                    RayTraceResult ray = mc.player.rayTrace(dist, partialTicks);
                    if (ray == null)
                        return pointedEntity;
                    else if (ray.hitVec.distanceTo(mc.player.getPositionVector())+0.5f >= pointedEntity.getPositionVector().distanceTo(mc.player.getPositionVector()))
                        return pointedEntity;
                }
            }
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public static RayTraceResult getBlocksRay(){
        return Minecraft.getMinecraft().player.rayTrace(128, Minecraft.getMinecraft().getRenderPartialTicks());
    }

    @SideOnly(Side.CLIENT)
    public static int getEntityRay(){
        Entity entity = ModUtils.getMouseOver(Minecraft.getMinecraft().getRenderPartialTicks(), 128, false);
        return entity == null ? 0 : entity.getEntityId();
    }

    public static void drawText(FontRenderer font, float posX, float posY, String text, int color, float textSize){
        drawText(font, posX, posY, text, color, textSize, Element.Align.LEFT);
    }

    public static void drawText(FontRenderer font, float posX, float posY, String text, int color, float textSize, Element.Align align){
        float localPosX = posX*(1f/textSize);
        float localPosY = posY*(1f/textSize);
        glPushMatrix();
        glScaled(textSize, -textSize, 1f);
        font.drawString(text, align == Element.Align.LEFT ? localPosX : align == Element.Align.CENTER ? localPosX - font.getStringWidth(text)/2f : localPosX - font.getStringWidth(text), localPosY, color, false);
        GlStateManager.color(1, 1, 1);
        glPopMatrix();
    }

    private static String[] languagesForTablet = new String[]{
            "en",
            "ru"
    };

    @SideOnly(Side.CLIENT)
    public static List<String> getLines(Language language, int chapter, int article) throws IOException{
        Minecraft mc = Minecraft.getMinecraft();
        String lang = language.getLanguageCode().split("_")[0];
        if (!ArrayUtils.contains(languagesForTablet, lang))
            lang = "en";
        InputStream stream = mc.getResourceManager().getResource(new ResourceLocation("meem:tablet/lang/" + lang + "/c" + chapter + "/a" + article + ".lang")).getInputStream();
        return IOUtils.readLines(stream, Charsets.UTF_8);
    }

    public static HashMap<String, String> getLangMap(List<String> list){
        HashMap<String, String> result = new HashMap<>();
        for(String line : list){
            if (line.contains("=")){
                String[] splited = line.split("=");
                result.put(splited[0], splited[1]);
            }
        }
        return result;
    }
}
