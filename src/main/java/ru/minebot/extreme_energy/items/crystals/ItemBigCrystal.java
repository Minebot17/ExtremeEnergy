package ru.minebot.extreme_energy.items.crystals;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemBigCrystal extends Item{

    public ItemBigCrystal(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.BIGCRYSTAL.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.BIGCRYSTAL.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        return new ActionResult(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }
}
