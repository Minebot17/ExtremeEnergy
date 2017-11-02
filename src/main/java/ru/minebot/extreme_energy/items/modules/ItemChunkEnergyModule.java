package ru.minebot.extreme_energy.items.modules;

import net.minecraft.util.math.ChunkPos;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.ChipArgs;
import ru.minebot.extreme_energy.modules.IGenerator;
import ru.minebot.extreme_energy.modules.Module;

public class ItemChunkEnergyModule extends Module implements IGenerator {
    public ItemChunkEnergyModule() {
        super(Reference.ExtremeEnergyItems.MODULEENERGYCHUNK.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULEENERGYCHUNK.getRegistryName(), 1, false);
    }

    @Override
    public int generateEnergy(ChipArgs args) {
        if (!args.player.world.isRemote)
        return ModUtils.extractEnergyFromChunk(args.player.world, new ChunkPos(args.player.getPosition()), 20);
        else return 0;
    }

    @Override
    public int getTier() {
        return 1;
    }
}
