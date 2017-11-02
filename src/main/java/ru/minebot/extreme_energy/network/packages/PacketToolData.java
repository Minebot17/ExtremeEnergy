package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.equipment.ItemEnergySword;
import ru.minebot.extreme_energy.items.equipment.ItemEnergyTool;
import ru.minebot.extreme_energy.network.AbstractPacket;

public class PacketToolData extends AbstractPacket{
    protected NBTTagCompound data;

    public PacketToolData(){}

    public PacketToolData(NBTTagCompound data){
        this.data = data;
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        ByteBufUtils.writeTag(buf, data);
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        data = ByteBufUtils.readTag(buf);
    }

    @Override
    public void clientHandler(EntityPlayer player) {
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
        ItemStack stack = player.inventory.getCurrentItem();
        if (stack.getItem() instanceof ItemEnergyTool || stack.getItem() instanceof ItemEnergySword)
            ModUtils.getNotNullTag(stack).setTag(ExtremeEnergy.NBT_CATEGORY, data);
    }
}
