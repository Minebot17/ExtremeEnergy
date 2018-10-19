package ru.minebot.extreme_energy.items.modules;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.gui.elements.moduleGui.IModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.KeyModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.PowerModuleGui;
import ru.minebot.extreme_energy.init.ModConfig;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.*;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketFrequencyTeleport;
import ru.minebot.extreme_energy.network.packages.PacketPlaySound;
import ru.minebot.extreme_energy.other.ChargeSaveData;

import java.util.HashMap;

public class ItemTeleportModule extends ModuleFunctional implements IChip, ISwordModule, IKey, IArmorCoreModule {
    private static boolean isPressed;
    private static HashMap<BlockPos, Integer> poses = new HashMap<>();

    public ItemTeleportModule() {
        super(Reference.ExtremeEnergyItems.MODULETELEPORT.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULETELEPORT.getRegistryName());
    }

    @Override
    public void firstUpdate(FuncArgs args){
        if (args.world.isRemote){
            firstUpdateClient(args);
        }
    }

    @SideOnly(Side.CLIENT)
    private void firstUpdateClient(FuncArgs args){
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (ModUtils.inRadius(args.pos, player.getPosition(), 64)) {
            int radius = args.radius > ModConfig.maxTeleportRadius ? ModConfig.maxTeleportRadius : args.radius;
            if (ModUtils.inRadius(args.pos, player.getPosition(), radius) && (args.isPublic || ModUtils.contains(args.cards, player.getUniqueID().hashCode()))) {
                Minecraft mc = Minecraft.getMinecraft();
                Vec3d pos = new Vec3d(
                        mc.player.getPositionVector().x + (ModUtils.random.nextFloat()-0.5f),
                        mc.player.getPositionVector().y + (ModUtils.random.nextFloat()-0.5f)*2f + 0.4f,
                        mc.player.getPositionVector().z + (ModUtils.random.nextFloat()-0.5f)
                );
                Vec3d speed = new Vec3d(args.pos).addVector(0.5f, 0.5f, 0.5f).subtract(pos).normalize().scale(0.1f);
                mc.effectRenderer.spawnEffectParticle(ModConfig.portalParticleID, pos.x, pos.y, pos.z, speed.x, speed.y, speed.z);

                Integer cd = poses.computeIfAbsent(args.pos, k -> radius*25);

                if (cd <= 0) {
                    poses.remove(args.pos);
                    NetworkWrapper.instance.sendToServer(new PacketFrequencyTeleport(args.pos, args.frequency));
                } else {
                    cd--;
                    poses.replace(args.pos, cd);
                }
            }
            else if (poses.containsKey(args.pos))
                poses.remove(args.pos);
        }
    }

    @Override
    public void removeModule(FuncArgs args){
        if (poses.containsKey(args.pos))
            poses.remove(args.pos);
    }

    @Override
    public int getTier() {
        return 1;
    }

    @Override
    public void onModuleActivated(ChipArgs args, int keyIndex) {
        if (args.blocksRay != null && args.blocksRay.typeOfHit == RayTraceResult.Type.BLOCK){
            if (args.data.getInteger("power") == 0) {
                args.data.setInteger("power", 1);
            }
            BlockPos playerPos = args.player.getPosition();
            int power = args.data.getInteger("power");
            BlockPos result = args.blocksRay.getBlockPos().offset(args.blocksRay.sideHit);
            if (args.blocksRay.sideHit == EnumFacing.DOWN)
                result.down();
            if (isCorrect(playerPos, result, 2, power * 20)){
                if (!args.player.world.isRemote) {
                    ModUtils.addEnergyToChunk(args.player.world, new ChunkPos(new BlockPos(result)), power * 200);
                    args.player.setPositionAndUpdate(result.getX(), result.getY(), result.getZ());
                    Vec3d particlePos = args.player.getPositionVector();
                    NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(0, playerPos.getX(), playerPos.getY(), playerPos.getZ(), 64);
                    NetworkWrapper.instance.sendToAllAround(new PacketPlaySound(SoundEvent.REGISTRY.getIDForObject(SoundEvents.ENTITY_ENDERMEN_TELEPORT), (float) particlePos.x, (float) particlePos.y, (float) particlePos.z), point);
                }
                else {
                    createParticles(args, result);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void createParticles(ChipArgs args, BlockPos result){
        Vec3d particlePos = args.player.getPositionVector();
        Vec3d vec = new Vec3d(result).subtract(particlePos).normalize().scale(0.1f);
        ParticleManager manager = Minecraft.getMinecraft().effectRenderer;
        for (int i = 0; i < 100; i++)
            manager.spawnEffectParticle(ModConfig.portalParticleID,
                    particlePos.x+ModUtils.random.nextFloat()-0.5f,
                    particlePos.y+ModUtils.random.nextFloat()*2f-1f,
                    particlePos.z+ModUtils.random.nextFloat()-0.5f,
                    vec.x, vec.y, vec.z
            );
    }

    private boolean isCorrect(BlockPos result, BlockPos playerPos, double min, double max) {
        double distance = playerPos.getDistance(result.getX(), result.getY(), result.getZ());
        return distance > min && distance < max;
    }

    @Override
    public int getEnergy(ChipArgs args, int power, int keyIndex) {
        return 10000 * power;
    }

    @Override
    public int[] getKeyCodes(NBTTagCompound data) {
        return new int[]{data.getInteger("keyTeleportCode")};
    }

    @Override
    public void onEntityHit(EntityLivingBase target, EntityLivingBase attacker, ItemStack sword, int power) {
        int a = 0;
        int x = ModUtils.random.nextInt(5*power) + target.getPosition().getX();
        int y = ModUtils.random.nextInt(5*power) + target.getPosition().getY();
        int z = ModUtils.random.nextInt(5*power) + target.getPosition().getZ();
        while (!isNotSafe(target.world, new BlockPos(x, y, z)) && a < 250){
            x = ModUtils.random.nextInt(5*power) + target.getPosition().getX();
            y = ModUtils.random.nextInt(5*power) + target.getPosition().getY();
            z = ModUtils.random.nextInt(5*power) + target.getPosition().getZ();
            a++;
        }
        ModUtils.addEnergyToChunk(attacker.world, new ChunkPos(attacker.getPosition()), power*20);
        target.world.spawnParticle(EnumParticleTypes.END_ROD, target.getPositionVector().x, target.getPositionVector().y, target.getPositionVector().z, 0.1f,0.1f,0.1f, 20);
        target.setPosition(x, y, z);
        target.world.spawnParticle(EnumParticleTypes.END_ROD, target.getPositionVector().x, target.getPositionVector().y, target.getPositionVector().z, 0.1f,0.1f,0.1f, 20);
    }

    @Override
    public int getEnergy(ItemStack sword, int power) {
        return power*250;
    }

    @Override
    public void onDamaged(LivingHurtEvent event, Entity attacker, int power, NBTTagCompound data, EntityPlayer player) {
        if (player.getHealth() < 4){
            int a = 0;
            int x = ModUtils.random.nextInt(10*power) + player.getPosition().getX();
            int y = ModUtils.random.nextInt(10*power) + player.getPosition().getY();
            int z = ModUtils.random.nextInt(10*power) + player.getPosition().getZ();
            while (!isSafe(player.world, new BlockPos(x, y, z)) && a < 250){
                x = ModUtils.random.nextInt(10*power) + player.getPosition().getX();
                y = ModUtils.random.nextInt(10*power) + player.getPosition().getY();
                z = ModUtils.random.nextInt(10*power) + player.getPosition().getZ();
                a++;
            }
            ModUtils.addEnergyToChunk(player.world, new ChunkPos(player.getPosition()), power*20);
            player.world.spawnParticle(EnumParticleTypes.END_ROD, player.getPositionVector().x, player.getPositionVector().y, player.getPositionVector().z, 0.1f,0.1f,0.1f, 20);
            player.setPositionAndUpdate(x, y, z);
            player.world.spawnParticle(EnumParticleTypes.END_ROD, player.getPositionVector().x, player.getPositionVector().y, player.getPositionVector().z, 0.1f,0.1f,0.1f, 20);
        }
    }

    private boolean isSafe(World world, BlockPos pos){
        boolean saveChunk = ChargeSaveData.getOrCreateData(world).map.get(new ChunkPos(pos)) < ModConfig.maxCapOfChunk;
        Block top = world.getBlockState(pos.up()).getBlock();
        Block center = world.getBlockState(pos).getBlock();
        Block down = world.getBlockState(pos.down()).getBlock();
        return saveChunk && center != Blocks.FIRE && down != Blocks.CACTUS && top == Blocks.AIR && center == Blocks.AIR && down != Blocks.AIR && down != Blocks.LAVA;
    }

    private boolean isNotSafe(World world, BlockPos pos){
        IBlockState center = world.getBlockState(pos);
        IBlockState down = world.getBlockState(pos.down());
        return center == Blocks.FIRE || down == Blocks.FIRE || down == Blocks.CACTUS || down == Blocks.LAVA || center == Blocks.LAVA;
    }

    @Override
    public int getEnergy(int power) {
        return 500 * power;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Teleport is", "enabled"};
    }

    @Override
    public int onImplantWork(ChipArgs args) {
        return 0;
    }

    @Override
    public IModuleGui[] getGui() {
        return new IModuleGui[]{
                new PowerModuleGui(),
                new KeyModuleGui("Teleport key: ", "keyTeleportCode")
        };
    }
}
