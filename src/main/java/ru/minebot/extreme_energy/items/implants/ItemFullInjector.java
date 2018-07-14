package ru.minebot.extreme_energy.items.implants;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.capability.IImplant;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketNotifyModule;
import ru.minebot.extreme_energy.other.ImplantData;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFullInjector extends Item {

    public ItemFullInjector(){
        setUnlocalizedName(Reference.ExtremeEnergyItems.FULLINJECTOR.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.FULLINJECTOR.getRegistryName());
        setMaxStackSize(1);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return "Injector (" + (stack.hasTagCompound() ? ModUtils.getNotNullCategory(stack).getString("NIname") : "Full") + ")";
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        if (stack.hasTagCompound()){
            NBTTagList list = ModUtils.getNotNullCategory(stack).getTagList("Items", 10);
            for (int i = 0; i < list.tagCount(); ++i) {
                NBTTagCompound stackTag = list.getCompoundTagAt(i);
                ItemStack module = new ItemStack(stackTag);
                if (!module.isEmpty())
                    tooltip.add(module.getDisplayName());
            }
            NBTTagCompound tag = ModUtils.getNotNullCategory(stack).getCompoundTag("core");
            ItemStack core = new ItemStack(tag);
            if (!core.isEmpty())
                tooltip.add(core.getDisplayName());
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (playerIn.isSneaking()){
            IImplant cap = playerIn.getCapability(ImplantProvider.IMPLANT, null);
            ImplantData data = cap.getImplant();
            if (data == null){
                NetworkWrapper.instance.sendToServer(new PacketNotifyModule(true));
                NBTTagCompound module = ModUtils.getNotNullCategory(playerIn.inventory.getCurrentItem());
                int type = module.getString("NIname").equals(new ItemStack(ModItems.baseImplant).getDisplayName()) ? 0 : module.getString("NIname").equals(new ItemStack(ModItems.advancedImplant).getDisplayName()) ? 1 : 2;
                NBTTagCompound implant = module.hasKey("implant") ? module.getCompoundTag("implant") : getImplantTag(type);
                ImplantData implantData = new ImplantData(type, module, implant, module.getCompoundTag("core"), playerIn.getUniqueID().hashCode());
                playerIn.inventory.removeStackFromSlot(playerIn.inventory.currentItem);
                cap.setImplant(implantData);
            }
            else if (worldIn.isRemote)
                ModUtils.sendModMessage(playerIn, "alreadyHaveImplant");
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    public NBTTagCompound getImplantTag(int type){
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("isOn", false);
        tag.setByteArray("activesArray", new byte[(type + 1)*2]);
        tag.setInteger("voltage", 0);
        tag.setInteger("energy", 0);
        tag.setByte("page", (byte)1);
        tag.setBoolean("isShowInfo", true);
        tag.setInteger("frequency", 0);
        tag.setInteger("favorite", 0);
        return tag;
    }
}
