package net.nebulacraft.nebulaships.config;

import net.nebulacraft.nebulaships.NebulaShips;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ManageConfig {

    private Map<ConfigTypes, HandleConfig> configurations;

    public ManageConfig() {
        configurations = new HashMap<>();
    }

    public void loadFiles(NebulaShips plugin) {
        registerFile(ConfigTypes.MESSAGES, new HandleConfig(plugin, "messages"));
        registerFile(ConfigTypes.SETTINGS, new HandleConfig(plugin, "config"));

        configurations.values().forEach(HandleConfig::saveDefaultConfig);

        Messages.setConfiguration(getFile(ConfigTypes.MESSAGES).getConfig());
    }

    public HandleConfig getFile(ConfigTypes type) {
        return configurations.get(type);
    }

    public void reloadFiles() {
        configurations.values().forEach(HandleConfig::reload);
        Messages.setConfiguration(getFile(ConfigTypes.MESSAGES).getConfig());
    }

    public void registerFile(ConfigTypes type, HandleConfig config) {
        configurations.put(type, config);
    }

    public FileConfiguration getFileConfiguration(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }
}
