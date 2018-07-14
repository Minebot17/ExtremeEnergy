package ru.minebot.extreme_energy.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.energy.IFrequencyHandler;
import ru.minebot.extreme_energy.init.ModGuiHandler;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFrequencyInstaller extends Item {
    public ItemFrequencyInstaller(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.FREQUENCYINSTALLER.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.FREQUENCYINSTALLER.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te != null && te instanceof IFrequencyHandler){
            IFrequencyHandler handler = (IFrequencyHandler) te;
            NBTTagCompound tag = ModUtils.getNotNullCategory(player.inventory.getCurrentItem());
            if (player.isSneaking())
                handler.setFrequency(tag.getInteger("frequency"));
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        BlockPos pos = playerIn.getPosition();
        playerIn.openGui(ExtremeEnergy.instance, ModGuiHandler.FI_SCREEN, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        if (Minecraft.getMinecraft().player != null && !Minecraft.getMinecraft().player.isCreative()){
            NBTTagCompound tag = ModUtils.getNotNullCategory(stack);
            tooltip.add("Frequency: " + tag.getInteger("frequency"));
        }
    }
}
