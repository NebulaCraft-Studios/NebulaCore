package net.nebulacraft.nebulacore.commands;

import lombok.Getter;
import net.nebulacraft.nebulacore.NebulaCore;
import net.nebulacraft.nebulacore.commands.cmds.*;
import net.nebulacraft.nebulacore.config.ConfigTypes;
import net.nebulacraft.nebulacore.config.Messages;
import net.nebulacraft.nebulacore.util.cmd.PermissionsChecker;
import net.nebulacraft.nebulacore.util.TextUtility;
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
        subCommands.add(new TestCommand());
        subCommands.add(new TempExit());
        subCommands.add(new SummonEntity());
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
                player.sendMessage(TextUtility.color("&7------------- &r" + NebulaCore.getConfiguration(ConfigTypes.MESSAGES).getString("Messages.General.SERVER_NAME") + "&r&7 -------------"));
                for (int i = 0; i < getSubCommands().size(); i++) {
                    if (!PermissionsChecker.checkPermissions(player, getSubCommands().get(i).permission())) continue;
                    Messages.COMMAND_INFO.send(commandSender, "%command_syntax%", getSubCommands().get(i).getSyntax(), "%command_name%", getSubCommands().get(i).getName(), "%command_description%", getSubCommands().get(i).getDescription());
                }
                player.sendMessage(TextUtility.color("&7------------- &r" + NebulaCore.getConfiguration(ConfigTypes.MESSAGES).getString("Messages.General.SERVER_NAME") + "&r&7 -------------"));
            }
        }

        return true;
    }

}
