/*
package hu.ditservices.utils.reflection;

import hu.ditservices.utils.Version;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ClazzContainer {
    private static Class<?> packet, ChatComponentTextClass, CraftPlayerClass, IChatBaseComponentClass, PacketPlayOutPlayerListHeaderFooterClass;
    private static Object  ChatComponentText, IChatBaseComponent;

    static {
        try {
            IChatBaseComponentClass = classByName("net.minecraft.network.chat", "IChatBaseComponent");
            packet = classByName("net.minecraft.network.protocol", "Packet");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public ClazzContainer(){

    }

    public static Class<?> classByName(String newPackageName, String name) throws ClassNotFoundException {
        if (Version.ServerVersion.isCurrentLower(Version.ServerVersion.v1_17_R1) || newPackageName == null) {
            newPackageName = "net.minecraft.server." + Version.ServerVersion.getArrayVersion()[3];
        }

        return Class.forName(newPackageName + "." + name);
    }

    public static Object buildChatComponentText(String text) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> constructor;
        if (Version.ServerVersion.isCurrentEqualOrHigher(Version.ServerVersion.v1_17_R1)){
            if (ChatComponentTextClass == null){
                ChatComponentTextClass = Reflection.getClass("net.minecraft.network.chat.ChatComponentText");
            }

            constructor = ChatComponentTextClass.getConstructor(String.class);
            return constructor.newInstance(text);
        }else{
            if (ChatComponentTextClass == null){
                ChatComponentTextClass = Reflection.getClass("net.minecraft.server.%v.ChatComponentText");
            }
            constructor = Reflection.getClass("net.minecraft.server.%v.ChatComponentText").getConstructor(String.class);
            return constructor.newInstance(text);
        }
    }

    public static Object buildPacketPlayOutPlayerListHeaderFooter(Object header, Object footer) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> constructor;
        Field headerField;
        Field footerField;
        Object packet;
        if (Version.ServerVersion.isCurrentEqualOrHigher(Version.ServerVersion.v1_17_R1)){
            if (PacketPlayOutPlayerListHeaderFooterClass == null){
                PacketPlayOutPlayerListHeaderFooterClass = Reflection.getClass("net.minecraft.network.protocol.game.PacketPlayOutPlayerListHeaderFooter");
            }
        }else{
            if (PacketPlayOutPlayerListHeaderFooterClass == null){
                PacketPlayOutPlayerListHeaderFooterClass = Reflection.getClass("net.minecraft.server.%v.PacketPlayOutPlayerListHeaderFooter");
            }
        }
        if (Version.ServerVersion.isCurrentEqualOrLower(Version.ServerVersion.v1_12_R1)){
            constructor = PacketPlayOutPlayerListHeaderFooterClass.getConstructor();
            packet = constructor.newInstance();
            try {
                headerField = packet.getClass().getDeclaredField("a");
                headerField.setAccessible(true);
                headerField.set(packet, buildChatComponentText((String) header));
                footerField = packet.getClass().getDeclaredField("b");
                footerField.setAccessible(true);
                footerField.set(packet, buildChatComponentText((String) footer));
            } catch (Exception e){
                e.printStackTrace();
            }
        }else{
            constructor = PacketPlayOutPlayerListHeaderFooterClass.getConstructor(IChatBaseComponentClass,IChatBaseComponentClass);
            packet = constructor.newInstance(header,footer);
        }
        return packet;
    }


    public static Class<?> getIChatBaseComponent() {
        return IChatBaseComponentClass;
    }
    public static Class<?> getPacket() {
        return packet;
    }
}
*/
