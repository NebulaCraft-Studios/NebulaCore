package net.nebulacraft.nebulaships.commands.cmds;

import lombok.extern.log4j.Log4j2;
import net.nebulacraft.nebulaships.NebulaShips;
import net.nebulacraft.nebulaships.commands.SubCommand;
import net.nebulacraft.nebulaships.config.Messages;
import net.nebulacraft.nebulaships.util.cmd.CooldownManager;
import org.bukkit.entity.Player;

@Log4j2(topic = "NebulaShips")
public class ReloadCommand extends SubCommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reloads the plugin, refreshing configs, etc.";
    }

    @Override
    public String getSyntax() {
        return "/nebulaships reload";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (!player.hasPermission(permission())) return;;
        if (CooldownManager.manageCooldown(player)) return;

        LOGGER.warn("[" + player.getName() + "] is reloading NebulaShips. Smh, what have they done?");
        Messages.RELOADING.send(player);

        NebulaShips.configManager.reloadFiles();

        Messages.RELOADED.send(player);
        LOGGER.warn("Successfully reloaded NebulaShips... I hope.");
    }

    @Override
    public String permission() {
        return "nebulaships.reload";
    }
}
