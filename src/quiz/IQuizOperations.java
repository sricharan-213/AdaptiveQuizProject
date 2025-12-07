package quiz;

import java.util.List;

public interface IQuizOperations {

    List<Question> getQuestions();

    Question getCurrentQuestion();

    int getCurrentIndex();

    void goToQuestion(int index);

    void selectAnswer(int index, String option);

    int calculateScore();

    int getTotalQuestions();

    long getGlobalElapsedSeconds();

    void startGlobalTimer();

    void stopGlobalTimer();

    void addTimeForQuestion(int index, long seconds);

    long getTimeForQuestion(int index);
}


