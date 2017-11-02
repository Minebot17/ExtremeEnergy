package ru.minebot.extreme_energy.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModSoundHandler {

    public static SoundEvent
            hvg,
            tap0,
            tap1,
            ltiny,
            lsmall,
            lstandart,
            lbig;


    private static int index = 0;

    public static void init() {
        index = SoundEvent.REGISTRY.getKeys().size();

        hvg = register("hvg");
        tap0 = register("tap0");
        tap1 = register("tap1");
        ltiny = register("ltiny");
        lsmall = register("lsmall");
        lstandart = register("lstandart");
        lbig = register("lbig");
    }

    private static SoundEvent register(String name) {
        index++;
        ResourceLocation res = new ResourceLocation("meem:" + name);
        SoundEvent event = new SoundEvent(res);
        ForgeRegistries.SOUND_EVENTS.register(event.setRegistryName(res));
        return event;
    }
}
