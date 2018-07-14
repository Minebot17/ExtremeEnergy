package ru.minebot.extreme_energy.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.entities.EntityGrenade;
import ru.minebot.extreme_energy.init.ModUtils;

import javax.annotation.Nullable;
import java.util.List;

public class ItemGrenade extends Item {
    public ItemGrenade(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.GRENADE.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.GRENADE.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        worldIn.spawnEntity(new EntityGrenade(playerIn, playerIn.getLookVec()));
        playerIn.getHeldItem(handIn).shrink(1);
        return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(ModUtils.getDescription(stack.getItem()));
    }
}
