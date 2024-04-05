package net.nebulacraft.nebulacore.commands.cmds;
import net.nebulacraft.nebulacore.util.entities.DisplayEntities;
import net.nebulacraft.nebulacore.commands.SubCommand;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.block.data.type.Barrel;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static org.bukkit.Bukkit.createBlockData;

public class SummonEntity extends SubCommand {
    @Override
    public String getName() {
        return "summonentity";
    }

    @Override
    public String getDescription() {
        return "Summons a DisplayEntity";
    }

    @Override
    public String getSyntax() {
        return "/nebulacore summonentity";
    }

@Override
    public void perform(Player player, String[] args) {
        //Location test = new Location(player.getWorld(), Double.parseDouble(args[1]),Double.parseDouble(args[2]),Double.parseDouble(args[3]));
        //BlockDisplay display = (BlockDisplay) Objects.requireNonNull(Bukkit.getWorld(player.getWorld().getName())).spawnEntity(test, EntityType.BLOCK_DISPLAY);
        //display.setBlock(createBlockData(args[4]));
DisplayEntities.spawnDisplayEntity();
    }

    @Override
    public String permission() {
        return "nebulacore.summonentity";
    }
}
