package ru.minebot.extreme_energy.capability;

import ru.minebot.extreme_energy.other.ImplantData;

public interface IImplant {
    ImplantData getImplant();
    void setImplant(ImplantData data);
    void removeImplant();
    boolean hasImplant();
    boolean hasCore();
}
