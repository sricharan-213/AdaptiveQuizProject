package quiz;

import utils.FileHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizParser {

    public static List<Question> parseFromFile(File file) {
        List<String> lines = FileHandler.readAllLines(file);
        List<Question> questions = new ArrayList<>();

        List<String> buffer = new ArrayList<>();
        for (String rawLine : lines) {
            String line = rawLine.trim();
            if (line.isEmpty()) {
                if (!buffer.isEmpty()) {
                    Question q = buildQuestion(buffer);
                    if (q != null) {
                        questions.add(q);
                    }
                    buffer.clear();
                }
            } else {
                buffer.add(line);
            }
        }
        if (!buffer.isEmpty()) {
            Question q = buildQuestion(buffer);
            if (q != null) {
                questions.add(q);
            }
        }
        return questions;
    }

    private static Question buildQuestion(List<String> block) {
        if (block.size() < 7) {
            return null;
        }
        String questionText = block.get(0);
        String optA = stripPrefix(block.get(1), "A)");
        String optB = stripPrefix(block.get(2), "B)");
        String optC = stripPrefix(block.get(3), "C)");
        String optD = stripPrefix(block.get(4), "D)");

        String answerLine = block.stream()
                .filter(l -> l.toUpperCase().startsWith("ANSWER:"))
                .findFirst()
                .orElse("");
        String topicLine = block.stream()
                .filter(l -> l.toUpperCase().startsWith("TOPIC:"))
                .findFirst()
                .orElse("TOPIC: General");

        String answer = answerLine.replaceFirst("(?i)ANSWER:", "").trim().toUpperCase();
        if (answer.length() == 0) {
            return null;
        }
        String topic = topicLine.replaceFirst("(?i)TOPIC:", "").trim();

        return new Question(
                questionText,
                Arrays.asList(optA, optB, optC, optD),
                answer.substring(0, 1),
                topic
        );
    }

    private static String stripPrefix(String line, String prefix) {
        if (line.toUpperCase().startsWith(prefix.toUpperCase())) {
            return line.substring(prefix.length()).trim();
        }
        return line;
    }
}


