package hu.ditservices.listeners;

import hu.ditservices.STPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import hu.ditservices.utils.AdvancementHelper;

public class ChatEvents implements Listener {
    private STPlugin plugin;
    private FileConfiguration config;
    public ChatEvents(STPlugin instance){
        this.plugin = instance;
        this.config = plugin.config;
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onAdvance(PlayerAdvancementDoneEvent e){
        if (config.isSet("CustomAdvancement.enabled") && config.getBoolean("CustomAdvancement.enabled")){
            AdvancementHelper helper = new AdvancementHelper(this.config);
                if(helper.check(e.getAdvancement().getKey().getKey())) {
                    final Player player = e.getPlayer();
                    String title = helper.find(e.getAdvancement().getKey().getKey());
                    Bukkit.broadcastMessage(plugin.getPrefix()+ChatColor.translateAlternateColorCodes('&', helper.getText(player.getName(),title)));
                }
        }

    }
}
