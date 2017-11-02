package ru.minebot.extreme_energy.tile_entities;

import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.implants.Core;
import ru.minebot.extreme_energy.modules.IModuleTier;

public class InventoryBi extends BasicInventory{


    public InventoryBi(ItemStack itemStack) {
        super(itemStack, 3, new Item[]{
                ModItems.aggressiveModule,
                ModItems.lifeModule,
                ModItems.strangeModule,
                ModItems.electricalModule,
                ModItems.luckModule,
                ModItems.hungerEnergyModule,
                ModItems.motionEnergyModule,
                ModItems.linksInfoModule,
                ModItems.entityInfoModule
        });
    }

    public InventoryBi(ItemStack itemStack, int size, Item[] modules){
        super(itemStack, size, modules);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index == slots.length - 1  ? stack.getItem() instanceof Core : isAccepted(stack.getItem());
    }

    @Override
    public void save(){
        NBTTagCompound category = ModUtils.getNotNullCategory(itemStack);

        NBTTagList list = new NBTTagList();
        NBTTagCompound tag = new NBTTagCompound();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            if (!this.getStackInSlot(i).isEmpty()) {
                if (this.getStackInSlot(i).getItem() instanceof Core) {
                    this.getStackInSlot(i).writeToNBT(tag);
                } else {
                    NBTTagCompound stackTag = new NBTTagCompound();
                    stackTag.setByte("Slot", (byte) i);
                    this.getStackInSlot(i).writeToNBT(stackTag);
                    list.appendTag(stackTag);
                }
            }
        }
        category.setTag("Items", list);
        category.setTag("core", tag);
    }

    @Override
    public void load() {
        NBTTagCompound category = ModUtils.getNotNullCategory(itemStack);
        NBTTagList list = category.getTagList("Items", 10);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            ItemStack stack = new ItemStack(stackTag);
            this.slots[getNextEmptySlot(i, ((IModuleTier)stack.getItem()).getTier())] = stack;
        }
        NBTTagCompound tag = category.getCompoundTag("core");
        slots[slots.length - 1] = new ItemStack(tag);
    }

    private int getNextEmptySlot(int index, int tier) {
        for (int i = index; i < slots.length; i++)
            if (slots[i].isEmpty() && i >= tier*2)
                return i;
        Minecraft.getMinecraft().crashed(CrashReport.makeCrashReport(new Exception("Not find empty slot"), "In the implant was placed more modules than necessary"));
        return -1;
    }
}
