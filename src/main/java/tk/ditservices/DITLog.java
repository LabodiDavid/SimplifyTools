package tk.ditservices;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.event.Listener;

public class DITLog implements Listener {
    public static String Location = "plugins/SimplifyTools/logs/";

    /**
     * Gets the current date in yyyy-MM-dd format.
     */
    public static String getDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }
    /**
     * Gets the current time in HH:mm:ss format.
     */
    public static String getTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }
    /**
     * Logs a line in the today's log file.
     * @param text The text to log.
     */
    public static void logLine(String text){
        String fileName = DITLog.getDate()+".txt";

        File file = new File(DITLog.Location+fileName);
        if (!file.exists()){ createFile(); }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
            writer.write(text);
            writer.newLine();
            writer.close();

        } catch (Exception e){ e.printStackTrace(); }
    }


    private static void createFile(){
        String fileName = getDate() + ".txt";
        File directory = new File(DITLog.Location);
        if(!directory.exists()){
            directory.mkdirs();
        }

        File file = new File(DITLog.Location + fileName);
        if( !file.exists() ){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
