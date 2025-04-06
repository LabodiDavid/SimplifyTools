package hu.ditservices;

import hu.ditservices.handlers.*;
import hu.ditservices.listeners.*;
import hu.ditservices.utils.*;
import hu.ditservices.utils.Math;
import org.bstats.bukkit.Metrics;
import com.tchristofferson.configupdater.ConfigUpdater;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.jeff_media.updatechecker.UpdateCheckSource;
import com.jeff_media.updatechecker.UpdateChecker;
import com.jeff_media.updatechecker.UserAgentBuilder;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.logging.Logger;

public final class STPlugin extends JavaPlugin implements CommandExecutor, Listener {
    private static STPlugin instance;
    private final Logger log = Bukkit.getLogger();

    private FileConfiguration config;

    public long ServerStartTime;

    private YamlConfiguration langConfig;
    private ServerPasswordData serverPasswordData;

    @Override
    public void onEnable() {
        instance = this;

        if (this.initPlugin()) {
            this.log.info(ChatColor.stripColor(this.getPrefix())+"Started running.");
        }
    }

    @Override
    public FileConfiguration getConfig() {
        //TODO Implement defaults everywhere when config is not loaded for some reasons
        if (this.config != null) {
            return this.config;
        }

        return super.getConfig();
    }


    private void registerEvents() {
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new LogChat(this), this);
        getServer().getPluginManager().registerEvents(new LogCommand(this), this);
        getServer().getPluginManager().registerEvents(new LogConnect(this), this);

        if (this.config.isSet("CustomAdvancement.enabled") && this.config.getBoolean("CustomAdvancement.enabled")) {
            getServer().getPluginManager().registerEvents(new ChatEvents(this), this);
            this.log.info(ChatColor.stripColor(this.getPrefix()) + "Custom Advancement Messages enabled!");
        }

        if (this.config.isSet("ServerPassword.enabled") && this.config.getBoolean("ServerPassword.enabled")) {
            this.serverPasswordData = new ServerPasswordData(this);
            PluginCommand sloginCommand = this.getCommand("slogin");
            sloginCommand.setExecutor(new LoginCommandHandler(this));
            getServer().getPluginManager().registerEvents(new ServerPasswordEvents(this), this);
            this.log.info(ChatColor.stripColor(this.getPrefix()) + "Server Password enabled!");
        }
    }

    private void scheduleTasks() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new TPS(), 0, 1);

        if (this.config.getBoolean("ClearDropItems.enabled")) {
            new ClearDropItemsTask(this).runTaskTimer(this, 20L, 20L);
            this.log.info(ChatColor.stripColor(this.getPrefix()) + "Clear dropped items enabled!");
        }

        if (this.config.getBoolean("Saving.enabled") && this.config.getInt("Saving.interval") > 0) {
            long intervalTicks = Math.convert(Math.Convert.SECONDS, Math.Convert.TICKS, instance.config.getInt("Saving.interval"));
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new SaveHandler(), 0L, intervalTicks);
        }
    }

    private void initUpdateChecker() {
        new UpdateChecker(this, UpdateCheckSource.GITHUB_RELEASE_TAG, "LabodiDavid/SimplifyTools")
                .setDownloadLink("https://github.com/LabodiDavid/SimplifyTools/releases")
                .setDonationLink("https://paypal.me/labodidavid")
                .setChangelogLink("https://github.com/LabodiDavid/SimplifyTools/blob/main/docs/ChangeLog.md")
                .setNotifyOpsOnJoin(true)
                .setNotifyByPermissionOnJoin("st.admin")
                .setUserAgent(new UserAgentBuilder().addPluginNameAndVersion())
                .checkEveryXHours(24)
                .checkNow();
    }

    private void initMetrics() {
        Metrics metrics = new Metrics(this, 15108);
    }

    private boolean initPlugin() {
        try {
            this.ServerStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();
            if (Version.ServerVersion.isCurrentLower(Version.ServerVersion.v1_12_R1)) {
                throw new Exception("The server version is not supported! Update to to atleast 1.12 version to use this plugin!");
            }
            if (Version.ServerVersion.isCurrentHigher(Version.ServerVersion.v1_21_5_R1)) {
                this.log.warning(ChatColor.stripColor(this.getPrefix()) + "You are running a server version which is not tested with this plugin, errors can be occur!");
            }

            if (this.reload()) {
                TabHandler tab = new TabHandler();

                PluginCommand stCommand = this.getCommand("st");
                stCommand.setExecutor(new CommandHandler(this));
                stCommand.setTabCompleter(new DITTabCompleter());

                registerEvents();
                scheduleTasks();
                initUpdateChecker();
                initMetrics();

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            this.log.warning(ChatColor.stripColor(this.getPrefix()) + "INITIALIZATION ERROR: " + e.getMessage());
            this.log.warning(ChatColor.stripColor(this.getPrefix()) + "Plugin disabled!");
            this.setEnabled(false);
            return false;
        }
    }

    /**
     * Gets the plugin's prefix.
     *
     * @return String
     */
    public String getPrefix(){
        if (this.config.isSet("Prefix") && !Objects.requireNonNull(this.config.getString("Prefix")).isEmpty()) {
            return ChatColor.translateAlternateColorCodes('&', this.config.getString("Prefix"));
        }else{
            return ChatColor.translateAlternateColorCodes('&',"&a[&fSimplify&7Tools&2] &4- &f");
        }
    }

    /**
     * Returns the main plugin instance.
     *
     * @return STPlugin
     */
    public static STPlugin getInstance(){
        return instance;
    }

    /**
     * Gets the server's uptime in a human readable format.
     *
     * @return String
     */
    public static String getUptime() {
        long uptime = (System.currentTimeMillis() - instance.ServerStartTime) / 1000;
        StringBuilder returnText = new StringBuilder();

        int days = (int) (uptime / 86400);
        int hours = (int) ((uptime % 86400) / 3600);
        int minutes = (int) ((uptime % 3600) / 60);
        int seconds = (int) (uptime % 60);

        if (days > 1) {
            returnText.append(days).append(" days ");
        }
        if (hours > 1) {
            returnText.append(hours).append(" hours ");
        }
        if (minutes > 1) {
            returnText.append(minutes).append(" min ");
        }
        if (seconds > 1) {
            returnText.append(seconds).append("s ");
        }

        return returnText.toString();
    }

    public ServerPasswordData getServerPasswordData() {
        return this.serverPasswordData;
    }

    private void copyDefaultLang(String filename, File langFolder) {
        File outFile = new File(langFolder, filename);

        if (!outFile.exists()) {
            try (InputStream in = getResource(filename);
                 OutputStream out = new FileOutputStream(outFile)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            } catch (IOException e) {
                this.log.warning(ChatColor.stripColor(this.getPrefix()) + e.getMessage());
            }
        }
    }

    private void initLocalization() throws IOException {
        File langFolder = new File(getDataFolder(), "lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        String[] languages = {"en", "hu"};
        for (String l : languages) {
            File langFile = new File(langFolder, l + ".yml");
            if (!langFile.exists()) {
                copyDefaultLang(l + ".yml", langFolder);
            }
            ConfigUpdater.update(this, l + ".yml", langFile, Collections.emptyList());
        }

        String lang = this.config.getString("language", "en");
        File langFile = new File(getDataFolder(), "lang" + File.separator + lang + ".yml");
        if (!langFile.exists()) {
            getLogger().warning("Language file for '" + lang + "' not found. Falling back to English.");
            langFile = new File(getDataFolder(), "lang" + File.separator + "en.yml");
        }
        this.langConfig = YamlConfiguration.loadConfiguration(langFile);
    }

    public String getTranslatedText(String key) {
        return langConfig.getString(key, "Translation not found: " + key);
    }

    public boolean reload(){
        File configFile = new File(getDataFolder(), "config.yml");

        try {
            if (!configFile.exists()){
                this.saveDefaultConfig();
            }
            ConfigUpdater.update(this, "config.yml", configFile, Collections.emptyList());
        } catch (IOException e) {
            this.log.warning(ChatColor.stripColor(this.getPrefix()) + e.getMessage());
        }
        reloadConfig();
        this.config = getConfig();
        try {
            this.initLocalization();
        } catch (IOException e) {
            this.log.warning(ChatColor.stripColor(this.getPrefix()) + e.getMessage());
        }


        return true;
    }
    @Override
    public void onDisable() {
        this.log.info(ChatColor.stripColor(this.getPrefix()) + "Stopped running.");
    }
}
