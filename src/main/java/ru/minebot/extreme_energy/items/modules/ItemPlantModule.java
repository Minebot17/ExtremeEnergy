package ru.minebot.extreme_energy.items.modules;

import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockStem;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.gui.elements.moduleGui.IModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.PowerModuleGui;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.*;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketSpawnParticle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemPlantModule extends ModuleFunctional implements IChip {

    private static HashMap<BlockPos, Integer> poses = new HashMap<>();

    public ItemPlantModule() {
        super(Reference.ExtremeEnergyItems.MODULEPLANT.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULEPLANT.getRegistryName());
    }

    @Override
    public void firstUpdate(FuncArgs args){
        if (!args.world.isRemote && args.world.getTotalWorldTime()%5==0){
            int maxDelay = (int)((float)args.radius/(float)args.voltage * 5000f);
            Integer delay = poses.computeIfAbsent(args.pos, k -> maxDelay);

            if (delay > maxDelay)
                delay = maxDelay;

            if (delay <= 0){
                poses.put(args.pos, maxDelay);
                List<BlockPos> growables = new ArrayList<>();
                for (int x = -args.radius; x <= args.radius; x++)
                    for (int y = -args.radius; y <= args.radius; y++)
                        for (int z = -args.radius; z <= args.radius; z++) {
                            BlockPos pos = new BlockPos(args.pos.getX() + x, args.pos.getY() + y, args.pos.getZ() + z);
                            IBlockState plant = args.world.getBlockState(pos);
                            if ((plant.getBlock() instanceof BlockCrops || plant.getBlock() instanceof BlockStem || plant.getBlock() instanceof BlockCocoa) && ((IGrowable) plant.getBlock()).canGrow(args.world, pos, plant, false))
                                growables.add(pos);
                        }
                if (growables.size() != 0) {
                    int index = ModUtils.random.nextInt(growables.size());
                    BlockPos pos = growables.get(index);
                    IBlockState state = args.world.getBlockState(pos);
                    ((IGrowable)state.getBlock()).grow(args.world, ModUtils.random, pos, state);
                    NetworkWrapper.instance.sendToAllAround(
                            new PacketSpawnParticle(
                                    EnumParticleTypes.HEART.getParticleID(),
                                    pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f,
                                    0f, 0.1f, 0f,
                                    5, 0.3f, 0.01f
                            ),
                            new NetworkRegistry.TargetPoint(0, pos.getX(), pos.getY(), pos.getZ(), 64)
                    );
                }
            }
            else {
                delay--;
                poses.replace(args.pos, delay);
            }
        }
    }

    @Override
    public int getTier() {
        return 1;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Module is","active"};
    }

    @Override
    public int onImplantWork(ChipArgs args) {
        if (args.isModuleActive && args.energy > 1000) {
            if (!args.player.world.isRemote && args.player.world.getTotalWorldTime() % 20 == 0) {
                int radius = args.data.getInteger("radius");
                if (radius == 0){
                    radius = 0;
                    args.data.setInteger("radius", 1);
                }
                List<BlockPos> growables = new ArrayList<>();
                for (int x = -radius; x <= radius; x++)
                    for (int y = -radius; y <= radius; y++)
                        for (int z = -radius; z <= radius; z++) {
                            BlockPos pos = new BlockPos(args.player.getPosition().getX() + x, args.player.getPosition().getY() + y, args.player.getPosition().getZ() + z);
                            IBlockState plant = args.player.world.getBlockState(pos);
                            if (plant.getBlock() instanceof IGrowable && plant.getBlock() == Blocks.GRASS && ((IGrowable) plant.getBlock()).canGrow(args.player.world, pos, plant, false))
                                growables.add(pos);
                        }
                if (growables.size() != 0) {
                    int index = ModUtils.random.nextInt(growables.size());
                    BlockPos pos = growables.get(index);
                    IBlockState state = args.player.world.getBlockState(pos);
                    ((IGrowable) state.getBlock()).grow(args.player.world, ModUtils.random, pos, state);
                    NetworkWrapper.instance.sendToAllAround(
                            new PacketSpawnParticle(
                                    EnumParticleTypes.HEART.getParticleID(),
                                    pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                                    0f, 0.1f, 0f,
                                    5, 0.3f, 0.01f
                            ),
                            new NetworkRegistry.TargetPoint(0, pos.getX(), pos.getY(), pos.getZ(), 64)
                    );
                }
                return growables.size()*10;
            }
        }
        return 0;
    }

    @Override
    public IModuleGui[] getGui() {
        return new IModuleGui[]{new PowerModuleGui("Radius: ", "radius", 5)};
    }

    @Override
    public void removeModule(FuncArgs args){
        if (poses.containsKey(args.pos))
            poses.remove(args.pos);
    }
}
