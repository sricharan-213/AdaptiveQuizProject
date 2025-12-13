package ui;

import analytics.Analysis;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import main.App;
import quiz.Question;
import quiz.Quiz;
import user.UserManager;
import utils.DialogUtil;
import utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class QuizScreen extends BorderPane {

    private final Quiz quiz;
    private final List<ToggleButton> questionButtons = new ArrayList<>();

    private Label globalTimerLabel;
    private Label questionTimerLabel;
    private Label questionTextLabel;
    private RadioButton optionA;
    private RadioButton optionB;
    private RadioButton optionC;
    private RadioButton optionD;
    private ToggleGroup optionsGroup;

    private Timeline globalTimeline;
    private Timeline questionTimeline;
    private Timeline countdownTimeline;
    private Label timeLimitLabel;
    private long timeLimitSeconds; // countdown seconds
    private long questionElapsed; // total seconds for current question (includes previous visits)
    private long questionStartTime; // the time when we switched to current question (for calculating increment)

    public QuizScreen(Quiz quiz) {
        this.quiz = quiz;
        this.quiz.startGlobalTimer();
        buildUi();
        startTimers();
        loadQuestion(0);
    }

    private void buildUi() {
        setPadding(new Insets(10));
        setStyle("-fx-background-color: linear-gradient(to bottom, #E8EAF6, #C5CAE9);");

        // Left navigation panel
        VBox left = new VBox(10);
        left.setPadding(new Insets(15));
        left.setPrefWidth(200);
        left.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 12px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 2);");
        Label qNavTitle = new Label("Questions");
        qNavTitle.setStyle("-fx-text-fill: #3F51B5; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold;");

        VBox qButtonsBox = new VBox(6);

        for (int i = 0; i < quiz.getTotalQuestions(); i++) {
            int idx = i;
            ToggleButton btn = new ToggleButton("Q" + (i + 1));
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setMinHeight(35);
            btn.setStyle("-fx-background-color: #E0E0E0; " +
                    "-fx-text-fill: #424242; " +
                    "-fx-font-size: 13px; " +
                    "-fx-background-radius: 6px; " +
                    "-fx-cursor: hand;");
            btn.setOnAction(e -> switchQuestion(idx));
            questionButtons.add(btn);
            qButtonsBox.getChildren().add(btn);
        }

        left.getChildren().addAll(qNavTitle, new ScrollPane(qButtonsBox));
        setLeft(left);

        // Top timers
        HBox top = new HBox(20);
        top.setPadding(new Insets(15, 20, 15, 20));
        top.setAlignment(Pos.CENTER_RIGHT);
        top.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 10px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);");

        Label globalLbl = new Label("Quiz time:");
        globalLbl.setStyle("-fx-text-fill: #5C6BC0; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold;");
        globalTimerLabel = new Label("00:00");
        globalTimerLabel.setStyle("-fx-text-fill: #303F9F; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-color: #E8EAF6; " +
                "-fx-padding: 5px 12px; " +
                "-fx-background-radius: 6px;");
        Label questionLbl = new Label("Question time:");
        questionLbl.setStyle("-fx-text-fill: #5C6BC0; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold;");
        questionTimerLabel = new Label("00:00");
        questionTimerLabel.setStyle("-fx-text-fill: #303F9F; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-color: #E8EAF6; " +
                "-fx-padding: 5px 12px; " +
                "-fx-background-radius: 6px;");

        // countdown/time limit label (if applicable)
        timeLimitLabel = new Label("");
        timeLimitLabel.setStyle("-fx-text-fill: #B71C1C; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5px 12px; -fx-background-color: #FFEBEE; -fx-background-radius: 6px;");
        // Subject label at left
        Label subjectLabel = new Label("Subject: " + (quiz.getSubject() == null ? "General" : quiz.getSubject()));
        subjectLabel.setStyle("-fx-text-fill: #0D47A1; -fx-font-size: 16px; -fx-font-weight: bold;");
        HBox leftBox = new HBox(subjectLabel);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        BorderPane topContainer = new BorderPane();
        topContainer.setLeft(leftBox);
        HBox rightBox = new HBox(10, globalLbl, globalTimerLabel, questionLbl, questionTimerLabel, timeLimitLabel);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        topContainer.setRight(rightBox);
        setTop(topContainer);

        // Center content
        VBox center = new VBox(20);
        center.setPadding(new Insets(30, 40, 30, 40));
        center.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 12px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 3);");

        questionTextLabel = new Label();
        questionTextLabel.setWrapText(true);
        questionTextLabel.setStyle("-fx-text-fill: #212121; " +
                "-fx-font-size: 18px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px;");

        optionsGroup = new ToggleGroup();
        optionA = makeOptionRadio("A");
        optionB = makeOptionRadio("B");
        optionC = makeOptionRadio("C");
        optionD = makeOptionRadio("D");

        VBox optionsBox = new VBox(12, optionA, optionB, optionC, optionD);

        center.getChildren().addAll(questionTextLabel, optionsBox);
        setCenter(center);

        // Bottom navigation
        HBox bottom = new HBox(10);
        bottom.setPadding(new Insets(15, 20, 15, 20));
        bottom.setAlignment(Pos.CENTER_RIGHT);
        bottom.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 10px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);");

        Button prevBtn = Widgets.ghostButton("Previous");
        Button nextBtn = Widgets.ghostButton("Next");
        Button submitBtn = Widgets.primaryButton("Submit Quiz");

        prevBtn.setOnAction(e -> {
            int i = quiz.getCurrentIndex();
            if (i > 0) {
                switchQuestion(i - 1);
            }
        });
        nextBtn.setOnAction(e -> {
            int i = quiz.getCurrentIndex();
            if (i < quiz.getTotalQuestions() - 1) {
                switchQuestion(i + 1);
            }
        });
        submitBtn.setOnAction(e -> submitQuiz());

        bottom.getChildren().addAll(prevBtn, nextBtn, submitBtn);
        setBottom(bottom);
    }

    private RadioButton makeOptionRadio(String key) {
        RadioButton rb = new RadioButton();
        rb.setToggleGroup(optionsGroup);
        rb.setStyle("-fx-font-size: 15px; " +
                "-fx-text-fill: #424242; " +
                "-fx-padding: 10px; " +
                "-fx-background-color: #F5F5F5; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand;");
        rb.setOnAction(e -> {
            if (rb.isSelected()) {
                quiz.selectAnswer(quiz.getCurrentIndex(), key);
                updateQuestionNavStyles();
            }
        });
        rb.setOnMouseEntered(e -> {
            if (!rb.isSelected()) {
                rb.setStyle("-fx-font-size: 15px; " +
                        "-fx-text-fill: #424242; " +
                        "-fx-padding: 10px; " +
                        "-fx-background-color: #E3F2FD; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-cursor: hand;");
            }
        });
        rb.setOnMouseExited(e -> {
            if (!rb.isSelected()) {
                rb.setStyle("-fx-font-size: 15px; " +
                        "-fx-text-fill: #424242; " +
                        "-fx-padding: 10px; " +
                        "-fx-background-color: #F5F5F5; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-cursor: hand;");
            }
        });
        return rb;
    }

    private void startTimers() {
        globalTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            long secs = quiz.getGlobalElapsedSeconds();
            globalTimerLabel.setText(TimeUtil.formatSeconds(secs));
        }));
        globalTimeline.setCycleCount(Timeline.INDEFINITE);
        globalTimeline.play();

        // For the first question, start from 0
        questionStartTime = 0;
        questionElapsed = 0;
        questionTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            questionElapsed++;
            questionTimerLabel.setText(TimeUtil.formatSeconds(questionElapsed));
        }));
        questionTimeline.setCycleCount(Timeline.INDEFINITE);
        questionTimeline.playFromStart();

        // Start countdown time limit: default 1 minute per question
        timeLimitSeconds = (long) quiz.getSelectedCount() * 60L;
        if (timeLimitSeconds > 0) {
            timeLimitLabel.setText("Time left: " + TimeUtil.formatSeconds(timeLimitSeconds));
            if (countdownTimeline != null) countdownTimeline.stop();
            countdownTimeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
                timeLimitSeconds--;
                if (timeLimitSeconds <= 0) {
                    timeLimitLabel.setText("Time left: 00:00");
                    // auto-submit
                    autoSubmit();
                } else {
                    timeLimitLabel.setText("Time left: " + TimeUtil.formatSeconds(timeLimitSeconds));
                }
            }));
            countdownTimeline.setCycleCount(Timeline.INDEFINITE);
            countdownTimeline.play();
        }
    }

    private void pauseQuestionTimer() {
        // Pause the current question timer
        if (questionTimeline != null) {
            questionTimeline.stop();
        }
    }

    private void resumeQuestionTimer(int questionIndex) {
        // Get the total time already spent on this question before this visit
        long totalTimeSpent = quiz.getTimeForQuestion(questionIndex);
        // Set the start time to the total already spent
        questionStartTime = totalTimeSpent;
        // Start the elapsed counter from the total time spent
        questionElapsed = totalTimeSpent;
        questionTimerLabel.setText(TimeUtil.formatSeconds(questionElapsed));
        
        // Resume the timer
        if (questionTimeline != null) {
            questionTimeline.stop();
        }
        questionTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            questionElapsed++;
            questionTimerLabel.setText(TimeUtil.formatSeconds(questionElapsed));
        }));
        questionTimeline.setCycleCount(Timeline.INDEFINITE);
        questionTimeline.playFromStart();
    }

    private void switchQuestion(int newIndex) {
        int currentIndex = quiz.getCurrentIndex();
        if (currentIndex != newIndex) {
            // Pause the timer
            pauseQuestionTimer();
            // Calculate the increment: how much time was spent since we switched to this question
            long increment = questionElapsed - questionStartTime;
            if (increment > 0) {
                // Only save the increment (time spent in this visit)
                quiz.addTimeForQuestion(currentIndex, increment);
            }
        }
        loadQuestion(newIndex);
    }

    private void loadQuestion(int index) {
        quiz.goToQuestion(index);
        Question q = quiz.getCurrentQuestion();
        if (q == null) return;

        questionTextLabel.setText("Q" + (index + 1) + ". " + q.getText());

        List<String> opts = q.getOptions();
        if (opts.size() >= 4) {
            optionA.setText("A) " + opts.get(0));
            optionB.setText("B) " + opts.get(1));
            optionC.setText("C) " + opts.get(2));
            optionD.setText("D) " + opts.get(3));
        }

        // auto-select saved answer
        optionsGroup.selectToggle(null);
        String sel = q.getSelectedOption();
        if (sel != null) {
            switch (sel.toUpperCase()) {
                case "A":
                    optionsGroup.selectToggle(optionA);
                    break;
                case "B":
                    optionsGroup.selectToggle(optionB);
                    break;
                case "C":
                    optionsGroup.selectToggle(optionC);
                    break;
                case "D":
                    optionsGroup.selectToggle(optionD);
                    break;
                default:
                    break;
            }
        }

        // update nav button styles
        updateQuestionNavStyles();

        // Resume timer from where it left off (pauses when switching away)
        resumeQuestionTimer(index);
    }

    private void updateQuestionNavStyles() {
        for (int i = 0; i < questionButtons.size(); i++) {
            ToggleButton b = questionButtons.get(i);
            if (i == quiz.getCurrentIndex()) {
                // Current question - Blue
                b.setStyle("-fx-background-color: linear-gradient(to bottom, #2196F3, #1976D2); " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-cursor: hand;");
            } else {
                Question q = quiz.getQuestions().get(i);
                if (q.isAnswered()) {
                    // Answered - Green
                    b.setStyle("-fx-background-color: linear-gradient(to bottom, #4CAF50, #45a049); " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 13px; " +
                            "-fx-background-radius: 6px; " +
                            "-fx-cursor: hand;");
                } else {
                    // Unanswered - Grey
                    b.setStyle("-fx-background-color: #E0E0E0; " +
                            "-fx-text-fill: #424242; " +
                            "-fx-font-size: 13px; " +
                            "-fx-background-radius: 6px; " +
                            "-fx-cursor: hand;");
                }
            }
        }
    }

    private void submitQuiz() {
        if (!DialogUtil.confirm("Submit quiz", "Are you sure you want to submit the quiz?")) {
            return;
        }
        // Pause and save current question time
        pauseQuestionTimer();
        long increment = questionElapsed - questionStartTime;
        if (increment > 0) {
            quiz.addTimeForQuestion(quiz.getCurrentIndex(), increment);
        }
        quiz.stopGlobalTimer();
        if (globalTimeline != null) globalTimeline.stop();
        if (questionTimeline != null) questionTimeline.stop();

        int score = quiz.calculateScore();
        int totalQ = quiz.getTotalQuestions();
        long totalTime = quiz.getGlobalElapsedSeconds();

        // record with full quiz data for replay
        UserManager.getInstance().recordQuizResult(quiz, score, totalQ, totalTime);

        Analysis analysis = new Analysis(quiz, score, totalTime);
        App.setRoot(new ResultsScreen(quiz, analysis));
    }

    // Auto-submit without confirmation when countdown reaches zero
    private void autoSubmit() {
        // Stop timers and lock UI similar to submitQuiz
        pauseQuestionTimer();
        long increment = questionElapsed - questionStartTime;
        if (increment > 0) {
            quiz.addTimeForQuestion(quiz.getCurrentIndex(), increment);
        }
        quiz.stopGlobalTimer();
        if (globalTimeline != null) globalTimeline.stop();
        if (questionTimeline != null) questionTimeline.stop();
        if (countdownTimeline != null) countdownTimeline.stop();

        int score = quiz.calculateScore();
        int totalQ = quiz.getTotalQuestions();
        long totalTime = quiz.getGlobalElapsedSeconds();

        UserManager.getInstance().recordQuizResult(quiz.getSubject(), score, totalQ, totalTime);
        Analysis analysis = new Analysis(quiz, score, totalTime);
        // Navigate to results
        App.setRoot(new ResultsScreen(quiz, analysis));
    }
}


