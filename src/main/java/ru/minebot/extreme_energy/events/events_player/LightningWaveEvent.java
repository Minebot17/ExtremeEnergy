package ru.minebot.extreme_energy.events.events_player;

import net.minecraft.entity.player.EntityPlayer;

public class LightningWaveEvent implements IEventPlayer {

    @Override
    public void onEvent(EntityPlayer player, int voltage) {

    }

    @Override
    public float getChance(int value) {
        return 0;
    }

    @Override
    public int getRarity(int value) {
        return 0;
    }
}
