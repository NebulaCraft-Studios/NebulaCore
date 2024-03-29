package net.nebulacraft.nebulacore.util.model;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
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

    public static Map<Player, Double> shipSpeedData = new HashMap<>();
    public static Map<Player, Double> shipAngleData = new HashMap<>();
    public static Map<Player, Integer> shipSpectate = new HashMap<>();

    public static Map<Player, Boolean> attemptedDismount = new HashMap<>(); // If the player has already tried to dismount.

    boolean isMounted = false;
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        Player player = (Player) event.getPlayer();

        if (event.getPacketType() == PacketType.Play.Client.STEER_VEHICLE) {
            isMounted = true;
            if (!ShipManager.presentStands.containsKey(player)) return;
            int entityid = ShipManager.shipSpectate.get(player);
            WrapperPlayClientSteerVehicle packet = new WrapperPlayClientSteerVehicle(event);

            double currentSpeed = ShipManager.shipSpeedData.get(player); // get the current speed
            double currentAngle = ShipManager.shipAngleData.get(player); // get the current angle change;

            float vehicleForward = packet.getForward();
            float vehicleSideways = packet.getSideways();

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
if (isMounted){spectateEntity(player, entityid);}
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

            if (!attemptedDismount.containsKey(player)) {
                event.setCancelled(true);

                attemptedDismount.put(player, true);
                Messages.SHIP_EXIT_WARNING.send(player);

                BukkitScheduler scheduler = NebulaCore.getInstance().getServer().getScheduler();
                scheduler.runTaskLater(NebulaCore.getInstance(), new Runnable() {
                    public void run() {
                        attemptedDismount.remove(player);
                    }
                }, 600L); // After 30 seconds, remove the player, just so that if they forget about it then it won't immediately eject them.

            } else {
                presentShipTasks.get(player).cancel();
                presentStands.get(player).remove();
                attemptedDismount.remove(player);
                spectateEntity(player, player.getEntityId());
            }
        }
    }


}
