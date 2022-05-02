package tk.ditservices;

import org.bstats.bukkit.Metrics;
import org.bukkit.scheduler.BukkitScheduler;
import tk.ditservices.commands.DitCmd;
import tk.ditservices.listeners.ChatEvents;
import tk.ditservices.listeners.LogChat;
import tk.ditservices.listeners.LogCommand;
import tk.ditservices.listeners.LogConnect;
import com.tchristofferson.configupdater.ConfigUpdater;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import tk.ditservices.utils.Math;

import com.jeff_media.updatechecker.UpdateCheckSource;
import com.jeff_media.updatechecker.UpdateChecker;
import com.jeff_media.updatechecker.UserAgentBuilder;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public final class DITSystem extends JavaPlugin implements CommandExecutor, Listener {
    private static DITSystem instance;
    Logger log = Bukkit.getLogger();
    //private DitPluginManager dplug;
    public TabManager tab;

    public FileConfiguration config;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        this.DIT_initialize();
        this.tab = new TabManager(this,this.config);
        //this.dplug = new DitPluginManager(this);
        System.out.println(this.getPrefix()+"Started running.");

        new UpdateChecker(this, UpdateCheckSource.GITHUB_RELEASE_TAG, "LabodiDavid/SimplifyTools")
                .setDownloadLink("https://github.com/LabodiDavid/SimplifyTools/releases")
                .setDonationLink("https://paypal.me/labodidavid")
                .setChangelogLink("https://github.com/LabodiDavid/SimplifyTools/blob/main/docs/ChangeLog.md")
                .setNotifyOpsOnJoin(true)
                .setNotifyByPermissionOnJoin("st.admin")
                .setUserAgent(new UserAgentBuilder().addPluginNameAndVersion())
                .checkEveryXHours(24)
                .checkNow();
        Metrics metrics = new Metrics(this, 15108);
    }

    private void DIT_initialize() {
        if  (this.Reload()){
            initOptions();

            PluginCommand ditCmd = this.getCommand("st");
            ditCmd.setExecutor(new DitCmd(this));
            ditCmd.setTabCompleter(new DITTabCompleter());

            getServer().getPluginManager().registerEvents(this, this);
            getServer().getPluginManager().registerEvents(new LogChat(this), this);
            getServer().getPluginManager().registerEvents(new LogCommand(this), this);
            getServer().getPluginManager().registerEvents(new LogConnect(this), this);
            getServer().getPluginManager().registerEvents(new ChatEvents(this), this);

        }
    }
    public String getPrefix(){
        if (this.config.isSet("Prefix")){
            return ChatColor.translateAlternateColorCodes('&', this.config.getString("Prefix"));
        }else{
            return ChatColor.translateAlternateColorCodes('&',"&a[&fSimplify&7Tools&2] &4- &f");
        }
    }
    public static DITSystem getInstance(){
        return instance;
    }
    public void initOptions(){

        if (config.getBoolean("Saving.enabled") && config.getInt("Saving.interval")>0){
            BukkitScheduler scheduler = getServer().getScheduler();
            scheduler.scheduleSyncRepeatingTask(instance, new Runnable() {
                @Override
                public void run() {
                    String p = config.isSet("Saving.broadcastMsgProgress") ? config.getString("Saving.broadcastMsgProgress").replace("{PREFIX}",instance.getPrefix()) : instance.getPrefix()+"Auto save in progress..";
                    String d = config.isSet("Saving.broadcastMsgDone") ? config.getString("Saving.broadcastMsgDone").replace("{PREFIX}",instance.getPrefix()) : instance.getPrefix()+"Auto save done.";
                    Bukkit.broadcast(p,"st.st");
                    for(World w : Bukkit.getServer().getWorlds()){
                        w.save();
                    }
                    Bukkit.savePlayers();
                    Bukkit.broadcast(d,"st.st");
                }
            }, 0L, Math.convert(Math.Convert.SECONDS,Math.Convert.TICKS,instance.config.getInt("Saving.interval")));
        }

        if (this.config.isSet("CustomAdvancement.enabled")) {
            if(config.getBoolean("CustomAdvancement.enabled")){

                List<World> worlds = getServer().getWorlds();
                for (World w : worlds) {
                    if(w.getGameRuleValue(GameRule.ANNOUNCE_ADVANCEMENTS)) {
                        w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
                        log.info(this.getPrefix()+"Disabling vanilla advancement messages for " + w.getName());
                    }
                }
            }
        }

    }
    public boolean Reload(){
        File configFile = new File(getDataFolder(), "config.yml");
        try {
            ConfigUpdater.update(this, "config.yml", configFile, Collections.emptyList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        reloadConfig();
        this.config = getConfig();

        return true;
    }
    @Override
    public void onDisable() {
        System.out.println(this.getPrefix()+" stopped.");
    }
}
