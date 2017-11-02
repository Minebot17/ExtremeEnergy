package ru.minebot.extreme_energy.items.modules;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.gui.elements.moduleGui.IModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.PowerModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.StateModuleGui;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.*;

import java.util.List;

public class ItemAggressiveModule extends ModuleFunctional implements IChip, ISwordModule, IArmorCoreModule {
    private int power;

    public ItemAggressiveModule() {
        super(Reference.ExtremeEnergyItems.MODULEAGGRESSIVE.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULEAGGRESSIVE.getRegistryName());
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Aggression: " + (power + 1)};
    }

    @Override
    public void firstUpdate(FuncArgs args){
        power = getPower(args.voltage, args.radius, args.lossReduceCount);
        List<EntityPlayer> players = ModUtils.radiusFilterPlayers(args.pos, args.world.playerEntities, args.radius);
        if (!args.isPublic) {
            for (int j = 0; j < args.cards.length; j++)
                for (int i = 0; i < players.size(); i++)
                    if(players.get(i).getUniqueID().hashCode() == args.cards[j]) {
                        players.get(i).addPotionEffect(new PotionEffect(Potion.getPotionById(5), 20, power));
                        players.get(i).addPotionEffect(new PotionEffect(Potion.getPotionById(11), 20, power));
                    }
        }
        else{
            for (int i = 0; i < players.size(); i++) {
                players.get(i).addPotionEffect(new PotionEffect(Potion.getPotionById(5), 20, power));
                players.get(i).addPotionEffect(new PotionEffect(Potion.getPotionById(11), 20, power));
            }
        }
    }

    private int getPower(int voltage,  int radius, int lossReduce){
        return Math.round(voltage *0.0007f / ((float)radius/(10f/(lossReduce + 1)) + 1f));
    }

    @Override
    public int onImplantWork(ChipArgs args) {
        if (args.isModuleActive && args.energy > 1000){
            if (args.data.getInteger("stateIndex") == 0)
                args.player.addPotionEffect(new PotionEffect(Potion.getPotionById(5), 20, args.data.getInteger("power")-1));
            else
                args.player.addPotionEffect(new PotionEffect(Potion.getPotionById(11), 20, args.data.getInteger("power")-1));
            return args.data.getInteger("power") * 10;
        }
        return 0;
    }

    @Override
    public int getTier() {
        return 0;
    }

    @Override
    public IModuleGui[] getGui() {
        return new IModuleGui[]{
                new PowerModuleGui(),
                new StateModuleGui("Mode: ", new String[]{"Attack", "Defense"})
        };
    }

    @Override
    public void onEntityHit(EntityLivingBase target, EntityLivingBase attacker, ItemStack sword, int power) {
        target.addPotionEffect(new PotionEffect(Potion.getPotionById(18), 100, power-1));
    }

    @Override
    public int getEnergy(ItemStack sword, int power) {
        return 100;
    }

    @Override
    public void onDamaged(LivingHurtEvent event, Entity attacker, int power, NBTTagCompound tag, EntityPlayer player) {
        if (attacker instanceof EntityLivingBase){
            EntityLivingBase entityPlayer = (EntityLivingBase) attacker;
            entityPlayer.addPotionEffect(new PotionEffect(Potion.getPotionById(4), 20 * power, power-1));
        }
    }

    @Override
    public int getEnergy(int power) {
        return 500*power;
    }
}
