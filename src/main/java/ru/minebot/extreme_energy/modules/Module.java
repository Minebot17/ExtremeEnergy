package ru.minebot.extreme_energy.modules;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.init.ModUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class Module extends Item implements IModuleTier {

    private boolean multiply;

    public Module(String unlocName, String regName, int maxSize, boolean multiply){
        setUnlocalizedName(unlocName);
        setRegistryName(regName);
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setMaxStackSize(maxSize);
        this.multiply = multiply;
    }

    public boolean isMultiply(){
        return multiply;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(ModUtils.getDescription(stack.getItem()));
        if (!(this instanceof ModuleAddValue))
            tooltip.add("Tire " + (getTier()+1));
    }

    public static ArrayList<String> getLocalizedNames(Item[] items){
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < items.length; i++)
            result.add(new ItemStack(items[i]).getDisplayName());
        return result;
    }
}
