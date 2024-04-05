package net.nebulacraft.nebulacore.util.entities;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.UserLoginEvent;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;

import me.tofaa.entitylib.APIConfig;
import me.tofaa.entitylib.EntityLib;
import me.tofaa.entitylib.meta.display.BlockDisplayMeta;
import net.nebulacraft.nebulacore.NebulaCore;
import org.bukkit.Bukkit;

import org.bukkit.Material;
import org.bukkit.World;

import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.bukkit.Bukkit.createBlockData;

public class DisplayEntities implements PacketListener {


    private static AtomicInteger nextEntityId = new AtomicInteger(1000);
    private static AtomicInteger x = new AtomicInteger(100);
    private static AtomicInteger z = new AtomicInteger(100);
    public static User user;

    @Override
    public void onUserLogin(UserLoginEvent event) {
        PacketListener.super.onUserLogin(event);

    }

    public static void spawnDisplayEntity() {


        var entity = EntityLib.getApi().createEntity(EntityTypes.BLOCK_DISPLAY);
        Bukkit.getOnlinePlayers().forEach( player -> {
            entity.addViewer(player.getUniqueId());
        });
        entity.spawn(new Location(new Vector3d(x.getAndIncrement(), 100, z.getAndIncrement()), 0f, 0f));
        var meta = (BlockDisplayMeta) entity.getEntityMeta();
        meta.setHasGlowingEffect(true);
    }


}