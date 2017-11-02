package ru.minebot.extreme_energy.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class CommonProxy{
    public abstract void prePreInit(FMLPreInitializationEvent event);
    public abstract void preInit(FMLPreInitializationEvent event);
    public abstract void init(FMLInitializationEvent event);
    public abstract void postInit(FMLPostInitializationEvent event);

    public EntityPlayer getEntityPlayer (MessageContext ctx) {
        return ctx.getServerHandler().player;
    }
}
