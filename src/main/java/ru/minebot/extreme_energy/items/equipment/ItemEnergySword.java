package ru.minebot.extreme_energy.items.equipment;

import cofh.redstoneflux.api.*;
import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.init.ModGuiHandler;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.implants.Implant;
import ru.minebot.extreme_energy.modules.ISwordModule;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketSwordMeta;
import ru.minebot.extreme_energy.other.ImplantData;

public class ItemEnergySword extends Item {
    public static int maxCap = 500000;

    public ItemEnergySword() {
        super();
        setUnlocalizedName(Reference.ExtremeEnergyItems.ENERGYSWORD.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyItems.ENERGYSWORD.getRegistryName());
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setMaxDamage(2);
        setHasSubtypes(true);
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        NBTTagCompound tag = ModUtils.getNotNullCategory(stack);
        ItemStack module1 = getModule(stack);
        if (stack.getItem() instanceof ItemEnergySword && stack.getMetadata() == 1 && !module1.isEmpty()){
            int power = tag.getInteger("power");
            if (module1.getItem() instanceof ISwordModule) {
                ISwordModule module = (ISwordModule) module1.getItem();
                if (module.getEnergy(stack, power) <= tag.getInteger("energy")) {
                    module.onHit(entityLiving, stack, power);
                    tag.setInteger("energy", tag.getInteger("energy") - module.getEnergy(stack, power));
                }
            }
            tag.setInteger("energy", tag.getInteger("energy") - getEnergyByPower(stack));
        }
        return false;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.inventory.getCurrentItem();
        NBTTagCompound tag = ModUtils.getNotNullCategory(stack);
        if (playerIn.isSneaking()) {
            BlockPos pos = playerIn.getPosition();
            playerIn.openGui(ExtremeEnergy.instance, ModGuiHandler.SWORD_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        else if (worldIn.isRemote && tag.getInteger("energy") >= getEnergyByPower(tag.getInteger("power"))){
            stack.setItemDamage(stack.getMetadata() == 0 ? 1 : 0);
            NetworkWrapper.instance.sendToServer(new PacketSwordMeta(stack.getMetadata()));
        }
        return new ActionResult(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(slot);
        NBTTagCompound tag = ModUtils.getNotNullCategory(stack);
        if (!tag.hasKey("power"))
            tag.setInteger("power", 1);

        if (slot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", stack.getMetadata() == 0 ? 1 : (tag.getInteger("power")*5), 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
        }

        return multimap;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        ItemStack module1 = getModule(stack);
        NBTTagCompound tag = ModUtils.getNotNullCategory(stack);
        if (stack.getItem() instanceof ItemEnergySword && stack.getMetadata() == 1 && !module1.isEmpty()){
            int power = tag.getInteger("power");
            if (module1.getItem() instanceof ISwordModule) {
                ISwordModule module = (ISwordModule) module1.getItem();
                if (module.getEnergy(stack, power) <= tag.getInteger("energy")) {
                    module.onEntityHit(target, attacker, stack, power);
                    tag.setInteger("energy", tag.getInteger("energy") - module.getEnergy(stack, power));
                }
            }
            tag.setInteger("energy", tag.getInteger("energy") - getEnergyByPower(stack));
        }
        return true;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem() ? true : oldStack.getMetadata() != newStack.getMetadata();
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return oldStack.getItem() != newStack.getItem() ? true : oldStack.getMetadata() != newStack.getMetadata();
    }

    @Override
    public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
        return false;
    }

    final int powerToEnergy = 1000;
    protected int getEnergyByPower(ItemStack stack){
        return ModUtils.getNotNullCategory(stack).getInteger("power") * powerToEnergy;
    }

    protected int getEnergyByPower(int power){
        return power * powerToEnergy;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof EntityPlayer){
            NBTTagCompound data = ModUtils.getNotNullCategory(stack);
            EntityPlayer player = (EntityPlayer)entityIn;
            if (worldIn.isRemote && data.getInteger("energy") < getEnergyByPower(data.getInteger("power")) && stack.getMetadata() == 1){
                stack.setItemDamage(0);
                NetworkWrapper.instance.sendToServer(new PacketSwordMeta(0));
            }

            int extractSpeed = 500;

            if (data.getBoolean("powCap") && data.getInteger("energy") != ItemEnergySword.maxCap) {
                int energy = data.getInteger("energy");
                for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                    ItemStack stack1 = player.inventory.getStackInSlot(i);
                    if (stack1.getItem() instanceof IEnergyContainerItem) {
                        energy += ((IEnergyContainerItem) stack1.getItem()).extractEnergy(stack1, extractSpeed, false);
                    }
                }
                data.setInteger("energy", energy > ItemEnergySword.maxCap ? ItemEnergySword.maxCap : energy);
            }

            if (data.getBoolean("powImplant") && data.getInteger("energy") != ItemEnergySword.maxCap){
                ImplantData iData = player.getCapability(ImplantProvider.IMPLANT, null).getImplant();
                if (iData != null && iData.implant.getInteger("energy") > extractSpeed && iData.implant.getBoolean("isOn")){
                    int energy = data.getInteger("energy");
                    iData.implant.setInteger("energy", iData.implant.getInteger("energy") - extractSpeed);
                    energy += extractSpeed;
                    data.setInteger("energy", energy > ItemEnergySword.maxCap ? ItemEnergySword.maxCap : energy);
                }
            }
        }
    }

    private ItemStack getModule(ItemStack stack){
        NBTTagCompound stackTag = ModUtils.getNotNullTag(stack).getTagList("Items", 10).getCompoundTagAt(0);
        return new ItemStack(stackTag);
    }
}
