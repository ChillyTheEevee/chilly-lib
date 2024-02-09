package world.sc2.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import world.sc2.config.ConfigManager;
import world.sc2.utility.ChatUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager implements TabExecutor {
	private final JavaPlugin plugin;
	private final HelpCommand helpCommand;
	private final Map<String, Command> commands = new HashMap<>();
	private final String invalid_command;
	private final String warning_no_permission;

	public CommandManager(JavaPlugin plugin, ConfigManager configManager) {
		this.plugin = plugin;

		invalid_command = configManager.getConfig("commands/generic.yml").get().getString("messages.warning_invalid_command");
		warning_no_permission = configManager.getConfig("commands/generic.yml").get().getString("messages.warning_no_permission");

		helpCommand = new HelpCommand(configManager.getConfig("commands/help.yml"), commands);

		addCommand("help", helpCommand);
		addCommand("reload", new ReloadCommand(configManager.getConfig("commands/reload.yml"),
				configManager));

		PluginCommand command = plugin.getCommand(plugin.getName());
		if (command != null){
			command.setExecutor(this);
		} else {
			Bukkit.getLogger().severe("Could not create " + plugin.getName() + "'s super command because the"
			+ " command is not specified in plugin.yml");
		}
	}

	public void addCommand(String commandName, Command command) {
		commands.put(commandName, command);
		helpCommand.addCommand(command);
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String name, String[] args) {
		if (args.length == 0) {
			try {
				List<String> authors = plugin.getDescription().getAuthors();
				if (authors.size() > 1) {
					sender.sendMessage(ChatUtils.chat(String.format("&d%s v%s by %s",
							plugin.getName(),
							plugin.getDescription().getVersion(),
							authors)));
				} else if (authors.size() == 1) {
					sender.sendMessage(ChatUtils.chat(String.format("&d%s v%s by %s",
							plugin.getName(),
							plugin.getDescription().getVersion(),
							authors.get(0))));
				} else {
						sender.sendMessage(ChatUtils.chat(String.format("&d%s v%s",
								plugin.getName(),
								plugin.getDescription().getVersion())));
					}
				} catch (Exception e) {
				Bukkit.getLogger().severe("Plugin " + plugin.getName() + " does not have a Description!");
			}
			sender.sendMessage(ChatUtils.chat("&7/" + plugin.getName().toLowerCase() + " help"));
			return true;
		}
		
		for (String subCommand : commands.keySet()) {
			if (args[0].equalsIgnoreCase(subCommand)) {
				boolean hasPermission = false;
				for (String permission : commands.get(subCommand).getRequiredPermission()){
					if (sender.hasPermission(permission)){
						hasPermission = true;
						break;
					}
				}
				if (!hasPermission){
					sender.sendMessage(ChatUtils.chat(warning_no_permission));
					return true;
				}
				if (!commands.get(subCommand).onCommand(sender, args)) {
					sender.sendMessage(ChatUtils.chat(commands.get(subCommand).getUsageMessage()));
				}
				return true;
			}
		}
		sender.sendMessage(ChatUtils.chat(invalid_command));
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String name, String[] args) {
		if (args.length == 1) {
			return new ArrayList<>(commands.keySet());
		} else if (args.length > 1) {
			for (String arg : commands.keySet()) {
				if (args[0].equalsIgnoreCase(arg)) {
					return commands.get(arg).onTabComplete(sender, args);
				}
			}
		}
		return null;
	}
}
