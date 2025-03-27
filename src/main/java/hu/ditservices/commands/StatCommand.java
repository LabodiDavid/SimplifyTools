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

        player.sendMessage(STPlugin.getInstance().getPrefix() + ChatColor.GREEN + " === Your statistics === ");
        player.sendMessage(ChatColor.AQUA+"Connects: " + (player.getStatistic(Statistic.LEAVE_GAME)+1));
        player.sendMessage(ChatColor.AQUA+"Deaths: " + player.getStatistic(Statistic.DEATHS));
        player.sendMessage(ChatColor.AQUA+"Mob kills: " + player.getStatistic(Statistic.MOB_KILLS));
        player.sendMessage(ChatColor.AQUA+"Player kills: " + player.getStatistic(Statistic.PLAYER_KILLS));
        player.sendMessage(ChatColor.AQUA+"Sleep count: " + player.getStatistic(Statistic.SLEEP_IN_BED));
        player.sendMessage(ChatColor.AQUA+"Enchant count: " + player.getStatistic(Statistic.ITEM_ENCHANTED));
        player.sendMessage(ChatColor.AQUA + "Total Play Time: " + hours + "h " + minutes + "m " + seconds + "s");
        player.sendMessage(ChatColor.GREEN+" =================== ");
        return true;
    }
}
