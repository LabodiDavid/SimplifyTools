package tk.ditservices.utils;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.lang.Math;

import java.lang.reflect.InvocationTargetException;

public class Server {
    public enum RAM {
        FREE,USED,TOTAL
    }
    /**
     * Get RAM usage statistics of the MC server.
     * @param t The RAM usage type you want to get. Server.RAM enum. It could be FREE,TOTAL,USED
     */
    public static long getRAM(Server.RAM t) {
        long mb = 1048576L;
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        if (t == RAM.FREE){
            return runtime.freeMemory() / mb;
        }
        if (t == RAM.TOTAL){
            return runtime.totalMemory() / mb;
        }
        if (t == RAM.USED){
            return (runtime.totalMemory() - runtime.freeMemory()) / mb;
        }

        return 0;
    }
    /**
     * Get a player's ping.
     * @param player The player object.
     */
    public static int getPlayerPing(Player player){
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            return (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | NoSuchFieldException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return -1;
    }
    /**
     * Get an average of the currently online player's ping.
     */
    public static int getAveragePing(){
        int sum = 0;
        int avg = 0;
        int onlineplayers = 0;
        if (Bukkit.getOnlinePlayers().size() !=0){
            onlineplayers =Bukkit.getOnlinePlayers().size();
            for (Player player : Bukkit.getOnlinePlayers()){
                sum += Server.getPlayerPing(player);
            }
            avg = Math.floorDiv(sum,onlineplayers);

        }
            return avg;

    }
}
