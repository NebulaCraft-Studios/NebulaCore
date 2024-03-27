package net.nebulacraft.nebulacore.commands.cmds;

import net.nebulacraft.nebulacore.commands.SubCommand;
import net.nebulacraft.nebulacore.util.model.SummonCustomModel;
import org.bukkit.entity.Player;

public class TestCommand extends SubCommand {
    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/nebulaships test";
    }

    @Override
    public void perform(Player player, String[] args) {
        SummonCustomModel.summonModel("SHIP_ONE", player.getLocation());
    }

    @Override
    public String permission() {
        return "nebulaships.test";
    }
}
