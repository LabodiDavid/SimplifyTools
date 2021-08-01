package tk.ditservices;
import tk.ditservices.DITSystem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event;
import org.bukkit.plugin.*;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
public class DitPluginManager {
    private static DITSystem plugin;
    public DitPluginManager(DITSystem instance){
        plugin = instance;
    }
    /**
     * Loads the requested plugin.
     * @param pluginName Plugin name in string.
     */
    public static int load(final String pluginName) {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        boolean there = false;
        for (Plugin pl : pm.getPlugins()){
            if (pl.getName().toLowerCase().startsWith(pluginName)){
                there = true;
            }
        }


        if (there)
        { return 1;} //plugin already exists
        else {
            String name = "";
            String path = plugin.getDataFolder().getParent();
            File folder = new File(path);
            ArrayList<File> files = new ArrayList<File>();
            File[] listOfFiles = folder.listFiles();
            for (File compare : listOfFiles) {
                if (compare.isFile()) {
                    try {
                        name = plugin.getPluginLoader().getPluginDescription(compare).getName();
                    } catch (InvalidDescriptionException e) {
                        plugin.getLogger().warning("[Loading Plugin] " + compare.getName() + " didn't match");
                    }
                    if (name.toLowerCase().startsWith(pluginName.toLowerCase())) {
                        files.add(compare);
                        try {
                            Bukkit.getServer().getPluginManager().loadPlugin(compare);
                        } catch (UnknownDependencyException e) {
                            return 2; //missing dependent plugin
                        } catch (InvalidPluginException e) {
                            return -1; //not a plugin
                        } catch (InvalidDescriptionException e) {
                            return 3; //invalid description
                        }
                    }
                }
            }

            Plugin[] plugins = pm.getPlugins();
            for (Plugin pl : plugins) {
                for (File compare : files) {
                    try {
                        if (pl.getName().equalsIgnoreCase(plugin.getPluginLoader().getPluginDescription(compare).getName()))
                            if (!pl.isEnabled()){
                                pm.enablePlugin(pl);
                            }else { return 5; }
                    } catch (InvalidDescriptionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return 0; //success
    }


    @SuppressWarnings("unchecked")
    public static int unload(String pluginName) {
        pluginName = pluginName.toLowerCase().trim();
        PluginManager manager = Bukkit.getServer().getPluginManager();
        SimplePluginManager spm = (SimplePluginManager) manager;
        SimpleCommandMap commandMap = null;
        List<Plugin> plugins = null;
        Map<String, Plugin> lookupNames = null;
        Map<String, Command> knownCommands = null;
        Map<Event, SortedSet<RegisteredListener>> listeners = null;
        boolean reloadlisteners = true;
        try {
            if (spm != null) {
                Field pluginsField = spm.getClass().getDeclaredField("plugins");
                pluginsField.setAccessible(true);
                plugins = (List<Plugin>) pluginsField.get(spm);

                Field lookupNamesField = spm.getClass().getDeclaredField("lookupNames");
                lookupNamesField.setAccessible(true);
                lookupNames = (Map<String, Plugin>) lookupNamesField.get(spm);

                try {
                    Field listenersField = spm.getClass().getDeclaredField("listeners");
                    listenersField.setAccessible(true);
                    listeners = (Map<Event, SortedSet<RegisteredListener>>) listenersField.get(spm);
                } catch (Exception e) {
                    reloadlisteners = false;
                }

                Field commandMapField = spm.getClass().getDeclaredField("commandMap");
                commandMapField.setAccessible(true);
                commandMap = (SimpleCommandMap) commandMapField.get(spm);

                Field knownCommandsField = commandMap.getClass().getDeclaredField("knownCommands");
                knownCommandsField.setAccessible(true);
                knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean in = false;

        for (Plugin pl : Bukkit.getServer().getPluginManager().getPlugins()) {
            if (in)
                break;
            if (pl.getName().toLowerCase().startsWith(pluginName.toLowerCase())) {
                if (pl.isEnabled()) {
                    manager.disablePlugin(pl);
                    if (plugins != null && plugins.contains(pl))
                        plugins.remove(pl);

                    if (lookupNames != null && lookupNames.containsKey(pl.getName())) {
                        lookupNames.remove(pl.getName());
                    }

                    if (listeners != null && reloadlisteners) {
                        for (SortedSet<RegisteredListener> set : listeners.values()) {
                            for (Iterator<RegisteredListener> it = set.iterator(); it.hasNext(); ) {
                                RegisteredListener value = it.next();

                                if (value.getPlugin() == pl) {
                                    it.remove();
                                }
                            }
                        }
                    }

                    if (commandMap != null) {
                        for (Iterator<Map.Entry<String, Command>> it = knownCommands.entrySet().iterator(); it.hasNext(); ) {
                            Map.Entry<String, Command> entry = it.next();
                            if (entry.getValue() instanceof PluginCommand) {
                                PluginCommand c = (PluginCommand) entry.getValue();
                                if (c.getPlugin() == pl) {
                                    c.unregister(commandMap);
                                    it.remove();
                                }
                            }
                        }
                    }
                    for (Plugin plu : Bukkit.getServer().getPluginManager().getPlugins()) {
                        if (plu.getDescription().getDepend() != null) {
                            for (String depend : plu.getDescription().getDepend()) {
                                if (depend.equalsIgnoreCase(pl.getName())) {
                                    plugin.getLogger().info("[Unloading Plugin] " + plu.getName() + " must be disabled!");
                                    unload(plu.getName());
                                    return 1; //dependencies also disabled
                                }
                            }
                        }
                    }
                    in = true;
                } else { return 5; }
            }
        }
        if (!in) {
            plugin.getLogger().info("Not an existing plugin");
            return -1; //non-existent
        }
        System.gc();
        return 0; //success
    }



}
