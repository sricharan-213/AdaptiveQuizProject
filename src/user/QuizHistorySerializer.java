package user;

import quiz.Question;
import quiz.Quiz;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to serialize/deserialize Quiz data for history storage
 */
public class QuizHistorySerializer {

    /**
     * Serialize a Quiz object to a string format for storage
     * Format: "q0_text|q0_optA|q0_optB|q0_optC|q0_optD|q0_correct|q0_selected|q0_topic|q0_time;q1_..."
     */
    public static String serializeQuiz(Quiz quiz) {
        if (quiz == null || quiz.getQuestions() == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        List<Question> questions = quiz.getQuestions();
        for (int i = 0; i < questions.size(); i++) {
            if (i > 0) sb.append(";");
            Question q = questions.get(i);
            // Escape special characters: | -> ||, ; -> ;;
            String text = escape(q.getText());
            List<String> opts = q.getOptions();
            String optA = opts.size() > 0 ? escape(opts.get(0)) : "";
            String optB = opts.size() > 1 ? escape(opts.get(1)) : "";
            String optC = opts.size() > 2 ? escape(opts.get(2)) : "";
            String optD = opts.size() > 3 ? escape(opts.get(3)) : "";
            String correct = q.getCorrectOption() != null ? escape(q.getCorrectOption()) : "";
            String selected = q.getSelectedOption() != null ? escape(q.getSelectedOption()) : "";
            String topic = q.getTopic() != null ? escape(q.getTopic()) : "General";
            long time = quiz.getTimeForQuestion(i);
            sb.append(text).append("|")
              .append(optA).append("|")
              .append(optB).append("|")
              .append(optC).append("|")
              .append(optD).append("|")
              .append(correct).append("|")
              .append(selected).append("|")
              .append(topic).append("|")
              .append(time);
        }
        return sb.toString();
    }

    /**
     * Deserialize a string to a list of QuestionData objects
     */
    public static List<QuestionData> deserializeQuestions(String data) {
        List<QuestionData> questions = new ArrayList<>();
        if (data == null || data.isEmpty()) {
            return questions;
        }
        String[] questionStrings = data.split(";");
        for (String qStr : questionStrings) {
            if (qStr.isEmpty()) continue;
            String[] parts = qStr.split("\\|", -1); // -1 to include empty strings
            if (parts.length >= 9) {
                QuestionData qd = new QuestionData();
                qd.text = unescape(parts[0]);
                qd.optionA = unescape(parts[1]);
                qd.optionB = unescape(parts[2]);
                qd.optionC = unescape(parts[3]);
                qd.optionD = unescape(parts[4]);
                qd.correctOption = unescape(parts[5]);
                qd.selectedOption = unescape(parts[6]);
                qd.topic = unescape(parts[7]);
                try {
                    qd.timeSpent = Long.parseLong(parts[8]);
                } catch (NumberFormatException e) {
                    qd.timeSpent = 0;
                }
                questions.add(qd);
            }
        }
        return questions;
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("|", "||").replace(";", ";;");
    }

    private static String unescape(String s) {
        if (s == null) return "";
        return s.replace(";;", ";").replace("||", "|");
    }

    /**
     * Data class to hold deserialized question information
     */
    public static class QuestionData {
        public String text;
        public String optionA;
        public String optionB;
        public String optionC;
        public String optionD;
        public String correctOption;
        public String selectedOption;
        public String topic;
        public long timeSpent;
    }
}

