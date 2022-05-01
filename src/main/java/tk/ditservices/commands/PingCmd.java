package tk.ditservices.commands;

import tk.ditservices.DITSystem;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class PingCmd {
    public static boolean Run(CommandSender sender) {
        DITSystem plugin = DITSystem.getInstance();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            try {
                player.sendMessage(plugin.getPrefix() + "Your response time to the server: " + player.getPing() + " ms");
                return true;
            } catch (IllegalArgumentException | SecurityException e) {
                e.printStackTrace();
            }
        } else {
            if (sender instanceof ConsoleCommandSender) {
                ConsoleCommandSender konzoladmin = (ConsoleCommandSender) sender;
                konzoladmin.sendMessage(plugin.getPrefix() + "For this command you have to be a player!");
                return true;
            }
        }
        return false;
    }
}
