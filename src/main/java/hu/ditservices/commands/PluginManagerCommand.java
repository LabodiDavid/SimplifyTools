package hu.ditservices.commands;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import hu.ditservices.STPlugin;
//import hu.ditservices.DitPluginManager;
import org.bukkit.command.CommandSender;


public class PluginManagerCommand {
    public static boolean LoadPlugin(CommandSender sender, String[] args){
        Plugin plugin = Bukkit.getPluginManager().getPlugin(args[2]);
        if (plugin == null) {
            sender.sendMessage(STPlugin.getInstance().getPrefix()+"Plugin not exist!");
            return false;
        }
        if (plugin.isEnabled()){
            sender.sendMessage(STPlugin.getInstance().getPrefix()+"Plugin "+plugin.getName()+" already enabled!");
            return true;
        }

        Bukkit.getPluginManager().enablePlugin(plugin);
        sender.sendMessage(STPlugin.getInstance().getPrefix()+"Plugin "+plugin.getName()+" successfully enabled!");
        return true;
    }
    public static boolean UnloadPlugin(CommandSender sender, String[] args){
        Plugin plugin = Bukkit.getPluginManager().getPlugin(args[2]);
        if (plugin == null) {
            sender.sendMessage(STPlugin.getInstance().getPrefix()+"Plugin not exist!");
            return false;
        }
        if (!plugin.isEnabled()){
            sender.sendMessage(STPlugin.getInstance().getPrefix()+"Plugin "+plugin.getName()+" already disabled!");
            return true;
        }
        Bukkit.getPluginManager().disablePlugin(plugin);
        sender.sendMessage(STPlugin.getInstance().getPrefix()+"Plugin "+plugin.getName()+" successfully disabled!");
        return true;
    }

    /*public static boolean handleLoad(CommandSender sender, String[] args){
        switch (DitPluginManager.load(args[2])){
            case 0: sender.sendMessage(DITSystem.getInstance().getPrefix()+" Plugin loaded."); return true;
            case 2: sender.sendMessage(DITSystem.getInstance().getPrefix()+"Missing dependency!"); return true;
            case -1: sender.sendMessage(DITSystem.getInstance().getPrefix()+"Invalid/ not exist plugin!"); return true;
            case 3: sender.sendMessage(DITSystem.getInstance().getPrefix()+"Invalid description!"); return true;
            case 5: sender.sendMessage(DITSystem.getInstance().getPrefix()+"This plugin is already running!"); return true;
            default: break;
        }
        return false;
    }
    public static boolean handleUnload(CommandSender sender, String[] args){
        switch (DitPluginManager.unload(args[2])){
            case 0: sender.sendMessage(DITSystem.getInstance().getPrefix()+" Plugin disabled."); return true;
            case -1: sender.sendMessage(DITSystem.getInstance().getPrefix()+"This plugin is not exist."); return true;
            case 1: sender.sendMessage(DITSystem.getInstance().getPrefix()+"Plugin and their dependents disabled."); return true;
            case 5: sender.sendMessage(DITSystem.getInstance().getPrefix()+"This plugin is not loaded."); return true;
            default: break;
        }
        return false;
    }*/
}
