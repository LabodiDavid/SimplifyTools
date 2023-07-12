package hu.ditservices.commands;

import hu.ditservices.STPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class SaveCommand {
    /**
     * Saves all worlds and players data.
     *
     * @return boolean
     */
    public static boolean Run(){
        STPlugin plugin = STPlugin.getInstance();
        String p = plugin.config.getString("Saving.broadcastMsgProgress").replace("{PREFIX}",plugin.getPrefix());
        String d = plugin.config.getString("Saving.broadcastMsgDone").replace("{PREFIX}",plugin.getPrefix());
        Bukkit.broadcast(p,"st.st");
        for(World w : Bukkit.getServer().getWorlds()){
            w.save();
        }
        Bukkit.savePlayers();
        Bukkit.broadcast(d,"st.st");
        return true;
    }
}
