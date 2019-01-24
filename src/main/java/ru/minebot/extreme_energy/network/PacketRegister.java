package ru.minebot.extreme_energy.network;

import ru.minebot.extreme_energy.network.packages.*;

public class PacketRegister {
    public static final Class[] packets = new Class[]{
            PacketFieldCreator.class,
            PacketFrequency.class,
            PacketPublicPrivate.class,
            PacketFrequencyItem.class,
            PacketImplantData.class,
            PacketToolMeta.class,
            PacketToolData.class,
            PacketSwordMeta.class,
            PacketModuleKey.class,
            PacketImplantDataWhenJoin.class,
            PacketSpawnParticle.class,
            PacketPlaySound.class,
            PacketLangMessage.class,
            PacketFrequencyTeleport.class,
            PacketTileRadius.class,
            PacketTransportEnergy.class,
            PacketSpawnLightning.class,
            PacketChargeData.class,
            PacketCS.class,
            PacketSpawnChain.class,
            PacketEntityMotion.class,
            PacketConfig.class,
            PacketEntityPotionEffects.class,
            PacketSaveMarks.class,
            PacketSliderPos.class,
            PacketNotifyModule.class,
            PacketSpendImplantEnergy.class,
            PacketClientImplantData.class,
            PacketModuleSync.class,
            PacketDoubleFrequency.class,
            PacketEB.class,
            PacketSG.class,
            PacketSwitchCapacitor.class
    };

    public static void register(){
        for (Class packet : packets)
            NetworkWrapper.instance.registerPacket(packet);
    }
}
