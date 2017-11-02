package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.implants.Implant;
import ru.minebot.extreme_energy.modules.ChipArgs;
import ru.minebot.extreme_energy.modules.IKey;
import ru.minebot.extreme_energy.network.AbstractPacket;
import ru.minebot.extreme_energy.other.ImplantData;

import java.util.List;

public class PacketModuleKey extends AbstractPacket{
    protected ItemStack key;
    protected ChipArgs args;
    protected int keyIndex;
    protected int moduleIndex;

    public PacketModuleKey(){}

    public PacketModuleKey(ItemStack key, ChipArgs args, int keyIndex, int moduleIndex){
        this.key = key;
        this.args = args;
        this.keyIndex = keyIndex;
        this.moduleIndex = moduleIndex;
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, key);
        RayTraceResult ray = args.blocksRay;
        buf.writeInt(ray.typeOfHit == RayTraceResult.Type.BLOCK ? 0 : ray.typeOfHit == RayTraceResult.Type.ENTITY ? 1 : 2);

        buf.writeBoolean(args.isModuleActive);
        buf.writeBoolean(args.isImplantPowered);
        buf.writeBoolean(args.isImplantCharging);
        buf.writeFloat(args.voltageReceiving);
        buf.writeInt(args.energy);
        ByteBufUtils.writeTag(buf, args.data);

        if (RayTraceResult.Type.MISS != ray.typeOfHit) {
            buf.writeFloat((float) ray.hitVec.x);
            buf.writeFloat((float) ray.hitVec.y);
            buf.writeFloat((float) ray.hitVec.z);
            buf.writeInt(ray.sideHit.getIndex());
            buf.writeInt(ray.getBlockPos().getX());
            buf.writeInt(ray.getBlockPos().getY());
            buf.writeInt(ray.getBlockPos().getZ());
        }

        buf.writeInt(args.entityCollide);

        buf.writeInt(keyIndex);
        buf.writeInt(moduleIndex);
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        key = ByteBufUtils.readItemStack(buf);
        int c = buf.readInt();
        RayTraceResult.Type type = c == 0 ? RayTraceResult.Type.BLOCK : c == 1 ? RayTraceResult.Type.ENTITY : RayTraceResult.Type.MISS;
        args = new ChipArgs(
                null,
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readFloat(),
                buf.readInt(),
                ByteBufUtils.readTag(buf),
                type == RayTraceResult.Type.MISS ? null : new RayTraceResult(
                        type,
                        new Vec3d(buf.readFloat(), buf.readFloat(), buf.readFloat()),
                        EnumFacing.getFront(buf.readInt()),
                        new BlockPos(buf.readInt(), buf.readInt(), buf.readInt())
                ),
                buf.readInt()
        );
        keyIndex = buf.readInt();
        moduleIndex = buf.readInt();
    }

    @Override
    public void clientHandler(EntityPlayer player) {
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
        ImplantData data = player.getCapability(ImplantProvider.IMPLANT, null).getImplant();
        if (key.getItem() instanceof IKey && data != null){
            args.player = player;
            List<ItemStack> stacks = ModUtils.getModules(data);
            args.data = ModUtils.getNotNullCategory(stacks.get(moduleIndex));
            ((IKey)key.getItem()).onModuleActivated(args, keyIndex);
        }
    }
}
