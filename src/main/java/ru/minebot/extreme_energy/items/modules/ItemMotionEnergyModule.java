package ru.minebot.extreme_energy.items.modules;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.modules.ChipArgs;
import ru.minebot.extreme_energy.modules.IGenerator;
import ru.minebot.extreme_energy.modules.Module;

public class ItemMotionEnergyModule extends Module implements IGenerator {

    public ItemMotionEnergyModule() {
        super(Reference.ExtremeEnergyItems.MODULEENERGYMOTION.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULEENERGYMOTION.getRegistryName(), 1, false);
    }

    @Override
    public int generateEnergy(ChipArgs args) {
        if (!args.player.world.isRemote) {
            BlockPos lastPos = new BlockPos(args.data.getInteger("lastX"), args.data.getInteger("lastY"), args.data.getInteger("lastZ"));
            BlockPos pos = args.player.getPosition();
            args.data.setInteger("lastX", pos.getX());
            args.data.setInteger("lastY", pos.getY());
            args.data.setInteger("lastZ", pos.getZ());
            if (args.player.isSprinting())
                return 200;
            if (!lastPos.equals(pos))
                return 100;
        }
        return 0;
    }

    @Override
    public int getTier() {
        return 0;
    }
}
