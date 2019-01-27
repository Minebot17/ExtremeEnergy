package ru.minebot.extreme_energy.energy;

import cofh.redstoneflux.api.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.events.Events;
import ru.minebot.extreme_energy.events.events_block.IEventBlock;
import ru.minebot.extreme_energy.events.events_player.IEventPlayer;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.items.implants.Implant;
import ru.minebot.extreme_energy.items.equipment.ItemEnergySword;
import ru.minebot.extreme_energy.items.equipment.ItemEnergyTool;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketImplantData;
import ru.minebot.extreme_energy.other.ChargeSaveData;
import ru.minebot.extreme_energy.other.ImplantData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class FieldCreatorStandart extends TileEntity implements IFieldCreatorEnergy, IEnergyStorage, IEnergyReceiver, IEnergyConnection, ITickable {

    public boolean active;
    protected int energy;
    protected int capacity;
    protected int maxReceive;

    protected int maxRadius;
    protected int maxVoltage;
    protected int radius;
    protected int voltage;
    protected int frequency;
    protected ArrayList<BlockPos> links;
    protected int lossReduce;
    protected boolean enableEvents;
    protected HashMap<EntityPlayer, HashMap<IEventPlayer, Integer>> eventBuffer;
    protected HashMap<IEventBlock, Integer> eventBlockBuffer;
    protected List<IEventBlock> activeEvents;

    @Override
    public void applyLinkedBlocks(){
        brokeLinksAll();
        if (isActive() && getEnergyStored() != 0) {
            List<TileEntity> tes = ModUtils.radiusFilter(getPos(), world.tickableTileEntities, radius);
            for (TileEntity te : tes) {
                if ((te instanceof FieldTransmitterStandart && ((FieldTransmitterStandart) te).getFrequencyReceive() == frequency) || (te instanceof IFieldReceiverEnergy && ((IFieldReceiverEnergy) te).getFrequency() == frequency)) {
                    IFieldReceiverEnergy block = (IFieldReceiverEnergy) te;
                    if (block.getLink() == null || getPos().equals(block.getLink()))
                        createLink(te.getPos());
                    else if (block.getVoltage() < getRealVoltage()) {
                        ((IFieldCreatorEnergy) world.getTileEntity(block.getLink())).brokeLink(te.getPos());
                        createLink(te.getPos());
                    }
                }
            }
        }
    }

    @Override
    public void update() {
        if (!isActive())
            return;
        int convertedVoltage = getRealVoltage();

        // Powered machines
        List<BlockPos> toRemove = new ArrayList<>();
        for (int i = 0; i < links.size(); i++){
            TileEntity tile = world.getTileEntity(links.get(i));
            if (tile == null)
                toRemove.add(links.get(i));
            else if (tile instanceof IFieldReceiverEnergy) {
                IFieldReceiverEnergy ifre = (IFieldReceiverEnergy) tile;
                if (ifre.isActive()) {
                    int energy = (int) Math.sqrt(convertedVoltage);
                    extractEnergy(energy / 4 * 3, false);

                    if (!world.isRemote && world.getTotalWorldTime() % 4 == 0) {
                        ChargeSaveData data = ChargeSaveData.getOrCreateData(world);
                        Integer e = data.map.get(new ChunkPos(getPos()));
                        data.map.put(new ChunkPos(getPos()), e + energy * 4);
                        data.markDirty();
                    }

                    ifre.onField();
                }
            }
            else
                toRemove.add(links.get(i));
        }
        for (BlockPos pos : toRemove)
            brokeLink(pos);

        // Powered implants
        List<EntityPlayer> players = ModUtils.radiusFilterPlayers(getPos(), world.playerEntities, getRadius());
        if (!world.isRemote && world.getTotalWorldTime()%5==0) {
            if (getEnergyStored() != 0) {
                for (int i = 0; i < players.size(); i++) {
                    ImplantData data = players.get(i).getCapability(ImplantProvider.IMPLANT, null).getImplant();
                    if (data != null) {
                        int type = data.type;
                        NBTTagCompound tag = data.implant;
                        int energy = tag.getInteger("energy");
                        if (Implant.getMaxVoltage(type) > convertedVoltage && Implant.getMaxEnergy(type) != energy && tag.getBoolean("isOn") && tag.getInteger("frequency") == getFrequency()) {
                            int toEnergy = extractEnergy(convertedVoltage, false);
                            if (energy + toEnergy > Implant.getMaxEnergy(type))
                                energy = Implant.getMaxEnergy(type);
                            else
                                energy += toEnergy;
                            tag.setInteger("energy", energy);
                            tag.setInteger("voltage", convertedVoltage);
                            NetworkWrapper.instance.sendTo(new PacketImplantData(players.get(i)), (EntityPlayerMP) players.get(i));
                        } else
                            tag.setInteger("voltage", 0);
                    }

                    for (int j = 0; j < players.get(i).inventory.getSizeInventory(); j++) {
                        if (players.get(i).inventory.getStackInSlot(j).getItem() instanceof ItemEnergyTool || players.get(i).inventory.getStackInSlot(j).getItem() instanceof ItemEnergySword) {
                            NBTTagCompound tag = ModUtils.getNotNullCategory(players.get(i).inventory.getStackInSlot(j));
                            if (tag.getBoolean("powField") && tag.getInteger("frequency") == getFrequency()) {
                                int toEnergy = tag.getInteger("energy") + extractEnergy(convertedVoltage, false);
                                tag.setInteger("energy", toEnergy > ItemEnergyTool.maxCap ? ItemEnergyTool.maxCap : toEnergy);
                            }
                        }
                    }
                }
            }
        }


        // Do field playerEvents
        if (!world.isRemote && enableEvents && world.getTotalWorldTime()%5==0) {
            List<IEventPlayer> events;
            if (eventBuffer == null)
                eventBuffer = new HashMap<>();
            events = Events.getPossiblePlayerEvents(convertedVoltage);
            for (EntityPlayer player : players) {
                HashMap<IEventPlayer, Integer> playerKD = eventBuffer.computeIfAbsent(player, k -> {
                    HashMap<IEventPlayer, Integer> map = new HashMap<>();
                    for (IEventPlayer event : events) {
                        map.put(event, event.getRarity(convertedVoltage));
                    }
                    return map;
                });

                for (IEventPlayer event : events) {
                    Integer eventKD = playerKD.get(event);
                    if (eventKD == null) {
                        Random rnd = new Random();
                        if (rnd.nextFloat() < event.getChance(convertedVoltage))
                            event.onEvent(player, convertedVoltage);
                        playerKD.put(event, event.getRarity(convertedVoltage));
                    } else if (eventKD <= 0) {
                        playerKD.remove(event);
                        if (playerKD.size() == 0)
                            eventBuffer.remove(player);
                    } else {
                        eventKD -= 5;
                        playerKD.replace(event, eventKD);
                    }
                }
            }

            // Do field block events_player
            if (activeEvents == null)
                activeEvents = new ArrayList<>();
            if (eventBlockBuffer == null)
                eventBlockBuffer = new HashMap<>();
            List<IEventBlock> blockEvents = Events.getPossibleBlockEvents(voltage);
            for (IEventBlock e : blockEvents){
                Integer eventKD = eventBlockBuffer.get(e);
                if (eventKD == null) {
                    Random rnd = new Random();
                    if (rnd.nextFloat() < e.getChance(convertedVoltage)) {
                        e.onEvent(getPos(), convertedVoltage);
                        activeEvents.add(e);
                    }
                    eventBlockBuffer.put(e, e.getRarity(convertedVoltage));
                } else if (eventKD <= 0) {
                    eventBlockBuffer.remove(e);
                } else {
                    eventKD -= 5;
                    eventBlockBuffer.replace(e, eventKD);
                }
            }

            for (IEventBlock e : activeEvents){
                boolean end = e.update(getPos(), voltage);
                if (end){
                    e.endEvent(getPos(), voltage);
                    activeEvents.remove(e);
                }
            }
        }
    }

    public void endAllBlockEvents(){
        if (activeEvents != null)
            for (IEventBlock e : activeEvents){
                e.endEvent(getPos(), voltage);
                activeEvents.remove(e);
            }
    }

    @Override
    public int getMaxRadius() {
        return maxRadius;
    }

    @Override
    public int getRealVoltage(){
        return voltage/(radius/(lossReduce + 1) + 1);
    }

    @Override
    public void brokeLink(BlockPos pos){
        for (int i = 0; i < links.size(); i++)
            if (links.get(i).equals(pos)){
                ((IFieldReceiverEnergy)world.getTileEntity(links.get(i))).setLink(null);
                links.remove(i);
                break;
            }
        markDirty();
        markUpdate();
    }

    @Override
    public void createLink(BlockPos pos){
        IFieldReceiverEnergy receiverEnergy = ((IFieldReceiverEnergy)world.getTileEntity(pos));
        receiverEnergy.setLink(getPos());
        receiverEnergy.setVoltage(getRealVoltage());
        links.add(pos);
        markDirty();
        markUpdate();
    }

    @Override
    public List<BlockPos> getLinks() {
        return links;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void brokeLinksAll(){
        for (int i = 0; i < links.size(); i++)
            if (world.getTileEntity(links.get(i)) != null)
                ((IFieldReceiverEnergy) world.getTileEntity(links.get(i))).setLink(null);
        links.clear();
        markDirty();
        markUpdate();
    }

    @Override
    public int getFrequency(){ return frequency; }

    @Override
    public int getRadius() {
        return radius;
    }

    @Override
    public int getMaxVoltage() {
        return maxVoltage;
    }

    @Override
    public int getVoltage() {
        return voltage;
    }

    @Override
    public void setVoltage(int voltage) {
        this.voltage = voltage;
        applyLinkedBlocks();
        markDirty();
        markUpdate();
    }

    @Override
    public void setRadius(int radius) {
        this.radius = radius;
        applyLinkedBlocks();
        markDirty();
        markUpdate();
    }

    @Override
    public void setMaxRadius(int radius) {
        this.maxRadius = radius;
    }

    @Override
    public void setFrequency(int frequency){
        this.frequency = frequency;
        applyLinkedBlocks();
        markDirty();
        markUpdate();
    }

    protected void markUpdate(){
        world.notifyBlockUpdate(getPos(), getBlockType().getDefaultState(), getBlockType().getDefaultState(), 0);
    }

    public NBTTagCompound writeToNBTFieldStats(NBTTagCompound nbt){
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        if (links.size() != 0) {
            category.setBoolean("HasLinks", true);
            int[] array = new int[links.size() * 3];
            for (int i = 0; i < links.size(); i++) {
                array[i * 3] = links.get(i).getX();
                array[i * 3 + 1] = links.get(i).getY();
                array[i * 3 + 2] = links.get(i).getZ();
            }
            category.setIntArray("Links", array);
        }
        else
            category.setBoolean("HasLinks", false);

        category.setBoolean("FieldEnableEvents", enableEvents);
        category.setBoolean("FieldActive", active);
        category.setInteger("FieldLossReduce", lossReduce);
        category.setInteger("FieldMaxVoltage", maxVoltage);
        category.setInteger("FieldMaxRadius", maxRadius);
        category.setInteger("FieldVoltage", voltage);
        category.setInteger("FieldRadius", radius);
        category.setInteger("FieldFrequency", frequency);
        nbt.setTag(ExtremeEnergy.NBT_CATEGORY, category);
        return  nbt;
    }

    public void readFromNBTFieldStats(NBTTagCompound nbt){
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        links.clear();
        if (category.getBoolean("HasLinks")){
            int[] array = category.getIntArray("Links");
            for (int i = 0; i < array.length/3; i++){
                links.add(new BlockPos(array[i * 3], array[i * 3 + 1], array[i * 3 + 2]));
            }
        }

        enableEvents = category.getBoolean("FieldEnableEvents");
        active = category.getBoolean("FieldActive");
        lossReduce = category.getInteger("FieldLossReduce");
        maxVoltage = category.getInteger("FieldMaxVoltage");
        maxRadius = category.getInteger("FieldMaxRadius");
        voltage = category.getInteger("FieldVoltage");
        radius = category.getInteger("FieldRadius");
        frequency = category.getInteger("FieldFrequency");
    }

    public NBTTagCompound writeToNBTEnergy(NBTTagCompound nbt){

        if (getEnergyStored() < 0) {
            energy = 0;
        }
        NBTTagCompound category = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY);
        category.setInteger("Energy", energy);
        category.setInteger("capacity", capacity);
        nbt.setTag(ExtremeEnergy.NBT_CATEGORY, category);
        return nbt;
    }

    public void readFromNBTEnergy(NBTTagCompound nbt){

        this.energy = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY).getInteger("Energy");
        capacity = nbt.getCompoundTag(ExtremeEnergy.NBT_CATEGORY).getInteger("capacity");

        if (energy > capacity) {
            energy = capacity;
        }
    }

    // IEnergyStorage
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {

        int energyReceived = Math.min(capacity - getEnergyStored(), Math.min(this.maxReceive, maxReceive));

        boolean apply = getEnergyStored() == 0;

        if (!simulate) {
            energy += energyReceived;
        }

        if (apply)
            applyLinkedBlocks();

        markDirty();
        markUpdate();
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {

        int energyExtracted = Math.min(getEnergyStored(), Math.min(this.voltage, maxExtract));

        if (!simulate) {
            energy -= energyExtracted;
        }

        if (getEnergyStored() == 0)
            applyLinkedBlocks();
        markDirty();
        markUpdate();
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity;
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return true;
    }

    @Override
    public int getEnergyStored(EnumFacing from) {
        return getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return getMaxEnergyStored();
    }

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        int energyReceived = Math.min(capacity - getEnergyStored(), Math.min(this.maxReceive, maxReceive));

        boolean apply = getEnergyStored() == 0;

        if (!simulate) {
            energy += energyReceived;
        }

        if (apply)
            applyLinkedBlocks();

        markDirty();
        markUpdate();
        return energyReceived;
    }
}
