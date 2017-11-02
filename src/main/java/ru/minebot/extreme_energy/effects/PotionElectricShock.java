package ru.minebot.extreme_energy.effects;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import ru.minebot.extreme_energy.init.ModUtils;

public class PotionElectricShock extends Potion{
    protected PotionElectricShock() {
        super(false, 0);
        setPotionName("Electric Shock");
        setIconIndex(0, 0);
    }

    public static PotionElectricShock createPotion(){
        return new PotionElectricShock();
    }

    @Override
    public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier){
        entityLivingBaseIn.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 6);
    }

    @Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int power){
        if (entityLivingBaseIn.world.getTotalWorldTime()%10==0) {
            entityLivingBaseIn.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 1);
            entityLivingBaseIn.knockBack(entityLivingBaseIn, 0.15f * (power+1), ModUtils.random.nextFloat()-0.5f, ModUtils.random.nextFloat()-0.5f);
        }
    }

    @Override
    public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier){
        entityLivingBaseIn.addPotionEffect(new PotionEffect(Potion.getPotionById(10), 30, 2));
    }

    @Override
    public Potion setIconIndex(int par1, int par2) {
        return super.setIconIndex(par1, par2);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public int getStatusIconIndex() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("meem:textures/potions.png"));
        return super.getStatusIconIndex();
    }

    public boolean isReady(int duration, int amplifier) {
        return true;
    }
}
