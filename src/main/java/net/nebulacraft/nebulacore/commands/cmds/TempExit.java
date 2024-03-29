package net.nebulacraft.nebulacore.commands.cmds;

import net.nebulacraft.nebulacore.commands.SubCommand;
import net.nebulacraft.nebulacore.util.model.ShipManager;
import org.bukkit.entity.Player;

public class TempExit extends SubCommand {
    @Override
    public String getName() {
        return "tempexit";
    }

    @Override
    public String getDescription() {
        return "exit when you are on a vehicle controlling ship";
    }

    @Override
    public String getSyntax() {
        return "/nebulacore tempexit";
    }

    @Override
    public void perform(Player player, String[] args) {
        ShipManager.tempExit(player);
    }

    @Override
    public String permission() {
        return "nebulacore.tempexit";
    }
}
