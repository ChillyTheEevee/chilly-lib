package world.sc2.command;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import world.sc2.config.Config;
import world.sc2.config.ConfigManager;
import world.sc2.utility.ChatUtils;

import java.util.List;

public class ReloadCommand extends Command {

	private final Config config;
	private final ConfigManager configManager;

	private static final String reloadSuccessfulMessageKey = "messages.reload_successful";

	public ReloadCommand(Config config, ConfigManager configManager){
		super(config);
		this.config = config;

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
	public String[] getRequiredPermission() {
		return new String[]{"es.reload"};
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}
}
