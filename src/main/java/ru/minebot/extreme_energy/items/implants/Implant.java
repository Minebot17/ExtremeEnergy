package ru.minebot.extreme_energy.items.implants;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.other.ImplantData;

public class Implant extends Item {
    private int guiID;

    public Implant(String unlocName, String regName, int guiID){
        setUnlocalizedName(unlocName);
        setRegistryName(regName);
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        this.guiID = guiID;
        setMaxStackSize(1);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        BlockPos pos = playerIn.getPosition();
        playerIn.openGui(ExtremeEnergy.instance, guiID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    public static int getMaxVoltage(int type){
        return type == 0 ? 1000 : type == 1 ? 4000 : 10000;
    }
    public static int getMaxEnergy(int type){ return type == 0 ? 100000 : type == 1 ? 1000000 : 10000000; }
}
