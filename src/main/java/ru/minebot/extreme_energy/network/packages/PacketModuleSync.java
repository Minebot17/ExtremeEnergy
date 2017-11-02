package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.capability.IImplant;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.network.AbstractPacket;
import ru.minebot.extreme_energy.other.ImplantData;

import java.util.ArrayList;
import java.util.List;

public class PacketModuleSync extends AbstractPacket {
    protected List<NBTTagCompound> inImplant;
    protected List<NBTTagCompound> inCore;

    public PacketModuleSync(){}

    @SideOnly(Side.CLIENT)
    public PacketModuleSync(boolean stock){
        IImplant cap = Minecraft.getMinecraft().player.getCapability(ImplantProvider.IMPLANT, null);
        if (cap.hasImplant()){
            List<ItemStack> items = ModUtils.getModules(cap.getImplant());
            inImplant = new ArrayList<>();
            for (ItemStack stack : items)
                inImplant.add(ModUtils.getNotNullCategory(stack));

            items = ModUtils.getCoreModules(cap.getImplant());
            inCore = new ArrayList<>();
            for (ItemStack stack : items)
                inCore.add(ModUtils.getNotNullCategory(stack));
        }
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeByte(inImplant.size());
        buf.writeByte(inCore.size());
        for (NBTTagCompound tag : inImplant)
            ByteBufUtils.writeTag(buf, tag);
        for (NBTTagCompound tag : inCore)
            ByteBufUtils.writeTag(buf, tag);
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        int countImplant = buf.readByte();
        int countCore = buf.readByte();
        inImplant = new ArrayList<>();
        inCore = new ArrayList<>();
        for (int i = 0; i < countImplant; i++)
            inImplant.add(ByteBufUtils.readTag(buf));
        for (int i = 0; i < countCore; i++)
            inCore.add(ByteBufUtils.readTag(buf));
    }

    @Override
    public void clientHandler(EntityPlayer player) {
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
        IImplant cap = player.getCapability(ImplantProvider.IMPLANT, null);
        if (cap.hasImplant()){
            ImplantData data = cap.getImplant();
            List<ItemStack> iStacks = ModUtils.getModules(data);
            if (iStacks.size() == inImplant.size())
                for (int i = 0; i < iStacks.size(); i++)
                    ModUtils.getNotNullTag(iStacks.get(i)).setTag(ExtremeEnergy.NBT_CATEGORY, inImplant.get(i));

            List<ItemStack> cStacks = ModUtils.getCoreModules(data);
            if (cStacks.size() == inCore.size())
                for (int i = 0; i < cStacks.size(); i++)
                    ModUtils.getNotNullTag(cStacks.get(i)).setTag(ExtremeEnergy.NBT_CATEGORY, inCore.get(i));

            ModUtils.setModules(data, iStacks);
            ModUtils.setCoreModules(data, cStacks);
            cap.setImplant(data);
        }
    }
}
