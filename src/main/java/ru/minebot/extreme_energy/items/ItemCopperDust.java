package ru.minebot.extreme_energy.items;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemCopperDust extends Item{

    public ItemCopperDust(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.COPPERDUST.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.COPPERDUST.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
