/*
package hu.ditservices.utils.reflection;

import hu.ditservices.utils.Version;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflection {

    private static Method playerHandleMethod, sendPacketMethod, chatSerializerMethodA,chatSerializerMethodgetString;
    private static Field playerConnectionField;
    private static Class<?> chatSerializer;

    private Reflection(){}


    public static Class<?> getClass(String name) {
        try {
            return Class.forName(name.replace("%v",Version.ServerVersion.getCurrent().toString()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getCraftClass(String className) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + Version.ServerVersion.getArrayVersion()[3] + "." + className);
    }

    public static Object getPlayerHandle(Player player) throws Exception {
        if (playerHandleMethod == null) {
            playerHandleMethod = player.getClass().getDeclaredMethod("getHandle");
        }

        return playerHandleMethod.invoke(player);
    }

    public static void sendPacket(Player player, Object packet) {
        if (player == null) {
            return;
        }

        try {
            Object playerHandle = getPlayerHandle(player);

            if (playerConnectionField == null) {
                playerConnectionField = playerHandle.getClass().getDeclaredField(
                        (Version.ServerVersion.isCurrentEqualOrHigher(Version.ServerVersion.v1_17_R1) ? "b" : "playerConnection"));
            }

            Object playerConnection = playerConnectionField.get(playerHandle);

            if (sendPacketMethod == null) {
                sendPacketMethod = playerConnection.getClass().getDeclaredMethod(
                        Version.ServerVersion.isCurrentEqualOrHigher(Version.ServerVersion.v1_18_R1) ? "a" : "sendPacket",
                        ClazzContainer.getPacket());
            }

            sendPacketMethod.invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getChatSerializerString(Object serializer) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (chatSerializerMethodgetString==null){
            if (Version.ServerVersion.isCurrentEqualOrLower(Version.ServerVersion.v1_12_R1)){
                chatSerializerMethodgetString = ClazzContainer.getIChatBaseComponent().getDeclaredMethod("getText");
            }else{
                chatSerializerMethodgetString = ClazzContainer.getIChatBaseComponent().getDeclaredMethod("getString");
            }
        }
        return (String) chatSerializerMethodgetString.invoke(serializer);
        //return (String) ClazzContainer.getIChatBaseComponent().cast(chatSerializerMethodgetString.invoke(serializer));
    }



    public static Object asChatSerializer(String json) throws Exception {
        try {
            if (Version.ServerVersion.isCurrentEqualOrHigher(Version.ServerVersion.v1_17_R1)){
                if (chatSerializer==null){
                    chatSerializer = Class.forName("net.minecraft.network.chat.IChatBaseComponent$ChatSerializer");
                }

            }else{
                if (chatSerializer==null){
                    chatSerializer = Class.forName("net.minecraft.server." + Version.ServerVersion.getArrayVersion()[3] + ".IChatBaseComponent$ChatSerializer");
                }
            }
            if (chatSerializerMethodA == null) {
                chatSerializerMethodA = chatSerializer.getMethod("a", String.class);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return chatSerializerMethodA.invoke(chatSerializer,json);
        //return ClazzContainer.getIChatBaseComponent().cast(chatSerializerMethodA.invoke(chatSerializer, json));
    }

    */
/*public static Class<?> getNMSClassRegex(String nmsClass) {

        String version = null;
        Pattern pat = Pattern.compile("net\\.minecraft\\.(?:server)?\\.(v(?:\\d_)+R\\d)");
        for (Package p : Package.getPackages()) {
            String name = p.getName();
            Matcher m = pat.matcher(name);
            if (m.matches()) version = m.group(1);
        }

        if (version == null) return null;

        try {
            return Class.forName(String.format(nmsClass, version));
        } catch (ClassNotFoundException e) {
            return null;
        }

    }*//*

}
*/
