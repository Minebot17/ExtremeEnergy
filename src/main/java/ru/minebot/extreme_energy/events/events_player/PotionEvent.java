package ru.minebot.extreme_energy.events.events_player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketLangMessage;

public class PotionEvent implements IEventPlayer {
    public enum potions {
        BEGIN_FLY("beginFly", 25, 100),
        BEGIN_LIGHT("beginLight", 24, 300),
        GET_HIT("getHit", 7, 1),
        GET_ENERGY("getEnergy", 1, 600);

        private String key;
        private int id;
        private int duration;

        potions(String key, int id, int duration){
            this.key = key;
            this.duration = duration;
            this.id = id;
        }

        @Override
        public String toString() {
            return key;
        }
        public int getID(){ return id; }
        public int getDuration(){ return duration; }
    }

    protected potions[] activePotions = new potions[]{
            potions.BEGIN_FLY,
            potions.BEGIN_LIGHT,
            potions.GET_ENERGY,
            potions.GET_HIT
    };

    @Override
    public void onEvent(EntityPlayer player, int voltage) {
        potions potion = activePotions[ModUtils.random.nextInt(activePotions.length)];
        player.addPotionEffect(new PotionEffect(Potion.getPotionById(potion.getID()), potion.getDuration()));
        NetworkWrapper.instance.sendTo(new PacketLangMessage("potionEvent", activePotions[ModUtils.random.nextInt(activePotions.length)].toString(), TextFormatting.BLUE), (EntityPlayerMP)player);
    }

    @Override
    public float getChance(int value) {
        return ((-(1f/1000f))*(((float) value - 5000f)*((float) value - 5000f)) + 1000f)/200f;
    }

    @Override
    public int getRarity(int value) {
        return 1200;
    }
}
