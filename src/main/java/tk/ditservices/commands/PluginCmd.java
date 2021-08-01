package tk.ditservices.commands;

import tk.ditservices.DITSystem;
import tk.ditservices.DitPluginManager;
import org.bukkit.command.CommandSender;


public class PluginCmd {
    public static boolean handleLoad(CommandSender sender, String[] args){
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
    }
}
