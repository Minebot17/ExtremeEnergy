package ru.minebot.extreme_energy.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ImplantProvider implements ICapabilitySerializable<NBTBase> {

    @CapabilityInject(IImplant.class)
    public static final Capability<IImplant> IMPLANT = null;
    private IImplant instance = IMPLANT.getDefaultInstance();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == IMPLANT;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == IMPLANT ? IMPLANT.cast(this.instance) : null;
    }

    @Override
    public NBTBase serializeNBT() {
        return IMPLANT.getStorage().writeNBT(IMPLANT, instance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        IMPLANT.getStorage().readNBT(IMPLANT, instance, null, nbt);
    }
}
