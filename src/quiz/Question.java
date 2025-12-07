package quiz;

import java.util.List;

public class Question {

    private final String text;
    private final List<String> options;
    private final String correctOption; // "A", "B", "C", "D"
    private final String topic;

    private String selectedOption; // null if unanswered
    private long timeSpentSeconds;

    public Question(String text, List<String> options, String correctOption, String topic) {
        this.text = text;
        this.options = options;
        this.correctOption = correctOption;
        this.topic = topic;
        this.selectedOption = null;
        this.timeSpentSeconds = 0;
    }

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public String getTopic() {
        return topic;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public boolean isAnswered() {
        return selectedOption != null;
    }

    public boolean isCorrect() {
        return selectedOption != null && selectedOption.equalsIgnoreCase(correctOption);
    }

    public long getTimeSpentSeconds() {
        return timeSpentSeconds;
    }

    public void addTimeSpent(long seconds) {
        this.timeSpentSeconds += seconds;
    }
}


