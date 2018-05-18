package ru.minebot.extreme_energy.items.modules;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.init.ModGuiHandler;
import ru.minebot.extreme_energy.items.implants.Implant;
import ru.minebot.extreme_energy.modules.ChipArgs;
import ru.minebot.extreme_energy.modules.IGenerator;
import ru.minebot.extreme_energy.modules.Module;

public class ItemNuclearEnergyModule extends Module implements IGenerator {
    public ItemNuclearEnergyModule() {
        super(Reference.ExtremeEnergyItems.MODULEENERGYNUCLEAR.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULEENERGYNUCLEAR.getRegistryName(), 1, false);
    }

    @Override
    public int generateEnergy(ChipArgs args) {
        if (!args.player.world.isRemote) {
            int maxEnergy = Implant.getMaxEnergy(args.player.getCapability(ImplantProvider.IMPLANT, null).getImplant().type);
            if (args.energy - 10 < maxEnergy) {
                int count = args.data.getInteger("californium");
                int timer = args.data.getInteger("timer");
                if (count != 0 && timer < 0) {
                    args.data.setInteger("californium", count - 1);
                    args.data.setInteger("timer", 60000);
                    timer = 60000;
                }
                args.data.setInteger("timer", timer - 1);
                return count == 0 ? 0 : 1000;
            }
            return 0;
        }
        return 0;
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        BlockPos pos = playerIn.getPosition();
        playerIn.openGui(ExtremeEnergy.instance, ModGuiHandler.NUCLEAR_ENERGY_MODULE_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
