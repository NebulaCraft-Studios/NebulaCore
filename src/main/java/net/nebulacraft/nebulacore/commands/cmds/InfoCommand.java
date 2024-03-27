package net.nebulacraft.nebulacore.commands.cmds;

import net.nebulacraft.nebulacore.NebulaCore;
import net.nebulacraft.nebulacore.commands.SubCommand;
import net.nebulacraft.nebulacore.config.ConfigTypes;
import net.nebulacraft.nebulacore.util.TextUtility;
import net.nebulacraft.nebulacore.util.cmd.CooldownManager;
import org.bukkit.entity.Player;

public class InfoCommand extends SubCommand {


    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Provides information about NebulaShips";
    }

    @Override
    public String getSyntax() {
        return "/nebulaships info";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (!player.hasPermission(permission())) return;
        if(CooldownManager.manageCooldown(player)) return;

        player.sendMessage(TextUtility.color(
                "&7------------- &r" + NebulaCore.configManager.getFile(ConfigTypes.MESSAGES).getConfig().getString("Messages.General.SERVER_NAME") + "&r&7 -------------\n"+
                        "&3Version: &r{#5C16FF}" + NebulaCore.getInstance().getDescription().getVersion() + "{/#00DFFF}&r\n" +
                        "&3Authors: &r{#5C16FF}" + NebulaCore.getInstance().getDescription().getAuthors() + "{/#00DFFF}&r\n" +
                        "&3Website: &r{#5C16FF}" + NebulaCore.getInstance().getDescription().getWebsite() + "{/#00DFFF}&r\n" +
                        "&3Get Started: &r{#5C16FF}/nebulacraft help{/#00DFFF}&r\n" +
                        "&7------------- &r" + NebulaCore.configManager.getFile(ConfigTypes.MESSAGES).getConfig().getString("Messages.General.SERVER_NAME") + "&r&7 -------------"));
    }

    @Override
    public String permission() {
        return "nebulaships.info";
    }
}
