package net.nebulacraft.nebulacore;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.extern.log4j.Log4j2;
import net.nebulacraft.nebulacore.commands.CommandCompletion;
import net.nebulacraft.nebulacore.commands.CommandManager;
import net.nebulacraft.nebulacore.config.ConfigTypes;
import net.nebulacraft.nebulacore.config.ManageConfig;
import net.nebulacraft.nebulacore.listeners.GravityHandler;
import net.nebulacraft.nebulacore.util.gravity.VelocityManager;
import net.nebulacraft.nebulacore.util.model.ShipManager;
import net.nebulacraft.nebulacore.util.model.SummonCustomModel;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@Log4j2(topic = "NebulaCore")
public final class NebulaCore extends JavaPlugin {

    public static ManageConfig configManager;

    public String name = """

              _   _      _           _        _____              \s
             | \\ | |    | |         | |      / ____|             \s
             |  \\| | ___| |__  _   _| | __ _| |     ___  _ __ ___\s
             | . ` |/ _ \\ '_ \\| | | | |/ _` | |    / _ \\| '__/ _ \\
             | |\\  |  __/ |_) | |_| | | (_| | |___| (_) | | |  __/
             |_| \\_|\\___|_.__/ \\__,_|_|\\__,_|\\_____\\___/|_|  \\___|
            """;

    @Override
    public void onEnable() {

        LOGGER.info("Registering Commands.");
        getCommand("nebulacore").setExecutor(new CommandManager());
        getCommand("nebulacore").setTabCompleter(new CommandCompletion());

        LOGGER.info("Registering ConfigManager.");
        configManager = new ManageConfig();
        configManager.loadFiles(this);

        LOGGER.info("Registering PacketEvents.");
        PacketEvents.getAPI().getEventManager().registerListener(new ShipManager(),
                PacketListenerPriority.LOW);
        PacketEvents.getAPI().init();

        LOGGER.info("Registering Events.");
        getServer().getPluginManager().registerEvents(new ShipManager(), this);
        getServer().getPluginManager().registerEvents(new GravityHandler(), this);

        LOGGER.info("Registering NebulaPlanets Gravity Task.");
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new VelocityManager(), 1, 1);

        LOGGER.info(name);
        LOGGER.info("NebulaCore has loaded. Enjoy!");
    }

    @Override
    public void onDisable() {
        LOGGER.info("Terminating PacketEvents API.");
        PacketEvents.getAPI().terminate();
        LOGGER.info("NebulaCore has been disabled.");
    }

    public static NebulaCore getInstance() {
        return getPlugin(NebulaCore.class);
    }

    public static FileConfiguration getConfiguration(ConfigTypes config) {
        return configManager.getFile(config).getConfig();
    }

    @Override
    public void onLoad() {
        LOGGER.info("Loading PacketEvents API.");

        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings().reEncodeByDefault(false)
                .checkForUpdates(true)
                .bStats(false);
        PacketEvents.getAPI().load();
    }
}
