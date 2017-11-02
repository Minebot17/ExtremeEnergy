package ru.minebot.extreme_energy;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import ru.minebot.extreme_energy.init.ModBlocks;

import java.util.Random;

public class WorldGen implements IWorldGenerator {

    private WorldGenerator smallCristalGen;
    private WorldGenerator cristalGen;
    private WorldGenerator bigCristalGen;

    private WorldGenerator copperGen;
    private WorldGenerator uraniumGen;

    public WorldGen(){
        smallCristalGen = new WorldGenMinable(ModBlocks.smallCristalOre.getDefaultState(), 4);
        cristalGen = new WorldGenMinable(ModBlocks.cristalOre.getDefaultState(), 3);
        bigCristalGen = new WorldGenMinable(ModBlocks.bigCristalOre.getDefaultState(), 3);

        copperGen = new WorldGenMinable(ModBlocks.copperOre.getDefaultState(), 5);
        uraniumGen = new WorldGenMinable(ModBlocks.uraniumOre.getDefaultState(), 3);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.getDimension() == 0){
            runGenerator(smallCristalGen, world, random, chunkX, chunkZ, 50, 30, 60);
            runGenerator(cristalGen, world, random, chunkX, chunkZ, 40, 15, 40);
            runGenerator(bigCristalGen, world, random, chunkX, chunkZ, 20, 0, 15);

            runGenerator(copperGen, world, random, chunkX, chunkZ, 50, 10, 50);
            runGenerator(uraniumGen, world, random, chunkX, chunkZ, 40, 2, 30);
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
