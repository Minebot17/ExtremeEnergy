package ru.minebot.extreme_energy.capability;

import net.minecraft.item.ItemStack;
import ru.minebot.extreme_energy.items.implants.Core;
import ru.minebot.extreme_energy.other.ImplantData;

public class ImplantHandler implements IImplant {
    private ImplantData data;

    @Override
    public ImplantData getImplant() {
        return data;
    }

    @Override
    public void setImplant(ImplantData data) {
        this.data = data;
    }

    @Override
    public void removeImplant() {
        data = null;
    }

    @Override
    public boolean hasImplant() {
        return data != null;
    }

    @Override
    public boolean hasCore(){ return hasImplant() && !new ItemStack(data.core).isEmpty() && new ItemStack(data.core).getItem() instanceof Core; }
}
