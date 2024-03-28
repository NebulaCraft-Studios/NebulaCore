package net.nebulacraft.nebulacore.commands.cmds;

import lombok.extern.log4j.Log4j2;
import net.nebulacraft.nebulacore.NebulaCore;
import net.nebulacraft.nebulacore.commands.SubCommand;
import net.nebulacraft.nebulacore.config.Messages;
import net.nebulacraft.nebulacore.util.cmd.CooldownManager;
import net.nebulacraft.nebulacore.util.gravity.VelocityManager;
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
        return "/nebulacore reload";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (!player.hasPermission(permission())) return;;
        if (CooldownManager.manageCooldown(player)) return;

        LOGGER.warn("[" + player.getName() + "] is reloading NebulaCore. Smh, what have they done?");
        Messages.RELOADING.send(player);

        NebulaCore.configManager.reloadFiles();
        VelocityManager.reloadConfigValues();

        Messages.RELOADED.send(player);
        LOGGER.warn("Successfully reloaded NebulaCore... I hope.");
    }

    @Override
    public String permission() {
        return "nebulacore.reload";
    }
}
