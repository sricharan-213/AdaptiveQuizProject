package user;

import java.util.ArrayList;
import java.util.List;

public class User {

    public static class QuizHistoryEntry {
        private String dateTimeIso;
        private String subject;
        private int score;
        private int totalQuestions;
        private double percentage;
        private long totalTimeSeconds;
        // New field: serialized per-question data (JSON-like format for backward compatibility)
        // Format: "q0_text|q0_optA|q0_optB|q0_optC|q0_optD|q0_correct|q0_selected|q0_topic|q0_time;q1_..."
        private String questionData; // null for old entries

        public QuizHistoryEntry() {
        }

        public QuizHistoryEntry(String dateTimeIso, String subject, int score, int totalQuestions, double percentage, long totalTimeSeconds) {
            this(dateTimeIso, subject, score, totalQuestions, percentage, totalTimeSeconds, null);
        }

        public QuizHistoryEntry(String dateTimeIso, String subject, int score, int totalQuestions, double percentage, long totalTimeSeconds, String questionData) {
            this.dateTimeIso = dateTimeIso;
            this.subject = subject == null ? "General" : subject;
            this.score = score;
            this.totalQuestions = totalQuestions;
            this.percentage = percentage;
            this.totalTimeSeconds = totalTimeSeconds;
            this.questionData = questionData;
        }

        public String getDateTimeIso() {
            return dateTimeIso;
        }

        public String getSubject() {
            return subject;
        }

        public int getScore() {
            return score;
        }

        public int getTotalQuestions() {
            return totalQuestions;
        }

        public double getPercentage() {
            return percentage;
        }

        public long getTotalTimeSeconds() {
            return totalTimeSeconds;
        }

        public String getQuestionData() {
            return questionData;
        }

        public void setQuestionData(String questionData) {
            this.questionData = questionData;
        }

        /**
         * Check if this history entry has detailed question data for replay
         */
        public boolean hasDetailedData() {
            return questionData != null && !questionData.isEmpty();
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


