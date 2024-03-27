package net.nebulacraft.nebulaships.util.cmd;

import net.nebulacraft.nebulaships.config.Messages;
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
