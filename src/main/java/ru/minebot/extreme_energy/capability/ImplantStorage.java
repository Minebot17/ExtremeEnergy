package ru.minebot.extreme_energy.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import ru.minebot.extreme_energy.other.ImplantData;

import javax.annotation.Nullable;

public class ImplantStorage implements Capability.IStorage<IImplant> {
    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IImplant> capability, IImplant instance, EnumFacing side) {
        if (!instance.hasImplant())
            return new NBTTagCompound();

        ImplantData data = instance.getImplant();
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("type", data.type);
        tag.setInteger("playerID", data.playerID);
        tag.setTag("modules", data.modules);
        tag.setTag("implants", data.implant);
        tag.setTag("core", data.core);
        return tag;
    }

    @Override
    public void readNBT(Capability<IImplant> capability, IImplant instance, EnumFacing side, NBTBase nbt) {
        NBTTagCompound tag = (NBTTagCompound) nbt;
        if (tag.hasKey("type"))
            instance.setImplant(new ImplantData(tag.getInteger("type"), tag.getCompoundTag("modules"), tag.getCompoundTag("implants"), tag.getCompoundTag("core"), tag.getInteger("playerID")));
        else
            instance.removeImplant();
    }
}
