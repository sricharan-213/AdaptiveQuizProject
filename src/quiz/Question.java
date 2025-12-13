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

    /**
     * Get the option text for a given option letter (A, B, C, D)
     * Returns null if letter is invalid or options list is empty
     */
    public String getOptionText(String optionLetter) {
        if (optionLetter == null || options == null || options.isEmpty()) {
            return null;
        }
        optionLetter = optionLetter.toUpperCase().trim();
        int index = -1;
        switch (optionLetter) {
            case "A": index = 0; break;
            case "B": index = 1; break;
            case "C": index = 2; break;
            case "D": index = 3; break;
            default: return null;
        }
        if (index >= 0 && index < options.size()) {
            return options.get(index);
        }
        return null;
    }

    /**
     * Get the selected option text (actual text, not letter)
     */
    public String getSelectedOptionText() {
        if (selectedOption == null) {
            return null;
        }
        return getOptionText(selectedOption);
    }

    /**
     * Get the correct option text (actual text, not letter)
     */
    public String getCorrectOptionText() {
        if (correctOption == null) {
            return null;
        }
        return getOptionText(correctOption);
    }
}


