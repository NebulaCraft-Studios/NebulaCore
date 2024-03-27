package net.nebulacraft.nebulacore.util.model;

import net.nebulacraft.nebulacore.NebulaCore;
import net.nebulacraft.nebulacore.config.ConfigTypes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class SummonCustomModel {

    public static void summonModel(String modelName, Location location) {

        int id = NebulaCore.getConfiguration(ConfigTypes.NEBULASHIPS).getInt("Models." + modelName + ".custom_model_id");
        String item = NebulaCore.getConfiguration(ConfigTypes.NEBULASHIPS).getString("Models." + modelName + ".item");

        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);

        ItemStack customModel = new ItemStack(Material.getMaterial(item));
        ItemMeta modelMeta = customModel.getItemMeta();
        modelMeta.setCustomModelData(id);
        customModel.setItemMeta(modelMeta);

        armorStand.setHelmet(customModel);

        double speed = 1;

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(NebulaCore.getInstance(), () -> {
            Location currentLocation = armorStand.getLocation();
            Vector direction = currentLocation.getDirection().normalize().multiply(speed);
            direction.setY(0);
            Location newLocation = currentLocation.add(direction);
            armorStand.teleport(newLocation);
        }, 0L, 1L);

    }
}
