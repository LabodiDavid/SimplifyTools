package hu.ditservices.commands;

import hu.ditservices.utils.TPS;
import hu.ditservices.STPlugin;
import hu.ditservices.utils.Cooldown;
import hu.ditservices.utils.Version;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class DitCmd implements CommandExecutor {
    private final String noArgMsg;
    private final STPlugin plugin;
    private final Cooldown cd;
    private final FileConfiguration config;

    public DitCmd(final STPlugin instance){
        this.plugin = instance;
        this.noArgMsg = plugin.getPrefix()+ ChatColor.DARK_RED + "To list all SimplifyTools commands use the '/help SIMPLIFYTOOLS' command!";
        this.cd = new Cooldown(plugin);
        this.config = plugin.config;
    }

    public boolean addToCoolDown(CommandSender sender){
        if (sender instanceof Player){
            Player p = (Player) sender;
            this.cd.Add(p);
            return true;
        }
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("st")){

            if (cd.Check(sender)){
                if (command.getName().equals("st") && args.length==0)
                {
                    this.addToCoolDown(sender);
                    sender.sendMessage(plugin.getPrefix()+ChatColor.GREEN+"Version: "+plugin.getDescription().getVersion());
                    sender.sendMessage(this.noArgMsg);
                    return true;
                }
                if (command.getName().equals("st") && args[0].contains("help"))
                {
                    sender.sendMessage(plugin.getPrefix()+ChatColor.GREEN+"Version: "+plugin.getDescription().getVersion());
                    sender.sendMessage(this.noArgMsg);
                    return true;
                }
                if (command.getName().equals("st") && args[0].contains("settings"))
                {
                        this.addToCoolDown(sender);
                        sender.sendMessage(plugin.getPrefix()+ ChatColor.GREEN+" === Plugin Information === ");
                        sender.sendMessage(plugin.getPrefix()+ChatColor.GREEN+"Plugin Version: "+plugin.getDescription().getVersion());
                        sender.sendMessage(plugin.getPrefix()+ChatColor.GREEN+"Server Version: "+ Version.ServerVersion.getCurrent().toString());
                        sender.sendMessage(ChatColor.GREEN+" -------- Features -------- ");
                        sender.sendMessage(plugin.getPrefix()+ChatColor.GREEN+"Tab customization: "+(config.getBoolean("Tab.enabled") ? ChatColor.GREEN+"Enabled" : ChatColor.RED+"Disabled"));
                        sender.sendMessage(plugin.getPrefix()+ChatColor.GREEN+"Custom Advancement Msg: "+(config.getBoolean("CustomAdvancement.enabled") ? ChatColor.GREEN+"Enabled" : ChatColor.RED+"Disabled"));
                        sender.sendMessage(ChatColor.GREEN+" ========================== ");
                        return true;
                }

                if (command.getName().equals("st") && args[0].contains("reload") && sender.hasPermission("st.reload")){

                    if(plugin.Reload()){
                        this.addToCoolDown(sender);
                        sender.sendMessage(plugin.getPrefix()+ChatColor.GREEN+"Successfully reload!");
                        sender.sendMessage(plugin.getPrefix()+ChatColor.RED+"Notice: Restart your server if the settings didn't applied.");
                        return true;
                    }
                }

                if (command.getName().equals("st") && args[0].contains("tps") && sender.hasPermission("st.tps")){
                        this.addToCoolDown(sender);
                        sender.sendMessage(plugin.getPrefix()+ChatColor.GREEN+"Plugin Calculated TPS: "+TPS.getColor()+String.format("%.2f", TPS.getTPS()));
                        return true;

                }

                if (command.getName().equalsIgnoreCase("st") && args[0].contains("pmanager")){

                    if (sender.hasPermission("st.pmanager.unload") || sender.hasPermission("st.pmanager.load") || sender.hasPermission("st.pmanager")) {
                        this.addToCoolDown(sender);
                        if (args.length==1){
                            sender.sendMessage(plugin.getPrefix()+ChatColor.DARK_RED+"Invalid arguments!");
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
                    this.addToCoolDown(sender);
                    return SaveCmd.Run(sender);
                }

                if (command.getName().equalsIgnoreCase("st") && args[0].contains("ping") &&  sender.hasPermission("st.ping")){
                    this.addToCoolDown(sender);
                    return PingCmd.Run(sender);
                }
                if (command.getName().equalsIgnoreCase("st") && args[0].contains("stats") && sender.hasPermission("st.stats")){
                    this.addToCoolDown(sender);
                    return StatCmd.Run(sender);
                }
            }else{
                cd.CDText(sender);
            }
        }
        return true;
    }


}
