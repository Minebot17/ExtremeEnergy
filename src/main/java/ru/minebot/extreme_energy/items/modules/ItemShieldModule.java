package ru.minebot.extreme_energy.items.modules;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.blocks.BlockShield;
import ru.minebot.extreme_energy.gui.elements.moduleGui.IModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.KeyModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.PowerModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.StateModuleGui;
import ru.minebot.extreme_energy.init.ModBlocks;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemShieldModule extends ModuleFunctional implements IChip, IKey, ISwordModule, IArmorCoreModule {

    public static IBlockState defState;
    public static HashMap<BlockPos, ShieldData> shields = new HashMap<>();
    public final int delay = 100;

    public ItemShieldModule() {
        super(Reference.ExtremeEnergyItems.MODULESHIELD.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULESHIELD.getRegistryName());
    }

    @Override
    public int getTier() {
        return 2;
    }

    private IBlockState getState(){
        if (defState == null)
            defState = ModBlocks.shield.getDefaultState().withProperty(BlockShield.isFC, true);
        return defState;
    }

    @Override
    public void onModuleActivated(ChipArgs args, int keyIndex) {
        if (!args.player.world.isRemote){
            int power = args.data.getInteger("power");
            int mode = args.data.getInteger("stateIndex");
            if (args.blocksRay != null && args.blocksRay.typeOfHit == RayTraceResult.Type.BLOCK) {
                double distance = args.player.getPositionVector().distanceTo(args.blocksRay.hitVec);

                if (mode == 0) {
                    BlockPos pos = args.blocksRay.getBlockPos().offset(args.blocksRay.sideHit);
                    Block block = args.player.world.getBlockState(pos).getBlock();
                    if (distance <= 5 && (isFree(block))) {
                        ModUtils.addShieldToRemove(args.player.world, pos, power * 40);
                        args.player.world.setBlockState(pos, ModBlocks.shield.getDefaultState());
                    }
                } else if (mode == 1 && distance <= 5) {
                    EnumFacing facing = args.player.getHorizontalFacing();
                    EnumFacing hit = args.blocksRay.sideHit;
                    EnumFacing right = facing.rotateY();
                    EnumFacing up = hit;
                    BlockPos pos = new BlockPos(0, 0, 0);
                    List<BlockPos> poses = new ArrayList<>();

                    poses.add(pos);
                    poses.add(pos.offset(right));
                    poses.add(pos.offset(right.getOpposite()));
                    poses.add(pos.offset(up));
                    if (power >= 2){
                        poses.add(pos.offset(right, 2));
                        poses.add(pos.offset(right).offset(up));
                        poses.add(pos.offset(right.getOpposite(), 2));
                        poses.add(pos.offset(right.getOpposite()).offset(up));
                    }
                    if (power >= 3){
                        poses.add(pos.offset(right.getOpposite(), 2).offset(up));
                        poses.add(pos.offset(right, 2).offset(up));
                        poses.add(pos.offset(up, 2));
                    }
                    if (power >= 4){
                        poses.add(pos.offset(right.getOpposite(), 3));
                        poses.add(pos.offset(right, 3));
                        poses.add(pos.offset(right.getOpposite()).offset(up, 2));
                        poses.add(pos.offset(right).offset(up, 2));
                    }

                    BlockPos p1 = args.blocksRay.getBlockPos().offset(args.blocksRay.sideHit);
                    for (BlockPos p2 : poses){
                        BlockPos newPos = new BlockPos(p1.getX() + p2.getX(), p1.getY() + p2.getY(), p1.getZ() + p2.getZ());
                        Block block = args.player.world.getBlockState(newPos).getBlock();
                        if (isFree(block)) {
                            ModUtils.addShieldToRemove(args.player.world, newPos, power * 40);
                            args.player.world.setBlockState(newPos, ModBlocks.shield.getDefaultState());
                        }
                    }
                }
            }

            BlockPos target = null;
            if (mode == 2){
                target = args.player.getPosition();
            }
            else if (mode == 3 && args.entityCollide != 0){
                target = args.player.world.getEntityByID(args.entityCollide).getPosition();
            }

            if (target != null){
                int radius = power + 2;
                double blocklen = 1;
                double todec=Math.PI/180d;
                double len=2d*Math.PI*radius;
                double count = len/blocklen;
                double angle = 180/count;
                for (double i = -count/2; i < count/2; i++){
                    int y = Math.round((float)(Math.sin(i * angle * todec) * radius));
                    double radius2 = Math.cos(i * angle * todec) * radius;
                    double len2 = 2 * Math.PI * radius2;
                    double count2 = len2 / blocklen;
                    double angle2 = 360 / count2;
                    for (int j = 0; j < count2; j++) {
                        int z = Math.round((float)(Math.cos(j * todec * angle2) * radius2));
                        int x = Math.round((float)(Math.sin(j * todec * angle2) * radius2));
                        BlockPos pos = new BlockPos(x + target.getX(), y + target.getY(), z + target.getZ());
                        Block block = args.player.world.getBlockState(pos).getBlock();
                        if (isFree(block)) {
                            ModUtils.addShieldToRemove(args.player.world, pos, power * 80);
                            args.player.world.setBlockState(pos, ModBlocks.shield.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getEnergy(ChipArgs args, int power, int keyIndex) {
        int mode = args.data.getInteger("stateIndex");
        return mode == 0 ? power * 50 : mode == 1 ? power * 200 : power * 500;
    }

    @Override
    public int[] getKeyCodes(NBTTagCompound data) {
        return new int[]{data.getInteger("keyCode")};
    }

    @Override
    public void onEntityHit(EntityLivingBase target, EntityLivingBase attacker, ItemStack sword, int power) {
        World world = attacker.world;
        EnumFacing facing = attacker.getHorizontalFacing();
        BlockPos pos = target.getPosition();
        for (int i = 0; i < 4; i++){
            BlockPos blockPos = pos.offset(facing).offset(facing).offset(EnumFacing.UP, i);
            placeShield(world, blockPos);
        }
        for (int i = 0; i < 3; i++){
            BlockPos blockPos = pos.offset(facing.rotateY()).offset(facing).offset(EnumFacing.UP, i);
            placeShield(world, blockPos);
        }
        for (int i = 0; i < 3; i++){
            BlockPos blockPos = pos.offset(facing.rotateY().getOpposite()).offset(facing).offset(EnumFacing.UP, i);
            placeShield(world, blockPos);
        }
        for (int i = 0; i < 2; i++){
            BlockPos blockPos = pos.offset(facing.rotateY()).offset(facing.rotateY()).offset(EnumFacing.UP, i);
            placeShield(world, blockPos);
        }
        for (int i = 0; i < 2; i++){
            BlockPos blockPos = pos.offset(facing.rotateY().getOpposite()).offset(facing.rotateY().getOpposite()).offset(EnumFacing.UP, i);
            placeShield(world, blockPos);
        }
    }

    private void placeShield(World world, BlockPos pos){
        if (world.isAirBlock(pos) || world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
            ModUtils.addShieldToRemove(world, pos, delay);
            world.setBlockState(pos, ModBlocks.shield.getDefaultState());
        }
    }

    @Override
    public int getEnergy(ItemStack sword, int power) {
        return 500;
    }

    @Override
    public void removeModule(FuncArgs args){
        if (!args.world.isRemote)
            removeShield(args);
    }

    @Override
    public void firstUpdate(FuncArgs args){
        if (!args.world.isRemote && args.world.getTotalWorldTime()%40 == 0){
            if (shields.containsKey(args.pos)) {
                if (shields.get(args.pos).isUpdate()){
                    removeShield(args);
                    createShield(args);
                }
                else
                    updateShield(args);
            }
            else
                createShield(args);
        }
    }

    @Override
    public void loadWorld(FuncArgs args, boolean active){
        for (int x = -args.radius; x <= args.radius; x++)
            for (int y = -args.radius; y <= args.radius; y++)
                for (int z = -args.radius; z <= args.radius; z++) {
                    BlockPos pos = new BlockPos(x + args.pos.getX(), y + args.pos.getY(), z + args.pos.getZ());
                    if (args.world.getBlockState(pos).equals(getState()))
                        args.world.setBlockToAir(pos);
                }
        if (active)
            createShield(args);
    }

    @Override
    public void changeRadius(FuncArgs args){
        if (shields.containsKey(args.pos))
            shields.get(args.pos).markDirty();
    }

    @Override
    public void changeVoltage(FuncArgs args){
        if (shields.containsKey(args.pos))
            shields.get(args.pos).markDirty();
    }

    @Override
    public void changeLink(FuncArgs args){
        if (shields.containsKey(args.pos))
            shields.get(args.pos).markDirty();
    }

    @Override
    public void changeActive(FuncArgs args, boolean isActive){
        if (isActive)
            createShield(args);
        else
            removeShield(args);
    }

    private void updateShield(FuncArgs args){
        if (shields.containsKey(args.pos)){
            List<BlockPos> poses = shields.get(args.pos).shields;
            for (BlockPos pos : poses)
                if (isFree(args.world, pos))
                    args.world.setBlockState(pos, getState());
        }
    }

    private void createShield(FuncArgs args){
        if (args.voltage != 0 && !shields.containsKey(args.pos)) {
            List<BlockPos> poses = new ArrayList<>();

            int radius = (int) ((float) args.radius * ((float) args.voltage / (100f * (float) args.radius)));
            if (radius > args.radius)
                radius = args.radius;

            double todec=Math.PI/180d;
            double len=2d*Math.PI*radius;
            double angle = 180/len;
            for (double i = -len/2; i < len/2; i++){
                int y = Math.round((float)(Math.sin(i * angle * todec) * radius));
                double radius2 = Math.cos(i * angle * todec) * radius;
                double len2 = 2 * Math.PI * radius2;
                double angle2 = 360 / len2;
                for (int j = 0; j < len2; j++) {
                    int z = Math.round((float)(Math.cos(j * todec * angle2) * radius2));
                    int x = Math.round((float)(Math.sin(j * todec * angle2) * radius2));
                    BlockPos pos = new BlockPos(x + args.pos.getX(), y + args.pos.getY(), z + args.pos.getZ());
                    Block block = args.world.getBlockState(pos).getBlock();
                    poses.add(pos);
                    if (isFree(block))
                        args.world.setBlockState(pos, getState());
                }
            }
            shields.put(args.pos, new ShieldData(poses));
        }
    }

    private void removeShield(FuncArgs args){
        if (shields.containsKey(args.pos)) {
            List<BlockPos> poses = shields.get(args.pos).shields;
            for (BlockPos pos : poses)
                if (isShield(args.world, pos))
                    args.world.setBlockToAir(pos);
            shields.remove(args.pos);
        }
    }

    @Override
    public int onImplantWork(ChipArgs args) {
        return 0;
    }

    @Override
    public void onDamaged(LivingHurtEvent event, Entity attacker, int power, NBTTagCompound data, EntityPlayer player) {
        float chance = power * 0.05f;
        if (ModUtils.random.nextFloat() <= chance)
            event.setAmount(0);
    }

    @Override
    public int getEnergy(int power) {
        return power * 2500;
    }

    private boolean isFree(Block block){
        return block == Blocks.AIR || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockVine;
    }

    private boolean isFree(World world, BlockPos pos){
        Block block = world.getBlockState(pos).getBlock();
        return block == Blocks.AIR || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockVine;
    }

    private boolean isShield(World world, BlockPos pos){
        return world.getBlockState(pos) == getState();
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Shield module","is active"};
    }

    @Override
    public IModuleGui[] getGui() {
        return new IModuleGui[]{
                new PowerModuleGui(),
                new StateModuleGui("Mode: ", new String[]{"Block", "Wall", "Self dome", "Enemy dome"}),
                new KeyModuleGui("Place shield: ", "keyCode")
        };
    }
    public class ShieldData {
        public List<BlockPos> shields;
        private boolean isUpdate;

        public ShieldData(List<BlockPos> shields){
            this.shields = shields;
        }

        public void markDirty(){
            isUpdate = true;
        }

        public boolean isUpdate(){
            boolean buffer = isUpdate;
            isUpdate = false;
            return buffer;
        }
    }
}
