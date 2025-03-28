package hu.ditservices.commands;

import hu.ditservices.STPlugin;
import hu.ditservices.utils.Version;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SettingsCommand {
    public static boolean Run(CommandSender sender) {
        STPlugin plugin = STPlugin.getInstance();

        String enabled = plugin.getTranslatedText("settings.enabled");
        String disabled = plugin.getTranslatedText("settings.disabled");

        sender.sendMessage( ChatColor.GREEN+" === Plugin Information === " + "\n"
                + ChatColor.GREEN + "Plugin " + plugin.getTranslatedText("version.pre.text") + ChatColor.translateAlternateColorCodes('&',"&a[&fSimplify&7Tools&2] &4- &f") + plugin.getDescription().getVersion() + "\n"
                + ChatColor.GREEN + plugin.getTranslatedText("settings.server") + plugin.getTranslatedText("version.pre.text") + Version.ServerVersion.getCurrent().toString() + "\n"
                + ChatColor.GREEN + " -------- " + plugin.getTranslatedText("settings.features") +" -------- " + "\n"
                + ChatColor.GREEN + plugin.getTranslatedText("settings.feature.tab") + (plugin.getConfig().getBoolean("Tab.enabled") ? ChatColor.GREEN + enabled : ChatColor.RED + disabled) + "\n"
                + ChatColor.GREEN + plugin.getTranslatedText("settings.feature.CustomAdvancementMsg") + (plugin.getConfig().getBoolean("CustomAdvancement.enabled") ? ChatColor.GREEN + enabled : ChatColor.RED + disabled) + "\n"
                + ChatColor.GREEN + plugin.getTranslatedText("settings.feature.AutoSave") + (plugin.getConfig().getBoolean("Saving.enabled") ? ChatColor.GREEN + enabled : ChatColor.RED + disabled) + "\n"
                + ChatColor.GREEN + plugin.getTranslatedText("settings.feature.pmanager") + (plugin.getConfig().getBoolean("PluginManager.enabled") ? ChatColor.GREEN + enabled : ChatColor.RED + disabled) + "\n"
                + ChatColor.GREEN + " ========================== "
        );

        return true;
    }
}
