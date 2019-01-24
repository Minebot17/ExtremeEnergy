package gloomyfolken_extreme_energy.hooklib.example;

import gloomyfolken_extreme_energy.hooklib.minecraft.HookLoader;
import gloomyfolken_extreme_energy.hooklib.minecraft.PrimaryClassTransformer;

public class ExampleHookLoader extends HookLoader {

    // включает саму HookLib'у. Делать это можно только в одном из HookLoader'ов.
    // При желании, можно включить gloomyfolken_extreme_energy.hooklib.minecraft.HookLibPlugin и не указывать здесь это вовсе.
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{PrimaryClassTransformer.class.getName()};
    }

    @Override
    public void registerHooks() {
        //регистрируем класс, где есть методы с аннотацией @Hook
        registerHookContainer("gloomyfolken_extreme_energy.hooklib.example.AnnotationHooks");
    }
}
