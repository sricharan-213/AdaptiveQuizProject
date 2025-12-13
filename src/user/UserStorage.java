package user;

import utils.FileHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserStorage {

    private static final String USERS_DIR = "resources/users";

    public static void saveUser(User user) {
        File dir = new File(USERS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, user.getUsername() + ".txt");
        List<String> lines = new ArrayList<>();
        lines.add("username=" + user.getUsername());
        lines.add("password=" + user.getPassword());
        lines.add("email=" + user.getEmail());
        lines.add("history_count=" + user.getHistory().size());
        int idx = 0;
        for (User.QuizHistoryEntry h : user.getHistory()) {
            lines.add("h" + idx + "_datetime=" + h.getDateTimeIso());
            lines.add("h" + idx + "_subject=" + (h.getSubject() == null ? "General" : h.getSubject()));
            lines.add("h" + idx + "_score=" + h.getScore());
            lines.add("h" + idx + "_totalQuestions=" + h.getTotalQuestions());
            lines.add("h" + idx + "_percentage=" + h.getPercentage());
            lines.add("h" + idx + "_totalTimeSeconds=" + h.getTotalTimeSeconds());
            // Save question data if present (new format)
            if (h.getQuestionData() != null && !h.getQuestionData().isEmpty()) {
                lines.add("h" + idx + "_questionData=" + h.getQuestionData());
            }
            idx++;
        }
        FileHandler.writeLines(file, lines);
    }

    public static User loadUser(String username) {
        File file = new File(USERS_DIR, username + ".txt");
        if (!file.exists()) {
            return null;
        }
        List<String> lines = FileHandler.readAllLines(file);
        String u = null;
        String p = null;
        String e = null;
        int historyCount = 0;
        for (String line : lines) {
            if (line.startsWith("username=")) {
                u = line.substring("username=".length());
            } else if (line.startsWith("password=")) {
                p = line.substring("password=".length());
            } else if (line.startsWith("email=")) {
                e = line.substring("email=".length());
            } else if (line.startsWith("history_count=")) {
                try {
                    historyCount = Integer.parseInt(line.substring("history_count=".length()));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        if (u == null || p == null) {
            return null;
        }
        Student user = new Student(u, p, e);
        for (int i = 0; i < historyCount; i++) {
            String dt = null;
            String subject = "General";
            int score = 0;
            int totalQ = 0;
            double percentage = 0.0;
            long totalTime = 0;
            String questionData = null;
            for (String line : lines) {
                if (line.startsWith("h" + i + "_datetime=")) {
                    dt = line.substring(("h" + i + "_datetime=").length());
                } else if (line.startsWith("h" + i + "_subject=")) {
                    subject = line.substring(("h" + i + "_subject=").length());
                } else if (line.startsWith("h" + i + "_score=")) {
                    try {
                        score = Integer.parseInt(line.substring(("h" + i + "_score=").length()));
                    } catch (NumberFormatException ignored) {
                    }
                } else if (line.startsWith("h" + i + "_totalQuestions=")) {
                    try {
                        totalQ = Integer.parseInt(line.substring(("h" + i + "_totalQuestions=").length()));
                    } catch (NumberFormatException ignored) {
                    }
                } else if (line.startsWith("h" + i + "_percentage=")) {
                    try {
                        percentage = Double.parseDouble(line.substring(("h" + i + "_percentage=").length()));
                    } catch (NumberFormatException ignored) {
                    }
                } else if (line.startsWith("h" + i + "_totalTimeSeconds=")) {
                    try {
                        totalTime = Long.parseLong(line.substring(("h" + i + "_totalTimeSeconds=").length()));
                    } catch (NumberFormatException ignored) {
                    }
                } else if (line.startsWith("h" + i + "_questionData=")) {
                    questionData = line.substring(("h" + i + "_questionData=").length());
                }
            }
            if (dt != null) {
                // Backward compatibility: if percentage missing, compute
                if (percentage == 0.0 && totalQ > 0) {
                    percentage = (100.0 * score) / totalQ;
                }
                user.addHistory(new User.QuizHistoryEntry(dt, subject, score, totalQ, percentage, totalTime, questionData));
            }
        }
        return user;
    }

    public static List<User> loadAllUsers() {
        List<User> users = new ArrayList<>();
        File dir = new File(USERS_DIR);
        if (!dir.exists() || !dir.isDirectory()) {
            return users;
        }
        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".txt"));
        if (files == null) return users;
        for (File f : files) {
            String username = f.getName().substring(0, f.getName().length() - 4);
            User u = loadUser(username);
            if (u != null) {
                users.add(u);
            }
        }
        return users;
    }
}


