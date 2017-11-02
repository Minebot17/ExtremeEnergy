package ru.minebot.extreme_energy.events.events_player;

import net.minecraft.entity.player.EntityPlayer;
import ru.minebot.extreme_energy.events.IEvent;

public interface IEventPlayer extends IEvent {
    void onEvent(EntityPlayer player, int voltage);
}
