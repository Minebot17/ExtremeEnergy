package ru.minebot.extreme_energy.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL12;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.init.CommonEvents;
import ru.minebot.extreme_energy.init.ModKeys;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.implants.Implant;
import ru.minebot.extreme_energy.items.equipment.ItemEnergyTool;
import ru.minebot.extreme_energy.modules.ChipArgs;
import ru.minebot.extreme_energy.modules.IInfo;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketClientImplantData;
import ru.minebot.extreme_energy.network.packages.PacketImplantData;
import ru.minebot.extreme_energy.network.packages.PacketNotifyModule;
import ru.minebot.extreme_energy.network.packages.PacketToolData;
import ru.minebot.extreme_energy.other.ImplantData;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL;

public class RenderHUD {
    final Minecraft mc = Minecraft.getMinecraft();
    final FontRenderer font = Minecraft.getMinecraft().fontRenderer;
    final ImplantsHUD implantsHUD = new ImplantsHUD();
    final ImplantsRadialMenu menu = new ImplantsRadialMenu();
    final ToolRadialMenu toolMenu = new ToolRadialMenu();
    final int[] toSave = new int[]{
            GL_BLEND,
            GL_LIGHTING,
            GL_LIGHT0,
            GL_LIGHT1,
            GL_DEPTH_TEST,
            GL_RESCALE_NORMAL,
            GL_COLOR_MATERIAL,
            GL_ALPHA_TEST
    };
    int func;
    boolean mask;
    private int lastPosX;
    private int lastPosY;

    @SubscribeEvent
    public void tickRender(TickEvent.RenderTickEvent e) {
        if (mc.currentScreen == null && mc.world != null && mc.isGuiEnabled()) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            ImplantData data = player.getCapability(ImplantProvider.IMPLANT, null).getImplant();
            glPushMatrix();
            boolean[] glData = save();

            glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            glDisable(GL_LIGHTING);
            glDisable(GL_DEPTH_TEST);
            glDepthMask(false);
            ScaledResolution res = new ScaledResolution(mc);


            if (data != null) {
                NBTTagCompound iTag = data.implant;
                boolean isOn = iTag.getBoolean("isOn");
                if (isOn) {
                    byte[] modulesActive = iTag.getByteArray("activesArray");
                    int energy = iTag.getInteger("energy");
                    int voltage = iTag.getInteger("voltage");
                    List<ItemStack> modules = ModUtils.getModules(data);
                    for (int i = 0; i < modules.size(); i++)
                        if (modules.get(i).getItem() instanceof IInfo && modulesActive[i] == 1) {
                            glPushMatrix();
                            ((IInfo) modules.get(i).getItem()).renderScreen(new ChipArgs(player, modulesActive[i] != 0, isOn, voltage != 0, voltage, energy, ModUtils.getNotNullCategory(modules.get(i)), ModUtils.getBlocksRay(), ModUtils.getEntityRay()), mc, Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), new ScaledResolution(mc));
                            glPopMatrix();
                        }
                }
            }

            if (data != null && data.implant.getBoolean("isOn") && data.implant.getBoolean("isShowInfo")){
                glPushMatrix();
                int x = (res.getScaledWidth() - 109) / 8;
                int y = (res.getScaledHeight() - 290) / 2;
                glTranslated(x, y, 0);
                implantsHUD.draw(mc, font, data.implant.getInteger("energy"), Implant.getMaxEnergy(data.type), data.implant.getInteger("voltage"));
                glPopMatrix();
                glPushMatrix();
                int x1 = res.getScaledWidth() - 100;
                int y1 = res.getScaledHeight() - 100;
                glTranslated(x1, y1, 0);
                try{
                implantsHUD.drawCharge(mc, font, ModUtils.clientChunkCharge.get(new ChunkPos(player.getPosition())));}
                catch (Exception d){d.printStackTrace();}
                glPopMatrix();
            }
            if (ModKeys.openRadialMenuKey.isKeyDown()) {
                if (player.inventory.getCurrentItem().getItem() instanceof ItemEnergyTool) {
                    lastPosX = Mouse.getX();
                    lastPosY = Mouse.getY();
                    glPushMatrix();
                    int x = (res.getScaledWidth()) / 2;
                    int y = (res.getScaledHeight()) / 2;
                    glTranslated(x, y, 0);
                    toolMenu.draw(mc);
                    glPopMatrix();
                } else if (data != null && data.implant.getBoolean("isOn")) {
                    lastPosX = Mouse.getX();
                    lastPosY = Mouse.getY();
                    glPushMatrix();
                    int x = (res.getScaledWidth()) / 2;
                    int y = (res.getScaledHeight()) / 2;
                    glTranslated(x, y, 0);
                    menu.draw(mc, data);
                    glPopMatrix();
                }
            }
            glEnable(GL12.GL_RESCALE_NORMAL);
            load(glData);
            glEnable(GL_DEPTH_TEST);
            glPopMatrix();
        }
    }

    @SubscribeEvent
    public void mouseEvent(MouseEvent e){
        if (ModKeys.openRadialMenuKey.isKeyDown() && e.getButton() == 0 && e.isButtonstate()){
            mc.setIngameNotInFocus();
            Mouse.setCursorPosition(lastPosX, lastPosY);
            mc.mouseHelper.grabMouseCursor();
            EntityPlayer player = Minecraft.getMinecraft().player;
            ImplantData data = player.getCapability(ImplantProvider.IMPLANT, null).getImplant();
            ItemStack stack = player.inventory.getCurrentItem();
            if (stack.getItem() instanceof ItemEnergyTool && toolMenu.itemSelect != -1){
                stack.getTagCompound().getCompoundTag("meemCategory").setInteger("mode", toolMenu.itemSelect);
                NetworkWrapper.instance.sendToServer(new PacketToolData(stack.getTagCompound().getCompoundTag("meemCategory")));
            }
            else if (menu.itemSelect != -1) {
                byte[] array = data.implant.getByteArray("activesArray");
                array[menu.itemSelect] = array[menu.itemSelect] == 0 ? (byte) 1 : 0;
                data.implant.setByteArray("activesArray", array);
                NetworkWrapper.instance.sendToServer(new PacketNotifyModule(menu.itemSelect, array[menu.itemSelect] != 0));
                NetworkWrapper.instance.sendToServer(new PacketClientImplantData(false));
            }
        }
    }

    private boolean[] save(){
        boolean[] data = new boolean[toSave.length];
        for (int i = 0; i < toSave.length; i++)
            data[i] = glGetBoolean(toSave[i]);
        func = glGetInteger(GL_DEPTH_FUNC);
        mask = glGetBoolean(GL_DEPTH_WRITEMASK);
        glPushAttrib(GL_CURRENT_BIT);
        return data;
    }

    private void load(boolean[] data){
        for (int i = 0; i < data.length; i++) {
            if (data[i])
                glEnable(toSave[i]);
            else
                glDisable(toSave[i]);
        }
        glDepthFunc(func);
        glDepthMask(mask);
        glPopAttrib();
    }
}
