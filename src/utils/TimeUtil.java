package utils;

public class TimeUtil {

    public static String formatSeconds(long seconds) {
        long mins = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }
}


