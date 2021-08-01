package tk.ditservices.utils;

import tk.ditservices.DITSystem;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Cooldown {
    private int delay = 2; //seconds
    DITSystem plugin;
    Map<String, Long> cooldowns = new HashMap<>();
    public long time_left;
    FileConfiguration config;
    public Cooldown(DITSystem instance){
        this.plugin = instance;
        this.config = plugin.config;
        if (config.isSet("Cooldown.seconds")){
            int cd = this.delay;
            if (cd!=config.getInt("Cooldown.seconds")){
                this.delay = config.getInt("Cooldown.seconds");
            }
        }
    }
    /**
     * Checks if the command sender has a cooldown in time.
     * @param sender The CommandSender object.
     */
    public boolean Check(CommandSender sender){
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (config.getBoolean("Cooldown.enabled")) {
                if (this.cooldowns.containsKey(player.getName())) {
                    if (this.cooldowns.get(player.getName()) > System.currentTimeMillis()) {
                        this.time_left = (this.cooldowns.get(player.getName()) - System.currentTimeMillis()) / 1000;
                        if (this.time_left == 0) {
                            return true;
                        }
                        return false;
                    }
                    return true;
                }
            } else{
                return true;
            }
        }
        if (sender instanceof ConsoleCommandSender){
            return true;
        }

        return true;
    }
    public void Add(Player player){
        if (config.getBoolean("Cooldown.enabled")) {
                if (this.cooldowns.containsKey(player.getName())) {
                    this.cooldowns.remove(player.getName());
                }
                this.cooldowns.put(player.getName(), System.currentTimeMillis() + (this.delay * 1000L));
            }
        }
    public void CDText(CommandSender sender){
        String msg = config.getString("Cooldown.Msg");
        msg = msg.replace("{PREFIX}", plugin.getPrefix());
        msg = msg.replace("{SECONDS}",String.valueOf(this.time_left));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',msg));
    }

}
