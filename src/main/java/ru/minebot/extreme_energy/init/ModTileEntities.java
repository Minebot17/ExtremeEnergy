package ru.minebot.extreme_energy.init;

import net.minecraftforge.fml.common.registry.GameRegistry;
import ru.minebot.extreme_energy.tile_entities.*;

public class ModTileEntities {

    public static void init(){
        GameRegistry.registerTileEntity(EnergySender.class, "tile_entity_ES");
        GameRegistry.registerTileEntity(TileEntityHVG.class, "tile_entity_HVG");
        GameRegistry.registerTileEntity(TileEntityHTF.class, "tile_entity_HTF");
        GameRegistry.registerTileEntity(TileEntityHPC.class, "tile_entity_HPC");
        GameRegistry.registerTileEntity(TileEntityHPA.class, "tile_entity_HPA");
        GameRegistry.registerTileEntity(TileEntityFC.class, "tile_entity_FC");
        GameRegistry.registerTileEntity(TileEntityFT.class, "tile_entity_FT");
        GameRegistry.registerTileEntity(TileEntitySG.class, "tile_entity_SG");
        GameRegistry.registerTileEntity(TileEntityFG.class, "tile_entity_FG");
        GameRegistry.registerTileEntity(TileEntityTG.class, "tile_entity_TG");
        GameRegistry.registerTileEntity(TileEntityFTR.class, "tile_entity_FTR");
        GameRegistry.registerTileEntity(TileEntityEE.class, "tile_entity_EE");
        GameRegistry.registerTileEntity(TileEntityEB.class, "tile_entity_EB");
        GameRegistry.registerTileEntity(TileEntityCS.class, "tile_entity_CS");
        GameRegistry.registerTileEntity(TileEntityNR.class, "tile_entity_NR");
        GameRegistry.registerTileEntity(TileEntityLightningRod.class, "tile_entity_LR");
        GameRegistry.registerTileEntity(EnergySenderCable.class, "tile_entity_cable");
    }
}
