package net.nebulacraft.nebulaships;

import lombok.extern.log4j.Log4j2;
import net.nebulacraft.nebulaships.commands.CommandCompletion;
import net.nebulacraft.nebulaships.commands.CommandManager;
import net.nebulacraft.nebulaships.config.ManageConfig;
import org.bukkit.plugin.java.JavaPlugin;

@Log4j2(topic = "NebulaShips")
public final class NebulaShips extends JavaPlugin {

    public static ManageConfig configManager;

    public String name = "\n  _   _      _           _        _____ _     _           \n" + " | \\ | |    | |         | |      / ____| |   (_)          \n" + " |  \\| | ___| |__  _   _| | __ _| (___ | |__  _ _ __  ___ \n" + " | . ` |/ _ \\ '_ \\| | | | |/ _` |\\___ \\| '_ \\| | '_ \\/ __|\n" + " | |\\  |  __/ |_) | |_| | | (_| |____) | | | | | |_) \\__ \\\n" + " |_| \\_|\\___|_.__/ \\__,_|_|\\__,_|_____/|_| |_|_| .__/|___/\n" + "                                               | |        \n" + "                                               |_|        ";

    @Override
    public void onEnable() {

        LOGGER.info("Registering commands.");
        getCommand("nebulaships").setExecutor(new CommandManager());
        getCommand("nebulaships").setTabCompleter(new CommandCompletion());

        LOGGER.info("Registering ConfigManager.");
        configManager = new ManageConfig();
        configManager.loadFiles(this);

        LOGGER.info(name);
        LOGGER.info("NebulaShips has loaded. Enjoy!");
    }

    @Override
    public void onDisable() {
        LOGGER.info("NebulaShips has been disabled.");
    }

    public static NebulaShips getInstance() {
        return getPlugin(NebulaShips.class);
    }
}
