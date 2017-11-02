package ru.minebot.extreme_energy.events;

public interface IEvent {
    float getChance(int value); // Шанс (0 - 1) срабатывания события, при его вызове
    int getRarity(int value); // Каждый какой тик пытаться вызвать событие?
}
