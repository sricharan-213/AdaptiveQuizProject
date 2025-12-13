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
    private final String subject; // subject derived from filename
    private final int selectedCount; // how many questions were selected for this attempt

    public Quiz(List<Question> questions) {
        this(questions, "General", questions == null ? 0 : questions.size());
    }

    public Quiz(List<Question> questions, String subject, int selectedCount) {
        this.questions = new ArrayList<>(questions);
        this.perQuestionTime = new HashMap<>();
        this.currentIndex = 0;
        this.subject = subject == null ? "General" : subject;
        this.selectedCount = selectedCount;
    }

    // Factory: create quiz by randomly selecting N questions from a bank (no repetition)
    public static Quiz fromBank(List<Question> bank, String subject, int selectN) {
        List<Question> pick = new ArrayList<>();
        if (bank == null || bank.isEmpty() || selectN <= 0) {
            return new Quiz(pick, subject, 0);
        }
        java.util.List<Integer> idx = new java.util.ArrayList<>();
        for (int i = 0; i < bank.size(); i++) idx.add(i);
        java.util.Collections.shuffle(idx);
        selectN = Math.min(selectN, bank.size());
        for (int i = 0; i < selectN; i++) pick.add(bank.get(idx.get(i)));
        return new Quiz(pick, subject, selectN);
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

    public String getSubject() {
        return subject;
    }

    public int getSelectedCount() {
        return selectedCount;
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


