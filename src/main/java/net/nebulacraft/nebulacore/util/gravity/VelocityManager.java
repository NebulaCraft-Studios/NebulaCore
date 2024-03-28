package net.nebulacraft.nebulacore.util.gravity;

import net.nebulacraft.nebulacore.NebulaCore;
import net.nebulacraft.nebulacore.config.ConfigTypes;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class VelocityManager implements Runnable {

    private static HashMap<UUID, Vector> velocities = new HashMap<>();
    private static HashMap<UUID, Location> positions = new HashMap<>();
    private static HashMap<UUID, Boolean> onGround = new HashMap<>();

    private static boolean affectsEntities = NebulaCore.getConfiguration(ConfigTypes.NEBULAPLANETS).getBoolean("Gravity.AFFECTS_ENTITIES");
    private static boolean affectsPlayers = NebulaCore.getConfiguration(ConfigTypes.NEBULAPLANETS).getBoolean("Gravity.AFFECTS_PLAYERS");

    public static void updateAllVelocities() {
        for (World world: NebulaCore.getInstance().getServer().getWorlds()) {
            if (!isWorldEnabled(world)) continue;
            if (affectsEntities) {
                for (Entity entity : world.getEntities()) {
                    if (!affectsPlayers && entity instanceof Player) continue;
                    updateVelocity(world, entity);
                }
            } else if (affectsPlayers) {
                for (Entity entity : world.getPlayers()) {
                    updateVelocity(world, entity);
                }
            }
        }
    }

    public static void updateVelocity(World world, Entity entity) {

        Vector newVector = entity.getVelocity().clone();
        UUID uuid = entity.getUniqueId();

        Player player = null;
        if (entity instanceof Player) {
            player = (Player) entity;
            if (player.isDead() || player.isFlying() || player.isGliding() || player.isInsideVehicle() || player.isSneaking()) {
                clearVelocity(uuid);
                return;
            }

            Material material = world.getBlockAt(entity.getLocation()).getType();
            if (material == Material.LADDER || material == Material.WATER || material == Material.LAVA) {
                clearVelocity(uuid);
                return;
            }
            if (player.hasPotionEffect(PotionEffectType.LEVITATION)) {
                clearVelocity(uuid);
                return;
            }
        }

        if (velocities.containsKey(uuid) && onGround.containsKey(uuid) && !entity.isOnGround()) {
            double gravity = getWorldGravity(world);

            Vector oldVector = velocities.get(uuid);
            if (!onGround.get(uuid).booleanValue()) {
                Vector clonedVector = oldVector.clone();

                clonedVector.subtract(newVector);
                newVector.setX(oldVector.getX());
                newVector.setZ(oldVector.getZ());
                double clonedY = clonedVector.getY();;
                if (clonedY > 0.0 && (newVector.getY() < -0.01 || newVector.getY() > 0.01)) {
                    newVector.setY(oldVector.getY() - clonedY * gravity);

                    entity.setVelocity(newVector.clone());
                }
            }

            entity.setVelocity(newVector.clone());
        }

        velocities.put(uuid, newVector.clone());
        onGround.put(uuid, entity.isOnGround());
        positions.put(uuid, entity.getLocation());
    }

    public static void clearVelocity(UUID uuid) {
        if (positions.containsKey(uuid)) {
            velocities.remove(uuid);
            positions.remove(uuid);
            onGround.remove(uuid);
        }
    }

    public void run() {
        updateAllVelocities();
    }

    public static void reloadConfigValues() {
        affectsEntities = NebulaCore.getConfiguration(ConfigTypes.NEBULAPLANETS).getBoolean("Gravity.AFFECTS_ENTITIES");
        affectsPlayers = NebulaCore.getConfiguration(ConfigTypes.NEBULAPLANETS).getBoolean("Gravity.AFFECTS_PLAYERS");
    }

    public static boolean isWorldEnabled(World world) {
        if (NebulaCore.getConfiguration(ConfigTypes.NEBULAPLANETS).getBoolean("Gravity.WORLDS." + world.getName() + ".enabled")) {
            return true;
        }
        return false;
    }

    public static double getWorldGravity(World world) {
        return NebulaCore.getConfiguration(ConfigTypes.NEBULAPLANETS).getDouble("Gravity.WORLDS." + world.getName() + ".gravity");
    }
}
