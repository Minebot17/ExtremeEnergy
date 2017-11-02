package ru.minebot.extreme_energy.items.modules;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.init.ModConfig;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.IArmorCoreModule;
import ru.minebot.extreme_energy.modules.ModuleAddValue;

import java.util.List;

public class ItemSecurityModule extends ModuleAddValue implements IArmorCoreModule {

    public ItemSecurityModule() {
        super(Reference.ExtremeEnergyItems.MODULESECURITY.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULESECURITY.getRegistryName(), 1, false);
    }

    @Override
    public int getTier() {
        return 1;
    }

    @Override
    public void onDamaged(LivingHurtEvent event, Entity attacker, int power, NBTTagCompound data, EntityPlayer player) {
        if (player.isPotionActive(Potion.getPotionFromResourceLocation("meem:electricShock"))){
            if (event.getSource() == DamageSource.LIGHTNING_BOLT)
                ModUtils.addEnergyToChunk(player.world, new ChunkPos(player.getPosition()), 100);
            player.removePotionEffect(Potion.getPotionFromResourceLocation("meem:electricShock"));
            player.heal(4);
        }
    }

    @Override
    public int getEnergy(int power) {
        return 100;
    }
}
