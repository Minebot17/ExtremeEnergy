package ru.minebot.extreme_energy.items.implants;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.minebot.extreme_energy.ExtremeEnergy;

public class Core extends Item {
    private int guiID;
    protected int maxPower;

    public Core(String unlocName, String regName, int guiID, int maxPower){
        setUnlocalizedName(unlocName);
        setRegistryName(regName);
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setMaxStackSize(1);
        this.guiID = guiID;
        this.maxPower = maxPower;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        BlockPos pos = playerIn.getPosition();
        playerIn.openGui(ExtremeEnergy.instance, guiID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    public int getMaxPower(){ return maxPower; }
}
