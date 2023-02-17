package hu.ditservices.handlers;

import hu.ditservices.STPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SaveHandler implements Runnable{
    STPlugin plugin = STPlugin.getInstance();
    @Override
    public void run() {

        String p = plugin.config.isSet("Saving.broadcastMsgProgress") ? plugin.config.getString("Saving.broadcastMsgProgress").replace("{PREFIX}",plugin.getPrefix()) : plugin.getPrefix()+"Auto save in progress..";
        String d = plugin.config.isSet("Saving.broadcastMsgDone") ? plugin.config.getString("Saving.broadcastMsgDone").replace("{PREFIX}",plugin.getPrefix()) : plugin.getPrefix()+"Auto save done.";
        if (Bukkit.getOnlinePlayers().size()>0){
            for (Player player : Bukkit.getOnlinePlayers()){
                player.sendMessage(p);
            }
        }
        plugin.getLogger().info(p);
        for(World w : Bukkit.getServer().getWorlds()){
            w.save();
        }
        Bukkit.savePlayers();
        if (Bukkit.getOnlinePlayers().size()>0){
            for (Player player : Bukkit.getOnlinePlayers()){
                player.sendMessage(d);
            }
        }
        plugin.getLogger().info(d);

    }
}
