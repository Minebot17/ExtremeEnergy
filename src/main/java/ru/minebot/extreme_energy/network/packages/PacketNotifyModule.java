package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import ru.minebot.extreme_energy.capability.IImplant;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.IChip;
import ru.minebot.extreme_energy.network.AbstractPacket;
import ru.minebot.extreme_energy.network.NetworkWrapper;

import java.util.List;

public class PacketNotifyModule extends AbstractPacket {
    protected int index;
    protected boolean isOn;

    public PacketNotifyModule(){}

    public PacketNotifyModule(int index, boolean isOn){
        this.index = index;
        this.isOn = isOn;
    }

    public PacketNotifyModule(boolean isOn){
        this.index = -1;
        this.isOn = isOn;
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(index);
        buf.writeBoolean(isOn);
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        index = buf.readInt();
        isOn = buf.readBoolean();
    }

    @Override
    public void clientHandler(EntityPlayer player) {
        IImplant cap = player.getCapability(ImplantProvider.IMPLANT, null);
        if (!cap.hasImplant())
            return;

        List<ItemStack> stacks = ModUtils.getModules(cap.getImplant());
        if (index != -1) {
            if (stacks.size() <= index)
                return;

            if (stacks.get(index).getItem() instanceof IChip)
                ((IChip) stacks.get(index).getItem()).changeActive(player, isOn);
        }
        else {
            for (ItemStack stack : stacks)
                if (stack.getItem() instanceof IChip)
                    ((IChip) stack.getItem()).changeActive(player, isOn);
        }
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
        IImplant cap = player.getCapability(ImplantProvider.IMPLANT, null);
        if (!cap.hasImplant())
            return;

        List<ItemStack> stacks = ModUtils.getModules(cap.getImplant());
        if (index != -1) {
            if (stacks.size() <= index)
                return;

            if (stacks.get(index).getItem() instanceof IChip)
                ((IChip) stacks.get(index).getItem()).changeActive(player, isOn);
            NetworkWrapper.instance.sendTo(new PacketNotifyModule(index, isOn), player);
        }
        else {
            for (ItemStack stack : stacks)
                if (stack.getItem() instanceof IChip)
                    ((IChip)stack.getItem()).changeActive(player, isOn);

            NetworkWrapper.instance.sendTo(new PacketNotifyModule(isOn), player);
        }
    }
}
