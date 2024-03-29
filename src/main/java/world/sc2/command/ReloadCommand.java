package world.sc2.command;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import world.sc2.config.Config;
import world.sc2.config.ConfigManager;
import world.sc2.utility.ChatUtils;

import java.util.List;
import java.util.Objects;

public class ReloadCommand extends Command {

	private final ConfigManager configManager;
	private final JavaPlugin plugin;

	private static final String reloadSuccessfulMessageKey = "messages.reload_successful";

	public ReloadCommand(Config config, JavaPlugin plugin, ConfigManager configManager){
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
