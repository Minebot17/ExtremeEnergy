package ru.minebot.extreme_energy.modules;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FuncArgs{
    public World world;
    public BlockPos pos;
    public int radius;
    public int frequency;
    public int voltage;
    public int lossReduceCount;
    public boolean isPublic;
    public int[] cards;
    public FuncArgs(World world, BlockPos pos, int frequency, int radius, int voltage, int lossReduceCount){
        this.world = world;
        this.pos = pos;
        this.radius = radius;
        this.voltage = voltage;
        this.lossReduceCount = lossReduceCount;
        this.isPublic = true;
        this.cards = null;
        this.frequency = frequency;
    }
    public FuncArgs(World world, BlockPos pos, int frequency, int radius, int voltage, int lossReduceCount, int[] cards){
        this.world = world;
        this.pos = pos;
        this.radius = radius;
        this.voltage = voltage;
        this.lossReduceCount = lossReduceCount;
        this.isPublic = false;
        this.cards = cards;
        this.frequency = frequency;
    }
    public FuncArgs(World world, BlockPos pos, int frequency, int radius, int voltage, int lossReduceCount, boolean isPublic, int[] cards){
        this.world = world;
        this.pos = pos;
        this.radius = radius;
        this.voltage = voltage;
        this.lossReduceCount = lossReduceCount;
        this.isPublic = isPublic;
        this.cards = cards;
        this.frequency = frequency;
    }
}
