package ru.minebot.extreme_energy.events.events_chunk;

import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import ru.minebot.extreme_energy.init.LightningEvents;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketSpawnLightning;
import ru.minebot.extreme_energy.tile_entities.TileEntityLightningRod;

import java.util.ArrayList;
import java.util.List;

public class LightningFromSky implements IEventChunk {
    private static int tryCount = 0;

    @Override
    public float getChance(int value) {
        return -(1f/100000000f)*value*value + 0.025f;
    }

    @Override
    public int onEvent(World world, EntityPlayer player) {
        if (tryCount > 100){
            tryCount = 0;
            return 0;
        }

        BlockPos pos = new ChunkPos(player.getPosition())
                .getBlock(0, 200, 0)
                .add(ModUtils.random.nextInt(16), 0, ModUtils.random.nextInt(16));

        RayTraceResult result = world.rayTraceBlocks(new Vec3d(pos).addVector(0.5f, 0, 0.5f), new Vec3d(pos.add(0, -200, 0)).addVector(0.5f, 0, 0.5f), false, true, false);
        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK){
            BlockPos begin = new BlockPos(pos.getX() + ModUtils.random.nextInt(4)-2, 255, pos.getZ() + ModUtils.random.nextInt(4)-2);
            BlockPos end = result.getBlockPos();

            List<TileEntity> tileEntityList = world.loadedTileEntityList;
            List<TileEntityLightningRod> lightningRods = new ArrayList<>();
            for (TileEntity tile : tileEntityList){
                if (tile instanceof TileEntityLightningRod && ModUtils.inRadius(tile.getPos(), end, ((TileEntityLightningRod)tile).pillars+3))
                    lightningRods.add((TileEntityLightningRod) tile);
            }
            if (lightningRods.size() != 0){
                TileEntityLightningRod rod = lightningRods.get(ModUtils.random.nextInt(lightningRods.size()));
                RayTraceResult ray = world.rayTraceBlocks(new Vec3d(begin).addVector(0.5f, -55, 0.5f), new Vec3d(rod.getPos().add(0, rod.pillars, 0)), false, true, false);
                if (ray == null){
                    rod.receiveEnergy(ModUtils.random.nextInt(100000) + 50000, false);
                    end = rod.getPos().add(0, rod.pillars, 0);
                }
            }

            NetworkWrapper.instance.sendToAllAround(new PacketSpawnLightning(
                    new Vec3d(begin),
                    new Vec3d(end).addVector(0.5f, 1f, 0.25f),
                    LightningEvents.Type.BIG
            ), new NetworkRegistry.TargetPoint(0, end.getX(), end.getY(), end.getZ(), 64));
            if (lightningRods.size() == 0 && (world.getBlockState(end).getBlock() != Blocks.AIR && !(world.getBlockState(end).getBlock() instanceof BlockLiquid)) && world.getBlockState(end.up()).getBlock() == Blocks.AIR)
                world.setBlockState(end.up(), Blocks.FIRE.getDefaultState());
            BlockPos end2 = end;
            List<EntityLivingBase> entities = world.getEntities(EntityLivingBase.class, input -> ModUtils.inRadius(end2, input.getPosition(), 2));
            for (EntityLivingBase entity : entities)
                entity.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("meem:electricShock"), 60, 2));
            return lightningRods.size() == 0 ? -100000 : 0;
        }
        else {
            tryCount++;
            return onEvent(world, player);
        }
    }
}
