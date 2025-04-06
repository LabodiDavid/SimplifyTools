package hu.ditservices.handlers;

import hu.ditservices.STPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ClearDropItemsTask extends BukkitRunnable {
    private final STPlugin plugin;
    private int countdown;
    private final int interval; // In seconds
    private final List<String> exceptItemList;
    private final List<String> exceptWorldList;
    private final boolean shouldBroadcast;

    public ClearDropItemsTask(STPlugin plugin) {
        this.plugin = plugin;
        this.interval = plugin.getConfig().getInt("ClearDropItems.interval", 300);
        this.countdown = this.interval;
        // Get the exception list from the config (should be a list of item type names, e.g., ["DIAMOND", "GOLD_INGOT"])
        this.exceptItemList = plugin.getConfig().getStringList("ClearDropItems.except");
        this.exceptWorldList = plugin.getConfig().getStringList("ClearDropItems.skipWorlds");
        this.shouldBroadcast = plugin.getConfig().getBoolean("ClearDropItems.broadcastMsg", true);
    }

    @Override
    public void run() {
        if (this.shouldBroadcast) {
            if (countdown == 60) {
                Bukkit.broadcastMessage(plugin.getPrefix() + ChatColor.RED + plugin.getTranslatedText("cleardropitems.oneMin"));
            }

            if (countdown == 10) {
                Bukkit.broadcastMessage(plugin.getPrefix() + ChatColor.RED + plugin.getTranslatedText("cleardropitems.tenSec"));
            }
        }


        if (countdown <= 0) {
            int removed = 0;

            for (World world : Bukkit.getWorlds()) {
                if (this.exceptWorldList.contains(world.getName())) {
                    continue;
                }
                for (Entity entity : world.getEntities()) {

                    if (entity instanceof Item) {
                        Item item = (Item) entity;
                        String itemType = item.getItemStack().getType().toString();
                        // Check if the item is in the exception list
                        boolean isException = this.exceptItemList.stream()
                                .anyMatch(ex -> ex.equalsIgnoreCase(itemType));
                        if (!isException) {
                            item.remove();
                            removed++;
                        }
                    }
                }
            }
            if (this.shouldBroadcast) {
                Bukkit.broadcastMessage(plugin.getPrefix() + ChatColor.GREEN + plugin.getTranslatedText("cleardropitems.cleared").replace("%REMOVECOUNT%",String.valueOf(removed)));
            }

            // Reset the countdown to the interval for the next cycle
            countdown = interval;
        } else {
            countdown--;
        }
    }
}
