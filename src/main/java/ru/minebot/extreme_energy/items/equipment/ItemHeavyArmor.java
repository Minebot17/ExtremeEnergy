package ru.minebot.extreme_energy.items.equipment;

import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;

public class ItemHeavyArmor extends ItemArmor{
    public static ArmorMaterial material = EnumHelper.addArmorMaterial("meem:heavy", "meem:heavy", 0, new int[]{0, 0, 0, 0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2);

    public ItemHeavyArmor(String name, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
        super(material, renderIndexIn, equipmentSlotIn);
        setUnlocalizedName(Reference.ExtremeEnergyItems.HEAVYARMOR.getUnlocalizedName()+name);
        setRegistryName(Reference.ExtremeEnergyItems.HEAVYARMOR.getRegistryName()+name);
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
    }
}
