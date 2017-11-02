package ru.minebot.extreme_energy.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.init.ModUtils;

import javax.annotation.Nullable;
import java.util.List;

public class ItemIdCard extends Item{
    public ItemIdCard(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.IDCARD.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.IDCARD.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        NBTTagCompound tag = ModUtils.getNotNullCategory(stack);
        tag.setInteger("id", playerIn.getUniqueID().hashCode());

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        NBTTagCompound tag = ModUtils.getNotNullCategory(stack);
        tooltip.add("ID: " + tag.getInteger("id"));
    }
}
