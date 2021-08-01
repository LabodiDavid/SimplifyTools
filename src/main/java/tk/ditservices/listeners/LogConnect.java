package tk.ditservices.listeners;

import tk.ditservices.DITLog;
import tk.ditservices.DITSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LogConnect implements Listener {
    DITSystem plugin;
    private String JoinMsg;
    private String LeaveMsg;
    FileConfiguration config;
    public LogConnect(DITSystem instance){
        this.plugin = instance;
        this.config = plugin.config;
        if (config.getBoolean("CustomMsg.enabled")){
            this.JoinMsg = this.config.isSet("CustomMsg.connect") ? this.config.getString("CustomMsg.connect") : "{PREFIX}{NAME} &aconnected to the server.";
            this.LeaveMsg = this.config.isSet("CustomMsg.connect") ? this.config.getString("CustomMsg.disconnect") : "{PREFIX}{NAME} leaved the game.";
        }

    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (config.getBoolean("CustomMsg.enabled")){
            event.setJoinMessage(null);
            String emsg = this.JoinMsg;
            emsg=emsg.replace("{NAME}",player.getName());
            emsg=emsg.replace("{PREFIX}",plugin.getPrefix());
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',emsg));
        }

        if (config.getBoolean("Log.Connect")) {
            String format = config.isSet("Log.FormatConnect") ? config.getString("Log.FormatConnect") : "[{DATE}] - {TIME} - {ACTION} - {PLAYERNAME} UUID: {UUID}";
            String LogMsg = format.replace("{DATE}",DITLog.getDate()).replace("{TIME}",DITLog.getTime()).replace("{PLAYERNAME}",player.getDisplayName()).replace("{UUID}",player.getUniqueId().toString()).replace("{ACTION}","[CONNECT]");
            DITLog.logLine(ChatColor.stripColor(LogMsg));
        }
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if (config.getBoolean("CustomMsg.enabled")){
            event.setQuitMessage(null);
            String emsg = this.LeaveMsg;
            emsg=emsg.replace("{NAME}",player.getName());
            emsg=emsg.replace("{PREFIX}",plugin.getPrefix());
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',emsg));
        }
        if (config.getBoolean("Log.Connect")) {
            String format = config.isSet("Log.FormatConnect") ? config.getString("Log.FormatConnect") : "[{DATE}] - {TIME} - {ACTION} - {PLAYERNAME} UUID: {UUID}";
            String LogMsg = format.replace("{DATE}",DITLog.getDate()).replace("{TIME}",DITLog.getTime()).replace("{PLAYERNAME}",player.getDisplayName()).replace("{UUID}",player.getUniqueId().toString()).replace("{ACTION}","[DISCONNECT]");
            DITLog.logLine(ChatColor.stripColor(LogMsg));
        }
        if (config.getBoolean("Saving.enabled")){
            if(config.getBoolean("Saving.onDisconnect")){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "st save-all");
            }
        }

    }
}
