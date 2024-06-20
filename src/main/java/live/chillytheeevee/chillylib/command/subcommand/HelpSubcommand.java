package live.chillytheeevee.chillylib.command.subcommand;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import live.chillytheeevee.chillylib.config.Config;
import live.chillytheeevee.chillylib.utility.ChatUtils;

import java.util.*;

/**
 * A special {@link Subcommand} that is automatically generated for every plugin running chilly-lib. This command prints
 * a messages that shows all registered commands for the plugins along with their usages and descriptions.
 */
public class HelpSubcommand extends Subcommand {
	private final String invalid_number;
	private final JavaPlugin plugin;
	private final List<Subcommand> subcommands;

	/**
	 * Constructs a new HelpSubcommand from the given {@link Config}. This Config must contain the "usage",
	 * "description", and "permission" fields. This HelpSubcommand also requires an instance of the plugin for which
	 * this Subcommand is registered under as well as a Map of all subcommands registered under the plugin. This
	 * subcommand is automatically constructed by the {@link SubcommandManager} class.
	 * @param config The configuration file for this Subcommand.
	 * @param plugin The instance of the plugin for which this HelpSubcommand is registered.
	 * @param subcommands A Map of all Subcommands used by this plugin.
	 */
	public HelpSubcommand(Config config, JavaPlugin plugin, Map<String, Subcommand> subcommands){
		super(config);

		this.plugin = plugin;

		invalid_number = config.get().getString("messages.warning_invalid_number");

		this.subcommands = new ArrayList<>();
		for (String key : subcommands.keySet()) {
			this.subcommands.add(subcommands.get(key));
		}
	}


	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		Map<Integer, ArrayList<String>> helpCommandList;
		List<String[]> helpEntries = new ArrayList<>();
		List<String> helpLines = new ArrayList<>();
		
		for (Subcommand c : subcommands) {
			for (String permission : c.getRequiredPermission()) {
				if (sender.hasPermission(permission)) {
					helpEntries.add(c.getHelpEntry());
					break;
				}
			}
		}
		for (String[] commandHelp : helpEntries) {
			helpLines.addAll(Arrays.asList(commandHelp));
		}

		helpCommandList = ChatUtils.paginateTextList(12, helpLines);
		
		if (helpCommandList.isEmpty()) {
			return true;
		}
		
		// args[0] is "help" and args.length > 0
		if (args.length == 1) {
			for (String line : helpCommandList.get(0)) {
				sender.sendMessage(ChatUtils.chat(line));
			}
			ChatUtils.chat("&8&m                                             ");
			sender.sendMessage(ChatUtils.chat(String.format("&8[&51&8/&5%s&8]", helpCommandList.size())));
			return true;
		}

		if (args.length == 2) {
			try {
				Integer.parseInt(args[1]);
			} catch (NumberFormatException nfe) {
				sender.sendMessage(ChatUtils.chat(invalid_number));
				return true;
			}

			int pageNumber = Integer.parseInt(args[1]);
			if (pageNumber < 1) {
				pageNumber = 1;
			}
			if (pageNumber > helpCommandList.size()) {
				pageNumber = helpCommandList.size();
			}
			
			for (String entry : helpCommandList.get(pageNumber - 1)) {
				sender.sendMessage(ChatUtils.chat(entry));
			}
			ChatUtils.chat("&8&m                                             ");
			sender.sendMessage(ChatUtils.chat(String.format("&8[&5%s&8/&5%s&8]", pageNumber, helpCommandList.size())));
			return true;
		}

		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		if (args.length == 2) {
			List<String> subargs = new ArrayList<>();
			subargs.add("1");
			subargs.add("2");
			subargs.add("3");
			subargs.add("...");
			return subargs;
		}
		List<String> subargs = new ArrayList<>();
		subargs.add(" ");
		return subargs;
	}

	@Override
	public String[] getHelpEntry() {
		YamlConfiguration config = this.config.get();
		String pluginName = plugin.getName().toLowerCase();
		return new String[]{
				ChatUtils.chat("&8&m                                             "),
				ChatUtils.chat("&d" + Objects.requireNonNull(config.getString(USAGE_KEY))
						.replace("%s", pluginName)),
				ChatUtils.chat("&7" + Objects.requireNonNull(config.getString(DESCRIPTION_KEY))
						.replace("%s", pluginName)),
				ChatUtils.chat("&7> &d" + Objects.requireNonNull(config.getString(PERMISSION_KEY))
						.replace("%s", pluginName)),
		};
	}

	/**
	 * Registers the given {@link Subcommand} into this HelpSubcommand.
	 * @param subcommand the Subcommand to register into this HelpSubcommand.
	 */
	public void registerSubcommand(Subcommand subcommand) {
		subcommands.add(subcommand);
	}

}
