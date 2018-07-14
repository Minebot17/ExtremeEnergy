package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.capability.IImplant;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.implants.Core;
import ru.minebot.extreme_energy.network.AbstractPacket;
import ru.minebot.extreme_energy.other.ImplantData;

public class PacketClientImplantData extends AbstractPacket {
    protected boolean isOn;
    protected boolean showInfo;
    protected byte[] activeArray;
    protected int frequency;
    protected int page;
    protected boolean setArmor;
    protected boolean powCap;
    protected boolean powImplant;
    protected boolean enableModules;
    protected int armorPower;


    public PacketClientImplantData(){}

    public PacketClientImplantData(boolean setArmor) {
        IImplant cap = Minecraft.getMinecraft().player.getCapability(ImplantProvider.IMPLANT, null);
        if (cap.hasImplant()){
            NBTTagCompound tag = cap.getImplant().implant;
            isOn = tag.getBoolean("isOn");
            showInfo = tag.getBoolean("isShowInfo");
            activeArray = tag.getByteArray("activesArray");
            frequency = tag.getInteger("frequency");
            page = tag.getInteger("page");

            if (cap.hasCore() && setArmor){
                this.setArmor = true;
                NBTTagCompound core = cap.getImplant().core;
                powCap = core.getBoolean("powCap");
                powImplant = core.getBoolean("powImp");
                enableModules = core.getBoolean("enableModules");
                armorPower = core.getInteger("power");
            }
            else
                this.setArmor = false;
        }
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeBoolean(isOn);
        buf.writeBoolean(showInfo);
        buf.writeByte(activeArray.length);
        for (byte anActiveArray : activeArray) buf.writeByte(anActiveArray);
        buf.writeInt(frequency);
        buf.writeInt(page);
        buf.writeBoolean(setArmor);

        if (setArmor){
            buf.writeBoolean(powCap);
            buf.writeBoolean(powImplant);
            buf.writeBoolean(enableModules);
            buf.writeInt(armorPower);
        }
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        isOn = buf.readBoolean();
        showInfo = buf.readBoolean();
        byte count = buf.readByte();
        activeArray = new byte[count];
        for (int i = 0; i < count; i++)
            activeArray[i] = buf.readByte();
        frequency = buf.readInt();
        page = buf.readInt();
        setArmor = buf.readBoolean();

        if (setArmor){
            powCap = buf.readBoolean();
            powImplant = buf.readBoolean();
            enableModules = buf.readBoolean();
            armorPower = buf.readInt();
        }
    }

    @Override
    public void clientHandler(EntityPlayer player) {
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
        IImplant cap = player.getCapability(ImplantProvider.IMPLANT, null);
        if (cap.hasImplant()){
            ImplantData data = cap.getImplant();
            NBTTagCompound implant = data.implant;
            implant.setBoolean("isOn", isOn);
            implant.setBoolean("isShowInfo", showInfo);
            implant.setByteArray("activesArray", activeArray);
            if (frequency >= 0 && frequency < 100000000)
                implant.setInteger("frequency", frequency);
            if (page >= 0 && page < (data.type+1)*2)
                implant.setInteger("page", page);

            if (cap.hasCore() && setArmor){
                NBTTagCompound core = data.core;
                core.setBoolean("powCap", powCap);
                core.setBoolean("powImp", powImplant);
                core.setBoolean("enableModules", enableModules);
                if (armorPower >= 0 && armorPower < ((Core)ModUtils.getCore(data).getItem()).getMaxPower())
                    core.setInteger("power", armorPower);
                data.core = core;
            }
            data.implant = implant;
            cap.setImplant(data);
        }
    }
}
