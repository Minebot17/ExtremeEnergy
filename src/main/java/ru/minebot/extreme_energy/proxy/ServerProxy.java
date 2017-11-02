package ru.minebot.extreme_energy.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ru.minebot.extreme_energy.init.ModConfig;

public class ServerProxy extends CommonProxy {

    @Override
    public void prePreInit(FMLPreInitializationEvent event) {
        ModConfig.register(event, false);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event){
    }

    @Override
    public void init(FMLInitializationEvent event) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }
}
