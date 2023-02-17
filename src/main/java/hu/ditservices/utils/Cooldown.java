package hu.ditservices.utils;

import hu.ditservices.STPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Cooldown {
    STPlugin plugin;
    FileConfiguration config;

    Map<String, Long> cooldowns = new HashMap<>();
    private int delay = 2; //seconds
    public long time_left;

    public Cooldown(STPlugin instance){
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
                        return this.time_left == 0;
                    }
                    return true;
                }
            } else {
                return true;
            }
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
