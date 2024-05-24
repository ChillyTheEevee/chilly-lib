package world.sc2.command;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import world.sc2.config.Config;
import world.sc2.config.ConfigManager;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {

	private static final String COMMAND_CONFIG_DIRECTORY_PATH = "commands";
	private static final String BASE_COMMAND_NAME = "base_command";
	private static final String HELP_COMMAND_NAME = "help";
	private static final String RELOAD_COMMAND_NAME = "reload";


	// Fields
    private final HelpCommand helpCommand;
	private final Map<String, Command> commandMap;

	public CommandManager(JavaPlugin plugin, ConfigManager configManager) {
		this.commandMap = new HashMap<>();

		// Register Help and Reload commands.
		String helpCommandConfigPath = getCommandConfigPath(HELP_COMMAND_NAME);
		String reloadCommandConfigPath = getCommandConfigPath(RELOAD_COMMAND_NAME);
		Config helpCommandConfig = configManager.getConfig(helpCommandConfigPath);
		Config reloadCommandConfig = configManager.getConfig(reloadCommandConfigPath);
		configManager.saveAndUpdateConfig(helpCommandConfigPath);
		configManager.saveAndUpdateConfig(reloadCommandConfigPath);

		helpCommand = new HelpCommand(helpCommandConfig, plugin, commandMap);

		registerCommand("help", helpCommand);
		registerCommand("reload", new ReloadCommand(reloadCommandConfig, plugin, configManager));

		// Create and register base plugin command
		String baseCommandConfigPath = getCommandConfigPath(BASE_COMMAND_NAME);
		Config baseCommandConfig = configManager.getConfig(baseCommandConfigPath);
		configManager.saveAndUpdateConfig(baseCommandConfigPath);

        BaseCommand pluginBaseCommand = new BaseCommand(plugin, commandMap, baseCommandConfig);

		PluginCommand command = plugin.getCommand(plugin.getName());
		if (command != null){
			command.setExecutor(pluginBaseCommand);
		} else {
			Bukkit.getLogger().severe("Could not create " + plugin.getName() + "'s super command because the"
			+ " command is not specified in plugin.yml");
		}
	}

	/**
	 * Registers the given {@link Command} into this CommandManager using the given String commandName.
	 * @param commandName The CommandName to assign the command within this CommandManager.
	 * @param command The Command to register.
	 */
	public void registerCommand(String commandName, Command command) {
		commandMap.put(commandName, command);
		helpCommand.addCommand(command);
	}

	/**
	 * Returns the String path to a Command's Config file given its Command name.
	 * @param commandName The name of a Command.
	 * @return the String path to a Command's Config file given its Command name.
	 */
	private String getCommandConfigPath(String commandName) {
        return COMMAND_CONFIG_DIRECTORY_PATH + "/" + commandName + ".yml";
	}

}
