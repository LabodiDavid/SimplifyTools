package hu.ditservices.commands;

import hu.ditservices.STPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class SaveCmd {
    public static boolean Run(CommandSender sender){
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
