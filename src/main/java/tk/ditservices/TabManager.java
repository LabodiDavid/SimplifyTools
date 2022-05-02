package tk.ditservices;

import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import tk.ditservices.utils.Math;
import tk.ditservices.utils.Server;
/*import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerListHeaderFooter;
*/
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
//import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TabManager {
    private List<ChatComponentText> headers = new ArrayList<>();
    private List<ChatComponentText> footers = new ArrayList<>();

    private Integer refreshRate;

    private DITSystem plugin;
    private FileConfiguration config;
    private List<String> headeranimList = new ArrayList<String>();
    private List<String> footeranimList = new ArrayList<String>();

    private ArrayList<String> formatArray = new ArrayList<>();
    private List<Integer> DynamicHeaders = new ArrayList<>();
    private List<Integer> DynamicFooters = new ArrayList<>();

    private enum AddLine {
        HEADER, FOOTER
    }

    public TabManager(DITSystem instance,FileConfiguration c){

        this.plugin = instance;
        this.config = c;

        if(this.init()){
            this.showTab();
        }

    }



    public void showTab(){
        if (headers.isEmpty() && footers.isEmpty()){
            return;
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int count1 = 0; //headers
            int count2 = 0; //footers
            @Override
            public void run() {
                try {
                    //Tab code

                    if (count1>=headers.size()){
                        count1=0;
                    }
                    if (count2>=footers.size()){
                        count2=0;
                    }
                    //Re adding all lines  where we replaced something like the RAM usage to every refresh
                    //display current value. (We check those lines in the init())
                    if (DynamicHeaders.size()>0 && count1 < DynamicHeaders.size()){
                        if (DynamicHeaders.get(count1) == count1){
                            addHeaderFooter(AddLine.HEADER,headeranimList.get(count1),true,count1);
                        }
                    }
                    if (DynamicFooters.size()>0 && count2 < DynamicFooters.size()){
                        if (DynamicFooters.get(count2) == count2){
                            addHeaderFooter(AddLine.FOOTER,footeranimList.get(count2),true,count2);
                        }
                    }

                    PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(headers.get(count1),footers.get(count2));

                    if (Bukkit.getOnlinePlayers().size() !=0){
                        for (Player player : Bukkit.getOnlinePlayers()){
                            PlayerConnection pConn = ((CraftPlayer)player).getHandle().b;
                            //((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
                            pConn.a(packet);
                        }
                    }
                    if (headers.size()>1){
                        count1++;
                    }
                    if (footers.size()>1){
                        count2++;
                    }

            } catch (Exception e){
                    e.printStackTrace();
                }
            }
        },10, Math.convert(Math.Convert.SECONDS,Math.Convert.TICKS,this.refreshRate));
    }

    private boolean init(){
        if (config.getBoolean("Tab.enabled")){
            if (config.isSet("Tab.refreshRate")){
                this.refreshRate = config.getInt("Tab.refreshRate");
            }else{
                this.refreshRate=40;
            }

                //Getting the tab lines from the config
                headeranimList = config.getStringList("Tab.headerAnimation");
                footeranimList = config.getStringList("Tab.footerAnimation");

                //Investigating lines where we need to run the formatting every tab refresh for example for the RAM usage.
                //Only storing those lines indexes.
                formatArray.add("{TOTALRAM}");
                formatArray.add("{FREERAM}");
                formatArray.add("{USEDRAM}");
                formatArray.add("{AVERAGEPING}");
                formatArray.add("{ONLINEPLAYERS}");
                formatArray.add("{MAXPLAYERS}");
                for (String f: formatArray){
                    for (int i = 0; i<headeranimList.size(); i++){
                        if (headeranimList.get(i).contains(f) && !DynamicHeaders.contains(i)){
                            DynamicHeaders.add(i);
                        }
                    }
                    for (int i = 0; i<footeranimList.size(); i++){
                        if (footeranimList.get(i).contains(f) && !DynamicFooters.contains(i)){
                            DynamicFooters.add(i);
                        }
                    }
                }

                for (String hanim : headeranimList){
                        this.addHeaderFooter(AddLine.HEADER,hanim,false);
                }
                for (String fanim : footeranimList){
                        this.addHeaderFooter(AddLine.FOOTER,fanim,false);
                }

            return true;
        }
        return false;
    }

    /**
     * Manages the adding of the tab line and the final formatting of the text.
     * @param head_or_footer AddLine Enum value to choose where to add a tab line.
     * @param text The formatted tab line text.
     * @param dynamic If the text contains a replace which need to run every tab refresh then it's true.
     * @param index Index of the 'dynamic' line. This is an overloading so there we need the index[0] element.
     */
    public void addHeaderFooter(AddLine head_or_footer,String text,boolean dynamic,int... index){
        IChatBaseComponent tabText = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + format(text) +"\"}");
        if (head_or_footer==AddLine.HEADER){
            if (dynamic){
                headers.set(index[0],new ChatComponentText(tabText.getString()));
            }else {
                headers.add(new ChatComponentText(tabText.getString()));
            }

        }else{
            if (head_or_footer==AddLine.FOOTER){
                if (dynamic){
                    footers.set(index[0],new ChatComponentText(tabText.getString()));
                }else {
                    footers.add(new ChatComponentText(tabText.getString()));
                }
            }
        }
    }


    /**
     * Replaces the msg values to their represents and recognizes MC color codes.
     * @param msg The text.
     * @return Replaced text with recognized MC color codes.
     */
    private String format(String msg){
        if (msg.contains("{TOTALRAM}")){
            msg = msg.replace("{TOTALRAM}",String.valueOf(Server.getRAM(Server.RAM.TOTAL)));
        }
        if (msg.contains("{FREERAM}")){
            msg = msg.replace("{FREERAM}",String.valueOf(Server.getRAM(Server.RAM.FREE)));
        }
        if (msg.contains("{USEDRAM}")){
            msg = msg.replace("{USEDRAM}",String.valueOf(Server.getRAM(Server.RAM.USED)));
        }
        if (msg.contains("{AVERAGEPING}")){
            msg = msg.replace("{AVERAGEPING}",String.valueOf(Server.getAveragePing()));
        }
        if (msg.contains("{ONLINEPLAYERS}")){
            msg = msg.replace("{ONLINEPLAYERS}",String.valueOf(plugin.getServer().getOnlinePlayers().size()));
        }
        if (msg.contains("{MAXPLAYERS}")){
            msg = msg.replace("{MAXPLAYERS}",String.valueOf(plugin.getServer().getMaxPlayers()));
        }
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
