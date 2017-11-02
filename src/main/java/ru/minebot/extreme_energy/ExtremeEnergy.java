package ru.minebot.extreme_energy;

import cofh.redstoneflux.RedstoneFlux;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ru.minebot.extreme_energy.capability.IImplant;
import ru.minebot.extreme_energy.capability.ImplantHandler;
import ru.minebot.extreme_energy.capability.ImplantStorage;
import ru.minebot.extreme_energy.effects.PotionElectricShock;
import ru.minebot.extreme_energy.init.*;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.PacketRegister;
import ru.minebot.extreme_energy.proxy.CommonProxy;
import ru.minebot.extreme_energy.recipes.ModCrafts;
import ru.minebot.extreme_energy.recipes.assembler.AssemblerRecipes;
import ru.minebot.extreme_energy.recipes.crusher.CrusherRecipes;

// Hello, coder. Author - Minebot
@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = Reference.ACCEPTED_VERSIONS, dependencies= RedstoneFlux.VERSION_GROUP)
public class ExtremeEnergy {
    public static final String NBT_CATEGORY = "meemCategory";

    // Test world seed = -4567713627592776810
    @Instance
    public static ExtremeEnergy instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws Exception {
        System.out.println("Extreme Energy started loading");
        proxy.prePreInit(event);
        ModBlocks.init();
        ModBlocks.register();
        ModItems.init();
        ModItems.register();
        ModTileEntities.init();
        ModCrafts.register();
        OreDictionaryRegisters.register();
        ModEntitiesRegister.register();
        CrusherRecipes.init();
        AssemblerRecipes.init();
        proxy.preInit(event);
    }

    @EventHandler
    public void Init(FMLInitializationEvent event){
        proxy.init(event);
        CapabilityManager.INSTANCE.register(IImplant.class, new ImplantStorage(), ImplantHandler.class);
        GameRegistry.registerWorldGenerator(new WorldGen(), 0);
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new ModGuiHandler());
        MinecraftForge.EVENT_BUS.register(NetworkWrapper.instance);
        PacketRegister.register();
        MinecraftForge.EVENT_BUS.register(new CommonEvents());
        ModSoundHandler.init();
        ForgeRegistries.POTIONS.register(PotionElectricShock.createPotion().setRegistryName(new ResourceLocation("meem:electricShock")));
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        proxy.postInit(event);
        System.out.println("Extreme Energy loaded");
    }

    public static CreativeTabs tabExtremeEnergy = new CreativeTabs("tab_extreme_energy"){
        @Override
        public ItemStack getTabIconItem(){
            return new ItemStack(ModItems.bigCrystalActive);
        }
    };
}
