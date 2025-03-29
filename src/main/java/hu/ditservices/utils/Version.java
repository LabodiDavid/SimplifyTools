package hu.ditservices.utils;

import hu.ditservices.STPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class Version {
    public enum ServerVersion {
        v1_8_R1,
        v1_8_R2,
        v1_8_R3,
        v1_9_R1,
        v1_9_R2,
        v1_10_R1,
        v1_11_R1,
        v1_12_R1,
        v1_13_R1,
        v1_13_R2,
        v1_14_R1,
        v1_14_R2,
        v1_15_R1,
        v1_15_R2,
        v1_16_R1,
        v1_16_R2,
        v1_16_R3,
        v1_17_R1,
        v1_17_R2,
        v1_18_R1,
        v1_18_R2,
        v1_19_R1,
        v1_19_R2,
        v1_20_R1,
        v1_20_R2,
        v1_20_3_R1,
        v1_20_4_R1,
        v1_20_5_R1,
        v1_20_6_R1,
        v1_21_R1,
        v1_21_1_R1,
        v1_21_2_R1,
        v1_21_3_R1,
        v1_21_4_R1,
        v1_21_5_R1;

        private int value;

        private static String[] arrayVersion;
        private static ServerVersion current;

        ServerVersion() {
            value = Integer.valueOf(getNumberEscapeSequence().matcher(name()).replaceAll(""));
        }

        public static ServerVersion getCurrent() {
            if (current != null)
                return current;

            String[] v = getArrayVersion();
            String vv = v[v.length - 1];
            for (ServerVersion one : values()) {
                if (one.name().equalsIgnoreCase(vv)) {
                    current = one;
                    break;
                }
            }

            if (current == null) {
                current = ServerVersion.v1_16_R3;
            }

            return current;
        }

        /*public static String[] getArrayVersion() {
            if (arrayVersion == null) {
                arrayVersion = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().split("\\.");
            }

            return arrayVersion;
        }*/
        public static String[] getArrayVersion() {
            if (arrayVersion == null) {
                String version = null;
                try {
                    // Check if the Paper method getMinecraftVersion exists
                    Method minecraftVersionMethod = Bukkit.getServer().getClass().getMethod("getMinecraftVersion");
                    if (minecraftVersionMethod != null) {
                        version = (String) minecraftVersionMethod.invoke(Bukkit.getServer());
                    }
                } catch (NoSuchMethodException e) {
                    // The method doesn't exist in this server version; fall back below.
                } catch (Exception e) {
                    STPlugin plugin = STPlugin.getInstance();
                    plugin.getLogger().warning(ChatColor.stripColor(plugin.getPrefix()) + e.getMessage());
                }

                // Fallback: use Bukkit version if no version was obtained or it's empty.
                if (version == null || version.trim().isEmpty()) {
                    version = Bukkit.getServer().getBukkitVersion();
                    // Remove any suffix (e.g., "-R0.1-SNAPSHOT") for consistency.
                    int dashIndex = version.indexOf('-');
                    if (dashIndex != -1) {
                        version = version.substring(0, dashIndex);
                    }
                }

                // Construct a legacy-style version string.
                String legacyVersion = "v" + version.replace('.', '_') + "_R1";
                arrayVersion = new String[] { legacyVersion };
            }
            return arrayVersion;
        }
        /*public static String[] getArrayVersion() {
            if (arrayVersion == null) {
                String packageName = org.bukkit.Bukkit.getServer().getClass().getPackage().getName();
                String[] splitPackageName = packageName.split("\\.");

                // Check if the splitPackageName length is more than 3
                if (splitPackageName.length > 3) {
                    arrayVersion = new String[] {splitPackageName[3]};
                } else {
                    // Handle the case for newer versions
                    String version = "UNKNOWN";
                    try {
                        version = org.bukkit.Bukkit.getServer().getVersion().split("\\(MC: ")[1].split("\\)")[0];
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    arrayVersion = new String[] {"v" + version.replace('.', '_') + "_R1"};
                }
            }

            return arrayVersion;
        }*/

        public static boolean isCurrentEqualOrHigher(ServerVersion v) {
            return getCurrent().value >= v.value;
        }

        public static boolean isCurrentHigher(ServerVersion v) {
            return getCurrent().value > v.value;
        }

        public static boolean isCurrentLower(ServerVersion v) {
            return getCurrent().value < v.value;
        }

        public static boolean isCurrentEqualOrLower(ServerVersion v) {
            return getCurrent().value <= v.value;
        }

        public static boolean isCurrentEqual(ServerVersion v) {
            return getCurrent().value == v.value;
        }
    }

    private static final Pattern COMMA_SPACE_SEPARATED_PATTERN = Pattern.compile(", ");
    private static final Pattern NUMBER_ESCAPE_SEQUENCE = Pattern.compile("[^\\d]");

    public static Pattern getCommaSpaceSeparatedPattern() {
        return COMMA_SPACE_SEPARATED_PATTERN;
    }

    public static Pattern getNumberEscapeSequence() {
        return NUMBER_ESCAPE_SEQUENCE;
    }

}
