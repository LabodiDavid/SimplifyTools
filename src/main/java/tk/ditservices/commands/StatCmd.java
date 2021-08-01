package tk.ditservices.commands;

import tk.ditservices.DITSystem;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatCmd {
    public static boolean Run(CommandSender sender){
        Player player = (Player) sender;

        player.sendMessage(DITSystem.getInstance().getPrefix()+ ChatColor.GREEN+" === Your statistics === ");
        player.sendMessage(ChatColor.AQUA+"Connects: "+(player.getStatistic(Statistic.LEAVE_GAME)+1));
        player.sendMessage(ChatColor.AQUA+"Deaths: "+player.getStatistic(Statistic.DEATHS));
        player.sendMessage(ChatColor.AQUA+"Mob kills: "+player.getStatistic(Statistic.MOB_KILLS));
        player.sendMessage(ChatColor.AQUA+"Player kills: "+player.getStatistic(Statistic.PLAYER_KILLS));
        player.sendMessage(ChatColor.AQUA+"Sleep count: "+player.getStatistic(Statistic.SLEEP_IN_BED));
        player.sendMessage(ChatColor.AQUA+"Enchant count: "+player.getStatistic(Statistic.ITEM_ENCHANTED));
        player.sendMessage(ChatColor.GREEN+" =================== ");
        return true;
    }
}
