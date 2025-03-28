package hu.ditservices.listeners;

import hu.ditservices.STPlugin;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ServerPasswordEvents implements Listener {
    private STPlugin plugin;
    private FileConfiguration config;

    public ServerPasswordEvents(STPlugin instance){
        this.plugin = instance;
        this.config = plugin.getConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if ((!config.getBoolean("ServerPassword.rememberUntilRestart"))
                || (!plugin.getServerPasswordData().getAuthenticatedPlayers().getOrDefault(player.getUniqueId(),false))) {
            plugin.getServerPasswordData().getAuthenticatedPlayers().put(player.getUniqueId(),false);
            if (config.getBoolean("ServerPassword.preventInventory")) {
                // Store and clear inventory
                plugin.getServerPasswordData().getInventoryMap().put(player.getUniqueId(), player.getInventory().getContents());
                plugin.getServerPasswordData().getArmorMap().put(player.getUniqueId(), player.getInventory().getArmorContents());
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
            }
            player.sendMessage(ChatColor.RED + plugin.getTranslatedText("serverpassword.enabled"));
            player.sendMessage(plugin.getTranslatedText("serverpassword.note"));
            BukkitRunnable authReminder = new BukkitRunnable() {
                @Override
                public void run() {
                    // Check if the player is authenticated, default to false if not present.
                    if (plugin.getServerPasswordData().getAuthenticatedPlayers().getOrDefault(player.getUniqueId(), false)) {
                        this.cancel();
                        return;
                    }
                    player.sendMessage(ChatColor.RED + plugin.getTranslatedText("serverpassword.login"));
                }
            };
            authReminder.runTaskTimer(plugin, 0L, 100L);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if ((!plugin.getServerPasswordData().getAuthenticatedPlayers().getOrDefault(event.getPlayer().getUniqueId(),false))
        && config.getBoolean("ServerPassword.preventMove")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if ((!plugin.getServerPasswordData().getAuthenticatedPlayers().getOrDefault(event.getPlayer().getUniqueId(),false))
        && config.getBoolean("ServerPassword.preventBuild")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if ((!plugin.getServerPasswordData().getAuthenticatedPlayers().getOrDefault(event.getPlayer().getUniqueId(),false))
                && config.getBoolean("ServerPassword.preventBuild")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (plugin.getServerPasswordData().getAuthenticatedPlayers().getOrDefault(uuid, false)) {
            if (!config.getBoolean("ServerPassword.rememberUntilRestart")) {
                plugin.getServerPasswordData().getAuthenticatedPlayers().remove(uuid);
            }
        } else {
            if (plugin.getConfig().getBoolean("ServerPassword.preventInventory")) {
                if (plugin.getServerPasswordData().getInventoryMap().containsKey(uuid)) {
                    player.getInventory().setContents(plugin.getServerPasswordData().getInventoryMap().get(uuid));
                    plugin.getServerPasswordData().getInventoryMap().remove(uuid);
                }
                if (plugin.getServerPasswordData().getArmorMap().containsKey(uuid)) {
                    player.getInventory().setArmorContents(plugin.getServerPasswordData().getArmorMap().get(uuid));
                    plugin.getServerPasswordData().getArmorMap().remove(uuid);
                }
            }
        }
    }
}
