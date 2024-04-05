package net.nebulacraft.nebulacore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CommandCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player) || args.length == 0) return null;

        if (args.length == 1) {
            String arg = args[0].toLowerCase(Locale.ROOT);
            List<String> completions = new ArrayList<>();

            if (sender.hasPermission("nebulaships.reload")) {
                completions.add("reload");
            }
            if (sender.hasPermission("nebulaships.help")) {
                completions.add("help");
            }
            if (sender.hasPermission("nebulaships.info")) {
                completions.add("info");
            }
            completions.add("test");
            completions.add("tempexit");
completions.add("summonentity");
            return StringUtil.copyPartialMatches(arg, completions, new ArrayList<>(completions.size()));
        }

        return Arrays.asList("");
    }
}
