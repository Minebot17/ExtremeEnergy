package ru.minebot.extreme_energy.events.events_player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;
import ru.minebot.extreme_energy.capability.IImplant;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketLangMessage;
import ru.minebot.extreme_energy.other.ImplantData;

public class ImplantPowerDownEvent implements IEventPlayer {

    @Override
    public float getChance(int value) {
        return (float)(-(1f/1000f)*Math.pow(value-9750f, 2) + 1000f)/1000f;
    }

    @Override
    public int getRarity(int value) {
        return 12000;
    }

    @Override
    public void onEvent(EntityPlayer player, int voltage) {
        IImplant cap = player.getCapability(ImplantProvider.IMPLANT, null);
        if (cap.hasImplant()){
            ImplantData data = cap.getImplant();
            data.implant.setInteger("energy", 0);
            cap.setImplant(data);
            NetworkWrapper.instance.sendTo(new PacketLangMessage("playerEvent", "implantPowerDown", TextFormatting.BLUE), (EntityPlayerMP) player);
        }
    }
}
