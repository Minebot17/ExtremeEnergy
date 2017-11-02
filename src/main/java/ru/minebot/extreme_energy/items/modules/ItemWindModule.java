package ru.minebot.extreme_energy.items.modules;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.entities.EntityLavaPart;
import ru.minebot.extreme_energy.gui.elements.moduleGui.IModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.KeyModuleGui;
import ru.minebot.extreme_energy.gui.elements.moduleGui.PowerModuleGui;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.*;
import ru.minebot.extreme_energy.entities.EntityWindCharge;

import java.util.List;

public class ItemWindModule extends ModuleFunctional implements IChip, ISwordModule, IArmorCoreModule, IKey{

    public ItemWindModule() {
        super(Reference.ExtremeEnergyItems.MODULEWIND.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULEWIND.getRegistryName());
    }

    @Override
    public void firstUpdate(FuncArgs args){
        List<EntityPlayer> players = ModUtils.radiusFilterPlayers(args.pos, args.world.playerEntities, args.radius);
        for (EntityPlayer player : players)
            if (args.isPublic || ModUtils.contains(args.cards, player.getUniqueID().hashCode()))
                player.addPotionEffect(new PotionEffect(Potion.getPotionById(25), args.voltage/50, 0));
    }

    @Override
    public void onModuleActivated(ChipArgs args, int keyIndex) {
        EntityPlayer player = args.player;
        RayTraceResult ray = args.player.world.rayTraceBlocks(args.player.getPositionVector(), args.player.getPositionVector().add(new Vec3d(0, -2, 0)), false, true, false);
        float power = args.data.getInteger("power");
        if (keyIndex == 0) {
            Vec3d vec = args.player.getLookVec();
            player.isAirBorne = true;
            player.motionX += vec.x * power / 2f;
            player.motionY += vec.y * power / 2f;
            player.motionZ += vec.z * power / 2f;
        }
        else if (!args.data.getBoolean("afall") && ray == null) {
            player.motionY -= power * 2f;
            args.data.setBoolean("afall", true);
        }
    }

    @Override
    public int getEnergy(ChipArgs args, int power, int keyIndex) {
        RayTraceResult ray = args.player.world.rayTraceBlocks(args.player.getPositionVector(), args.player.getPositionVector().add(new Vec3d(0, -2, 0)), false, true, false);
        return keyIndex == 0 ? power * 2000 : ray == null ? power * 5000 : 0;
    }

    @Override
    public int[] getKeyCodes(NBTTagCompound data) {
        return new int[]{
                data.getInteger("keyCode1"),
                data.getInteger("keyCode2")
        };
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public void onEntityHit(EntityLivingBase target, EntityLivingBase attacker, ItemStack sword, int power) {
    }

    @Override
    public void onHit(EntityLivingBase attacker, ItemStack sword, int power) {
        if (!attacker.world.isRemote)
            attacker.world.spawnEntity(new EntityWindCharge(attacker, attacker.getLookVec(), power, power*3, EnumParticleTypes.LAVA));
    }

    @Override
    public int getEnergy(ItemStack sword, int power) {
        return 200 * power;
    }

    @Override
    public int onImplantWork(ChipArgs args) {
        if (args.data.getBoolean("afall")){
            Vec3d pos = args.player.getPositionVector();
            RayTraceResult ray = args.player.world.rayTraceBlocks(pos, pos.add(new Vec3d(args.player.motionX, args.player.motionY, args.player.motionZ).normalize()), false, true, false);
            if (ray != null){
                args.player.world.createExplosion(args.player, pos.x, pos.y, pos.z, (float)args.data.getInteger("power")*1.5f, false);
                args.data.setBoolean("afall", false);
                args.player.isAirBorne = false;
            }
        }
        return 0;
    }

    @Override
    public void onDamaged(LivingHurtEvent event, Entity attacker, int power, NBTTagCompound data, EntityPlayer player) {
        if (!player.world.isRemote && attacker instanceof EntityLivingBase){
            EntityLivingBase livingBase = (EntityLivingBase)attacker;
            Vec3d vector = new Vec3d(-livingBase.getPositionVector().x + player.getPositionVector().x, 0, -livingBase.getPositionVector().z + player.getPositionVector().z);
            livingBase.knockBack(player, 0.2f*power, vector.x, vector.z);
        }
    }

    @Override
    public int getEnergy(int power) {
        return 200*power;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Wind module","is active"};
    }

    @Override
    public IModuleGui[] getGui() {
        return new IModuleGui[]{
                new PowerModuleGui(),
                new KeyModuleGui("Impulse key: ", "keyCode1"),
                new KeyModuleGui("Fall key: ", "keyCode2")
        };
    }
}
