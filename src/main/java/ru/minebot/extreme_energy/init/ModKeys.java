package ru.minebot.extreme_energy.init;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class ModKeys {

    public static KeyBinding openImplantInterfaceKey = new KeyBinding("bind.openImplant", Keyboard.KEY_C, "category.meem");
    public static KeyBinding openRadialMenuKey = new KeyBinding("bind.openRadialMenu", Keyboard.KEY_X, "category.meem");
    public static KeyBinding openArmorSettings = new KeyBinding("bind.openArmorSettings", Keyboard.KEY_Z, "category.meem");

    public static void register(){
        ClientRegistry.registerKeyBinding(openImplantInterfaceKey);
        ClientRegistry.registerKeyBinding(openRadialMenuKey);
        ClientRegistry.registerKeyBinding(openArmorSettings);
    }
}
