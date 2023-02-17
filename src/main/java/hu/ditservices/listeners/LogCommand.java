package hu.ditservices.listeners;

import hu.ditservices.utils.Logger;
import hu.ditservices.STPlugin;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class LogCommand implements Listener {
    STPlugin plugin;
    FileConfiguration config;
    public LogCommand(STPlugin instance){

        this.plugin = instance;
        this.config = plugin.config;
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommand(PlayerCommandPreprocessEvent event){
        if (config.getBoolean("Log.Command")) {
            Player player = event.getPlayer();
            String format = config.isSet("Log.FormatCommand") ? config.getString("Log.FormatCommand") : "[{DATE}] - {TIME} - {ACTION} - {PLAYERNAME}: {COMMAND}";
            String LogMsg = format.replace("{DATE}", Logger.getDate()).replace("{TIME}", Logger.getTime()).replace("{PLAYERNAME}",player.getDisplayName()).replace("{COMMAND}",event.getMessage()).replace("{ACTION}","[COMMAND]");
            Logger.logLine(ChatColor.stripColor(LogMsg));
        }
    }
}
