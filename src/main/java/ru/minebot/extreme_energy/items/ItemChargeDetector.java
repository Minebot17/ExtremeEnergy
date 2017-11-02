package ru.minebot.extreme_energy.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL12;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.energy.IFieldCreatorEnergy;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketLangMessage;
import ru.minebot.extreme_energy.other.ChargeSaveData;

import javax.annotation.Nullable;
import java.util.List;

public class ItemChargeDetector extends Item {
    public ItemChargeDetector(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.CHARGEDETECTOR.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.CHARGEDETECTOR.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote){
            ChargeSaveData data = ChargeSaveData.getOrCreateData(worldIn);
            int index = getMessageIndex(playerIn, data.map.get(new ChunkPos(playerIn.getPosition())));
            NetworkWrapper.instance.sendTo(new PacketLangMessage("chargeDetector", getMessage(index), getMessageColor(index)), (EntityPlayerMP)playerIn);
        }
        return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    public static String getMessage(int index){
        String[] array = new String[]{
                "none",
                "interference",
                "veryLow",
                "low",
                "normal",
                "high",
                "veryHigh",
        };
        return array[index];
    }

    public static TextFormatting getMessageColor(int index){
        TextFormatting[] array = new TextFormatting[]{
                TextFormatting.GRAY,
                TextFormatting.DARK_GRAY,
                TextFormatting.DARK_GREEN,
                TextFormatting.GREEN,
                TextFormatting.YELLOW,
                TextFormatting.RED,
                TextFormatting.GOLD
        };
        return array[index];
    }

    private int getMessageIndex(EntityPlayer player, int energy){
        if (energy == 0)
            return 0;

        IFieldCreatorEnergy creator = null;
        List<TileEntity> list = ModUtils.radiusFilter(player.getPosition(), player.world.loadedTileEntityList, 100);
        for (TileEntity te : list)
            if (te instanceof IFieldCreatorEnergy && ((IFieldCreatorEnergy) te).isActive() && ((IFieldCreatorEnergy) te).getRadius() >= te.getPos().getDistance(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()) && ((IFieldCreatorEnergy) te).getEnergyStored() != 0)
                creator = (IFieldCreatorEnergy) te;
        if (creator != null)
            return 1;

        int[] array = new int[]{ 0, 200000, 4000000, 6000000, 8000000 };
        for (int i = 0; i < array.length-1; i++)
            if (energy >= array[i] && energy < array[i+1])
                return i+2;
        return array.length+1;
    }

    public static int getMessageIndexFromPos(World world, ChunkPos pos){
        int energy = ModUtils.clientChunkCharge.get(pos);
        if (energy == 0)
            return 0;

        int[] array = new int[]{ 0, 200000, 4000000, 6000000, 8000000 };
        for (int i = 0; i < array.length-1; i++)
            if (energy >= array[i] && energy < array[i+1])
                return i+2;
        return array.length+1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(ModUtils.getDescription(stack.getItem()));
    }
}
