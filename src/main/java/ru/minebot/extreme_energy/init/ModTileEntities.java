package ru.minebot.extreme_energy.init;

import net.minecraftforge.fml.common.registry.GameRegistry;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.tile_entities.*;

public class ModTileEntities {

    public static void init(){
        GameRegistry.registerTileEntity(EnergySender.class, Reference.MOD_ID + ":tile_entity_ES");
        GameRegistry.registerTileEntity(TileEntityHVG.class, Reference.MOD_ID + ":tile_entity_HVG");
        GameRegistry.registerTileEntity(TileEntityHTF.class, Reference.MOD_ID + ":tile_entity_HTF");
        GameRegistry.registerTileEntity(TileEntityHPC.class, Reference.MOD_ID + ":tile_entity_HPC");
        GameRegistry.registerTileEntity(TileEntityHPA.class, Reference.MOD_ID + ":tile_entity_HPA");
        GameRegistry.registerTileEntity(TileEntityFC.class, Reference.MOD_ID + ":tile_entity_FC");
        GameRegistry.registerTileEntity(TileEntityFT.class, Reference.MOD_ID + ":tile_entity_FT");
        GameRegistry.registerTileEntity(TileEntitySG.class, Reference.MOD_ID + ":tile_entity_SG");
        GameRegistry.registerTileEntity(TileEntityFG.class, Reference.MOD_ID + ":tile_entity_FG");
        GameRegistry.registerTileEntity(TileEntityTG.class, Reference.MOD_ID + ":tile_entity_TG");
        GameRegistry.registerTileEntity(TileEntityFTR.class, Reference.MOD_ID + ":tile_entity_FTR");
        GameRegistry.registerTileEntity(TileEntityEE.class, Reference.MOD_ID + ":tile_entity_EE");
        GameRegistry.registerTileEntity(TileEntityEB.class, Reference.MOD_ID + ":tile_entity_EB");
        GameRegistry.registerTileEntity(TileEntityCS.class, Reference.MOD_ID + ":tile_entity_CS");
        GameRegistry.registerTileEntity(TileEntityNR.class, Reference.MOD_ID + ":tile_entity_NR");
        GameRegistry.registerTileEntity(TileEntityLightningRod.class, Reference.MOD_ID + ":tile_entity_LR");
        GameRegistry.registerTileEntity(EnergySenderCable.class, Reference.MOD_ID + ":tile_entity_cable");
        GameRegistry.registerTileEntity(TileEntityHAS.class, Reference.MOD_ID + ":tile_entity_HAS");
    }
}
