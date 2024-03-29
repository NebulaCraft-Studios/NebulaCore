package net.nebulacraft.nebulacore.util.model;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerBoat;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCamera;
import net.nebulacraft.nebulacore.NebulaCore;
import net.nebulacraft.nebulacore.config.Messages;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShipManager implements PacketListener, Listener {
    public static Map<Player, ArmorStand> presentStands = new HashMap<>();
    public static Map<Player, BukkitTask> presentShipTasks = new HashMap<>();

    public static Map<String, Double> shipBrakingPower = new HashMap<>();
    public static Map<Player, String> playerShip = new HashMap<>();

    public static Map<Player, Double> shipSpeedData = new HashMap<>();
    public static Map<Player, Double> shipAngleData = new HashMap<>();
    public static Map<Player, Double> shipHeightData = new HashMap<>();

    public static Map<Player, Integer> shipSpectate = new HashMap<>();
    // don't forget them in the config
    // well wouldn't that be nice to be configured in the config
    // have you seen the plugins already, i have configs for literally everything xDDDDDDDD
    // hurry up 🧢

    boolean isMounted = false;
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        Player player = (Player) event.getPlayer();
        // TODO every 5 ticks if (getVelocity - shipBrakingPower.get(playerShip.get(event.getPlayer())) < 0) getVelocity - shipBrakingPower.get(playerShip.get(event.getPlayer()));

        // ELSE set velocity to 0

        /*if (event.getPacketType() == PacketType.Play.Client.STEER_VEHICLE) {
            isMounted = true;
            if (!ShipManager.presentStands.containsKey(player)) return;
            int entityid = ShipManager.shipSpectate.get(player);
            WrapperPlayClientSteerVehicle packet = new WrapperPlayClientSteerVehicle(event);

            double currentSpeed = ShipManager.shipSpeedData.get(player); // get the current speed
            double currentAngle = ShipManager.shipAngleData.get(player); // get the current angle change;
            double currentHeight = ShipManager.shipHeightData.get(player); // get the current height;

            float vehicleForward = packet.getForward();
            float vehicleSideways = packet.getSideways();

            System.out.println(vehicleForward + " // "  + vehicleSideways + " // " + packet.isJump());

            if (vehicleForward > 0) { // We are trying to move forwards, so increase speed
                ShipManager.shipSpeedData.put(player, currentSpeed + 0.015); // The speed is quite sensitive so we only add 0.05.
            } else if (vehicleForward < 0) { // We are trying to move backwards, so decrease speed
                ShipManager.shipSpeedData.put(player, currentSpeed - 0.015);
            }

            if (vehicleSideways > 0) { // We are trying to move left, decrease angle
                ShipManager.shipAngleData.put(player, currentAngle - 0.3); // 360 degrees in a full circle, so 1 is not much compared to the speed.
            } else if (vehicleSideways < 0) { // We are trying to move right, increase angle
                ShipManager.shipAngleData.put(player, currentAngle + 0.3);
            }

            if (packet.isJump()) { // If the packet was the player jumping
                ShipManager.shipHeightData.put(player, currentHeight + 0.1);
            }
            spectateEntity(player, entityid);
        }*/

        if (event.getPacketType() == PacketType.Play.Client.STEER_BOAT) {
            isMounted = true;
            if (!ShipManager.presentStands.containsKey(player)) return;
            int entityid = ShipManager.shipSpectate.get(player);
            WrapperPlayClientSteerBoat packet = new WrapperPlayClientSteerBoat(event);

            double currentSpeed = ShipManager.shipSpeedData.get(player); // get the current speed
            double currentAngle = ShipManager.shipAngleData.get(player); // get the current angle change;
            double currentHeight = ShipManager.shipHeightData.get(player); // get the current height;

            boolean left = packet.isLeftPaddleTurning();
            boolean right = packet.isRightPaddleTurning();

            if (left && right) {
                ShipManager.shipSpeedData.put(player, currentAngle + 0.0005);
            } else if (left && !right) {
                ShipManager.shipAngleData.put(player, currentAngle - 0.25);
            } else if (!left && right) {
                ShipManager.shipAngleData.put(player, currentAngle + 0.25);
            }

            spectateEntity(player, entityid);
        }
    }
    public static void spectateEntity(Player player, int entityid) {


        WrapperPlayServerCamera cameraPacket = new WrapperPlayServerCamera(entityid);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, cameraPacket);
    }
    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
        if (event.getEntity() instanceof Player player) {
            isMounted = false;
            if (!ShipManager.presentStands.containsKey(player)) return;
            event.setCancelled(true);

            double currentHeight = ShipManager.shipHeightData.get(player); // get the current height;

            ShipManager.shipHeightData.put(player, currentHeight - 0.1);
        }
    }

    public static void tempExit(Player player) {
        if (!ShipManager.presentStands.containsKey(player)) return;

        presentShipTasks.get(player).cancel();
        presentShipTasks.remove(player);

        presentStands.get(player).remove();
        presentStands.remove(player);

        shipSpectate.remove(player);
        shipAngleData.remove(player);
        shipHeightData.remove(player);

        spectateEntity(player, player.getEntityId());
    }


    public static void shipBrakeTask(Player player) {
        System.out.println(shipSpeedData.get(player));
        BukkitScheduler scheduler = NebulaCore.getInstance().getServer().getScheduler();
        scheduler.runTaskTimer(NebulaCore.getInstance(), () -> {
            Double currentVelocty = shipSpeedData.get(player);
            System.out.println(currentVelocty);
            if ((currentVelocty - shipBrakingPower.get(playerShip.get(player))) > 0) {

                shipSpeedData.put(player, currentVelocty - shipBrakingPower.get(playerShip.get(player)));
            } else {

                shipSpeedData.put(player, 0.0);
            } // hold on
        },0L, 5L); // fuck you
    }

}
