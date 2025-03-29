package hu.ditservices.utils;

public class Math {
    public enum Convert {
        TICKS,SECONDS,MINUTES,HOURS
    }
    /**
     * Converting time measurement units from one to another.
     * @param from The unit FROM you want to convert. Math.Convert
     * @param to The unit TO you want to convert. Math.Convert
     * @param value An integer which you want to convert.
     */
    public static long convert(Convert from, Convert to, int value){
        if (from== Convert.SECONDS && to==Convert.TICKS){
            return value*20L;
        }
        return 0;
    }
}
