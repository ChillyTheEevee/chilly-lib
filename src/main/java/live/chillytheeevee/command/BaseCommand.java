package live.chillytheeevee.command;

import live.chillytheeevee.command.subcommand.Subcommand;
import live.chillytheeevee.config.Config;
import live.chillytheeevee.utility.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A {@link TabExecutor} that is called for a plugin's base command. This command is the underlying internal base
 * beneath all subcommands.
 */
public class BaseCommand implements TabExecutor {

    private static final String WARNING_INVALID_COMMAND_KEY = "messages.warning_invalid_command";
    private static final String WARNING_NO_PERMISSION_KEY = "messages.warning_no_permission";

    // Dependencies
    private final Plugin plugin;
    private final Config baseCommandConfig;
    private final Map<String, Subcommand> commandMap;

    /**
     * Constructs BaseCommand for the given plugin using the map of commands and the plugin's baseCommandConfig.
     * @param plugin The plugin to create a BaseCommand for.
     * @param commandMap A map of all subcommands registered for the plugin.
     * @param baseCommandConfig The {@link Config} for this BaseCommand.
     */
    public BaseCommand(Plugin plugin, Map<String, Subcommand> commandMap, Config baseCommandConfig) {
        this.plugin = plugin;
        this.baseCommandConfig = baseCommandConfig;
        this.commandMap = commandMap;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command cmd,
                             @NotNull String name, String[] args) {
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

        for (String subCommand : commandMap.keySet()) {
            if (args[0].equalsIgnoreCase(subCommand)) {
                boolean hasPermission = false;
                for (String permission : commandMap.get(subCommand).getRequiredPermission()){
                    if (sender.hasPermission(permission)){
                        hasPermission = true;
                        break;
                    }
                }
                if (!hasPermission){
                    sender.sendMessage(ChatUtils.chat(baseCommandConfig.get().getString(WARNING_NO_PERMISSION_KEY)));
                    return true;
                }
                if (!commandMap.get(subCommand).onCommand(sender, args)) {
                    sender.sendMessage(ChatUtils.chat("&4" + commandMap.get(subCommand).getUsageMessage()));
                }
                return true;
            }
        }
        sender.sendMessage(ChatUtils.chat(baseCommandConfig.get().getString(WARNING_INVALID_COMMAND_KEY)));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command cmd, @NotNull String name, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(commandMap.keySet());
        } else if (args.length > 1) {
            for (String arg : commandMap.keySet()) {
                if (args[0].equalsIgnoreCase(arg)) {
                    return commandMap.get(arg).onTabComplete(sender, args);
                }
            }
        }
        return null;
    }
}
