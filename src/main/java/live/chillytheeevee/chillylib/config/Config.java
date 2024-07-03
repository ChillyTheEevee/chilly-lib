package live.chillytheeevee.chillylib.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * A representation of a YAML configuration file within a {@link org.bukkit.plugin.Plugin}'s jar file or data directory.
 * Each Config has an internal {@link YamlConfiguration} file and contains methods for saving this Config and reloading
 * the Config into the game. The YamlConfiguration returned by {@link Config#get()} may change after calling the
 * {@link Config#reload()} command.
 */
public class Config {
    private final String relativePath;
    private File file;
    private YamlConfiguration config;
    private final JavaPlugin plugin;

    /**
     * Constructs a new Config for the given JavaPlugin with the given relative file path.
     *
     * @param plugin       The JavaPlugin to register this Config under
     * @param relativePath The relative path of this Config from the plugin's data directory.
     */
    public Config(JavaPlugin plugin, String relativePath) {
        this.plugin = plugin;
        this.relativePath = relativePath;
    }

    /**
     * Returns the relative path of this config from the plugin's data directory.
     *
     * @return the relative path of this Config from the plugin's data directory.
     */
    public String getRelativePath() {
        return relativePath;
    }

    /**
     * Returns the file name of this Config. For example, a config located at the relative path
     * /data/position/player1.yml will return the String "player1.yml".
     *
     * @return the file name of this Config.
     */
    public String getFileName() {
        if (file == null) {
            reload();
        }
        return file.getName();
    }

    /**
     * Returns the file name of this Config without the .yml file name extension. A config located at the relative path
     * /data/position/player1.yml will return the String "player1".
     *
     * @return The base name of this Config.
     */
    public String getBaseName() {
        String fullFileName = getFileName();
        return fullFileName.substring(0, fullFileName.length() - 4); // only works for file name extensions of length 3
    }

    /**
     * Saves this Config to the plugin's data folder. Any values changed by the plugin will be updated.
     *
     * @return This instance of Config
     */
    public Config save() {
        if ((this.config == null) || (this.file == null))
            return this;
        try {
            if (!config.getConfigurationSection("").getKeys(true).isEmpty())
                config.save(this.file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public YamlConfiguration get() {
        if (this.config == null)
            reload();

        return this.config;
    }

    /**
     * Saves the config stored within the plugin's jar file into its data directory. This method will not replace a
     * configuration file if it is already present. Throws an exception if this config does not exist within the
     * plugin's jar file.
     *
     * @return This instance of Config.
     */
    public Config saveDefaultConfig() {
        file = new File(plugin.getDataFolder(), this.relativePath);

        plugin.saveResource(this.relativePath, false);

        return this;
    }

    /**
     * Reloads this Config to reflect any external changes made to it since instantiation. If the YamlConfiguration
     * stored within the plugin's data directory is missing some keys present within the plugin's jar file, then the
     * values of those keys will be copied over as the default values of this Config.
     *
     * @return This Config.
     */
    public Config reload() {
        if (file == null)
            this.file = new File(plugin.getDataFolder(), this.relativePath);

        this.config = YamlConfiguration.loadConfiguration(file);

        Reader defConfigStream;
        try {
            defConfigStream = new InputStreamReader(plugin.getResource(this.relativePath), StandardCharsets.UTF_8);

            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                this.config.setDefaults(defConfig);
            }
        } catch (NullPointerException ignored) {
        }
        return this;
    }

    public Config copyDefaults(boolean force) {
        get().options().copyDefaults(force);
        return this;
    }

    /**
     * A convenience method for setting a key-value pair inside the internal {@link YamlConfiguration} of this Config.
     *
     * @param key   The key to set
     * @param value The value to set
     * @return This Config
     */
    public Config set(String key, Object value) {
        get().set(key, value);
        return this;
    }

    /**
     * A convenience method for getting the value of a key-value pair inside the internal {@link YamlConfiguration} of
     * this Config.
     *
     * @param key The key to get the value from.
     * @return The Object stored at the given key within this Config's YamlConfiguration, or null if the key does not
     * exist within the YamlConfiguration.
     */
    public Object get(String key) {
        return get().get(key);
    }
}