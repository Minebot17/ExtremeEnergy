package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import ru.minebot.extreme_energy.network.AbstractPacket;

public class PacketLangMessage extends AbstractPacket{
    protected boolean hasFormatting;
    protected String formatting;
    protected String key;

    public PacketLangMessage(){}

    public PacketLangMessage(String category, String key){
        this.key = category + "." + key;
        this.formatting = null;
        hasFormatting = false;
    }

    public PacketLangMessage(String category, String key, TextFormatting formatting){
        this.key = category + "." + key;
        this.formatting = formatting.toString();
        hasFormatting = true;
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, key);
        buf.writeBoolean(hasFormatting);
        if (hasFormatting)
            ByteBufUtils.writeUTF8String(buf, formatting);
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        key = ByteBufUtils.readUTF8String(buf);
        hasFormatting = buf.readBoolean();
        if (hasFormatting)
            formatting = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void clientHandler(EntityPlayer player) {
        player.sendMessage(new TextComponentString((hasFormatting ? formatting : "") + I18n.format(key)));
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
    }
}
