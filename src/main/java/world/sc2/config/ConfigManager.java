package world.sc2.config;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

//All credit to spigotmc.org user Bimmr for this manager
public class ConfigManager {

    private final JavaPlugin plugin;
    private final HashMap<String, Config> configs = new HashMap<>();

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public HashMap<String, Config> getConfigs() {
        return configs;
    }

    public Config getConfig(String name) {
        if (!configs.containsKey(name))
            configs.put(name, new Config(plugin, name));

        return configs.get(name);
    }

    public Config reloadConfig(String name) {
        return getConfig(name).reload();
    }

    public void reloadConfigs() {
        for (String config : configs.keySet()){
            configs.get(config).reload();
        }
    }

    public void saveConfigs() {
        for (String config : configs.keySet()) {
            saveConfig(config);
        }
    }

    /**
     * If a config doesn't exist in the specified data folder, then the config is copied over from the jar file. If the
     * specified config is also not stored in the jar file, then a new empty config file is created.
     * @param name The path to the config from the plugin's assigned data file
     */
    public void saveConfig(String name){
        File config = new File(plugin.getDataFolder(), name);
        if (!config.exists()){
            if (plugin.getResource(name) != null) {
                plugin.saveResource(name, false);
                getConfig(name).save();
            } else {
                try {
                    config.getParentFile().mkdirs();
                    config.createNewFile();
                    getConfig(name).save();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Updates a config by comparing in the plugin's assigned data folder by copying over data from the jar file
     * @param name The path to the config from the plugin's assigned data file
     */
    public void updateConfig(String name) {
        File configFile = new File(plugin.getDataFolder(), name);
        try {
            ConfigUpdater.update(plugin, name, configFile, new ArrayList<>());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves and updates the given config
     * @param name The path to the config from the plugin's assigned data file
     */
    public void saveAndUpdateConfig(String name) {
        saveConfig(name);
        updateConfig(name);
    }

}