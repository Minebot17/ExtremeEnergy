package ru.minebot.extreme_energy.items.modules;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.gui.elements.moduleGui.IModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.PowerModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.StateModuleGui;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.*;

import java.util.List;

public class ItemElectricalModule extends ModuleFunctional implements IChip, ISwordModule, IArmorCoreModule {
    private int power;

    public ItemElectricalModule() {
        super(Reference.ExtremeEnergyItems.MODULEELECTRIC.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULEELECTRIC.getRegistryName());
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Electric power: " + (power + 1)};
    }

    @Override
    public void firstUpdate(FuncArgs args){
        power = getPower(args.voltage, args.radius, args.lossReduceCount);
        List<EntityPlayer> players = ModUtils.radiusFilterPlayers(args.pos, args.world.playerEntities, args.radius);
        if (!args.isPublic) {
            for (int j = 0; j < args.cards.length; j++)
                for (int i = 0; i < players.size(); i++)
                    if (players.get(i).getUniqueID().hashCode() == args.cards[j]) {
                        players.get(i).addPotionEffect(new PotionEffect(Potion.getPotionById(1), 20, power));
                        players.get(i).addPotionEffect(new PotionEffect(Potion.getPotionById(3), 20, power));
                        players.get(i).addPotionEffect(new PotionEffect(Potion.getPotionById(8), 20, power));
                    }
        }
        else{
            for (int i = 0; i < players.size(); i++) {
                players.get(i).addPotionEffect(new PotionEffect(Potion.getPotionById(1), 20, power));
                players.get(i).addPotionEffect(new PotionEffect(Potion.getPotionById(3), 20, power));
                players.get(i).addPotionEffect(new PotionEffect(Potion.getPotionById(8), 20, power));
            }
        }
    }

    private int getPower(int voltage,  int radius, int lossReduce){
        return Math.round(voltage *0.0007f / ((float)radius/(10f/(lossReduce + 1)) + 1f));
    }

    @Override
    public int onImplantWork(ChipArgs args) {
        if (args.isModuleActive && args.energy > 1000){
            int state = args.data.getInteger("stateIndex");
            if (state == 0)
                args.player.addPotionEffect(new PotionEffect(Potion.getPotionById(1), 20, args.data.getInteger("power")-1));
            else if (state == 1)
                args.player.addPotionEffect(new PotionEffect(Potion.getPotionById(3), 20, args.data.getInteger("power")-1));
            else
                args.player.addPotionEffect(new PotionEffect(Potion.getPotionById(8), 20, args.data.getInteger("power")-1));
            return args.data.getInteger("power") * 50;
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
                new StateModuleGui("Mode: ", new String[]{"Movement", "Digging", "Jumping"})
        };
    }

    @Override
    public void onEntityHit(EntityLivingBase target, EntityLivingBase attacker, ItemStack sword, int power) {
        target.addPotionEffect(new PotionEffect(Potion.getPotionById(2), 100, power-1));
    }

    @Override
    public int getEnergy(ItemStack sword, int power) {
        return 100 * power;
    }

    @Override
    public void onDamaged(LivingHurtEvent event, Entity attacker, int power, NBTTagCompound tag, EntityPlayer player) {
        if (attacker instanceof EntityLivingBase){
            EntityLivingBase livingBase = (EntityLivingBase) attacker;
            livingBase.attackEntityFrom(DamageSource.MAGIC, power * 2);
        }
    }

    @Override
    public int getEnergy(int power) {
        return 250 * power;
    }
}
