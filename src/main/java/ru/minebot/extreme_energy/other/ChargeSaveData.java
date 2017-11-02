package ru.minebot.extreme_energy.other;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketChargeData;

import java.util.HashMap;

public class ChargeSaveData extends WorldSavedData {
    public static final String NAME = "meemChunkChargeData";

    public HashMap<ChunkPos, Integer> map = new HashMap<>();

    public ChargeSaveData(){
        super(NAME);
    }

    public ChargeSaveData(String name) {
        super(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        map = new HashMap<>();
        NBTTagList list = (NBTTagList) nbt.getTag("meemChunk");
        for (NBTBase base : list){
            NBTTagCompound tag = (NBTTagCompound)base;
            map.put(new ChunkPos(tag.getInteger("x"), tag.getInteger("z")), tag.getInteger("value"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();
        for (ChunkPos pos : map.keySet()){
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("x", pos.x);
            tag.setInteger("z", pos.z);
            tag.setInteger("value", map.get(pos));
            list.appendTag(tag);
        }
        nbt.setTag("meemChunk", list);
        return nbt;
    }

    @Override
    public void markDirty(){
        super.markDirty();
        NetworkWrapper.instance.sendToAll(new PacketChargeData(map));
    }

    public static boolean hasData(World world){
        WorldSavedData data = world.loadData(ChargeSaveData.class, NAME);
        return data != null;
    }

    public static void createNewData(World world){
        world.setData(NAME, new ChargeSaveData());
    }

    public static ChargeSaveData getOrCreateData(World world){
        WorldSavedData data = world.loadData(ChargeSaveData.class, NAME);
        if (data == null){
            data = new ChargeSaveData();
            world.setData(NAME, data);
        }
        return (ChargeSaveData) data;
    }
}
