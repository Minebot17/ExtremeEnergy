package ru.minebot.extreme_energy.items.modules;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.entities.EntityLavaPart;
import ru.minebot.extreme_energy.gui.elements.moduleGui.IModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.KeyModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.StateModuleGui;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.*;

import java.util.HashMap;

public class ItemLavaModule extends ModuleFunctional implements IChip, IArmorCoreModule, ISwordModule, IKey {
    private static HashMap<BlockPos, IBlockState> toReplace = new HashMap<>();

    public ItemLavaModule() {
        super(Reference.ExtremeEnergyItems.MODULELAVA.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULELAVA.getRegistryName());
    }

    @Override
    public int getTier() {
        return 1;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Lava module", "is active"};
    }

    @Override
    public int onImplantWork(ChipArgs args) {
        if (!args.player.world.isRemote && args.energy > 1000){
            if (args.data.getInteger("stateIndex") == 0) {
                replace(args.player, true);

                int toEnergy = 0;
                BlockPos playerPos = args.player.getPosition().down();
                for (int x = -2; x <= 2; x++)
                    for (int z = -2; z <= 2; z++) {
                        BlockPos pos = new BlockPos(playerPos.getX() + x, playerPos.getY(), playerPos.getZ() + z);
                        IBlockState block = args.player.world.getBlockState(pos);
                        if (block.getBlock() == Blocks.LAVA || block.getBlock() == Blocks.FLOWING_LAVA) {
                            args.player.world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
                            toReplace.put(pos, block);
                            toEnergy++;
                        }
                    }
                return toEnergy * 50;
            }
            else {
                args.player.addPotionEffect(new PotionEffect(Potion.getPotionById(12), 20, 0));
                return 50;
            }
        }
        return 0;
    }

    @Override
    public void changeActive(EntityPlayer player, boolean isOn){
        if(toReplace.size() != 0)
            replace(player, false);
    }

    private void replace(EntityPlayer player, boolean radius){
        BlockPos[] array = new BlockPos[toReplace.size()];
        array = toReplace.keySet().toArray(array);
        for (int i = 0; i < toReplace.size(); i++) {
            BlockPos pos = array[i];
            IBlockState state = toReplace.get(array[i]);
            if (!(player.world.getBlockState(pos) == Blocks.OBSIDIAN.getDefaultState() && radius) || !ModUtils.inRadius(pos, player.getPosition(), 3)) {
                player.world.setBlockState(pos, state);
                toReplace.remove(pos);
            }
        }
    }

    @Override
    public void firstUpdate(FuncArgs args){
        if (args.world.getTotalWorldTime()%(100000/args.voltage)==0){
            for (int x = -1; x <= 1; x++)
                for (int z = -1; z <= 1; z++){
                    BlockPos pos = new BlockPos(args.pos.getX() + x, args.pos.getY() - 1, args.pos.getZ() + z);
                    if (args.world.isAirBlock(pos))
                        args.world.setBlockState(pos, Blocks.LAVA.getDefaultState());
                }
        }
    }

    @Override
    public IModuleGui[] getGui() {
        return new IModuleGui[]{
                new StateModuleGui("Mode: ", new String[]{"Walk", "Resist"}),
                new KeyModuleGui("Breath: ", "keyCode")
        };
    }

    @Override
    public void onEntityHit(EntityLivingBase target, EntityLivingBase attacker, ItemStack sword, int power) {
        target.setFire(power*2);
    }

    @Override
    public int getEnergy(ItemStack sword, int power) {
        return 250 * power;
    }

    @Override
    public void onDamaged(LivingHurtEvent event, Entity attacker, int power, NBTTagCompound data, EntityPlayer player) {
        if (attacker instanceof EntityLivingBase){
            EntityLivingBase livingBase = (EntityLivingBase)attacker;
            livingBase.setFire(power);
        }
    }

    @Override
    public int getEnergy(int power) {
        return 250 * power;
    }

    @Override
    public void onModuleActivated(ChipArgs args, int keyIndex) {
        if (!args.player.world.isRemote)
            args.player.world.spawnEntity(new EntityLavaPart(args.player, args.player.getLookVec(), 1f, 0.05f));
    }

    @Override
    public int getEnergy(ChipArgs args, int power, int keyIndex) {
        return 100;
    }

    @Override
    public int[] getKeyCodes(NBTTagCompound data) {
        return new int[]{ data.getInteger("keyCode") };
    }
}
