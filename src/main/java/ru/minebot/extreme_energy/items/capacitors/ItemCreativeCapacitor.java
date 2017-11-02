package ru.minebot.extreme_energy.items.capacitors;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCreativeCapacitor extends Capacitor {

    public ItemCreativeCapacitor(){
        super(999999999, 999999999, 999999999);
        setUnlocalizedName(Reference.ExtremeEnergyItems.CREATIVECAPACITOR.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.CREATIVECAPACITOR.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setMaxStackSize(1);
        setMaxDamage(capacity);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
        tooltip.add("over9000/over9000+1 RF");
    }

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        return maxReceive;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        return maxExtract;
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        return 999999999;
    }
}
