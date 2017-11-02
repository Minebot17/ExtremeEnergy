package ru.minebot.extreme_energy.init;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.gui.*;
import ru.minebot.extreme_energy.gui.containers.*;
import ru.minebot.extreme_energy.items.implants.Implant;
import ru.minebot.extreme_energy.items.modules.ItemPathInfoModule;
import ru.minebot.extreme_energy.other.ImplantData;
import ru.minebot.extreme_energy.tile_entities.*;

import javax.annotation.Nullable;
import java.util.List;

public class ModGuiHandler implements IGuiHandler {

    public static final int HVG_GUI = 0;
    public static final int HTF_GUI = 1;
    public static final int HPC_GUI = 2;
    public static final int HPA_GUI = 3;
    public static final int FC_GUI = 4;
    public static final int BI_GUI = 5;
    public static final int IMPLANT_SCREEN = 6;
    public static final int FI_SCREEN = 7;
    public static final int TOOL_SCREEN = 8;
    public static final int SWORD_GUI = 9;
    public static final int BASIC_CORE_GUI = 10;
    public static final int ARMOR_SCREEN = 11;
    public static final int AI_GUI = 12;
    public static final int EI_GUI = 13;
    public static final int ADVANCED_CORE_GUI = 14;
    public static final int EXTREME_CORE_GUI = 15;
    public static final int FT_GUI = 16;
    public static final int SG_GUI = 17;
    public static final int FG_GUI = 18;
    public static final int TG_GUI = 19;
    public static final int FTR_GUI = 20;
    public static final int EB_GUI = 21;
    public static final int CS_SCREEN = 22;
    public static final int NR_GUI = 23;
    public static final int NUCLEAR_ENERGY_MODULE_GUI = 24;
    public static final int MARKER_SCREEN = 25;
    public static final int ENERGYBALANCER_GUI = 26;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if (ID == HVG_GUI)
            return new HvgContainer(player.inventory, (TileEntityHVG) world.getTileEntity(pos));
        else if (ID == HTF_GUI)
            return new HtfContainer(player, player.inventory, (TileEntityHTF) world.getTileEntity(pos));
        else if (ID == HPC_GUI)
            return new HpcContainer(player, player.inventory, (TileEntityHPC) world.getTileEntity(pos));
        else if (ID == HPA_GUI)
            return new HpaContainer(player, player.inventory, (TileEntityHPA) world.getTileEntity(pos));
        else if (ID == FC_GUI)
            return new FcContainer(player.inventory, (TileEntityFC) world.getTileEntity(pos));
        else if (ID == BI_GUI)
            return new BiContainer(player.inventory, new InventoryBi(player.inventory.getCurrentItem()));
        else if (ID == SWORD_GUI)
            return new SwordContainer(player.inventory, new InventorySword(world, player.inventory.getCurrentItem()));
        else if (ID == BASIC_CORE_GUI)
            return new BasicCoreContainer(player.inventory, new InventoryBasicCore(player.inventory.getCurrentItem()));
        else if (ID == ADVANCED_CORE_GUI)
            return new AdvancedCoreContainer(player.inventory, new InventoryAdvancedCore(player.inventory.getCurrentItem()));
        else if (ID == EXTREME_CORE_GUI)
            return new ExtremeCoreContainer(player.inventory, new InventoryExtremeCore(player.inventory.getCurrentItem()));
        else if (ID == AI_GUI)
            return new AiContainer(player.inventory, new InventoryAi(player.inventory.getCurrentItem()));
        else if (ID == EI_GUI)
            return new EiContainer(player.inventory, new InventoryEi(player.inventory.getCurrentItem()));
        else if (ID == FT_GUI)
            return new FtContainer(player.inventory, (TileEntityFT) world.getTileEntity(pos));
        else if (ID == SG_GUI)
            return new SgContainer(player.inventory, (TileEntitySG) world.getTileEntity(pos));
        else if (ID == FG_GUI)
            return new FgContainer(player.inventory, (TileEntityFG) world.getTileEntity(pos));
        else if (ID == TG_GUI)
            return new TgContainer(player.inventory, (TileEntityTG) world.getTileEntity(pos));
        else if (ID == FTR_GUI)
            return new FtrContainer(player.inventory, (TileEntityFTR) world.getTileEntity(pos));
        else if (ID == EB_GUI)
            return new EbContainer(player.inventory, (TileEntityEB) world.getTileEntity(pos));
        else if (ID == NR_GUI)
            return new NrContainer(player.inventory, (TileEntityNR) world.getTileEntity(pos));
        else if (ID == NUCLEAR_ENERGY_MODULE_GUI)
            return new NuclearEnergyModuleContainer(player.inventory, player.inventory.getCurrentItem());
        else if (ID == ENERGYBALANCER_GUI)
            return new EnergyBalancerContainer(player.inventory, new InventoryEnergyBalancer(world, player.inventory.getCurrentItem()));

        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if (ID == HVG_GUI)
            return new HvgGui(player.inventory, (TileEntityHVG) world.getTileEntity(pos));
        else if (ID == HTF_GUI)
            return new HtfGui(player, player.inventory, (TileEntityHTF) world.getTileEntity(pos));
        else if (ID == HPC_GUI)
            return new HpcGui(player, player.inventory, (TileEntityHPC) world.getTileEntity(pos));
        else if (ID == HPA_GUI)
            return new HpaGui(player, player.inventory, (TileEntityHPA) world.getTileEntity(pos));
        else if (ID == FC_GUI)
            return new FcGui(player.inventory, (TileEntityFC) world.getTileEntity(pos));
        else if (ID == BI_GUI)
            return new BiGui(player.inventory, new InventoryBi(player.inventory.getCurrentItem()));
        else if (ID == IMPLANT_SCREEN) {
            ImplantData iData = player.getCapability(ImplantProvider.IMPLANT, null).getImplant();
            if (iData != null)
                return new ImplantsScreen(iData);
            else
                ModUtils.sendModMessage(player, "notHaveImplant");
        }
        else if (ID == FI_SCREEN)
            return new FrequencyInstallerScreen(ModUtils.getNotNullCategory(player.inventory.getCurrentItem()));
        else if (ID == TOOL_SCREEN) {
            ItemStack stack = player.inventory.getCurrentItem();
            return new ToolScreen(ModUtils.getNotNullCategory(stack));
        }
        else if (ID == SWORD_GUI){
            ItemStack stack = player.inventory.getCurrentItem();
            return new SwordGui(player.inventory, stack, ModUtils.getNotNullCategory(stack));
        }
        else if (ID == BASIC_CORE_GUI)
            return new BasicCoreGui(player.inventory, player.inventory.getCurrentItem());
        else if (ID == ARMOR_SCREEN) {
            ImplantData iData = player.getCapability(ImplantProvider.IMPLANT, null).getImplant();
            if (iData != null) {
                if (!new ItemStack(iData.core).isEmpty())
                    return new ArmorSettingsScreen(iData, player.inventory.armorInventory);
                else
                    ModUtils.sendModMessage(player, "notHaveCore");
            }
            else
                ModUtils.sendModMessage(player, "notHaveImplant");
        }
        else if (ID == ADVANCED_CORE_GUI)
            return new AdvancedCoreGui(player.inventory, player.inventory.getCurrentItem());
        else if (ID == EXTREME_CORE_GUI)
            return new ExtremeCoreGui(player.inventory, player.inventory.getCurrentItem());
        else if (ID == AI_GUI)
            return new AiGui(player.inventory, new InventoryAi(player.inventory.getCurrentItem()));
        else if (ID == EI_GUI)
            return new EiGui(player.inventory, new InventoryEi(player.inventory.getCurrentItem()));
        else if (ID == FT_GUI)
            return new FtGui(player.inventory, (TileEntityFT) world.getTileEntity(pos));
        else if (ID == SG_GUI)
            return new SgGui(player.inventory, (TileEntitySG) world.getTileEntity(pos));
        else if (ID == FG_GUI)
            return new FgGui(player.inventory, (TileEntityFG) world.getTileEntity(pos));
        else if (ID == TG_GUI)
            return new TgGui(player.inventory, (TileEntityTG) world.getTileEntity(pos));
        else if (ID == FTR_GUI)
            return new FtrGui(player.inventory, (TileEntityFTR) world.getTileEntity(pos));
        else if (ID == EB_GUI)
            return new EbGui(player.inventory, (TileEntityEB) world.getTileEntity(pos));
        else if (ID == CS_SCREEN)
            return new CsScreen((TileEntityCS) world.getTileEntity(pos));
        else if (ID == NR_GUI)
            return new NrGui(player.inventory, (TileEntityNR) world.getTileEntity(pos));
        else if (ID == NUCLEAR_ENERGY_MODULE_GUI)
            return new NuclearEnergyModuleGui(player.inventory, player.inventory.getCurrentItem());
        else if (ID == MARKER_SCREEN) {
            List<ItemStack> modules = ModUtils.getModules(player.getCapability(ImplantProvider.IMPLANT, null).getImplant());
            ItemStack module = ItemStack.EMPTY;
            for (ItemStack item : modules)
                if (item.getItem() instanceof ItemPathInfoModule)
                    module = item;

            return ItemPathInfoModule.activeMark != null ? new MarkerScreen(module, ItemPathInfoModule.activeMark) : new MarkerScreen(module, new BlockPos(x, y ,z));
        }
        else if (ID == ENERGYBALANCER_GUI)
            return new EnergyBalancerGui(world, player.inventory, player.inventory.getCurrentItem());

        return null;
    }
}
