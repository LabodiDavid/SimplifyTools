package hu.ditservices.handlers;

import hu.ditservices.STPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class DITTabCompleter implements TabCompleter {

    List<String> arguments = new ArrayList<String>();

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {

        if (command.getName().equals("st")) {

            if (arguments.isEmpty()){
                String curr_cmd = "";
                for (Plugin plug : Bukkit.getPluginManager().getPlugins()) {
                    List<Command> cmdList = PluginCommandYamlParser.parse(plug);
                    for (int i = 0; i <= cmdList.size() - 1; i++) {
                        if (cmdList.get(i).getLabel().contains("st ")) {
                            curr_cmd =cmdList.get(i).getLabel();
                            curr_cmd = curr_cmd.replace("st ","");
                            arguments.add(curr_cmd);
                        }
                    }
                }
            }
            List<String> idea = new ArrayList<>();
            List<String> result = new ArrayList<>();
            String curr_cmd = "";
            List<String> vizsg_list = new ArrayList<>();
            boolean vizsg_2 = false;
            if (args.length == 1) {
                for (String a : arguments){
                    if (a.toLowerCase().startsWith(args[0].toLowerCase())){
                        result.add(a);
                    }
                }
                return result;
            }
            if (args.length >= 2) {
                for (String a : arguments) {
                    vizsg_2 = false;
                    if (a.contains(" ")){
                        vizsg_list = Arrays.asList(a.split(" "));
                        vizsg_2 = true;
                    }
                    if (vizsg_2 && args.length ==2){ // length-1
                        idea.add(vizsg_list.get(1));
                    }
                    if (vizsg_2&& args[1].startsWith(vizsg_list.get(1))) {
                        idea.add(vizsg_list.get(1));
                    }
                }

                if (args[0].equalsIgnoreCase("pmanager") && args[1].equalsIgnoreCase("unload") || args[1].equalsIgnoreCase("load")) {
                    if ((args.length == 3) && STPlugin.getInstance().getConfig().getBoolean("PluginManager.enabled") &
                            (commandSender.hasPermission("st.pmanager")
                                    || commandSender.hasPermission("st.pmanager.load")
                                    || commandSender.hasPermission("st.pmanager.unload"))
                    ) {
                        result.clear();
                        PluginManager pm = Bukkit.getServer().getPluginManager();
                        for (Plugin pl : pm.getPlugins()) {
                            if (pl.getName().toLowerCase().startsWith(args[2].toLowerCase()) && args[1].equalsIgnoreCase("unload")) {
                                result.add(pl.getName());
                            }
                            if (!pl.isEnabled()){
                                if (pl.getName().toLowerCase().startsWith(args[2].toLowerCase()) && args[1].equalsIgnoreCase("load")) {
                                    result.add(pl.getName());
                                }
                            }
                        }
                        return result;
                    }
                }
                return idea;
            }
        }
        return null;
    }
}