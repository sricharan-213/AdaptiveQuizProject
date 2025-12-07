package analytics;

import java.util.List;

public class StatsUtil {

    public static double average(List<Integer> values) {
        if (values == null || values.isEmpty()) return 0;
        long sum = 0;
        for (int v : values) sum += v;
        return (double) sum / values.size();
    }

    public static double averageLong(List<Long> values) {
        if (values == null || values.isEmpty()) return 0;
        long sum = 0;
        for (long v : values) sum += v;
        return (double) sum / values.size();
    }

    public static double percentage(int score, int total) {
        if (total <= 0) return 0;
        return 100.0 * score / total;
    }
}


