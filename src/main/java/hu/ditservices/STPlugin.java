package hu.ditservices;

import hu.ditservices.handlers.DITTabCompleter;
import hu.ditservices.handlers.SaveHandler;
import hu.ditservices.handlers.TabHandler;
import hu.ditservices.utils.*;
import hu.ditservices.utils.Math;
import org.bstats.bukkit.Metrics;
import hu.ditservices.commands.DitCmd;
import hu.ditservices.listeners.ChatEvents;
import hu.ditservices.listeners.LogChat;
import hu.ditservices.listeners.LogCommand;
import hu.ditservices.listeners.LogConnect;
import com.tchristofferson.configupdater.ConfigUpdater;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.jeff_media.updatechecker.UpdateCheckSource;
import com.jeff_media.updatechecker.UpdateChecker;
import com.jeff_media.updatechecker.UserAgentBuilder;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.Objects;
import java.util.logging.Logger;

public final class STPlugin extends JavaPlugin implements CommandExecutor, Listener {
    private static STPlugin instance;
    private final Logger log = Bukkit.getLogger();

    public FileConfiguration config = getConfig();

    public long ServerStartTime;

    @Override
    public void onEnable() {
        instance = this;
        try {
            this.Init();
        } catch (Exception e) {
            this.log.warning("[SimplifyTools] - INITIALIZATION ERROR: "+e.getMessage());
            this.log.warning("[SimplifyTools] - Plugin disabled!");
            this.setEnabled(false);
            return;
        }
        this.log.info(ChatColor.stripColor(this.getPrefix())+"Started running.");
    }


    private void Init() throws Exception {
        this.ServerStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        if (Version.ServerVersion.isCurrentLower(Version.ServerVersion.v1_12_R1)){
            throw new Exception("The server version is not supported! Update to a version between 1.12 - 1.20.1 to run this plugin.");
        }
        if (this.Reload()){


            TabHandler tab = new TabHandler();
            PluginCommand ditCmd = this.getCommand("st");
            ditCmd.setExecutor(new DitCmd(this));
            ditCmd.setTabCompleter(new DITTabCompleter());

            getServer().getPluginManager().registerEvents(this, this);
            getServer().getPluginManager().registerEvents(new LogChat(this), this);
            getServer().getPluginManager().registerEvents(new LogCommand(this), this);
            getServer().getPluginManager().registerEvents(new LogConnect(this), this);
            if (this.config.isSet("CustomAdvancement.enabled") && this.config.getBoolean("CustomAdvancement.enabled")){
                getServer().getPluginManager().registerEvents(new ChatEvents(this), this);
                this.log.info(ChatColor.stripColor(this.getPrefix()) + "Custom Advancement Messages enabled!");
            }
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new TPS(), 0, 1);

            if (this.config.getBoolean("Saving.enabled") && this.config.getInt("Saving.interval")>0){
                Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new SaveHandler(), 0L, Math.convert(Math.Convert.SECONDS,Math.Convert.TICKS,instance.config.getInt("Saving.interval")));
            }

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
    }
    public String getPrefix(){
        if (this.config.isSet("Prefix") && !Objects.requireNonNull(this.config.getString("Prefix")).isEmpty()){
            return ChatColor.translateAlternateColorCodes('&', this.config.getString("Prefix"));
        }else{
            return ChatColor.translateAlternateColorCodes('&',"&a[&fSimplify&7Tools&2] &4- &f");
        }
    }

    public static STPlugin getInstance(){
        return instance;
    }

    public static String getUptime(){
        int uptime = (int)(System.currentTimeMillis() - instance.ServerStartTime)/1000;
        String returnText = "";

        // Get remaining seconds from total minutes.
        int seconds = uptime % 60;
        // Convert to minutes, get remaining minutes from total hours.
        int minutes = (uptime / 60) % 60;
        // Convert to hours, get remaining hours from total days.
        int hours = (uptime / 3600) % 24;
        // Convert to days.
        int days = uptime / 86400;

        if (days>1){
            returnText = returnText+ days+" days ";
        }
        if (hours>1){
            returnText = returnText+ hours+ " hours ";
        }
        if (minutes>1){
            returnText = returnText+ minutes+ " min ";
        }
        if (seconds>1){
            returnText = returnText+ seconds+ "s ";
        }

        return returnText;

    }

    public boolean Reload(){
        File configFile = new File(getDataFolder(), "config.yml");

        try {
            if (!configFile.exists()){
                this.saveDefaultConfig();
            }
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
        this.log.info(ChatColor.stripColor(this.getPrefix()) + "Started running.");
    }
}
