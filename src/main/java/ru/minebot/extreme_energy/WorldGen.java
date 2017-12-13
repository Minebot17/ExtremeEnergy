package ru.minebot.extreme_energy;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import ru.minebot.extreme_energy.init.ModBlocks;

import static ru.minebot.extreme_energy.init.ModConfig.*;
import java.util.Random;

public class WorldGen implements IWorldGenerator {

    private WorldGenerator smallCristalGen;
    private WorldGenerator cristalGen;
    private WorldGenerator bigCristalGen;

    private WorldGenerator copperGen;
    private WorldGenerator uraniumGen;

    public WorldGen(){
        smallCristalGen = new WorldGenMinable(ModBlocks.smallCristalOre.getDefaultState(), smallCristalOreSetting.count);
        cristalGen = new WorldGenMinable(ModBlocks.cristalOre.getDefaultState(), cristalOreSetting.count);
        bigCristalGen = new WorldGenMinable(ModBlocks.bigCristalOre.getDefaultState(), bigCristalOreSetting.count);

        copperGen = new WorldGenMinable(ModBlocks.copperOre.getDefaultState(), copperOreSetting.count);
        uraniumGen = new WorldGenMinable(ModBlocks.uraniumOre.getDefaultState(), uraniumOreSetting.count);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.getDimension() == 0 && enableGeneration){
            runGenerator(smallCristalGen, world, random, chunkX, chunkZ, smallCristalOreSetting.chance, smallCristalOreSetting.minHeight, smallCristalOreSetting.maxHeight);
            runGenerator(cristalGen, world, random, chunkX, chunkZ, cristalOreSetting.chance, cristalOreSetting.minHeight, cristalOreSetting.maxHeight);
            runGenerator(bigCristalGen, world, random, chunkX, chunkZ, bigCristalOreSetting.chance, bigCristalOreSetting.minHeight, bigCristalOreSetting.maxHeight);

            runGenerator(copperGen, world, random, chunkX, chunkZ, copperOreSetting.chance, copperOreSetting.minHeight, copperOreSetting.maxHeight);
            runGenerator(uraniumGen, world, random, chunkX, chunkZ, uraniumOreSetting.chance, uraniumOreSetting.minHeight, uraniumOreSetting.maxHeight);
        }
    }

    private void runGenerator(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {
        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        int heightDiff = maxHeight - minHeight + 1;
        for (int i = 0; i < chancesToSpawn; i ++) {
            int x = chunk_X * 16 + rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightDiff);
            int z = chunk_Z * 16 + rand.nextInt(16);
            generator.generate(world, rand, new BlockPos(x, y, z));
        }
    }
}
