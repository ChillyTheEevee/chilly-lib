package world.sc2.config;

import org.bukkit.plugin.java.JavaPlugin;

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

    public Config saveConfig(String name) {
        return getConfig(name).save();
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
            configs.get(config).save();
        }
    }

}