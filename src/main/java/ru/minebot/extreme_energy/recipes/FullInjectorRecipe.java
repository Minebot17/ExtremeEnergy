package ru.minebot.extreme_energy.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.implants.Implant;
import ru.minebot.extreme_energy.items.implants.ItemInjector;

import javax.annotation.Nullable;

public class FullInjectorRecipe implements IRecipe{

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        boolean check = true;
        ItemStack implant = ItemStack.EMPTY;
        ItemStack injector = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); i++)
            if (inv.getStackInSlot(i).getItem() instanceof Implant) {
                implant = inv.getStackInSlot(i);
                break;
            }
        for (int i = 0; i < inv.getSizeInventory(); i++)
            if (inv.getStackInSlot(i).getItem() instanceof ItemInjector) {
                injector = inv.getStackInSlot(i);
                break;
            }
        for (int i = 0; i < inv.getSizeInventory(); i++)
            if (!inv.getStackInSlot(i).isEmpty() && inv.getStackInSlot(i) != implant && inv.getStackInSlot(i) != injector) {
                check = false;
            }

        return !implant.isEmpty() && !injector.isEmpty() && check;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack result = new ItemStack(ModItems.fullInjector);
        ItemStack implant = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); i++)
            if (inv.getStackInSlot(i).getItem() instanceof Implant) {
                implant = inv.getStackInSlot(i);
                break;
            }
        result.setTagCompound(ModUtils.getNotNullTag(implant));
        ModUtils.getNotNullCategory(result).setString("NIname", implant.getDisplayName());
        return result;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width != 1 || height != 1;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(ModItems.fullInjector);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            if (inv.getStackInSlot(i).getItem() instanceof Implant)
                inv.removeStackFromSlot(i);
            else if (inv.getStackInSlot(i).getItem() instanceof ItemInjector) {
                if (inv.getStackInSlot(i).getCount() == 1)
                    inv.removeStackFromSlot(i);
                else
                    inv.getStackInSlot(i).setCount(inv.getStackInSlot(i).getCount() - 1);
            }
        }

        return NonNullList.create();
    }

    private ResourceLocation name;
    @Override
    public IRecipe setRegistryName(ResourceLocation name) {
        this.name = name;
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return name;
    }

    @Override
    public Class<IRecipe> getRegistryType() {
        return IRecipe.class;
    }
}
