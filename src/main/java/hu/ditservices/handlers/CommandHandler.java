package hu.ditservices.handlers;

import hu.ditservices.commands.*;
import hu.ditservices.utils.TPS;
import hu.ditservices.STPlugin;
import hu.ditservices.utils.Cooldown;
import hu.ditservices.utils.Version;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
    private final String noArgMsg;
    private final STPlugin plugin;
    private final Cooldown cd;
    private final FileConfiguration config;

    public CommandHandler(final STPlugin instance) {
        this.plugin = instance;
        this.noArgMsg = plugin.getPrefix() + ChatColor.DARK_RED + plugin.getTranslatedText("list.help");
        this.cd = new Cooldown(plugin);
        this.config = plugin.getConfig();
    }

    public boolean addToCoolDown(CommandSender sender) {
        if (sender instanceof Player) {
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

                if (command.getName().equals("st")) {
                    this.addToCoolDown(sender);
                }

                if (command.getName().equals("st") && (args.length == 0 || args[0].contains("help")))
                {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN+plugin.getTranslatedText("version.pre.text") + plugin.getDescription().getVersion());
                    sender.sendMessage(this.noArgMsg);
                    return true;
                }

                if (command.getName().equals("st") && args[0].contains("settings"))
                {
                        return SettingsCommand.Run(sender);
                }

                if (command.getName().equals("st") && args[0].contains("reload") && sender.hasPermission("st.reload")){

                    if(plugin.reload()){
                        sender.sendMessage(plugin.getPrefix()+ChatColor.GREEN+"Successfully reload!" + "\n"
                        + plugin.getPrefix() + ChatColor.RED + plugin.getTranslatedText("notice.settings.not.applied"));
                        return true;
                    }
                }

                if (command.getName().equals("st") && args[0].contains("tps") && sender.hasPermission("st.tps")) {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN+plugin.getTranslatedText("plugin.calculated.tps")+TPS.getColor()+String.format("%.2f", TPS.getTPS()));
                        return true;

                }

                if (command.getName().equalsIgnoreCase("st") && args[0].contains("pmanager")) {
                    if (sender.hasPermission("st.pmanager.unload") || sender.hasPermission("st.pmanager.load") || sender.hasPermission("st.pmanager")) {
                        if (args.length==1){
                            sender.sendMessage(plugin.getPrefix() + ChatColor.DARK_RED+plugin.getTranslatedText("invalid.arguments"));
                            return true;
                        }
                        if (config.getBoolean("PluginManager.enabled")) {
                            if (args[1].equalsIgnoreCase("load")) {
                                //PluginCmd.handleLoad(sender,args);
                                PluginManagerCommand.LoadPlugin(sender,args);
                            }
                            if (args[1].equalsIgnoreCase("unload")) {
                                //PluginCmd.handleUnload(sender,args);
                                PluginManagerCommand.UnloadPlugin(sender, args);
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.DARK_RED + plugin.getTranslatedText("pmanager.disabled"));
                            return true;
                        }
                    }
                }


                if (command.getName().equalsIgnoreCase("st") && args[0].contains("save-all") && sender.hasPermission("st.save")) {
                    return SaveCommand.Run();
                }

                if (command.getName().equalsIgnoreCase("st") && args[0].contains("ping") &&  sender.hasPermission("st.ping")) {
                    return PingCommand.Run(sender);
                }
                if (command.getName().equalsIgnoreCase("st") && args[0].contains("stats") && sender.hasPermission("st.stats")) {
                    return StatCommand.Run(sender);
                }
            }else{
                cd.CDText(sender);
            }
        }
        return true;
    }


}
