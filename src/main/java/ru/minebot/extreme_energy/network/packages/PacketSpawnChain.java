package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.entities.EntityLightningChain;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.implants.Implant;
import ru.minebot.extreme_energy.network.AbstractPacket;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.other.ImplantData;

public class PacketSpawnChain extends AbstractPacket {
    protected Vec3d begin;
    protected Vec3d end;
    protected int maxAge;
    protected int damage;

    public PacketSpawnChain(){}

    public PacketSpawnChain(Vec3d begin, Vec3d end, int maxAge, int damage) {
        this.begin = begin;
        this.end = end;
        this.maxAge = maxAge;
        this.damage = damage;
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeDouble(begin.x);
        buf.writeDouble(begin.y);
        buf.writeDouble(begin.z);
        buf.writeDouble(end.x);
        buf.writeDouble(end.y);
        buf.writeDouble(end.z);
        buf.writeInt(maxAge);
        buf.writeInt(damage);
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        begin = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        end = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        maxAge = buf.readInt();
        damage = buf.readInt();
    }

    @Override
    public void clientHandler(EntityPlayer player) {
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
        ImplantData data = player.getCapability(ImplantProvider.IMPLANT, null).getImplant();
        if (data != null && ModUtils.containsModule(data, ModItems.lightningModule) && data.implant.getInteger("energy") > damage*10000) {
            player.world.spawnEntity(new EntityLightningChain(player, begin, end, maxAge, damage));
            data.implant.setInteger("energy", data.implant.getInteger("energy") - damage*10000);
            NetworkWrapper.instance.sendTo(new PacketImplantData(player), player);
        }
    }
}
