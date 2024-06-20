package live.chillytheeevee.command.subcommand;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import live.chillytheeevee.config.Config;
import live.chillytheeevee.config.ConfigManager;
import live.chillytheeevee.utility.ChatUtils;

import java.util.List;
import java.util.Objects;

/**
 * A special {@link Subcommand} that is automatically generated for every plugin running chilly-lib. This command
 * reloads all {@link Config}s for the plugin by calling {@link ConfigManager#reloadConfigs()}.
 */
public class ReloadSubcommand extends Subcommand {

	private final ConfigManager configManager;
	private final JavaPlugin plugin;

	private static final String reloadSuccessfulMessageKey = "messages.reload_successful";

	public ReloadSubcommand(Config config, JavaPlugin plugin, ConfigManager configManager){
		super(config);
		this.plugin = plugin;
		this.configManager = configManager;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		configManager.reloadConfigs();

		if (sender instanceof Player player) {
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 2f);
		}
		sender.sendMessage(ChatUtils.chat(config.get().getString(reloadSuccessfulMessageKey)));
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
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
}
