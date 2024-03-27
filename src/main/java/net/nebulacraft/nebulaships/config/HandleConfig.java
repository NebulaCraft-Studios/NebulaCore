package net.nebulacraft.nebulaships.config;

import lombok.extern.log4j.Log4j2;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

@Log4j2(topic = "NebulaShips")
public class HandleConfig {

    private final JavaPlugin plugin;
    private final String name;
    private final File file;
    private FileConfiguration configuration;

    public HandleConfig(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name + ".yml";
        this.file = new File(plugin.getDataFolder(), this.name);
        this.configuration = new YamlConfiguration();

        new File(plugin.getDataFolder() + "/schematics").mkdir();
    }

    public void saveDefaultConfig() {
        if (!file.exists()) {
            plugin.saveResource(name, false);
        }

        try {
            configuration.load(file);
        } catch (InvalidConfigurationException | IOException error) {
            error.printStackTrace();
            LOGGER.error("----------------------- CONFIGURATION EXCEPTION -----------------------");
            LOGGER.error("There was an error loading config " + name);
            LOGGER.error("Please check that config for any configuration mistakes.");
            LOGGER.error("Plugin will now disable...");
            LOGGER.error("----------------------- CONFIGURATION EXCEPTION -----------------------");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    public void save() {
        if (configuration == null || file == null) return;
        try {
            getConfig().save(file);
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    public void reload() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return configuration;
    }

}
