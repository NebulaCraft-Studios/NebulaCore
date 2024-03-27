package net.nebulacraft.nebulacore.util.cmd;

import net.nebulacraft.nebulacore.NebulaCore;
import net.nebulacraft.nebulacore.config.ConfigTypes;
import net.nebulacraft.nebulacore.config.Messages;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private static Map<UUID, Long> cooldowns = new HashMap<>();
    private static long cooldownDuration = NebulaCore.configManager.getFile(ConfigTypes.SETTINGS).getConfig().getLong("Commands.COOLDOWN");

    // return true if player is on cooldown, command will return
    // if not, return false and command can continue

    public static boolean manageCooldown(Player player) {
        if (cooldowns.containsKey(player.getUniqueId())) {
            long secondsLeft = ((cooldowns.get(player.getUniqueId()) / 1000) + (cooldownDuration / 1000)) - (System.currentTimeMillis() / 1000);

            if (secondsLeft > 0) {
                Messages.COOLDOWN.send(player, "%cooldown%", String.valueOf(secondsLeft));
                return true;
            } else {
                cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
                return false;
            }
        }
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
        return false;
    }
}
