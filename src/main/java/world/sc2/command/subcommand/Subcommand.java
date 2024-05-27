package world.sc2.command.subcommand;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import world.sc2.config.Config;
import world.sc2.utility.ChatUtils;

import java.util.List;

/**
 * An abstract representation of a subcommand used after the {@link world.sc2.command.BaseCommand} of a plugin. Each
 * subcommand is designed to be treated similar to a normal paper-spigot Command. All
 * Commands must have a {@link Config} with at least the "usage", "description", and "permission" keys filled out in
 * order to function.
 */
public abstract class Subcommand {

	protected static final String USAGE_KEY = "usage";
	protected static final String DESCRIPTION_KEY = "description";
	protected static final String PERMISSION_KEY = "permission";

	protected Config config;

	/**
	 * Constructs a new Subcommand from the given {@link Config}. This Config must contain the "usage", "description",
	 * and "permission" fields.
	 * @param config The configuration file for this Subcommand.
	 */
	public Subcommand(Config config) {
		this.config = config;
	}

	/**
	 * Called whenever this Subcommand is executed by a {@link CommandSender}. The first argument of this subcommand
	 * will always be the name of this Subcommand, and thus the minimum size of args is 1.
	 * @param sender The CommandSender who invoked this subcommand.
	 * @param args The arguments the sender invoked with this subcommand call. The first element of this array is
	 *             always the name of this Subcommand.
	 * @return true if this Subcommand's syntax was used properly.
	 */
	public abstract boolean onCommand(CommandSender sender, String[] args);

	/**
	 * Called whenever a plugin's base command is prompted for a tab completion and this subcommand's name was the first
	 * argument of the base command. The first entry in args will always be the name of this Subcommand.
	 * @param sender The CommandSender that is typing out the Subcommand.
	 * @param args The arguments the user has already typed. The first element of this array will always be the name of
	 *             this Subcommand.
	 * @return A List of Strings that represents the tab complete options of this command given they've already typed
	 * the given arguments.
	 */
	public abstract List<String> onTabComplete(CommandSender sender, String[] args);

	/**
	 * Returns a String array of permissions that a CommandSender may have to execute this Subcommand. If a
	 * CommandSender has any one of these permissions, they have the ability to execute this command. This permissions
	 * array is defined within this Command's {@link Config} under the "permissions" key.
	 * @return The permission array of this Command defined within its Config.
	 */
	public String[] getRequiredPermission() {
		List<String> permissions = config.get().getStringList(PERMISSION_KEY);
		return permissions.toArray(new String[0]);
	}

	/**
	 * Returns the usage message of this Subcommand found within its {@link Config} with an added red escape code.
	 * @return the usage message of this Subcommand found within its {@link Config} with an added red escape code.
	 */
	public String getUsageMessage() {
		return "&4" + config.get().get(USAGE_KEY);
	}

	/**
	 * Returns the formatted help entry of this Subcommand using this Subcommand's {@link Config}.
	 * @return the formatted help entry of this Subcommand using this Subcommand's {@link Config}.
	 */
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
