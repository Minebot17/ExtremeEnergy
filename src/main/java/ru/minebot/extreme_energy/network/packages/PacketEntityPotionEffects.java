package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import ru.minebot.extreme_energy.items.modules.ItemEntityInfoModule;
import ru.minebot.extreme_energy.network.AbstractPacket;
import ru.minebot.extreme_energy.network.NetworkWrapper;

import java.util.ArrayList;
import java.util.List;

public class PacketEntityPotionEffects extends AbstractPacket {
    protected int id;
    protected List<PotionEffect> effectList;

    public PacketEntityPotionEffects(){}

    public PacketEntityPotionEffects(int id){
        this.id = id;
        effectList = new ArrayList<>();
    }

    public PacketEntityPotionEffects(List<PotionEffect> effectList){
        this.effectList = effectList;
    }


    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(id);
        buf.writeInt(effectList.size());
        for (PotionEffect anEffectList : effectList) {
            buf.writeInt(Potion.getIdFromPotion(anEffectList.getPotion()));
            buf.writeInt(anEffectList.getDuration());
        }
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        id = buf.readInt();
        int count = buf.readInt();
        effectList = new ArrayList<>();
        for (int i = 0; i < count; i++)
            effectList.add(new PotionEffect(Potion.getPotionById(buf.readInt()), buf.readInt()));
    }

    @Override
    public void clientHandler(EntityPlayer player) {
        ItemEntityInfoModule.effects = effectList;
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
        EntityLivingBase entity = (EntityLivingBase) player.world.getEntityByID(id);
        try {
            List<PotionEffect> list = new ArrayList<>(entity.getActivePotionEffects());
            NetworkWrapper.instance.sendTo(new PacketEntityPotionEffects(list), player);
        }
        catch (NullPointerException e){e.printStackTrace();}
    }
}
