package ru.minebot.extreme_energy.items.implants;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.capability.IImplant;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketNotifyModule;
import ru.minebot.extreme_energy.other.ImplantData;

public class ItemInjector extends Item{
    public ItemInjector(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.INJECTOR.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.INJECTOR.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setMaxStackSize(1);
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (playerIn.isSneaking()){
            IImplant cap = playerIn.getCapability(ImplantProvider.IMPLANT, null);
            ImplantData data = cap.getImplant();
            if (data != null){
                NetworkWrapper.instance.sendToServer(new PacketNotifyModule(false));
                cap.removeImplant();
                playerIn.inventory.removeStackFromSlot(playerIn.inventory.currentItem);
                NBTTagCompound category = new NBTTagCompound();
                NBTTagCompound tag = data.modules;
                tag.setTag("implant", data.implant);
                category.setTag(ExtremeEnergy.NBT_CATEGORY, tag);
                ItemStack implant = new ItemStack(data.type == 0 ? ModItems.baseImplant : data.type == 1 ? ModItems.advancedImplant : ModItems.extremeImplant);
                implant.setTagCompound(category);
                playerIn.inventory.addItemStackToInventory(implant);
            }
            else if (worldIn.isRemote)
                ModUtils.sendModMessage(playerIn, "notHaveImplant");
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
