package net.nebulacraft.nebulacore.util.model;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;
import net.nebulacraft.nebulacore.NebulaCore;
import net.nebulacraft.nebulacore.config.ConfigTypes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class SummonCustomModel {

    public static void summonModel(String modelName, Location location, double speed, double angle, Player owner) {

        ShipManager.shipSpeedData.put(owner, speed);
        ShipManager.shipAngleData.put(owner, angle);


        int id = NebulaCore.getConfiguration(ConfigTypes.NEBULASHIPS).getInt("Models." + modelName + ".custom_model_id");
        String item = NebulaCore.getConfiguration(ConfigTypes.NEBULASHIPS).getString("Models." + modelName + ".item");

        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);

        ShipManager.shipSpectate.put(owner, armorStand.getEntityId());
        ItemStack customModel = new ItemStack(Material.getMaterial(item));
        ItemMeta modelMeta = customModel.getItemMeta();
        modelMeta.setCustomModelData(id);
        customModel.setItemMeta(modelMeta);

        armorStand.setHelmet(customModel);

        ShipManager.presentStands.put(owner, armorStand);

        /*BukkitTask task = Bukkit.getScheduler().runTaskTimer(NebulaCore.getInstance(), () -> {
            Location currentLocation = armorStand.getLocation();
            Vector direction = currentLocation.getDirection().normalize().multiply(speed);
            direction.setY(0);
            Location newLocation = currentLocation.add(direction);
            armorStand.teleport(newLocation);
        }, 0L, 1L);*/

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(NebulaCore.getInstance(), () -> {
            double shipSpeed = ShipManager.shipSpeedData.get(owner);
            double turnangle = ShipManager.shipAngleData.get(owner);


            Location currentLocation = armorStand.getLocation();
            Vector direction = currentLocation.getDirection().normalize().multiply(shipSpeed);

            double angleRad = Math.toRadians(turnangle);
            double cosAngle = Math.cos(angleRad);
            double sinAngle = Math.sin(angleRad);
            double newX = direction.getX() * cosAngle - direction.getZ() * sinAngle;
            double newZ = direction.getX() * sinAngle + direction.getZ() * cosAngle;
            float currentYaw = currentLocation.getYaw();
            float newYaw = currentYaw + (float) turnangle;

            direction.setX(newX);
            direction.setZ(newZ);
            direction.setY(0);

            Location newLocation = currentLocation.add(direction);
            newLocation.setYaw(newYaw);
            //armorStand.teleport(newLocation);
            armorStand.teleport(newLocation);
        }, 0L, 1L);
        ShipManager.presentShipTasks.put(owner, task);

    }

}
