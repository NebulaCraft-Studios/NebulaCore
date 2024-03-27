package net.nebulacraft.nebulacore.util.cmd;

import net.nebulacraft.nebulacore.config.Messages;
import org.bukkit.entity.Player;

public class PermissionsChecker {

    public static boolean checkPermissions(Player player, String permission) {
        if (player.hasPermission(permission)) {
            return true;
        }
        Messages.PERMISSION_DENY.send(player);
        return false;
    }
}
