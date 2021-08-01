package tk.ditservices.commands;

import tk.ditservices.DITSystem;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class PingCmd {
    public static boolean Run(CommandSender sender) {
        DITSystem plugin = DITSystem.getInstance();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            try {
                Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
                int ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
                player.sendMessage(plugin.getPrefix() + "Your response time to the server: " + ping + " ms");
                return true;
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | NoSuchFieldException | InvocationTargetException e) {
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
