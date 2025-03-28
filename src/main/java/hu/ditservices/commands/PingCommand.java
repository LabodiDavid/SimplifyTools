package hu.ditservices.commands;

import hu.ditservices.STPlugin;
import hu.ditservices.utils.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class PingCommand {
    public static boolean Run(CommandSender sender) {
        STPlugin plugin = STPlugin.getInstance();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            try {
                player.sendMessage(plugin.getPrefix() + plugin.getTranslatedText("cmd.ping") + Server.getPlayerPing(player) + " ms");
                return true;
            } catch (IllegalArgumentException | SecurityException e) {
                e.printStackTrace();
            }
        } else {
            if (sender instanceof ConsoleCommandSender) {
                ConsoleCommandSender consoleCommandSender = (ConsoleCommandSender) sender;
                consoleCommandSender.sendMessage(plugin.getPrefix() + "For this command you have to be a player!");
                return true;
            }
        }
        return false;
    }
}
