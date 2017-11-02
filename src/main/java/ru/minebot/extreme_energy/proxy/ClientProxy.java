package ru.minebot.extreme_energy.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import ru.minebot.extreme_energy.entities.EntityGrenade;
import ru.minebot.extreme_energy.entities.EntityNuclearBomb;
import ru.minebot.extreme_energy.entities.RenderGrenade;
import ru.minebot.extreme_energy.entities.RenderNuclearBomb;
import ru.minebot.extreme_energy.gui.RenderHUD;
import ru.minebot.extreme_energy.gui.tablet.TabletRender;
import ru.minebot.extreme_energy.init.*;

public class ClientProxy extends CommonProxy {

    @Override
    public void prePreInit(FMLPreInitializationEvent event) {
        ModConfig.register(event, true);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        Minecraft.getMinecraft().gameSettings.fboEnable = true;
        if (!Minecraft.getMinecraft().getFramebuffer().isStencilEnabled())
            Minecraft.getMinecraft().getFramebuffer().enableStencil();
        ModItems.registerRenders();
        ModBlocks.registerRenders();
        RenderingRegistry.registerEntityRenderingHandler(EntityNuclearBomb.class, RenderNuclearBomb::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityGrenade.class, RenderGrenade::new);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        ModKeys.register();
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        MinecraftForge.EVENT_BUS.register(new RenderHUD());
        MinecraftForge.EVENT_BUS.register(new LightningEvents());
        MinecraftForge.EVENT_BUS.register(new TabletRender());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        TabletRender.init();
        ModParticlesRegister.register();
    }

    @Override
    public EntityPlayer getEntityPlayer (MessageContext ctx) {
        return ctx.side == Side.CLIENT ? Minecraft.getMinecraft().player : super.getEntityPlayer(ctx);
    }
}
