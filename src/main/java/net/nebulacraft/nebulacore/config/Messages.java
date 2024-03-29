package net.nebulacraft.nebulacore.config;

import net.nebulacraft.nebulacore.util.TextUtility;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public enum Messages {

    SERVER_NAME("General.SERVER_NAME"),
    PERMISSION_DENY("General.PERMISSION_DENY"),
    RELOADING("General.RELOADING"),
    RELOADED("General.RELOADED"),
    ERROR("General.ERROR"),
    COOLDOWN("General.COOLDOWN"),

    COMMAND_INFO("Commands.COMMAND_INFO"),

    SHIP_EXIT_WARNING("Ships.EXIT_WARNING");

    private static FileConfiguration config;
    private final String path;

    Messages(String path) {
        this.path = path;
    }

    public static void setConfiguration(FileConfiguration c) {
        config = c;
    }

    public void send(CommandSender reciever, Object... replacements) {
        Object value = config.get("Messages." + this.path);

        String message;
        if (value == null) {
            message = "[NebulaShips]: Message not found: " + this.path;
        } else {
            message = value instanceof List ? TextUtility.fromList((List<?>) value) : value.toString();
        }

        if (!message.isEmpty()) {
            reciever.sendMessage(TextUtility.color(replace(message, replacements)));
        }
    }

    private String replace(String message, Object... replacements) {
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 >= replacements.length) break;
            message = message.replace(String.valueOf(replacements[i]), String.valueOf(replacements[i + 1]));
        }

        String prefix = config.getString("Messages." + SERVER_NAME.getPath());
        return message.replace("%server_name%", prefix != null && !prefix.isEmpty() ? prefix : "");
    }

    public String getPath() {
        return this.path;
    }
}
