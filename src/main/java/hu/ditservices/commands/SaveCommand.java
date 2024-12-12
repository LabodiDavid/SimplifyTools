package hu.ditservices.commands;

import hu.ditservices.STPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class SaveCommand {
    /**
     * Saves all worlds and players data on the main thread.
     *
     * @return boolean
     */
    public static boolean Run() {
        STPlugin plugin = STPlugin.getInstance();
        String prefix = plugin.getPrefix();
        String progressMsg = plugin.getConfig().getString("Saving.broadcastMsgProgress").replace("{PREFIX}", prefix);
        String doneMsg = plugin.getConfig().getString("Saving.broadcastMsgDone").replace("{PREFIX}", prefix);

        // Notify players of save progress
        Bukkit.broadcast(progressMsg, "st.st");

        // Schedule task to run on the main thread
        Bukkit.getScheduler().runTask(plugin, () -> {
            // Save all worlds
            for (World world : Bukkit.getServer().getWorlds()) {
                world.save(); // Runs on main thread
            }

            // Save player data
            Bukkit.savePlayers(); // Also runs on main thread

            // Notify players of save completion
            Bukkit.broadcast(doneMsg, "st.st");
        });

        return true;
    }
}
