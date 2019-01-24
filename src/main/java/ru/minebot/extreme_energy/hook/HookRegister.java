package ru.minebot.extreme_energy.hook;

import gloomyfolken_extreme_energy.hooklib.example.ExampleHookLoader;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion(value = "1.12.2")
public class HookRegister extends ExampleHookLoader {
    @Override
    public void registerHooks() {
        registerHookContainer("ru.minebot.extreme_energy.hook.Hooks");
    }
}
