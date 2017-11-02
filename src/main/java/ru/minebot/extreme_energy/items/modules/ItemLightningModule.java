package ru.minebot.extreme_energy.items.modules;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.lwjgl.input.Keyboard;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.gui.elements.moduleGui.IModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.KeyModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.PowerModuleGui;
import ru.minebot.extreme_energy.init.LightningEvents;
import ru.minebot.extreme_energy.init.ModConfig;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.*;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketSpawnChain;
import ru.minebot.extreme_energy.network.packages.PacketSpawnLightning;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemLightningModule extends ModuleFunctional implements IChip, IKey, ISwordModule, IArmorCoreModule {
    private static Vec3d pressedCoords = null;

    public ItemLightningModule() {
        super(Reference.ExtremeEnergyItems.MODULELIGHTNING.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULELIGHTNING.getRegistryName());
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public void firstUpdate(FuncArgs args){
        if (!args.world.isRemote && args.world.getTotalWorldTime()%20==0){
            ArrayList<Entity> entities = new ArrayList<>();
            for (Entity entity : ModUtils.radiusFilterEntities(args.pos, args.world.loadedEntityList, args.radius))
                if (entity instanceof EntityLivingBase)
                    entities.add(entity);

            LightningEvents.Type type = getType(args.voltage/args.radius);
            int damage = args.voltage/(16*args.radius);

            List<Entity> toRemove = new ArrayList<>();
            for (Entity entity : entities){
                if (entity instanceof EntityPlayer) {
                    if (args.isPublic) {
                        toRemove.add(entity);
                        continue;
                    }
                    else if (ModUtils.empty(args.cards) || ModUtils.contains(args.cards, entity.getUniqueID().hashCode())) {
                        toRemove.add(entity);
                        continue;
                    }
                }

                RayTraceResult ray = args.world.rayTraceBlocks(new Vec3d(args.pos).add((entity.getPositionVector().subtract(new Vec3d(args.pos))).normalize()).addVector(0.5f, 0.5f, 0.5f), entity.getPositionVector(), false, true, false);
                if (ray != null)
                    toRemove.add(entity);
            }

            EntityLivingBase livingBase = null;
            entities.removeAll(toRemove);
            if (entities.size() != 0)
                livingBase = (EntityLivingBase) ModUtils.getNearestEntity(entities, args.pos);
            if (livingBase != null) {
                ModUtils.addEnergyToChunk(args.world, new ChunkPos(livingBase.getPosition()), args.voltage/5);
                NetworkWrapper.instance.sendToAllAround(
                        new PacketSpawnLightning(new Vec3d(args.pos.up()).addVector(0.5f, 0, 0.5f), livingBase.getPositionVector(), type),
                        new NetworkRegistry.TargetPoint(0, args.pos.getX(), args.pos.getY(), args.pos.getZ(), 64)
                );

                livingBase.attackEntityFrom(DamageSource.LIGHTNING_BOLT, damage);
                livingBase.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("meem:electricShock"), 20, 1));
            }
        }
    }

    @Override
    public void onModuleActivated(ChipArgs args, int keyIndex) {
        if (!args.player.world.isRemote && args.blocksRay != null && args.data.getInteger("KD") <= 0 && keyIndex == 0){
            args.data.setInteger("KD", 15);
            int voltage = args.data.getInteger("power");
            int maxDist = (int) ((10000f / ((float) voltage + 2000f)) * 100f - 320f);
            Vec3d to = args.blocksRay.hitVec;
            if (to.distanceTo(args.player.getPositionVector()) < maxDist) {
                ModUtils.addEnergyToChunk(args.player.world, new ChunkPos(new BlockPos(to)), voltage/5);
                NetworkWrapper.instance.sendToAllAround(new PacketSpawnLightning(args.player.getPositionVector().addVector(0, 1, 0), to, getType(voltage)), ModUtils.getTargetPoint(args.player, 64));

                Entity entity = args.player.world.getEntityByID(args.entityCollide);
                if (entity != null && entity.getEntityId() != args.player.getEntityId()) {
                    EntityLivingBase livingBase = (EntityLivingBase) args.player.world.getEntityByID(args.entityCollide);
                    int damage = (int) ((float) voltage / 100f);
                    livingBase.attackEntityFrom(DamageSource.LIGHTNING_BOLT, damage);
                    livingBase.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("meem:electricShock"), 40, 0));
                }
            }
        }
        if (args.player.world.isRemote && args.blocksRay != null && keyIndex == 1 && pressedCoords == null){
            pressedCoords = args.blocksRay.hitVec;
        }
    }

    private LightningEvents.Type getType(int voltage){
        if (voltage >= 700)
            return LightningEvents.Type.BIG;
        else if (voltage >= 300)
            return LightningEvents.Type.STANDART;
        else
            return LightningEvents.Type.SMALL;
    }

    @Override
    public int getEnergy(ChipArgs args, int power, int keyIndex) {
        if (keyIndex == 0) {
            int voltage = args.data.getInteger("power");
            int maxDist = (int) ((10000f / ((float) voltage + 2000f)) * 100f - 320f);
            return args.blocksRay.hitVec.distanceTo(args.player.getPositionVector()) < maxDist ? power * 5 : 0;
        }
        else if (keyIndex == 1) {
            int voltage = args.data.getInteger("power");
            return voltage*5;
        }
        return 0;
    }

    @Override
    public int[] getKeyCodes(NBTTagCompound data) {
        return new int[]{
                data.getInteger("keyCode1"),
                data.getInteger("keyCode2")
        };
    }

    @Override
    public void onEntityHit(EntityLivingBase target, EntityLivingBase attacker, ItemStack sword, int power) {
        if (!attacker.world.isRemote){
            float chance = (float)power/4f;
            if (chance > ModUtils.random.nextFloat()){
                List<EntityLivingBase> checked = new ArrayList<>();
                EntityLivingBase lastEntity = attacker;
                EntityLivingBase entity = target;
                ModUtils.addEnergyToChunk(attacker.world, new ChunkPos(target.getPosition()), power*100);
                for (int i = 0; i < power*2; i++){
                    NetworkWrapper.instance.sendToAllAround(new PacketSpawnLightning(lastEntity.getPositionVector().addVector(0, 1, 0), entity.getPositionVector(), LightningEvents.Type.SMALL), ModUtils.getTargetPoint(attacker, 64));
                    entity.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("meem:electricShock"), 40, 0));
                    checked.add(entity);
                    EntityLivingBase e = (EntityLivingBase) ModUtils.getNearestEntity(attacker.world.getEntities(EntityLivingBase.class, en -> !checked.contains(en)), entity.getPosition());
                    if (e != null && e.getPositionVector().distanceTo(entity.getPositionVector()) <= power) {
                        lastEntity = entity;
                        entity = e;
                    }
                    else break;
                }
            }
        }
    }

    @Override
    public int getEnergy(ItemStack sword, int power) {
        return power*1000;
    }

    @Override
    public int onImplantWork(ChipArgs args) {
        if (args.player.world.isRemote && pressedCoords != null){
            if (args.blocksRay == null)
                return 0;

            Vec3d end = args.blocksRay.hitVec;
            float dist = (float)pressedCoords.distanceTo(end);
            int voltage = args.data.getInteger("power");
            int time = getLightningType(getDamage(voltage, dist)).time;
            if (Keyboard.isKeyDown(args.data.getInteger("keyCode2")) && args.player.world.getTotalWorldTime()%time==0){
                LightningEvents.Type type = getLightningType(getDamage(voltage, dist));
                if (dist > 1)
                    LightningEvents.spawnLightning(args.player.world, pressedCoords, end, type);
            }
            else if (!Keyboard.isKeyDown(args.data.getInteger("keyCode2"))){
                int damage = getDamage(voltage, dist);
                int maxAge = getMaxAge(voltage, dist);
                if (dist > 1)
                    NetworkWrapper.instance.sendToServer(new PacketSpawnChain(pressedCoords, end, maxAge, damage));
                pressedCoords = null;
            }
        }
        if (!args.player.world.isRemote){
            args.data.setInteger("KD", args.data.getInteger("KD")-1);
        }
        return 0;
    }

    private int getMaxAge(float voltage, float dist){
        return (int)(voltage/6f/(dist/3f));
    }

    private int getDamage(float voltage, float dist){
        return (int)(voltage/10f/dist);
    }

    private LightningEvents.Type getLightningType(int damage){
        return  damage <= 2 ? LightningEvents.Type.TINY : damage <= 4 ? LightningEvents.Type.SMALL : damage <= 6 ? LightningEvents.Type.STANDART : LightningEvents.Type.BIG;
    }

    @Override
    public void onDamaged(LivingHurtEvent event, Entity attacker, int power, NBTTagCompound data, EntityPlayer player) {
        if (!player.world.isRemote){
            ModUtils.addEnergyToChunk(player.world, new ChunkPos(player.getPosition()), power*20);
            float chance = (float)power/10f;
            List<EntityLivingBase> entities = new ArrayList<>();
            for (Entity entity : ModUtils.radiusFilterEntities(player.getPosition(), player.world.loadedEntityList, 5))
                if (entity != player && entity instanceof EntityLivingBase && chance > ModUtils.random.nextFloat())
                    entities.add((EntityLivingBase) entity);

            for (EntityLivingBase entity : entities){
                NetworkWrapper.instance.sendToAllAround(new PacketSpawnLightning(player.getPositionVector().addVector(0, 1, 0), entity.getPositionVector(), LightningEvents.Type.SMALL), ModUtils.getTargetPoint(player, 64));
                entity.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("meem:electricShock"), 40, 0));
            }
        }
    }

    @Override
    public int getEnergy(int power) {
        return power*1000;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Module is active"};
    }

    @Override
    public IModuleGui[] getGui() {
        return new IModuleGui[]{
                new PowerModuleGui("Votage: ", "power", 1000),
                new KeyModuleGui("Discharge: ", "keyCode1"),
                new KeyModuleGui("Chain: ", "keyCode2")
        };
    }
}
