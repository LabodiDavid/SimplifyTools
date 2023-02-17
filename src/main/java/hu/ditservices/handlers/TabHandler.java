package hu.ditservices.handlers;

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

    public TabHandler() throws Exception {

        this.plugin = STPlugin.getInstance();
        if(this.init()){
            if (headers.isEmpty() && footers.isEmpty()){
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
                if (Bukkit.getOnlinePlayers().size()==0){
                    return;
                }

                if (count1 >= headers.size()) {
                    count1 = 0;
                }
                if (count2 >= footers.size()) {
                    count2 = 0;
                }
                //Re adding all lines  where we replaced something like the RAM usage to every refresh
                //display current value. (We check those lines in the init())
                if (DynamicHeaders.size() > 0 && count1 < DynamicHeaders.size()) {
                    if (DynamicHeaders.get(count1) == count1) {
                        addHeaderFooter(true, headeranimList.get(count1), true, count1);
                    }
                }
                if (DynamicFooters.size() > 0 && count2 < DynamicFooters.size()) {
                    if (DynamicFooters.get(count2) == count2) {
                        addHeaderFooter(false, footeranimList.get(count2), true, count2);
                    }
                }
                /*
                if (both){
                        plugin.getLogger().info("DEBUG: Sending both (header) JSON: "+WrappedChatComponent.fromHandle(headers.get(count1)).getJson());
                        plugin.getLogger().info("DEBUG: Sending both (footer) JSON: "+WrappedChatComponent.fromHandle(footers.get(count2)).getJson());

                        packet.getChatComponents().write(0, WrappedChatComponent.fromHandle(headers.get(count1))).write(1,WrappedChatComponent.fromHandle(footers.get(count2)));
                    }else{
                        if (header){
                            plugin.getLogger().info("DEBUG: Sending header JSON: "+WrappedChatComponent.fromHandle(headers.get(count1)).getJson());

                            packet.getChatComponents().write(0, WrappedChatComponent.fromHandle(headers.get(count1))).write(1,WrappedChatComponent.fromText("{\"text\":\"\"}"));
                        }else{
                            if (footer){
                                plugin.getLogger().info("DEBUG: Sending footer JSON: "+WrappedChatComponent.fromHandle(footers.get(count2)).getJson());

                                packet.getChatComponents().write(0,WrappedChatComponent.fromText("{\"text\":\"\"}")).write(1,WrappedChatComponent.fromHandle(footers.get(count2)));
                            }
                        }
                    }

                if (Bukkit.getOnlinePlayers().size() > 0) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        plugin.getProtocolManager().sendServerPacket(player, packet);
                    }
                }*/


                if (Version.ServerVersion.isCurrentEqualOrLower(Version.ServerVersion.v1_12_R1)) {
                    packet = ClazzContainer.buildPacketPlayOutPlayerListHeaderFooter(headers.get(count1),footers.get(count2));
                    for (Player player : Bukkit.getOnlinePlayers()){
                        Reflection.sendPacket(player,packet);
                    }
                }else{
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.setPlayerListHeaderFooter(headers.get(count1),footers.get(count2));
                    }
                }


                if (headers.size() > 1) {
                    count1++;
                }
                if (footers.size() > 1) {
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
    private void addHeaderFooter(boolean header,String text,boolean dynamic,int... index) throws Exception {
        try {
            //JsonObject json = new JsonObject();
            //json.addProperty("text",format(text));
            String Json = "{\"text\": \""+format(text)+"\"}";
            //String Json = format(text);
            //Json = Json.trim();
            //plugin.getLogger().info("JSON!: "+Json);
            Object tabText = Reflection.asChatSerializer(Json);

            //plugin.getLogger().info("DEBUG: Adding JSON: "+Json);
            if (header){
                if (dynamic){

                    headers.set(index[0], Reflection.getChatSerializerString(tabText));
                    //headers.set(index[0],Json);
                }else {
                    //headers.add(Json);
                    headers.add(Reflection.getChatSerializerString(tabText));
                }
            }else{
                if (dynamic){
                    //footers.set(index[0],Json);
                    footers.set(index[0], Reflection.getChatSerializerString(tabText));
                }else {
                    //footers.add(Json);
                    footers.add(Reflection.getChatSerializerString(tabText));
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
            msg = msg.replace("{TPS}",String.format("%.2f", TPS.getTPS()));
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }
}
