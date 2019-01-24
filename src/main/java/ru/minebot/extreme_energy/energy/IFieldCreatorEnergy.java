package ru.minebot.extreme_energy.energy;

import cofh.redstoneflux.api.*;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface IFieldCreatorEnergy extends IVoltageHandler, IEnergyStorage, ISwitchable, IRadiusHandler, IFrequencyHandler {
    void applyLinkedBlocks();
    void brokeLink(BlockPos pos);
    void brokeLinksAll();
    void createLink(BlockPos pos);
    List<BlockPos> getLinks();
    int getRealVoltage();
}
