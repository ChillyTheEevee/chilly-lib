package live.chillytheeevee.chillylib.command.subcommand;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import live.chillytheeevee.chillylib.command.BaseCommand;
import live.chillytheeevee.chillylib.config.Config;
import live.chillytheeevee.chillylib.config.ConfigManager;

import java.util.HashMap;
import java.util.Map;

/**
 * The manager of all {@link Subcommand} related tasks. Individual plugins must construct this SubcommandManager and
 * individually register each of the Subcommands they create.
 */
public class SubcommandManager {

	private static final String COMMAND_CONFIG_DIRECTORY_PATH = "commands";
	private static final String BASE_COMMAND_NAME = "base_command";
	private static final String BASE_COMMAND_CONFIG_PATH =
			COMMAND_CONFIG_DIRECTORY_PATH + "/" + BASE_COMMAND_NAME + ".yml";
	private static final String HELP_SUBCOMMAND_NAME = "help";
	private static final String RELOAD_SUBCOMMAND_NAME = "reload";


	// Fields
    private final HelpSubcommand helpSubcommand;
	private final Map<String, Subcommand> subcommandMap;

	public SubcommandManager(JavaPlugin plugin, ConfigManager configManager) {
		this.subcommandMap = new HashMap<>();

		// Register Help and Reload subcommands.
		String helpSubcommandConfigPath = getSubcommandConfigPath(HELP_SUBCOMMAND_NAME);
		String reloadSubcommandConfigPath = getSubcommandConfigPath(RELOAD_SUBCOMMAND_NAME);
		Config helpSubcommandConfig = configManager.getConfig(helpSubcommandConfigPath);
		Config reloadSubcommandConfig = configManager.getConfig(reloadSubcommandConfigPath);
		configManager.saveAndUpdateConfig(helpSubcommandConfigPath);
		configManager.saveAndUpdateConfig(reloadSubcommandConfigPath);

		helpSubcommand = new HelpSubcommand(helpSubcommandConfig, plugin, subcommandMap);

		registerSubcommand("help", helpSubcommand);
		registerSubcommand("reload", new ReloadSubcommand(reloadSubcommandConfig, plugin, configManager));

		// Create and register base plugin command
		Config baseCommandConfig = configManager.getConfig(BASE_COMMAND_CONFIG_PATH);
		configManager.saveAndUpdateConfig(BASE_COMMAND_CONFIG_PATH);

        BaseCommand pluginBaseCommand = new BaseCommand(plugin, subcommandMap, baseCommandConfig);

		PluginCommand command = plugin.getCommand(plugin.getName());
		if (command != null){
			command.setExecutor(pluginBaseCommand);
		} else {
			Bukkit.getLogger().severe("Could not create " + plugin.getName() + "'s super command because the"
			+ " command is not specified in plugin.yml");
		}
	}

	/**
	 * Registers the given {@link Subcommand} into this SubcommandManager using the given String subcommandName.
	 * @param subcommandName The name to assign this subcommand within this SubcommandManager.
	 * @param subcommand The Subcommand to register.
	 */
	public void registerSubcommand(String subcommandName, Subcommand subcommand) {
		subcommandMap.put(subcommandName, subcommand);
		helpSubcommand.registerSubcommand(subcommand);
	}

	/**
	 * Returns the String path to a Subcommand's Config file given its Subcommand name.
	 * @param subcommandName The name of a Subcommand.
	 * @return the String path to a Subcommand's Config file given its Subcommand name.
	 */
	private String getSubcommandConfigPath(String subcommandName) {
        return COMMAND_CONFIG_DIRECTORY_PATH + "/" + subcommandName + ".yml";
	}

}
