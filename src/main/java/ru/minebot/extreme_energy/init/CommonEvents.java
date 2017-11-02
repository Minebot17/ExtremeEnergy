package ru.minebot.extreme_energy.init;

import cofh.redstoneflux.api.*;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ru.minebot.extreme_energy.blocks.BlockShield;
import ru.minebot.extreme_energy.capability.IImplant;
import ru.minebot.extreme_energy.capability.ImplantProvider;
import ru.minebot.extreme_energy.energy.IFieldCreatorEnergy;
import ru.minebot.extreme_energy.events.Events;
import ru.minebot.extreme_energy.events.events_chunk.IEventChunk;
import ru.minebot.extreme_energy.items.equipment.ItemEnergyArmor;
import ru.minebot.extreme_energy.items.equipment.ItemHeavyArmor;
import ru.minebot.extreme_energy.items.implants.Implant;
import ru.minebot.extreme_energy.modules.ChipArgs;
import ru.minebot.extreme_energy.modules.IArmorCoreModule;
import ru.minebot.extreme_energy.modules.IChip;
import ru.minebot.extreme_energy.modules.IGenerator;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketChargeData;
import ru.minebot.extreme_energy.network.packages.PacketConfig;
import ru.minebot.extreme_energy.network.packages.PacketImplantData;
import ru.minebot.extreme_energy.network.packages.PacketSpawnLightning;
import ru.minebot.extreme_energy.other.ChargeSaveData;
import ru.minebot.extreme_energy.other.ImplantData;
import ru.minebot.extreme_energy.other.InfectedSaveData;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;

public class CommonEvents {
    /*private final ChunkPos[] chunkOffsets = new ChunkPos[]{
            new ChunkPos(0, 0),
            new ChunkPos(1, 1),
            new ChunkPos(1, 0),
            new ChunkPos(0, 1),
            new ChunkPos(-1, -1),
            new ChunkPos(-1, 0),
            new ChunkPos(0, -1),
            new ChunkPos(1, -1),
            new ChunkPos(-1, 1),
    };*/

    @SubscribeEvent
    public void PlayerTickEvent(TickEvent.PlayerTickEvent e){
        InfectedSaveData infectedData = InfectedSaveData.getOrCreateData(e.player.world);
        if (infectedData.map.containsKey(new ChunkPos(e.player.getPosition()))){
            int time = infectedData.map.get(new ChunkPos(e.player.getPosition()));
            int power = time > 48000 ? 2 : time > 24000 ? 1 : 0;
            e.player.addPotionEffect(new PotionEffect(Potion.getPotionById(20), 20, power));
        }

        IImplant cap = e.player.getCapability(ImplantProvider.IMPLANT, null);
        ImplantData imp = cap.getImplant();
        if (imp != null){
            List<ItemStack> stacks = ModUtils.getModules(imp);
            NBTTagCompound mTag = imp.modules;
            NBTTagCompound iTag = imp.implant;

            boolean isOn = iTag.getBoolean("isOn");
            byte[] modulesActive = iTag.getByteArray("activesArray");
            int energy = iTag.getInteger("energy");
            int voltage = iTag.getInteger("voltage");

            int checkedVoltage = 0;
            if (energy != Implant.getMaxEnergy(imp.type) && isOn){
                /*if (!e.player.world.isRemote){
                    int plusEnergy = ModUtils.extractEnergyFromChunk(new ChunkPos(e.player.getPosition()), Implant.getMaxVoltage(imp.type))/10;
                    checkedVoltage += plusEnergy;
                    energy += plusEnergy;
                    iTag.setInteger("energy", energy);
                }*/

                List<TileEntity> creators = ModUtils.radiusFilter(e.player.getPosition(), e.player.world.loadedTileEntityList, 100);
                for (TileEntity creator : creators)
                    if (creator instanceof IFieldCreatorEnergy && ((IFieldCreatorEnergy) creator).getFrequency() == imp.implant.getInteger("frequency") && ((IFieldCreatorEnergy) creator).getEnergyStored() != 0 && ModUtils.inRadius(creator.getPos(), e.player.getPosition(), ((IFieldCreatorEnergy) creator).getRadius()) && ((IFieldCreatorEnergy) creator).isActive()){
                        checkedVoltage += ((IFieldCreatorEnergy) creator).getVoltage();
                        break;
                    }
            }
            if (checkedVoltage != voltage){
                voltage = checkedVoltage;
                iTag.setInteger("voltage", voltage);
            }


            for (int i = 0; i < stacks.size(); i++){
                Item item = stacks.get(i).getItem();
                ChipArgs args = new ChipArgs(e.player, modulesActive[i] != 0, isOn, voltage != 0, voltage, energy, stacks.get(i).getTagCompound());
                if (item instanceof IChip)
                    ((IChip)item).update(args);
                else if (item instanceof IGenerator)
                    ((IGenerator)item).update(args);
            }
            int plusEnergy = 0;
            int minusEnergy = 0;
            RayTraceResult blocksRay = null;
            int entityRay = 0;
            if (e.player.world.isRemote){
                blocksRay = ModUtils.getBlocksRay();
                entityRay = ModUtils.getEntityRay();
            }
            for (int i = 0; i < stacks.size(); i++) {
                if (modulesActive[i] != 0 && isOn) {
                    Item item = stacks.get(i).getItem();
                    ChipArgs args = new ChipArgs(e.player, modulesActive[i] != 0, isOn, voltage != 0, voltage, energy, ModUtils.getNotNullCategory(stacks.get(i)), blocksRay, entityRay);
                    if (item instanceof IChip)
                        minusEnergy += ((IChip)item).onImplantWork(args);
                    else if (item instanceof IGenerator)
                        plusEnergy += ((IGenerator)item).generateEnergy(args);
                    ModUtils.setModules(imp, stacks);
                }
            }
            energy += (plusEnergy - minusEnergy);
            if (energy < 0) energy = 0;
            else if (energy > Implant.getMaxEnergy(imp.type)) energy = Implant.getMaxEnergy(imp.type);
            iTag.setInteger("energy", energy);
            cap.setImplant(new ImplantData(imp.type, mTag, iTag, imp.core, imp.playerID));
            if ((minusEnergy != 0 || plusEnergy != 0) && !e.player.world.isRemote)
                NetworkWrapper.instance.sendTo(new PacketImplantData(e.player), (EntityPlayerMP)e.player);
        }

        ChargeSaveData data = ChargeSaveData.getOrCreateData(e.player.world);
        // The electrical mod environment
        if (!e.player.world.isRemote && e.player.world.getTotalWorldTime()%20==0 && data != null && data.map != null){

            int charge = data.map.get(new ChunkPos(e.player.getPosition())) == null ? 0 : data.map.get(new ChunkPos(e.player.getPosition()));
            List<IEventChunk> events = Events.getPossibleChunkEvents(charge);
            if (charge < ModConfig.maxCapOfChunk-1000000) {
                for (IEventChunk event : events)
                    if (ModUtils.random.nextFloat() < event.getChance(charge)) {
                        data.map.put(new ChunkPos(e.player.getPosition()), charge - event.onEvent(e.player.world, e.player));
                        break;
                    }
            }
            else if (charge < ModConfig.maxCapOfChunk && events.size() != 0)
                data.map.put(new ChunkPos(e.player.getPosition()), charge - events.get(ModUtils.random.nextInt(events.size())).onEvent(e.player.world, e.player));
            else {
                OverloadEvent(e.player);
                data.map.put(new ChunkPos(e.player.getPosition()), 2000000);
            }
            data.markDirty();
        }
    }

    private void OverloadEvent(EntityPlayer player){
        Vec3d[] poses = new Vec3d[4];
        for (int i = 0; i < poses.length; i++)
            poses[i] = new Vec3d(ModUtils.random.nextFloat()*5f-2.5f, ModUtils.random.nextFloat()*5f-2.5f, ModUtils.random.nextFloat()*5f-2.5f);

        for (Vec3d pose : poses)
            NetworkWrapper.instance.sendToAllAround(
                    new PacketSpawnLightning(
                            pose.add(player.getPositionVector()),
                            player.getPositionVector().addVector(0, 1, 0),
                            LightningEvents.Type.BIG
                    ),
                    ModUtils.getTargetPoint(player, 64)
            );
        player.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 18);
        player.knockBack(player, 2, ModUtils.random.nextFloat()-0.5f, ModUtils.random.nextFloat()-0.5f);
    }

    @SubscribeEvent
    public void playerDamaged(LivingHurtEvent e){
        if (e.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e.getEntityLiving();
            IImplant cap = player.getCapability(ImplantProvider.IMPLANT, null);
            ImplantData data = cap.getImplant();
            boolean toClient = false;

            if (e.getSource() == DamageSource.FALL) {
                List<ItemStack> impModules = ModUtils.getModules(data);
                for (int i = 0; i < impModules.size(); i++)
                    if (impModules.get(i).getItem() == ModItems.windModule && ModUtils.isModuleActive(data, i)) {
                        int power = impModules.get(i).getTagCompound().getInteger("power");
                        int energyToLost = (int)(e.getAmount()*10*power);
                        if (energyToLost <= data.implant.getInteger("energy")){
                            data.implant.setInteger("energy", data.implant.getInteger("energy") - energyToLost);
                            e.setAmount(e.getAmount()/(power+3));
                        }
                        break;
                    }
            }

            if (e.getSource() == DamageSource.LIGHTNING_BOLT){
                List<ItemStack> impModules = ModUtils.getCoreModules(data);
                for (int i = 0; i < impModules.size(); i++)
                    if (impModules.get(i).getItem() == ModItems.securityModule) {
                        int energyToLost = (int)(e.getAmount()*30);
                        if (energyToLost <= data.implant.getInteger("energy")){
                            data.implant.setInteger("energy", data.implant.getInteger("energy") - energyToLost);
                            e.setAmount(0);
                            Potion potion = Potion.getPotionFromResourceLocation("meem:electricShock");
                            if (player.isPotionActive(potion))
                                player.removeActivePotionEffect(potion);
                            NetworkWrapper.instance.sendTo(new PacketImplantData(player), (EntityPlayerMP) player);
                            return; // Warning
                        }
                    }
            }

            int countEnergy = 0;
            int countHeavy = 0;
            if (data != null && !new ItemStack(data.core).isEmpty()) {
                int power = data.core.getInteger("power");
                if (power == 0){
                    power = 1;
                    data.core.setInteger("power", 1);
                }
                float number = e.getAmount() * power;
                for (int i = 0; i < player.inventory.armorInventory.size(); i++) {
                    Item item = player.inventory.armorInventory.get(i).getItem();
                    if (item instanceof ItemHeavyArmor)
                        countHeavy++;
                    else if (item instanceof ItemEnergyArmor)
                        countEnergy++;
                }
                int energyModules = 0;
                List<ItemStack> modules = ModUtils.getCoreModules(data);
                if (data.core.getBoolean("enableModules"))
                    for (int i = 0; i < modules.size(); i++)
                        energyModules += ((IArmorCoreModule)modules.get(i).getItem()).getEnergy(power);
                int energyToLost = countEnergy * (int)(175 * number) + countHeavy * (int)(250 * number) + energyModules;

                if (data.core.getBoolean("powCap")){
                    ItemStack capacitor = findFirstNotEmptyCapacitor(player.inventory);
                    while(!capacitor.isEmpty() && energyToLost > 0){
                        IEnergyContainerItem item = (IEnergyContainerItem) capacitor.getItem();
                        energyToLost -= item.extractEnergy(capacitor, energyToLost, false);
                        capacitor = findFirstNotEmptyCapacitor(player.inventory);
                    }
                }
                if (energyToLost > 0 && data.core.getBoolean("powImp")){
                    if (data.implant.getInteger("energy") < energyToLost)
                        data.implant.setInteger("energy", 0);
                    else {
                        data.implant.setInteger("energy", data.implant.getInteger("energy") - energyToLost);
                        energyToLost = 0;
                    }
                }

                if (energyToLost <= 0){
                    if (countEnergy != 0) {
                        e.setAmount(e.getAmount() / ((countEnergy + power) / 2f));
                        toClient = true;
                    }
                    if (countHeavy != 0) {
                        e.setAmount(e.getAmount() / (countHeavy + power));
                        toClient = true;
                    }
                    if (data.core.getBoolean("enableModules"))
                        for (int i = 0; i < modules.size(); i++)
                            ((IArmorCoreModule)modules.get(i).getItem()).onDamaged(e, e.getSource().getTrueSource(), power, ModUtils.getNotNullCategory(modules.get(i)), player);
                    if (toClient) {
                        cap.setImplant(data);
                        NetworkWrapper.instance.sendTo(new PacketImplantData(player), (EntityPlayerMP) player);
                    }
                }
            }
        }
    }

    private ItemStack findFirstNotEmptyCapacitor(IInventory inventory){
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.getItem() instanceof IEnergyContainerItem && ((IEnergyContainerItem)stack.getItem()).getEnergyStored(stack) != 0)
                return stack;
        }
        return ItemStack.EMPTY;
    }

    @SubscribeEvent
    public void placeBlock(BlockEvent.PlaceEvent e){
        if (!e.getWorld().isRemote){
            Block block = e.getPlacedBlock().getBlock();
            TileEntity te = e.getWorld().getTileEntity(e.getPos());
            if (block == ModBlocks.cable || (te != null && te instanceof IEnergyReceiver)){
                e.getPlayer().world.setBlockState(e.getPos(), e.getState());
                ModUtils.updateNetwork(e.getWorld(), e.getPos(), new ArrayList<>());
            }
        }
    }

    @SubscribeEvent
    public void breakBlock(BlockEvent.BreakEvent e){
        if (!e.getWorld().isRemote){
            Block block = e.getState().getBlock();
            TileEntity te = e.getWorld().getTileEntity(e.getPos());
            if (block == ModBlocks.cable || (te != null && te instanceof IEnergyReceiver)){
                e.getPlayer().world.setBlockToAir(e.getPos());
                ModUtils.updateNetwork(e.getWorld(), e.getPos(), new ArrayList<>());
            }
        }
    }

    @SubscribeEvent
    public void loginServer(EntityJoinWorldEvent e){
        /*if (!e.getWorld().isRemote && e.getEntity() instanceof EntityPlayer) {
            List<TileEntity> tes = e.getWorld().loadedTileEntityList;
            try {
                for (TileEntity te : tes)
                    if (te instanceof EnergySender)
                        ((EnergySender) te).calculateEnergyNetwork();
            }
            catch (ConcurrentModificationException a){a.printStackTrace();}
        }*/

        if (e.getWorld().isRemote && e.getEntity() instanceof EntityPlayer){
            NetworkWrapper.instance.sendToServer(new PacketChargeData(new HashMap<>()));
        }
    }

    @SubscribeEvent
    public void playerLogin(PlayerEvent.PlayerLoggedInEvent e){
        if (!e.player.world.isRemote){
            NetworkWrapper.instance.sendTo(new PacketConfig(), (EntityPlayerMP)e.player);
        }
    }

    @SubscribeEvent
    public void worldTicks(TickEvent.WorldTickEvent e){
        if(!e.world.isRemote && e.world.getTotalWorldTime()%20==0){
            InfectedSaveData data = InfectedSaveData.getOrCreateData(e.world);
            if (data.map.size() != 0){
                List<ChunkPos> toRemove = new ArrayList<>();
                for (ChunkPos pos : data.map.keySet()){
                    Integer time = data.map.get(pos);
                    if (time < 0)
                        toRemove.add(pos);
                    else
                        data.map.put(pos, time - 20);
                }
                for (ChunkPos remove : toRemove)
                    data.map.remove(remove);
                data.markDirty();
            }
        }

        if (!e.world.isRemote && (ModUtils.shields.size() != 0 || ModUtils.shieldToAdd.size() != 0)){
            if (ModUtils.shields.containsKey(e.world.provider.getDimension())) {
                HashMap<BlockPos, Integer> map = ModUtils.shields.get(e.world.provider.getDimension());
                List<BlockPos> toRemove = new ArrayList<>();
                try {
                    for (BlockPos pos : map.keySet()) {
                        Integer time = map.get(pos);
                        if (time < 0) {
                            e.world.setBlockToAir(pos);
                            toRemove.add(pos);
                        } else
                            map.put(pos, time - 1);
                    }
                }
                catch (ConcurrentModificationException e1){ e1.printStackTrace(); }
                for (BlockPos remove : toRemove)
                    map.remove(remove);
            }

            if (ModUtils.shieldToAdd.containsKey(e.world.provider.getDimension())) {
                HashMap<BlockPos, Integer> map = ModUtils.shieldToAdd.get(e.world.provider.getDimension());
                List<BlockPos> toRemove = new ArrayList<>();
                try {
                    for (BlockPos pos : map.keySet()) {
                        Integer time = map.get(pos);
                        if (time < 0) {
                            if (e.world.isAirBlock(pos))
                                e.world.setBlockState(pos, ModBlocks.shield.getDefaultState().withProperty(BlockShield.isFC, true));
                            toRemove.add(pos);
                        } else
                            map.put(pos, time - 1);
                    }
                }
                catch (ConcurrentModificationException e1){ e1.printStackTrace(); }
                for (BlockPos remove : toRemove)
                    map.remove(remove);
            }
        }
    }

    @SubscribeEvent
    public void clonePlayer(net.minecraftforge.event.entity.player.PlayerEvent.Clone e){
        IImplant cap = e.getEntityPlayer().getCapability(ImplantProvider.IMPLANT, null);
        cap.setImplant(e.getOriginal().getCapability(ImplantProvider.IMPLANT, null).getImplant());
    }

    @SubscribeEvent
    public void respawn(PlayerEvent.PlayerRespawnEvent e){
        NetworkWrapper.instance.sendTo(new PacketImplantData(e.player), (EntityPlayerMP) e.player);
    }

    @SubscribeEvent
    public void loadChunk(ChunkDataEvent e) {
        if (e.getWorld().isRemote)
            return;

        ChargeSaveData data = ChargeSaveData.getOrCreateData(e.getWorld());
        if (!data.map.containsKey(e.getChunk().getPos())){
            data.map.put(e.getChunk().getPos(), ModUtils.random.nextInt(ModConfig.maxCapOfChunk));
            data.markDirty();
        }
    }

    @SubscribeEvent
    public void worldLoad(WorldEvent.Load e){
        if (!e.getWorld().isRemote){
            ChargeSaveData data = ChargeSaveData.getOrCreateData(e.getWorld());
            for (ChunkPos pos : data.map.keySet()){
                int charge = data.map.get(pos);
                if (charge > ModConfig.maxCapOfChunk)
                    data.map.replace(pos, ModConfig.maxCapOfChunk);
            }
            data.markDirty();
        }
    }

    @SubscribeEvent
    public void worldSave(WorldEvent.Save e){
        if (!e.getWorld().isRemote){
            if (ModUtils.shields.containsKey(e.getWorld().provider.getDimension())) {
                HashMap<BlockPos, Integer> map = ModUtils.shields.get(e.getWorld().provider.getDimension());
                if (map.size() != 0)
                    for (BlockPos pos : map.keySet()) {
                        e.getWorld().setBlockToAir(pos);
                    }
            }
            if (ModUtils.shieldToAdd.containsKey(e.getWorld().provider.getDimension())) {
                HashMap<BlockPos, Integer> map = ModUtils.shieldToAdd.get(e.getWorld().provider.getDimension());
                if (map.size() != 0)
                    for (BlockPos pos : map.keySet()) {
                        e.getWorld().setBlockState(pos, ModBlocks.shield.getDefaultState().withProperty(BlockShield.isFC, true));
                    }
            }
        }
    }

    @SubscribeEvent

    public void attachCapability(AttachCapabilitiesEvent event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation("meem:implant"), new ImplantProvider());
        }
    }
}
