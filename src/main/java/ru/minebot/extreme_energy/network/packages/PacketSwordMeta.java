package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import ru.minebot.extreme_energy.items.equipment.ItemEnergySword;
import ru.minebot.extreme_energy.network.AbstractPacket;

public class PacketSwordMeta extends AbstractPacket {
    protected int meta;

    public PacketSwordMeta(){}

    public PacketSwordMeta(int meta){
        this.meta = meta;
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(meta);
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        meta = buf.readInt();
    }

    @Override
    public void clientHandler(EntityPlayer player) {
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
        ItemStack stack = player.inventory.getCurrentItem();
        if (stack.getItem() instanceof ItemEnergySword)
            stack.setItemDamage(meta);
    }
}
