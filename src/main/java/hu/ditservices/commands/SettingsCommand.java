package hu.ditservices.commands;

import hu.ditservices.STPlugin;
import hu.ditservices.utils.Version;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SettingsCommand {
    public static boolean Run(CommandSender sender) {
        STPlugin plugin = STPlugin.getInstance();

        sender.sendMessage( ChatColor.GREEN+" === Plugin Information === " + "\n"
                + ChatColor.GREEN+"Plugin Version: " + ChatColor.translateAlternateColorCodes('&',"&a[&fSimplify&7Tools&2] &4- &f") + plugin.getDescription().getVersion() + "\n"
                + ChatColor.GREEN+"Server Version: " + Version.ServerVersion.getCurrent().toString() + "\n"
                + ChatColor.GREEN+" -------- Features -------- " + "\n"
                + ChatColor.GREEN+"Tab customization: "+(plugin.getConfig().getBoolean("Tab.enabled") ? ChatColor.GREEN + "Enabled" : ChatColor.RED+"Disabled") + "\n"
                + ChatColor.GREEN+"Custom Advancement Msg: " + (plugin.getConfig().getBoolean("CustomAdvancement.enabled") ? ChatColor.GREEN+"Enabled" : ChatColor.RED+"Disabled") + "\n"
                + ChatColor.GREEN+" ========================== "
        );

        return true;
    }
}
