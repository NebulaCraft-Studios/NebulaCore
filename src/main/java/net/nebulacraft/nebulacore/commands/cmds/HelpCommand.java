package net.nebulacraft.nebulacore.commands.cmds;

import net.nebulacraft.nebulacore.NebulaCore;
import net.nebulacraft.nebulacore.commands.CommandManager;
import net.nebulacraft.nebulacore.commands.SubCommand;
import net.nebulacraft.nebulacore.config.ConfigTypes;
import net.nebulacraft.nebulacore.config.Messages;
import net.nebulacraft.nebulacore.util.TextUtility;
import net.nebulacraft.nebulacore.util.cmd.CooldownManager;
import net.nebulacraft.nebulacore.util.cmd.PermissionsChecker;
import org.bukkit.entity.Player;

public class HelpCommand extends SubCommand {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "A list of all commands to do with NebulaShips";
    }

    @Override
    public String getSyntax() {
        return "/nebulacore help";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (!player.hasPermission(permission())) return;
        if (CooldownManager.manageCooldown(player)) return;

        player.sendMessage(TextUtility.color("&7------------- &r" + NebulaCore.getConfiguration(ConfigTypes.MESSAGES).getString("Messages.General.SERVER_NAME") + "&r&7 -------------"));
        for (int i = 0; i < CommandManager.getSubCommands().size(); i++) {
            if (!PermissionsChecker.checkPermissions(player, CommandManager.getSubCommands().get(i).permission())) continue;
            Messages.COMMAND_INFO.send(player, "%command_syntax%", CommandManager.getSubCommands().get(i).getSyntax(), "%command_name%", CommandManager.getSubCommands().get(i).getName(), "%command_description%", CommandManager.getSubCommands().get(i).getDescription());
        }
        player.sendMessage(TextUtility.color("&7------------- &r" + NebulaCore.getConfiguration(ConfigTypes.MESSAGES).getString("Messages.General.SERVER_NAME") + "&r&7 -------------"));
    }

    @Override
    public String permission() {
        return "nebulacore.help";
    }
}
