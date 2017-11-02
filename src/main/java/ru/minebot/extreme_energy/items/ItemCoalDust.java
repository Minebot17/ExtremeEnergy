package ru.minebot.extreme_energy.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemCoalDust extends Item{
    public ItemCoalDust(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.COALDUST.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.COALDUST.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
