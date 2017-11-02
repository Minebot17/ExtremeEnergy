package ru.minebot.extreme_energy.network.packages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import ru.minebot.extreme_energy.init.ModItems;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.modules.ItemTeleportModule;
import ru.minebot.extreme_energy.network.AbstractPacket;
import ru.minebot.extreme_energy.tile_entities.TileEntityFC;

import java.util.ArrayList;
import java.util.List;

public class PacketFrequencyTeleport extends AbstractPacket {
    protected BlockPos pos;
    protected int frequency;

    public PacketFrequencyTeleport(){}

    public PacketFrequencyTeleport(BlockPos pos, int frequency) {
        this.pos = pos;
        this.frequency = frequency;
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeInt(frequency);
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        frequency = buf.readInt();
    }

    @Override
    public void clientHandler(EntityPlayer player) {
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
        TileEntityFC te = (TileEntityFC)(player.world.getTileEntity(pos) instanceof TileEntityFC ? player.world.getTileEntity(pos) : null);
        if (te != null && te.isActive() && te.getRadius() > player.getPosition().getDistance(pos.getX(), pos.getY(), pos.getZ())) {
            boolean yes = false;
            for (int i = 0; i < te.getSizeInventory(); i++)
                if (!te.getStackInSlot(i).isEmpty() && te.getStackInSlot(i).getItem() == ModItems.teleportModule)
                    yes = true;
            if (yes) {
                ModUtils.addEnergyToChunk(player.world, new ChunkPos(pos), 1000);
                List<TileEntity> entities = player.world.loadedTileEntityList;
                List<TileEntity> points = new ArrayList<>();
                for (TileEntity entity : entities)
                    if (entity instanceof TileEntityFC && !entity.getPos().equals(pos) && ((TileEntityFC) entity).getFrequency() == frequency && hasActiveTeleportModule((TileEntityFC) entity))
                        points.add(entity);
                if (points.size() != 0) {
                    TileEntityFC toTeleport = (TileEntityFC) ModUtils.getNearestTile(points, pos);
                    player.setPositionAndUpdate(((double) toTeleport.getPos().getX()) + 0.5d, ((double) toTeleport.getPos().getY()) + 1d, ((double) toTeleport.getPos().getZ()) + 0.5d);
                }
            }
        }
    }

    private boolean hasActiveTeleportModule(TileEntityFC tile){
        for (int i = 0; i < tile.getSizeInventory(); i++)
            if (tile.getStackInSlot(i).getItem() instanceof ItemTeleportModule && tile.isActive() && tile.hasField())
                return true;
        return false;
    }
}
