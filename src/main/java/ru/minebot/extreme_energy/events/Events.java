package ru.minebot.extreme_energy.events;

import ru.minebot.extreme_energy.events.events_block.IEventBlock;
import ru.minebot.extreme_energy.events.events_chunk.IEventChunk;
import ru.minebot.extreme_energy.events.events_chunk.LightningBetweenBlocksEvent;
import ru.minebot.extreme_energy.events.events_chunk.LightningFromSky;
import ru.minebot.extreme_energy.events.events_chunk.SparkWithPlayersEvent;
import ru.minebot.extreme_energy.events.events_player.*;
import ru.minebot.extreme_energy.events.events_sg.*;

import java.util.ArrayList;
import java.util.List;

public class Events {
    public final static IEventPlayer[] playerEvents = new IEventPlayer[]{
            new MessageEvent(),
            new PotionEvent(),
            new LightningWithPlayersEvent(),
            new ImplantPowerDownEvent()
    };

    public final static IEventBlock[] blockEvents = new IEventBlock[]{

    };

    public final static IEventChunk[] chunkEvents = new IEventChunk[]{
            new LightningBetweenBlocksEvent(),
            new SparkWithPlayersEvent(),
            new LightningFromSky()
    };

    public final static IEventSG[] sgEvents = new IEventSG[]{
            new ExplodeEvent(),
            new SteamEvent(),
            new PlayerShockEvent(),
            new SmeltEvent()
    };

    public static List<IEventPlayer> getPossiblePlayerEvents(int voltage){
        List<IEventPlayer> result = new ArrayList<>();
        for (int i = 0; i < playerEvents.length; i++)
            if (playerEvents[i].getChance(voltage) > 0)
                result.add(playerEvents[i]);
        return result;
    }

    public static List<IEventBlock> getPossibleBlockEvents(int voltage){
        List<IEventBlock> result = new ArrayList<>();
        for (int i = 0; i < blockEvents.length; i++)
            if (blockEvents[i].getChance(voltage) > 0)
                result.add(blockEvents[i]);
        return result;
    }

    public static List<IEventChunk> getPossibleChunkEvents(int charge){
        List<IEventChunk> result = new ArrayList<>();
        for (int i = 0; i < chunkEvents.length; i++)
            if (chunkEvents[i].getChance(charge) > 0)
                result.add(chunkEvents[i]);
        return result;
    }
}
