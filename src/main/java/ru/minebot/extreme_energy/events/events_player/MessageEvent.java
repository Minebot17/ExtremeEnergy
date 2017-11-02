package ru.minebot.extreme_energy.events.events_player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketLangMessage;

public class MessageEvent implements IEventPlayer {
    public enum messages {
        SPARK_FLASHED("sparkFlashed"),
        AIR_ELECTRIFIED("airElectrified"),
        HAIR_ELECTRIFIED("hairElectrified"),
        GROUND_RISE("groundRise");

        private String key;

        messages(String key){
            this.key = key;
        }

        @Override
        public String toString() {
            return key;
        }
    }

    protected messages[] activeMessages = new messages[]{
            messages.GROUND_RISE,
            messages.AIR_ELECTRIFIED,
            messages.HAIR_ELECTRIFIED,
            messages.SPARK_FLASHED
    };

    @Override
    public void onEvent(EntityPlayer player, int voltage) {
        NetworkWrapper.instance.sendTo(new PacketLangMessage("messageEvent", activeMessages[ModUtils.random.nextInt(activeMessages.length)].toString(), TextFormatting.BLUE), (EntityPlayerMP)player);
    }

    @Override
    public float getChance(int value) {
        return ((-(1f/1000f))*(((float) value - 4000f)*((float) value - 4000f)) + 1000f)/200f;
    }

    @Override
    public int getRarity(int value) {
        return 6000;
    }
}
