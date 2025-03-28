package hu.ditservices.commands;

import hu.ditservices.STPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatCommand {
    public static boolean Run(CommandSender sender){
        Player player = (Player) sender;

        // Calculate total play time from ticks (Minecraft runs at 20 ticks per second)
        long totalTicks = player.getStatistic(Statistic.TOTAL_WORLD_TIME);
        long totalSeconds = totalTicks / 20;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        STPlugin plugin = STPlugin.getInstance();
        plugin.getTranslatedText("");

        player.sendMessage(STPlugin.getInstance().getPrefix() + ChatColor.GREEN + " === "+plugin.getTranslatedText("stats.header")+" === ");
        player.sendMessage(ChatColor.AQUA+plugin.getTranslatedText("stats.connects") + (player.getStatistic(Statistic.LEAVE_GAME)+1));
        player.sendMessage(ChatColor.AQUA+plugin.getTranslatedText("stats.deaths") + player.getStatistic(Statistic.DEATHS));
        player.sendMessage(ChatColor.AQUA+plugin.getTranslatedText("stats.MobKills") + player.getStatistic(Statistic.MOB_KILLS));
        player.sendMessage(ChatColor.AQUA+plugin.getTranslatedText("stats.PlayerKills") + player.getStatistic(Statistic.PLAYER_KILLS));
        player.sendMessage(ChatColor.AQUA+plugin.getTranslatedText("stats.sleeps") + player.getStatistic(Statistic.SLEEP_IN_BED));
        player.sendMessage(ChatColor.AQUA+plugin.getTranslatedText("stats.enchants") + player.getStatistic(Statistic.ITEM_ENCHANTED));
        player.sendMessage(ChatColor.AQUA + plugin.getTranslatedText("stats.totaltime") + hours + plugin.getTranslatedText("stats.hour") + minutes + plugin.getTranslatedText("stats.minutes") + seconds + plugin.getTranslatedText("stats.seconds"));
        player.sendMessage(ChatColor.GREEN+" =================== ");
        return true;
    }
}
