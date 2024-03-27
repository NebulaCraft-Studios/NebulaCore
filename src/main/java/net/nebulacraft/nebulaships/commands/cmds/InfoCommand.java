package net.nebulacraft.nebulaships.commands.cmds;

import net.nebulacraft.nebulaships.NebulaShips;
import net.nebulacraft.nebulaships.commands.SubCommand;
import net.nebulacraft.nebulaships.config.ConfigTypes;
import net.nebulacraft.nebulaships.util.TextUtility;
import net.nebulacraft.nebulaships.util.cmd.CooldownManager;
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
                "&7------------- &r" + NebulaShips.configManager.getFile(ConfigTypes.MESSAGES).getConfig().getString("Messages.General.SERVER_NAME") + "&r&7 -------------\n"+
                        "&3Version: &r{#4821FF}" + NebulaShips.getInstance().getDescription().getVersion() + "{/#0064FF}&r\n" +
                        "&3Authors: &r{#4821FF}" + NebulaShips.getInstance().getDescription().getAuthors() + "{/#0064FF}&r\n" +
                        "&3Website: &r{#4821FF}" + NebulaShips.getInstance().getDescription().getWebsite() + "{/#0064FF}&r\n" +
                        "&3Get Started: &r{#4821FF}/nebulacraft help{/#0064FF}&r\n" +
                        "&7------------- &r" + NebulaShips.configManager.getFile(ConfigTypes.MESSAGES).getConfig().getString("Messages.General.SERVER_NAME") + "&r&7 -------------"));
    }

    @Override
    public String permission() {
        return "nebulaships.info";
    }
}
