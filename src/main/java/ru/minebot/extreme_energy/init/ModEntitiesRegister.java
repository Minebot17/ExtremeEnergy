package ru.minebot.extreme_energy.init;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.entities.*;

public class ModEntitiesRegister {

    public static void register(){
        EntityRegistry.registerModEntity(new ResourceLocation("meem:nb"), EntityNuclearBomb.class, "nuclearBomb", 0, ExtremeEnergy.instance,64,1,false);
        EntityRegistry.registerModEntity(new ResourceLocation("meem:grenade"), EntityGrenade.class, "grenade", 1, ExtremeEnergy.instance,32,1,false);
    }
}
