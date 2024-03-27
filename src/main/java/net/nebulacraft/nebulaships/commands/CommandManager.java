package net.nebulacraft.nebulaships.commands;

import lombok.Getter;
import net.nebulacraft.nebulaships.NebulaShips;
import net.nebulacraft.nebulaships.commands.cmds.HelpCommand;
import net.nebulacraft.nebulaships.commands.cmds.InfoCommand;
import net.nebulacraft.nebulaships.commands.cmds.ReloadCommand;
import net.nebulacraft.nebulaships.config.ConfigTypes;
import net.nebulacraft.nebulaships.config.Messages;
import net.nebulacraft.nebulaships.util.cmd.PermissionsChecker;
import net.nebulacraft.nebulaships.util.TextUtility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommandManager implements CommandExecutor {

    @Getter
    private static ArrayList<SubCommand> subCommands = new ArrayList<>();

    public CommandManager() {
        subCommands.add(new InfoCommand());
        subCommands.add(new HelpCommand());
        subCommands.add(new ReloadCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length > 0) {
                for (int i = 0; i < getSubCommands().size(); i++) {
                    if (strings[0].equalsIgnoreCase(getSubCommands().get(i).getName())) {
                        getSubCommands().get(i).perform(player, strings);
                    }
                }
            } else if (strings.length == 0) {
                player.sendMessage(TextUtility.color("&7------------- &r" + NebulaShips.configManager.getFile(ConfigTypes.MESSAGES).getConfig().getString("Messages.General.SERVER_NAME") + "&r&7 -------------"));
                for (int i = 0; i < getSubCommands().size(); i++) {
                    if (!PermissionsChecker.checkPermissions(player, getSubCommands().get(i).permission())) continue;
                    Messages.COMMAND_INFO.send(commandSender, "%command_syntax%", getSubCommands().get(i).getSyntax(), "%command_name%", getSubCommands().get(i).getName(), "%command_description%", getSubCommands().get(i).getDescription());
                }
                player.sendMessage(TextUtility.color("&7------------- &r" + NebulaShips.configManager.getFile(ConfigTypes.MESSAGES).getConfig().getString("Messages.General.SERVER_NAME") + "&r&7 -------------"));
            }
        }

        return true;
    }

}
