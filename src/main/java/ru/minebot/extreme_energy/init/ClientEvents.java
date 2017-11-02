package ru.minebot.extreme_energy.init;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.items.equipment.ItemEnergyTool;
import ru.minebot.extreme_energy.items.implants.Implant;
import ru.minebot.extreme_energy.modules.ChipArgs;
import ru.minebot.extreme_energy.modules.IKey;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.*;
import ru.minebot.extreme_energy.other.ImplantData;

import java.util.List;

public class ClientEvents {
    final Minecraft mc = Minecraft.getMinecraft();
    final FontRenderer font = Minecraft.getMinecraft().fontRenderer;

    @SubscribeEvent
    public void registerParticlesTexture(TextureStitchEvent.Pre e){
        //e.getMap().registerSprite(new ResourceLocation("meem:particles/coaldust"));
    }

    private boolean isRadialOpen;
    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent e){
        EntityPlayer player = Minecraft.getMinecraft().player;


        if (ModKeys.openImplantInterfaceKey.isPressed()){
            //Minecraft.getMinecraft().effectRenderer.addEffect(new CustomParticle(Minecraft.getMinecraft().world, player.getPosition()));
            player.openGui(ExtremeEnergy.instance, ModGuiHandler.IMPLANT_SCREEN, player.world, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
            return;
        }
        if (ModKeys.openRadialMenuKey.isKeyDown() && !isRadialOpen){
            isRadialOpen = true;
            Minecraft.getMinecraft().mouseHelper.ungrabMouseCursor();
            Mouse.setCursorPosition(Mouse.getX(), Mouse.getY()+1);
            Minecraft.getMinecraft().mouseHelper.grabMouseCursor();
            return;
        }
        else if (!ModKeys.openRadialMenuKey.isKeyDown() && isRadialOpen)
            isRadialOpen = false;
        if (ModKeys.openArmorSettings.isPressed()){
            player.openGui(ExtremeEnergy.instance, ModGuiHandler.ARMOR_SCREEN, player.world, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
            return;
        }

        ImplantData data = player.getCapability(ImplantProvider.IMPLANT, null).getImplant();
        if (data != null && data.implant.getBoolean("isOn")){
            List<ItemStack> modules = ModUtils.getModules(data);
            for (int i = 0; i < modules.size(); i++) {
                Item item = modules.get(i).getItem();
                if (item instanceof IKey && ModUtils.isModuleActive(data, i)) {
                    int[] keys = ((IKey)item).getKeyCodes(ModUtils.getNotNullCategory(modules.get(i)));
                    for (int j = 0; j < keys.length; j++)
                        if (Keyboard.isKeyDown(keys[j])){
                            NBTTagCompound iTag = data.implant;
                            RayTraceResult ray = player.rayTrace(200, Minecraft.getMinecraft().getRenderPartialTicks());
                            Entity entity = ModUtils.getMouseOver(Minecraft.getMinecraft().getRenderPartialTicks(), 100, true);
                            ChipArgs args = new ChipArgs(
                                    player,
                                    iTag.getByteArray("activesArray")[i] != 0,
                                    iTag.getBoolean("isOn"),
                                    iTag.getInteger("voltage") != 0,
                                    iTag.getInteger("voltage"),
                                    iTag.getInteger("energy"),
                                    ModUtils.getNotNullCategory(modules.get(i)),
                                    ray,
                                    entity != null ? entity.getEntityId() : 0
                            );
                            int needEnergy = ((IKey) item).getEnergy(args, ModUtils.getNotNullCategory(modules.get(i)).getInteger("power"), j);
                            if (data.implant.getInteger("energy") >= needEnergy) {
                                ((IKey) item).onModuleActivated(args, j);
                                NetworkWrapper.instance.sendToServer(new PacketModuleKey(modules.get(i), args, j, i));
                                data.implant.setInteger("energy", data.implant.getInteger("energy") - needEnergy);
                                NetworkWrapper.instance.sendToServer(new PacketSpendImplantEnergy(needEnergy));
                            }
                            else
                                ModUtils.sendModMessage(player, "notImplantEnergy");
                        }
                }
            }
        }
    }

    @SubscribeEvent
    public void harvestCheck(DrawBlockHighlightEvent e){
        if (e.getPlayer().inventory.getCurrentItem().getItem() instanceof ItemEnergyTool){
            ItemStack stack = e.getPlayer().inventory.getCurrentItem();
            NBTTagCompound data = ModUtils.getNotNullCategory(stack);
            if (data.getInteger("mode") == 0) {
                if (e.getTarget().typeOfHit == RayTraceResult.Type.BLOCK) {
                    int newMeta = getToolMeta(e.getTarget().getBlockPos(), data.getInteger("rbmmode"));
                    if (stack.getMetadata() != newMeta) {
                        stack.setItemDamage(newMeta);
                        NetworkWrapper.instance.sendToServer(new PacketToolMeta(newMeta));
                    }
                } else if (e.getTarget().typeOfHit == RayTraceResult.Type.ENTITY && e.getTarget().entityHit instanceof EntitySheep) {
                    int newMeta = 5;
                    if (stack.getMetadata() != newMeta) {
                        stack.setItemDamage(newMeta);
                        NetworkWrapper.instance.sendToServer(new PacketToolMeta(newMeta));
                    }
                }
            }
            else {
                int newMeta = data.getInteger("mode");
                if (stack.getMetadata() != newMeta) {
                    stack.setItemDamage(newMeta);
                    NetworkWrapper.instance.sendToServer(new PacketToolMeta(newMeta));
                }
            }
        }
    }

    private int getToolMeta(BlockPos blockPos, int rbmmode){ // 0 - shovel, 1 - hoe
        Block block = mc.world.getBlockState(blockPos).getBlock();
        Material material = mc.world.getBlockState(blockPos).getMaterial();
        boolean isDirt = block instanceof BlockDirt || block instanceof BlockGrass;
        boolean isLeaves = material == Material.LEAVES || material == Material.PLANTS || material == Material.VINE || material == Material.WEB || material == Material.CLOTH;
        String tool = block.getHarvestTool(mc.world.getBlockState(blockPos));
        return tool == null ? (isLeaves ? 5 : 0) : tool.equals("pickaxe") ? 1 : tool.equals("shovel") ? (rbmmode == 1 && isDirt ? 4 : 2) : tool.equals("axe") ? 3 : 0;
    }

    @SubscribeEvent
    public void onPlayerJoinInWorld(EntityJoinWorldEvent e){
        if (e.getEntity() instanceof EntityPlayer) {
            NetworkWrapper.instance.sendToServer(new PacketImplantDataWhenJoin());
        }
    }
}
