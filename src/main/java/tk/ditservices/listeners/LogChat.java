package tk.ditservices.listeners;

import tk.ditservices.DITLog;
import tk.ditservices.DITSystem;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class LogChat implements Listener {
    DITSystem plugin;
    FileConfiguration config;
    public LogChat(DITSystem instance){
        this.plugin = instance;
        this.config = plugin.config;
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event){
        if (config.getBoolean("Log.Chat")) {
            Player player = event.getPlayer();
            String format = config.isSet("Log.FormatChat") ? config.getString("Log.FormatChat") : "[{DATE}] - {TIME} - {ACTION} - {PLAYERNAME}: {MSG}";
            String LogMsg = format.replace("{DATE}",DITLog.getDate()).replace("{TIME}",DITLog.getTime()).replace("{PLAYERNAME}",player.getDisplayName()).replace("{MSG}",event.getMessage()).replace("{ACTION}","[CHAT]");
            DITLog.logLine(ChatColor.stripColor(LogMsg));
        }
    }
}
