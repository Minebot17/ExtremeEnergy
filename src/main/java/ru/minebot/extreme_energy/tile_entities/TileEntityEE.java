package ru.minebot.extreme_energy.tile_entities;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.ChunkPos;
import ru.minebot.extreme_energy.other.ChargeSaveData;

public class TileEntityEE extends TileEntity implements ITickable {
    public boolean active;
    private ChunkPos posC;
    private final int extract = 1000;

    public TileEntityEE(){
        active = true;
    }

    @Override
    public void update() {
        if (!world.isRemote && active && world.getTotalWorldTime()%20==0 && !world.isAirBlock(getPos().down())) {
            ChargeSaveData data = ChargeSaveData.getOrCreateData(world);
            try {
                Integer energy = data.map.get(posC);
                if (energy != 0) {
                    data.map.put(posC, Math.max(0, energy - extract));
                    data.markDirty();
                }
            }
            catch (NullPointerException e){ posC = new ChunkPos(getPos()); }
        }
    }
}
