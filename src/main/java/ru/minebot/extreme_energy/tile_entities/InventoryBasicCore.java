package ru.minebot.extreme_energy.tile_entities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumParticleTypes;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.IModuleTier;

public class InventoryBasicCore extends BasicInventory {

    public InventoryBasicCore(ItemStack core){
        super(core, 1,new Item[] {
                ModItems.electricalModule,
                ModItems.strangeModule,
                ModItems.lifeModule,
                ModItems.aggressiveModule
        });
    }

    public InventoryBasicCore(ItemStack itemStack, int size, Item[] modules){
        super(itemStack, size, modules);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return isAccepted(stack.getItem());
    }

    public void save(){
        NBTTagCompound category = ModUtils.getNotNullCategory(itemStack);

        NBTTagList list = new NBTTagList();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            if (!this.getStackInSlot(i).isEmpty()) {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot", (byte) i);
                this.getStackInSlot(i).writeToNBT(stackTag);
                list.appendTag(stackTag);
            }
        }
        category.setTag("Items", list);
    }

    public void load(){
        NBTTagCompound category = ModUtils.getNotNullCategory(itemStack);
        NBTTagList list = category.getTagList("Items", 10);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            ItemStack stack = new ItemStack(stackTag);
            this.slots[getNextEmptySlot(i, ((IModuleTier)stack.getItem()).getTier())] = stack;
        }
    }

    private int getNextEmptySlot(int index, int tier) {
        for (int i = index; i < slots.length; i++)
            if (slots[i].isEmpty() && i >= tier)
                return i;
        Minecraft.getMinecraft().crashed(CrashReport.makeCrashReport(new Exception("Not find empty slot"), "In the implant was placed more modules than necessary"));
        return -1;
    }
}
