package ru.minebot.extreme_energy.other;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashMap;

public class InfectedSaveData extends WorldSavedData {
    public static final String NAME = "meemChunkInfectedData";
    public HashMap<ChunkPos, Integer> map = new HashMap<>();

    public InfectedSaveData(){
        super(NAME);
    }

    public InfectedSaveData(String name) {
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

    public static InfectedSaveData getOrCreateData(World world){
        WorldSavedData data = world.loadData(InfectedSaveData.class, NAME);
        if (data == null){
            data = new InfectedSaveData();
            world.setData(NAME, data);
        }
        return (InfectedSaveData) data;
    }
}
