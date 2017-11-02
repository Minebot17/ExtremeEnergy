package ru.minebot.extreme_energy.energy;

import net.minecraft.util.math.BlockPos;

public interface IFieldReceiverEnergy extends IFrequencyHandler, IVoltageHandler, IWorkable {
    void onField();
    BlockPos getLink();
    void setLink(BlockPos pos);
    boolean hasField();
    void applyCreatorsAround();
}
