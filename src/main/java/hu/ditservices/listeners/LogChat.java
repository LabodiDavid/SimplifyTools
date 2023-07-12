package hu.ditservices.listeners;

import hu.ditservices.utils.Logger;
import hu.ditservices.STPlugin;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class LogChat implements Listener {
    STPlugin plugin;
    FileConfiguration config;
    public LogChat(STPlugin instance){
        this.plugin = instance;
        this.config = plugin.getConfig();
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event){
        if (config.getBoolean("Log.Chat")) {
            Player player = event.getPlayer();
            String format = config.isSet("Log.FormatChat") ? config.getString("Log.FormatChat") : "[{DATE}] - {TIME} - {ACTION} - {PLAYERNAME}: {MSG}";
            String LogMsg = format.replace("{DATE}", Logger.getDate()).replace("{TIME}", Logger.getTime()).replace("{PLAYERNAME}",player.getDisplayName()).replace("{MSG}",event.getMessage()).replace("{ACTION}","[CHAT]");
            Logger.logLine(ChatColor.stripColor(LogMsg));
        }
    }
}
