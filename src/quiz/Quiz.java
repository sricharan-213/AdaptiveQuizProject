package quiz;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Quiz implements IQuizOperations {

    private final List<Question> questions;
    private final Map<Integer, Long> perQuestionTime; // seconds
    private Instant globalStart;
    private Instant globalEnd;
    private int currentIndex;

    public Quiz(List<Question> questions) {
        this.questions = new ArrayList<>(questions);
        this.perQuestionTime = new HashMap<>();
        this.currentIndex = 0;
    }

    @Override
    public List<Question> getQuestions() {
        return questions;
    }

    @Override
    public Question getCurrentQuestion() {
        if (currentIndex < 0 || currentIndex >= questions.size()) {
            return null;
        }
        return questions.get(currentIndex);
    }

    @Override
    public int getCurrentIndex() {
        return currentIndex;
    }

    @Override
    public void goToQuestion(int index) {
        if (index >= 0 && index < questions.size()) {
            this.currentIndex = index;
        }
    }

    @Override
    public void selectAnswer(int index, String option) {
        if (index >= 0 && index < questions.size()) {
            questions.get(index).setSelectedOption(option);
        }
    }

    @Override
    public int calculateScore() {
        int score = 0;
        for (Question q : questions) {
            if (q.isCorrect()) {
                score++;
            }
        }
        return score;
    }

    @Override
    public int getTotalQuestions() {
        return questions.size();
    }

    @Override
    public long getGlobalElapsedSeconds() {
        if (globalStart == null) {
            return 0;
        }
        Instant end = (globalEnd != null) ? globalEnd : Instant.now();
        return Duration.between(globalStart, end).getSeconds();
    }

    @Override
    public void startGlobalTimer() {
        if (globalStart == null) {
            globalStart = Instant.now();
        }
    }

    @Override
    public void stopGlobalTimer() {
        if (globalStart != null && globalEnd == null) {
            globalEnd = Instant.now();
        }
    }

    @Override
    public void addTimeForQuestion(int index, long seconds) {
        if (index < 0 || index >= questions.size()) {
            return;
        }
        long existing = perQuestionTime.getOrDefault(index, 0L);
        perQuestionTime.put(index, existing + seconds);
        questions.get(index).addTimeSpent(seconds);
    }

    @Override
    public long getTimeForQuestion(int index) {
        return perQuestionTime.getOrDefault(index, 0L);
    }

    public Map<String, Long> getTimePerTopic() {
        Map<String, Long> topicTime = new HashMap<>();
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            long t = getTimeForQuestion(i);
            topicTime.put(q.getTopic(), topicTime.getOrDefault(q.getTopic(), 0L) + t);
        }
        return topicTime;
    }
}


