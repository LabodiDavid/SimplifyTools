package hu.ditservices.utils;

import hu.ditservices.STPlugin;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerPasswordData {
    private STPlugin plugin;
    public ServerPasswordData(STPlugin instance) {
        this.plugin = instance;
    }

    // Maps to store inventory, armor, and authentication status
    private final Map<UUID, ItemStack[]> inventoryMap = new HashMap<>();
    private final Map<UUID, ItemStack[]> armorMap = new HashMap<>();
    private final Map<UUID, Boolean> authenticatedPlayers = new HashMap<>();

    // Getter for the inventory map
    public Map<UUID, ItemStack[]> getInventoryMap() {
        return inventoryMap;
    }

    // Getter for the armor map
    public Map<UUID, ItemStack[]> getArmorMap() {
        return armorMap;
    }

    // Getter for the authenticated players map
    public Map<UUID, Boolean> getAuthenticatedPlayers() {
        return authenticatedPlayers;
    }

    public String getServerPassword() {
        return this.plugin.getConfig().getString("ServerPassword.password");
    }
}

