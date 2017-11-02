package ru.minebot.extreme_energy.items.modules;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.entities.EntityWaterPart;
import ru.minebot.extreme_energy.gui.elements.moduleGui.IModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.KeyModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.StateModuleGui;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.*;

import java.util.HashMap;

public class ItemWaterModule extends ModuleFunctional implements IChip, IKey {
    private static HashMap<BlockPos, IBlockState> toReplace = new HashMap<>();

    public ItemWaterModule() {
        super(Reference.ExtremeEnergyItems.MODULEWATER.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULEWATER.getRegistryName());
    }

    @Override
    public int getTier() {
        return 1;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Water module", "is active"};
    }

    @Override
    public int onImplantWork(ChipArgs args) {
        if (!args.player.world.isRemote && args.energy > 1000){
            if (args.data.getInteger("stateIndex") == 0) {
                replace(args.player, true);

                int toEnergy = 0;
                BlockPos playerPos = args.player.getPosition().down();
                for (int x = -3; x <= 3; x++)
                    for (int z = -3; z <= 3; z++) {
                        BlockPos pos = new BlockPos(playerPos.getX() + x, playerPos.getY(), playerPos.getZ() + z);
                        IBlockState block = args.player.world.getBlockState(pos);
                        if (block.getBlock() == Blocks.WATER || block.getBlock() == Blocks.FLOWING_WATER) {
                            args.player.world.setBlockState(pos, Blocks.ICE.getDefaultState());
                            toReplace.put(pos, block);
                            toEnergy++;
                        }
                    }
                return toEnergy * 10;
            }
            else {
                args.player.addPotionEffect(new PotionEffect(Potion.getPotionById(13), 20, 0));
                return 10;
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
            if (!(player.world.getBlockState(pos) == Blocks.ICE.getDefaultState() && radius) || !ModUtils.inRadius(pos, player.getPosition(), 4)) {
                player.world.setBlockState(pos, state);
                toReplace.remove(pos);
            }
        }
    }

    @Override
    public void firstUpdate(FuncArgs args){
        if (args.world.getTotalWorldTime()%(10000/args.voltage)==0){
            for (int x = -1; x <= 1; x++)
                for (int z = -1; z <= 1; z++){
                    BlockPos pos = new BlockPos(args.pos.getX() + x, args.pos.getY() - 1, args.pos.getZ() + z);
                    if (args.world.isAirBlock(pos))
                        args.world.setBlockState(pos, Blocks.WATER.getDefaultState());
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
    public void onModuleActivated(ChipArgs args, int keyIndex) {
        if (!args.player.world.isRemote)
            args.player.world.spawnEntity(new EntityWaterPart(args.player, args.player.getLookVec(), 1f, 0.05f));    }

    @Override
    public int getEnergy(ChipArgs args, int power, int keyIndex) {
        return 10;
    }

    @Override
    public int[] getKeyCodes(NBTTagCompound data) {
        return new int[]{ data.getInteger("keyCode") };
    }
}
