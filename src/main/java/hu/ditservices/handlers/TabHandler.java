package hu.ditservices.handlers;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import hu.ditservices.STPlugin;
import hu.ditservices.utils.*;
import hu.ditservices.utils.Math;
import hu.ditservices.utils.reflection.ClazzContainer;
import hu.ditservices.utils.reflection.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabHandler {
    public final List<String> headers = new ArrayList<>();
    public final List<String> footers = new ArrayList<>();

    public final List<WrappedChatComponent> headerComponents = new ArrayList<>();
    public final List<WrappedChatComponent> footerComponents = new ArrayList<>();

    private Integer refreshRate;

    private final STPlugin plugin;
    public List<String> headeranimList = new ArrayList<>();
    public List<String> footeranimList = new ArrayList<>();

    private final ArrayList<String> formatArray = new ArrayList<>();
    public final List<Integer> DynamicHeaders = new ArrayList<>();
    public final List<Integer> DynamicFooters = new ArrayList<>();

    private int count1 = 0; //headers
    private int count2 = 0; //footers
    private Object packet;

    private boolean isNewerThan1_20_2;

    public TabHandler() throws Exception {

        this.plugin = STPlugin.getInstance();
        this.isNewerThan1_20_2 = Version.ServerVersion.isCurrentHigher(Version.ServerVersion.v1_20_R2);
        if(this.init()){
            if ((headers.isEmpty() && footers.isEmpty() && !isNewerThan1_20_2)
                    || (headerComponents.isEmpty() && footerComponents.isEmpty() && isNewerThan1_20_2)){
                plugin.getLogger().warning(plugin.getPrefix()+"TAB customization disabled because empty customization config!");
                return;
            }
            this.updateTab();
        /*packet = plugin.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
        boolean both = headers.size() > 0 && footers.size() > 0;
        boolean header = !both && headers.size() > 0;
        boolean footer = !both && footers.size() > 0;*/

            //scheduleSyncRepeatingTask
            //Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new TabRunnable(this),20, Math.convert(Math.Convert.SECONDS,Math.Convert.TICKS,this.refreshRate));
        }

    }

    private boolean init() throws Exception {
        if (plugin.getConfig().getBoolean("Tab.enabled")){

            int availProcessors = Server.getCpuCores();
            if (availProcessors < 3) {
                plugin.getLogger().warning("You're currently having " + availProcessors + " CPU processors, which is not enough to run a minecraft server with scheduled plugins including SimplifyTools.");
                plugin.getLogger().warning("You will be experiencing many lags during the server is running. Consider upgrading your CPU to at least reach 3 cores.");
            }


            if (plugin.getConfig().isSet("Tab.refreshRate")){
                this.refreshRate = plugin.getConfig().getInt("Tab.refreshRate");
            }else{
                this.refreshRate=1;
            }

            //Getting the tab lines from the config
            headeranimList = plugin.getConfig().getStringList("Tab.headerAnimation");
            footeranimList = plugin.getConfig().getStringList("Tab.footerAnimation");

            //Investigating lines where we need to run the formatting every tab refresh for example for the RAM usage.
            //Only storing those lines indexes.
            formatArray.add("{TOTALRAM}");
            formatArray.add("{FREERAM}");
            formatArray.add("{USEDRAM}");
            formatArray.add("{AVERAGEPING}");
            formatArray.add("{ONLINEPLAYERS}");
            formatArray.add("{MAXPLAYERS}");
            formatArray.add("{MOTD}");
            formatArray.add("{UPTIME}");
            formatArray.add("{TPS}");
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
                this.addHeaderFooter(true,hanim,false);
            }
            for (String fanim : footeranimList){
                this.addHeaderFooter(false,fanim,false);
            }

            return true;
        }
        return false;
    }

    private void updateTab(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            try {
                if (Bukkit.getOnlinePlayers().isEmpty()){
                    return;
                }
                if (isNewerThan1_20_2) {
                    if (count1 >= headerComponents.size()) {
                        count1 = 0;
                    }
                    if (count2 >= footerComponents.size()) {
                        count2 = 0;
                    }
                } else {
                    if (count1 >= headers.size()) {
                        count1 = 0;
                    }
                    if (count2 >= footers.size()) {
                        count2 = 0;
                    }
                }

                //Re adding all lines  where we replaced something like the RAM usage to every refresh
                //display current value. (We check those lines in the init())
                if (!DynamicHeaders.isEmpty() && count1 < DynamicHeaders.size()) {
                    if (DynamicHeaders.get(count1) == count1) {
                        addHeaderFooter(true, headeranimList.get(count1), true, count1);
                    }
                }
                if (!DynamicFooters.isEmpty() && count2 < DynamicFooters.size()) {
                    if (DynamicFooters.get(count2) == count2) {
                        addHeaderFooter(false, footeranimList.get(count2), true, count2);
                    }
                }

                if (Version.ServerVersion.isCurrentEqualOrLower(Version.ServerVersion.v1_12_R1)) {
                    packet = ClazzContainer.buildPacketPlayOutPlayerListHeaderFooter(headers.get(count1),footers.get(count2));
                    for (Player player : Bukkit.getOnlinePlayers()){
                        Reflection.sendPacket(player,packet);
                    }
                } else if (Version.ServerVersion.isCurrentHigher(Version.ServerVersion.v1_20_R2)) {
                    // Use ProtocolLib for versions higher than 1.20.2
                    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
                    PacketContainer packet = protocolManager.createPacket(com.comphenix.protocol.PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);

                    // Set header and footer
                    packet.getChatComponents().write(0, headerComponents.get(count1));
                    packet.getChatComponents().write(1, footerComponents.get(count2));

                    // Send the packet to all players
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        protocolManager.sendServerPacket(player, packet);
                    }
                } else {
                    // Use the built-in Spigot API for versions between 1.13 and 1.20.1
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.setPlayerListHeaderFooter(headers.get(count1), footers.get(count2));
                    }
                }


                if (headers.size() > 1 || headerComponents.size() > 1) {
                    count1++;
                }
                if (footers.size() > 1 || footerComponents.size() > 1) {
                    count2++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        },0,Math.convert(Math.Convert.SECONDS,Math.Convert.TICKS,this.refreshRate));

    }



    /**
     * Manages the adding of the tab line and the final formatting of the text.
     * @param header If true the text will be added to the header, otherwise to the footer.
     * @param text The formatted tab line text.
     * @param dynamic If the text contains a replace which need to run every tab refresh then it's true.
     * @param index Index of the 'dynamic' line. This is an overloading so there we need the index[0] element.
     */
    private void addHeaderFooter(boolean header,String text,boolean dynamic,int... index) {
        try {
            String Json = "{\"text\": \""+format(text)+"\"}";
            boolean isNewerThan1202 = Version.ServerVersion.isCurrentHigher(Version.ServerVersion.v1_20_R2);

            if (isNewerThan1202) {
                WrappedChatComponent tabText = WrappedChatComponent.fromJson(Json);

                if (header){
                    if (dynamic){
                        headerComponents.set(index[0], tabText);
                    }else {
                        headerComponents.add(tabText);
                    }
                }else{
                    if (dynamic){
                        footerComponents.set(index[0], tabText);
                    }else {
                        footerComponents.add(tabText);
                    }
                }
            } else {
                Object tabText = Reflection.asChatSerializer(Json);

                if (header){
                    if (dynamic){
                        headers.set(index[0], Reflection.getChatSerializerString(tabText));
                    }else {
                        headers.add(Reflection.getChatSerializerString(tabText));
                    }
                }else{
                    if (dynamic){
                        footers.set(index[0], Reflection.getChatSerializerString(tabText));
                    }else {
                        footers.add(Reflection.getChatSerializerString(tabText));
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
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
        if (msg.contains("{UPTIME}")){
            msg = msg.replace("{UPTIME}", STPlugin.getUptime());
        }
        if (msg.contains("{MOTD}")){
            msg = msg.replace("{MOTD}", plugin.getServer().getMotd()); //LegacyComponentSerializer.legacyAmpersand().serialize(plugin.getServer().motd().asComponent())
        }
        if (msg.contains("{TPS}")){
            msg = msg.replace("{TPS}",String.valueOf(java.lang.Math.round(TPS.getTPS())));
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }
}
