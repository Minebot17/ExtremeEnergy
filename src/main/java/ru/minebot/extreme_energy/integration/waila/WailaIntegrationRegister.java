package ru.minebot.extreme_energy.integration.waila;

import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import ru.minebot.extreme_energy.energy.*;

@WailaPlugin(value = "meem")
public class WailaIntegrationRegister implements IWailaPlugin {

    public WailaIntegrationRegister() {

    }

    public void register(IWailaRegistrar reg) {
        reg.registerBodyProvider(new WailaIFieldCreatorEnergy(), FieldCreatorStandart.class);
        reg.registerBodyProvider(new WailaIFieldReceiverEnergy(), FieldReceiverEnergyStandart.class);
        reg.registerBodyProvider(new WailaFieldTransmitterEnergy(), FieldTransmitterStandart.class);
    }
}
