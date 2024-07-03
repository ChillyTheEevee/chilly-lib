package live.chillytheeevee.chillylib.config;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;


/**
 * A manager for all {@link Config} related tasks. Individual plugins must construct this ConfigManager. There should
 * only be one instance of ConfigManager per plugin, and that instance should be passed around using dependency
 * injection.
 *
 * @author Bimmr
 */
public class ConfigManager {

    private final JavaPlugin plugin;
    private final Map<String, Config> configs = new HashMap<>();

    // Performance
    private final FilenameFilter yamlExtensionFilter = (dir, name) -> {
        String lowercaseName = name.toLowerCase();
        return lowercaseName.endsWith(".yml") || lowercaseName.endsWith(".yaml");
    };

    /**
     * Constructs a new ConfigManager for the given {@link JavaPlugin}. There should only be one ConfigManager per
     * plugin, and this instance of ConfigManager should be passed around using dependency injection.
     *
     * @param plugin The plugin to create this ConfigManager for.
     */
    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Returns a read-only view of the internal Map of all {@link Config} files registered with this ConfigManager.
     *
     * @return a read-only view of the internal Map of all Config files registered with this ConfigManager.
     */
    public Map<String, Config> getConfigs() {
        return Collections.unmodifiableMap(configs);
    }

    /**
     * Returns the {@link Config} at the given relative file path. If no Config registered with this ConfigManager is
     * located at the given relative file path, then a new Config is created and registered with this ConfigManager
     * at the provided location.
     *
     * @param relativeFilePath The relative file path of the Config to return (from the plugin's data directory)
     * @return The Config value stored within the Config map in this ConfigManager for the given relativeFilePath, or a
     * new Config with the given relativeFilePath if it does not exist within that map.
     */
    public Config getConfig(String relativeFilePath) {
        if (!configs.containsKey(relativeFilePath))
            configs.put(relativeFilePath, new Config(plugin, relativeFilePath));

        return configs.get(relativeFilePath);
    }

    /**
     * Returns a Set of {@link Config}s that contains all Configs that reside in the directory at the given
     * relativeFilePath. If any Config files within this directory are unregistered with this ConfigManager, they are
     * registered. If the directory does not exist or if there are no Configs within this directory, an empty Set will
     * be returned.
     *
     * @param relativeFilePath The relative file path (from the plugin's
     * @return A Set of all {@link Config}s within the provided directory, or an empty set if there is no directory.
     */
    public Collection<Config> getConfigsInDirectory(String relativeFilePath) {
        File pluginDataFolder = plugin.getDataFolder();
        File directory = new File(pluginDataFolder, relativeFilePath);

        if (!directory.exists() || !directory.isDirectory()) {
            return new HashSet<>();
        }

        Collection<Config> configsInDirectory = new HashSet<>();
        File[] yamlFilesInDirectory = directory.listFiles(yamlExtensionFilter);

        if (yamlFilesInDirectory != null) {
            for (File configFile : yamlFilesInDirectory) {
                String relativePath = pluginDataFolder.toPath().relativize(configFile.toPath()).toString();
                configsInDirectory.add(getConfig(relativePath));
            }
        }

        return configsInDirectory;
    }

    /**
     * Reloads the {@link Config} with the given name. Exactly the same as instead calling getConfig(name).reload().
     *
     * @param name The name of this Config.
     * @return The Config that was reloaded.
     */
    public Config reloadConfig(String name) {
        return getConfig(name).reload();
    }

    /**
     * Reloads every {@link Config} registered with this ConfigManager by calling {@link Config#reload()}.
     */
    public void reloadConfigs() {
        for (Config config : configs.values()) {
            config.reload();
        }
    }

    /**
     * Saves every {@link Config} registered within this ConfigManager by internally calling saveConfig(name) for each
     * individual Config name.
     */
    public void saveConfigs() {
        for (String config : configs.keySet()) {
            saveConfig(config);
        }
    }

    /**
     * If a config doesn't exist in the specified data folder, then the config is copied over from the jar file. If the
     * specified config is also not stored in the jar file, then a new empty config file is created. If the config
     * does already exist in the specified data folder and any data was changed by the plugin, then any changes made
     * will be saved into the config on the data folder.
     *
     * @param name The path to the config from the plugin's assigned data file
     */
    public void saveConfig(String name) {
        File config = new File(plugin.getDataFolder(), name);
        if (!config.exists()) {
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
        } else {
            getConfig(name).save();
        }
    }

    /**
     * Updates a Config by comparing it to the plugin in the plugin's assigned data folder and copying over any missing
     * data from the jar file.
     *
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
     * Saves and updates the given config within this ConfigManager. equivalent to calling saveConfig(name) and then
     * updateConfig(name).
     *
     * @param name The path to the config from the plugin's assigned data file
     */
    public void saveAndUpdateConfig(String name) {
        saveConfig(name);
        updateConfig(name);
    }

}