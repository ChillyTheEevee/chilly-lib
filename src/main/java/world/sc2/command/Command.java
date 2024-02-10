package world.sc2.command;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import world.sc2.config.Config;
import world.sc2.utility.ChatUtils;

import java.util.List;

public abstract class Command {

	protected static final String USAGE_KEY = "usage";
	protected static final String DESCRIPTION_KEY = "description";
	protected static final String PERMISSION_KEY = "permission";

	protected Config config;

	public Command(Config config) {
		this.config = config;
	}
	public abstract boolean onCommand(CommandSender sender, String[] args);
	public abstract List<String> onTabComplete(CommandSender sender, String[] args);
	public String[] getRequiredPermission() {
		List<String> permissions = config.get().getStringList(PERMISSION_KEY);
		return permissions.toArray(new String[0]);
	}
	public String getUsageMessage() {
		return "&4" + config.get().get(USAGE_KEY);
	}
	public String[] getHelpEntry() {
		YamlConfiguration config = this.config.get();
		return new String[]{
				ChatUtils.chat("&8&m                                             "),
				ChatUtils.chat("&d" + config.getString(USAGE_KEY)),
				ChatUtils.chat("&7" + config.getString(DESCRIPTION_KEY)),
				ChatUtils.chat("&7> &d" + config.getString(PERMISSION_KEY))
		};
	}
}
