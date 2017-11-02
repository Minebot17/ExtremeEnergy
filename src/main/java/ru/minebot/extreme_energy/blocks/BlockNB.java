package ru.minebot.extreme_energy.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import ru.minebot.extreme_energy.ExtremeEnergy;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.entities.EntityNuclearBomb;
import ru.minebot.extreme_energy.network.NetworkWrapper;
import ru.minebot.extreme_energy.network.packages.PacketPlaySound;

public class BlockNB extends Block {
    public BlockNB(){
        super(Material.TNT);
        setUnlocalizedName(Reference.ExtremeEnergyBlocks.NB.getUnlocalizedName());
        setRegistryName(Reference.ExtremeEnergyBlocks.NB.getRegistryName());
        setHardness(1);
        setCreativeTab(ExtremeEnergy.tabExtremeEnergy);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (worldIn.isBlockPowered(pos)) {
            NetworkWrapper.instance.sendToAllAround(
                    new PacketPlaySound(SoundEvent.REGISTRY.getIDForObject(SoundEvents.ENTITY_TNT_PRIMED), pos.getX(), pos.getY(), pos.getZ()),
                    new NetworkRegistry.TargetPoint(worldIn.provider.getDimension(),pos.getX(), pos.getY(), pos.getZ(),32)
            );
            worldIn.setBlockToAir(pos);
            spawnEntity(worldIn, pos);
        }
    }

    protected void spawnEntity(World world, BlockPos pos){
        world.spawnEntity(new EntityNuclearBomb(world,null, pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f));
    }
}
