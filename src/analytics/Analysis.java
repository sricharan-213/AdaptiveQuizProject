package analytics;

import quiz.Question;
import quiz.Quiz;

import java.util.*;

public class Analysis {

    private final Quiz quiz;
    private final int score;
    private final long totalTimeSeconds;

    public Analysis(Quiz quiz, int score, long totalTimeSeconds) {
        this.quiz = quiz;
        this.score = score;
        this.totalTimeSeconds = totalTimeSeconds;
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return quiz.getTotalQuestions();
    }

    public double getPercentage() {
        return StatsUtil.percentage(score, quiz.getTotalQuestions());
    }

    public long getTotalTimeSeconds() {
        return totalTimeSeconds;
    }

    public Map<Integer, Long> getTimePerQuestion() {
        Map<Integer, Long> map = new LinkedHashMap<>();
        for (int i = 0; i < quiz.getTotalQuestions(); i++) {
            map.put(i, quiz.getTimeForQuestion(i));
        }
        return map;
    }

    public List<String> getWeakTopics() {
        Map<String, Integer> wrongPerTopic = new HashMap<>();
        for (Question q : quiz.getQuestions()) {
            if (q.isAnswered() && !q.isCorrect()) {
                wrongPerTopic.put(q.getTopic(), wrongPerTopic.getOrDefault(q.getTopic(), 0) + 1);
            }
        }
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(wrongPerTopic.entrySet());
        entries.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));
        List<String> topics = new ArrayList<>();
        for (Map.Entry<String, Integer> e : entries) {
            topics.add(e.getKey());
        }
        return topics;
    }

    public List<String> getAdaptiveSuggestions() {
        List<String> suggestions = new ArrayList<>();
        List<String> weakTopics = getWeakTopics();
        if (!weakTopics.isEmpty()) {
            suggestions.add("Review theory and practice more questions on: " + String.join(", ", weakTopics));
        }

        Map<String, Long> timePerTopic = quiz.getTimePerTopic();
        List<Map.Entry<String, Long>> sortedByTime = new ArrayList<>(timePerTopic.entrySet());
        sortedByTime.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));
        if (!sortedByTime.isEmpty()) {
            suggestions.add("You spend most time on: " + sortedByTime.get(0).getKey()
                    + ". Consider improving speed and confidence.");
        }

        double pct = getPercentage();
        if (pct < 50) {
            suggestions.add("Focus on fundamentals first; aim for smaller, targeted practice sets.");
        } else if (pct < 80) {
            suggestions.add("Good work. Strengthen your weaker topics and attempt mixed-level quizzes.");
        } else {
            suggestions.add("Excellent performance. Try advanced questions and time-bound practice.");
        }
        return suggestions;
    }
}


