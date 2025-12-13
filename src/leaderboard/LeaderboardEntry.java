package leaderboard;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public class LeaderboardEntry implements Comparable<LeaderboardEntry> {

    // Represents a single quiz attempt (subject-aware)
    private final String username;
    private final String subject;
    private final int score;
    private final int totalQuestions;
    private final double percentage;
    private final long totalTimeSeconds;
    private final String dateTimeIso; // ISO instant string

    public LeaderboardEntry(String username, String subject, int score, int totalQuestions, double percentage, long totalTimeSeconds, String dateTimeIso) {
        this.username = username;
        this.subject = subject == null ? "General" : subject;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.percentage = percentage;
        this.totalTimeSeconds = totalTimeSeconds;
        this.dateTimeIso = dateTimeIso == null ? "" : dateTimeIso;
    }

    public String getUsername() { return username; }
    public String getSubject() { return subject; }
    public int getScore() { return score; }
    public int getTotalQuestions() { return totalQuestions; }
    public double getPercentage() { return percentage; }
    public long getTotalTimeSeconds() { return totalTimeSeconds; }
    public String getDateTimeIso() { return dateTimeIso; }

    @Override
    public int compareTo(LeaderboardEntry o) {
        // 1) percentage DESC
        int cmp = Double.compare(o.percentage, this.percentage);
        if (cmp != 0) return cmp;
        // 2) total time ASC (faster ranks higher)
        cmp = Long.compare(this.totalTimeSeconds, o.totalTimeSeconds);
        if (cmp != 0) return cmp;
        // 3) older attempts first (earlier Instant)
        try {
            Instant a = this.dateTimeIso.isEmpty() ? Instant.EPOCH : Instant.parse(this.dateTimeIso);
            Instant b = o.dateTimeIso.isEmpty() ? Instant.EPOCH : Instant.parse(o.dateTimeIso);
            return a.compareTo(b);
        } catch (DateTimeParseException ex) {
            return 0;
        }
    }
}


