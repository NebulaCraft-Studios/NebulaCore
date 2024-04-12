package net.nebulacraft.nebulacore.util.entities;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.UserLoginEvent;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.Vector3d;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnLivingEntity;
import me.tofaa.entitylib.EntityLib;
import me.tofaa.entitylib.meta.EntityMeta;
import me.tofaa.entitylib.meta.display.BlockDisplayMeta;
import me.tofaa.entitylib.meta.display.ItemDisplayMeta;
import me.tofaa.entitylib.wrapper.WrapperEntity;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.NPC;

import java.util.Objects;
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

        WrapperEntity entity = EntityLib.getApi().createEntity(EntityTypes.BLOCK_DISPLAY);
        entity.spawn(new Location(new Vector3d(x.getAndIncrement(), 100, z.getAndIncrement()), 0f, 0f));
        Bukkit.getOnlinePlayers().forEach( player -> {
            entity.addViewer(player.getUniqueId());
        });
        EntityMeta meta = entity.getEntityMeta();
        BlockDisplayMeta meta1 = (BlockDisplayMeta) meta;
        meta1.setBlockId(11662);

        Bukkit.getOnlinePlayers().forEach( player -> {

            PacketEvents.getAPI().getPlayerManager().sendPacket(player, meta1.createPacket());

        });


    }


}