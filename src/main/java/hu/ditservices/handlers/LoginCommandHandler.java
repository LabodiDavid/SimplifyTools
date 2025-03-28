package hu.ditservices.handlers;

import hu.ditservices.STPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LoginCommandHandler implements CommandExecutor {

    private final STPlugin plugin;

    public LoginCommandHandler(STPlugin instance) {
        this.plugin = instance;
    }

    // Method to authenticate the player
    public boolean authenticate(Player player, String password) {
        if (plugin.getServerPasswordData().getServerPassword().equals(password)) {
            if (plugin.getConfig().getBoolean("ServerPassword.preventInventory")) {
                player.getInventory().setContents(plugin.getServerPasswordData().getInventoryMap().get(player.getUniqueId()));
                player.getInventory().setArmorContents(plugin.getServerPasswordData().getArmorMap().get(player.getUniqueId()));
            }
            plugin.getServerPasswordData().getAuthenticatedPlayers().put(player.getUniqueId(), true);
            return true;
        }
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        Player player = (Player) sender;
        UUID pUuid = player.getUniqueId();

        if (args.length != 1) {
            player.sendMessage("Usage: /slogin <PASSWORD>");
            return true;
        }

        if (plugin.getServerPasswordData().getAuthenticatedPlayers().getOrDefault(pUuid,false)) {
            player.sendMessage("You are already authenticated.");
            return true;
        }

        if (this.authenticate(player, args[0])) {
            player.sendMessage("You have been authenticated.");
            plugin.getServerPasswordData().getInventoryMap().remove(pUuid);
            plugin.getServerPasswordData().getArmorMap().remove(pUuid);
        } else {
            player.sendMessage("Incorrect password. Try again.");
        }
        return true;
    }
}

