package ru.minebot.extreme_energy.items.equipment;

import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemEnergyArmor extends ItemArmor{
    public static ArmorMaterial material = EnumHelper.addArmorMaterial("meem:energy", "meem:energy", 0, new int[]{0, 0, 0, 0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1);

    public ItemEnergyArmor(String name, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
        super(material, renderIndexIn, equipmentSlotIn);
        setUnlocalizedName(Reference.ExtremeEnergyItems.ENERGYARMOR.getUnlocalizedName()+name);
        setRegistryName(Reference.ExtremeEnergyItems.ENERGYARMOR.getRegistryName()+name);
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }

    @Override
    public boolean isDamageable()
    {
        return false;
    }
}
