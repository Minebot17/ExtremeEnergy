package ru.minebot.extreme_energy.init;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ru.minebot.extreme_energy.energy.IFieldCreatorEnergy;
import ru.minebot.extreme_energy.items.capacitors.Capacitor;
import ru.minebot.extreme_energy.items.capacitors.ItemCreativeCapacitor;

import java.util.ArrayList;
import java.util.List;

public class CapacitorsEvents {
    public final int updatePeriod = 20;
    public List<CapacitorPosition> positions;

    @SubscribeEvent
    public void serverTick(TickEvent.ServerTickEvent e){
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();

        if (positions == null || world.getTotalWorldTime() % updatePeriod == 0)
            findCapacitors(world);

        for (CapacitorPosition position : positions) {
            if (position.stack.getItem() == Items.AIR)
                continue;

            Capacitor capacitor = ((Capacitor)position.stack.getItem());
            if (position.creator != null)
                capacitor.receiveEnergy(position.stack, position.creator.extractEnergy(position.creator.getRealVoltage(), false), false);
            else
                capacitor.receiveEnergy(position.stack, ModUtils.extractEnergyFromChunk(world, position.chunk, capacitor.getMaxReceive()/2000), false);
        }
    }

    public void findCapacitors(World world){
        positions = new ArrayList<>();
        List<EntityPlayer> players = world.playerEntities;
        for (EntityPlayer player : players)
            for (int i = 0; i < player.inventory.getSizeInventory(); i++){
                ItemStack currentStack = player.inventory.getStackInSlot(i);
                if (!currentStack.isEmpty() && currentStack.getItem() instanceof Capacitor && !(currentStack.getItem() instanceof ItemCreativeCapacitor) && ModUtils.getNotNullCategory(currentStack).getBoolean("state"))
                    positions.add(new CapacitorPosition(currentStack, player.getPosition()));
            }

        List<TileEntity> tiles = world.loadedTileEntityList;
        for (TileEntity tile : tiles)
            if (tile instanceof IInventory)
                for (int i = 0; i < ((IInventory)tile).getSizeInventory(); i++){
                    ItemStack currentStack = ((IInventory)tile).getStackInSlot(i);
                    if (!currentStack.isEmpty() && currentStack.getItem() instanceof Capacitor && !(currentStack.getItem() instanceof ItemCreativeCapacitor) && ModUtils.getNotNullCategory(currentStack).getBoolean("state"))
                        positions.add(new CapacitorPosition(currentStack, tile.getPos()));
                }

        List<TileEntity> fieldCreators = ModUtils.filter(world.loadedTileEntityList, tile -> tile instanceof IFieldCreatorEnergy);
        for (CapacitorPosition capPos : positions){
            NBTTagCompound category = ModUtils.getNotNullCategory(capPos.stack);
            if (!category.getBoolean("state"))
                continue;

            for (TileEntity fieldCreator : fieldCreators) {
                IFieldCreatorEnergy field = ((IFieldCreatorEnergy)fieldCreator);
                if (field.isActive() && field.getFrequency() == category.getInteger("frequency") && ModUtils.inRadius(fieldCreator.getPos(), capPos.position, field.getRadius())) {
                    capPos.creator = field;
                    break;
                }
            }
        }
    }

    public class CapacitorPosition {
        public ItemStack stack;
        public BlockPos position;
        public IFieldCreatorEnergy creator;
        public ChunkPos chunk;

        public CapacitorPosition(ItemStack stack, BlockPos position) {
            this.stack = stack;
            this.position = position;
            chunk = new ChunkPos(position);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof CapacitorPosition && stack.equals(((CapacitorPosition) obj).stack);
        }

        @Override
        public int hashCode(){
            return stack.hashCode();
        }
    }
}
