package user;

import java.util.ArrayList;
import java.util.List;

public class User {

    public static class QuizHistoryEntry {
        private String dateTimeIso;
        private int score;
        private int totalQuestions;
        private long totalTimeSeconds;

        public QuizHistoryEntry() {
        }

        public QuizHistoryEntry(String dateTimeIso, int score, int totalQuestions, long totalTimeSeconds) {
            this.dateTimeIso = dateTimeIso;
            this.score = score;
            this.totalQuestions = totalQuestions;
            this.totalTimeSeconds = totalTimeSeconds;
        }

        public String getDateTimeIso() {
            return dateTimeIso;
        }

        public int getScore() {
            return score;
        }

        public int getTotalQuestions() {
            return totalQuestions;
        }

        public long getTotalTimeSeconds() {
            return totalTimeSeconds;
        }
    }

    private String username;
    private String password;
    private String email;

    private final List<QuizHistoryEntry> history;

    public User() {
        this.history = new ArrayList<>();
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.history = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public List<QuizHistoryEntry> getHistory() {
        return history;
    }

    public void addHistory(QuizHistoryEntry entry) {
        history.add(entry);
    }

    public int getTotalQuizzes() {
        return history.size();
    }

    public int getTotalScore() {
        return history.stream().mapToInt(QuizHistoryEntry::getScore).sum();
    }

    public double getAverageScore() {
        if (history.isEmpty()) return 0;
        return history.stream().mapToDouble(QuizHistoryEntry::getScore).average().orElse(0);
    }

    public double getAveragePercentage() {
        if (history.isEmpty()) return 0;
        return history.stream()
                .mapToDouble(h -> (h.getTotalQuestions() == 0)
                        ? 0
                        : (100.0 * h.getScore() / h.getTotalQuestions()))
                .average()
                .orElse(0);
    }

    public long getTotalTimeSeconds() {
        return history.stream().mapToLong(QuizHistoryEntry::getTotalTimeSeconds).sum();
    }
}


