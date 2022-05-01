package tk.ditservices.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import tk.ditservices.DITSystem;
import tk.ditservices.utils.Cooldown;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class DitCmd implements CommandExecutor {
    String noArgMsg;
    DITSystem plugin;
    Cooldown cd;
    FileConfiguration config;
    public DitCmd(DITSystem instance){
        this.plugin = instance;
        this.noArgMsg = plugin.getPrefix()+ ChatColor.DARK_RED +"To list all SimplifyTools commands use the '/help SIMPLIFYTOOLS' command!";
        this.cd = new Cooldown(plugin);
        this.config = plugin.config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("st")){

            if (cd.Check(sender)){
                if (command.getName().equals("st") && args.length==0)
                {
                    if (sender instanceof Player)
                    {
                        Player p = (Player) sender;
                        p.sendMessage(plugin.getPrefix()+ChatColor.GREEN+"Version: "+plugin.getDescription().getVersion());
                        p.sendMessage(this.noArgMsg);
                        cd.Add(p);
                        return true;
                    }else {
                        if (sender instanceof ConsoleCommandSender){
                            sender.sendMessage(this.noArgMsg);
                            return true;
                        }
                    }
                }
                if (command.getName().equals("st") && args[0].contains("help"))
                {
                    if (sender instanceof Player)
                    {
                        Player p = (Player) sender;
                        cd.Add(p);
                        p.sendMessage(plugin.getPrefix()+ChatColor.GREEN+"Version: "+plugin.getDescription().getVersion());
                        p.sendMessage(this.noArgMsg);
                        return true;
                    }else{
                        if (sender instanceof ConsoleCommandSender) {
                            ConsoleCommandSender consoleadmin = (ConsoleCommandSender) sender;
                            consoleadmin.sendMessage(this.noArgMsg);
                            return true;
                        }
                    }
                }

                if (command.getName().equals("st") && args[0].contains("reload") && sender.hasPermission("st.reload")){

                    if(plugin.Reload()){
                        if (sender instanceof Player){
                            Player p = (Player) sender;
                            cd.Add(p);
                        }
                        sender.sendMessage(plugin.getPrefix()+ChatColor.GREEN+"Successfully reload!");
                        sender.sendMessage(plugin.getPrefix()+ChatColor.RED+"Notice: Restart your server if the settings didn't applied.");
                    }
                }

                if (command.getName().equalsIgnoreCase("st") && args[0].contains("pmanager")){

                    if (sender.hasPermission("st.pmanager.unload") || sender.hasPermission("st.pmanager.load") || sender.hasPermission("st.pmanager")) {
                        if (sender instanceof Player){
                            Player p = (Player) sender;
                            cd.Add(p);
                        }
                        if (args.length==1){
                            String msg = plugin.getPrefix()+ChatColor.DARK_RED+"Invalid command!";
                            if (sender instanceof Player){
                                Player p = (Player) sender;
                                p.sendMessage(msg);
                            }else{
                                ConsoleCommandSender c = (ConsoleCommandSender) sender;
                                c.sendMessage(msg);
                            }
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("load")) {
                            //PluginCmd.handleLoad(sender,args);
                            PluginCmd.LoadPlugin(sender,args);
                        }
                        if (args[1].equalsIgnoreCase("unload")) {
                            //PluginCmd.handleUnload(sender,args);
                            PluginCmd.UnloadPlugin(sender, args);
                        }
                    }
                }


                if (command.getName().equalsIgnoreCase("st") && args[0].contains("save-all") && sender.hasPermission("st.save")){
                    if (sender instanceof Player){
                        Player p = (Player) sender;
                        cd.Add(p);
                    }
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

                if (command.getName().equalsIgnoreCase("st") && args[0].contains("ping") &&  sender.hasPermission("st.ping")){
                    if (sender instanceof Player){
                        Player p = (Player) sender;
                        cd.Add(p);
                    } return PingCmd.Run(sender);
                }
                if (command.getName().equalsIgnoreCase("st") && args[0].contains("stats") && sender.hasPermission("st.stats")){
                    if (sender instanceof Player){
                        Player p = (Player) sender;
                        cd.Add(p);
                    } return StatCmd.Run(sender);
                }
            }else{
                cd.CDText(sender);
            }
        }
        return true;
    }


}
