package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import ru.minebot.extreme_energy.capability.IImplant;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.modules.ItemPathInfoModule;
import ru.minebot.extreme_energy.network.AbstractPacket;
import ru.minebot.extreme_energy.other.ImplantData;

import java.util.ArrayList;
import java.util.List;

public class PacketSaveMarks extends AbstractPacket {
    private List<ItemPathInfoModule.Mark> markList;

    public PacketSaveMarks(){}

    public PacketSaveMarks(ItemStack stack) {
        this.markList = ((ItemPathInfoModule)stack.getItem()).markList;
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(markList.size());
        for (ItemPathInfoModule.Mark mark : markList){
            ByteBufUtils.writeUTF8String(buf, mark.name);
            buf.writeInt(mark.pos.getX());
            buf.writeInt(mark.pos.getY());
            buf.writeInt(mark.pos.getZ());
            buf.writeInt(mark.color);
        }
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        markList = new ArrayList<>();
        int count = buf.readInt();
        for (int i = 0; i < count; i++)
            markList.add(new ItemPathInfoModule.Mark(
                    ByteBufUtils.readUTF8String(buf),
                    new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()),
                    buf.readInt()
            ));
    }

    @Override
    public void clientHandler(EntityPlayer player) {
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
        IImplant cap = player.getCapability(ImplantProvider.IMPLANT, null);
        if (!cap.hasImplant())
            return;

        ImplantData data = cap.getImplant();
        List<ItemStack> modules = ModUtils.getModules(data);
        ItemStack module = ItemStack.EMPTY;
        for (ItemStack item : modules)
            if (item.getItem() instanceof ItemPathInfoModule)
                module = item;
        if (module.isEmpty())
            return;

        ItemPathInfoModule.save(module, markList);
    }
}
