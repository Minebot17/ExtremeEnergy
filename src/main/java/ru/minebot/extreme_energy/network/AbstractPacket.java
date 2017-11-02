package ru.minebot.extreme_energy.network;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import ru.minebot.extreme_energy.ExtremeEnergy;

public abstract class AbstractPacket implements IMessage {
    @Override
    public abstract void toBytes(ByteBuf buf);

    @Override
    public abstract void fromBytes(ByteBuf buf);

    public abstract void clientHandler(EntityPlayer player);

    public abstract void serverHandler(EntityPlayerMP player);

    public ByteBuf getOrCreateByteBuf()
    {
        return Unpooled.buffer();
    }
}
