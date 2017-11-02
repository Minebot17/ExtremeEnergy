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
import ru.minebot.extreme_energy.gui.elements.moduleGui.StateModuleGui;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.*;

import java.util.List;

public class ItemStrangeModule extends ModuleFunctional implements IChip, ISwordModule, IArmorCoreModule {
    private int power;

    public ItemStrangeModule() {
        super(Reference.ExtremeEnergyItems.MODULESTRANGE.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULESTRANGE.getRegistryName());
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Very strange"};
    }

    @Override
    public void firstUpdate(FuncArgs args){
        List<EntityPlayer> players = ModUtils.radiusFilterPlayers(args.pos, args.world.playerEntities, args.radius);
        if (!args.isPublic) {
            for (int j = 0; j < args.cards.length; j++)
                for (int i = 0; i < players.size(); i++)
                    if(players.get(i).getUniqueID().hashCode() == args.cards[j]) {
                        players.get(i).addPotionEffect(new PotionEffect(Potion.getPotionById(14), 20, 0));
                        players.get(i).addPotionEffect(new PotionEffect(Potion.getPotionById(16), 300, 0));
                    }
        }
        else{
            for (int i = 0; i < players.size(); i++) {
                players.get(i).addPotionEffect(new PotionEffect(Potion.getPotionById(14), 20, 0));
                players.get(i).addPotionEffect(new PotionEffect(Potion.getPotionById(16), 300, 0));
            }
        }
    }


    @Override
    public int onImplantWork(ChipArgs args) {
        if (args.isModuleActive && args.energy > 1000){
            if (args.data.getInteger("stateIndex") == 0)
                args.player.addPotionEffect(new PotionEffect(Potion.getPotionById(16), 300, 0));
            else
                args.player.addPotionEffect(new PotionEffect(Potion.getPotionById(14), 20, 0));
            return 30;
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
                new StateModuleGui("Mode: ", new String[]{"Night Vision", "Invisibility"})
        };
    }

    @Override
    public void onEntityHit(EntityLivingBase target, EntityLivingBase attacker, ItemStack sword, int power) {
        target.addPotionEffect(new PotionEffect(Potion.getPotionById(15), 100, power-1));
    }

    @Override
    public int getEnergy(ItemStack sword, int power) {
        return 100;
    }

    @Override
    public void onDamaged(LivingHurtEvent event, Entity attacker, int power, NBTTagCompound tag, EntityPlayer player) {
        if (attacker instanceof EntityLivingBase){
            EntityLivingBase livingBase = (EntityLivingBase) attacker;
            livingBase.addPotionEffect(new PotionEffect(Potion.getPotionById(24), 20 * power, 0));
        }
    }

    @Override
    public int getEnergy(int power) {
        return 750*power;
    }
}
