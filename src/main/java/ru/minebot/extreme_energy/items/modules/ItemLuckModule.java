package ru.minebot.extreme_energy.items.modules;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import ru.minebot.extreme_energy.Reference;
import ru.minebot.extreme_energy.gui.elements.moduleGui.IModuleGui;
import ru.minebot.extreme_energy.init.ModUtils;
import ru.minebot.extreme_energy.modules.ChipArgs;
import ru.minebot.extreme_energy.modules.FuncArgs;
import ru.minebot.extreme_energy.modules.IChip;
import ru.minebot.extreme_energy.modules.ModuleFunctional;

import java.util.List;

public class ItemLuckModule extends ModuleFunctional implements IChip {
    private int power;

    public ItemLuckModule() {
        super(Reference.ExtremeEnergyItems.MODULELUCK.getUnlocalizedName(), Reference.ExtremeEnergyItems.MODULELUCK.getRegistryName());
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Luck"};
    }

    @Override
    public void firstUpdate(FuncArgs args){
        List<EntityPlayer> players = ModUtils.radiusFilterPlayers(args.pos, args.world.playerEntities, args.radius);
        if (!args.isPublic) {
            for (int j = 0; j < args.cards.length; j++)
                for (int i = 0; i < players.size(); i++)
                    if(players.get(i).getUniqueID().hashCode() == args.cards[j])
                        players.get(i).addPotionEffect(new PotionEffect(Potion.getPotionById(26), 20, 0));
        }
        else
            for (int i = 0; i < players.size(); i++)
                players.get(i).addPotionEffect(new PotionEffect(Potion.getPotionById(26), 20, 0));
    }

    @Override
    public int onImplantWork(ChipArgs args) {
        if (args.isModuleActive && args.energy > 1000){
            args.player.addPotionEffect(new PotionEffect(Potion.getPotionById(26), 20, 0));
            return 5;
        }
        return 0;
    }

    @Override
    public int getTier() {
        return 0;
    }

    @Override
    public IModuleGui[] getGui() {
        return new IModuleGui[0];
    }
}
