package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import main.App;
import quiz.Quiz;
import quiz.QuizParser;
import user.User;
import user.UserManager;
import utils.DialogUtil;

import java.io.File;

public class MainMenuScreen extends BorderPane {

    public MainMenuScreen() {
        buildUi();
    }

    private void buildUi() {

        setPadding(new Insets(40));
        setStyle("-fx-background-color: linear-gradient(to bottom right, #E3F2FD, #BBDEFB);");

        User current = UserManager.getInstance().getCurrentUser();
        String name = current != null ? current.getUsername() : "Guest";

        Label welcome = new Label("Welcome, " + name);
        welcome.setStyle("""
                -fx-text-fill: #0D47A1;
                -fx-font-size: 32px;
                -fx-font-weight: bold;
        """);

        Label subtitle = new Label("Dashboard");
        subtitle.setStyle("""
                -fx-text-fill: #1565C0;
                -fx-font-size: 20px;
                -fx-font-weight: bold;
        """);

        VBox header = new VBox(8, welcome, subtitle);
        header.setAlignment(Pos.CENTER);
        setTop(header);

        // ⚡ Attractive horizontal button layout
        HBox buttonBox = new HBox(18);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(30));

        javafx.scene.control.Button startQuizBtn = Widgets.primaryButton("Start New Quiz");
        javafx.scene.control.Button historyBtn = Widgets.secondaryButton("Quiz History");
        javafx.scene.control.Button analyticsBtn = Widgets.secondaryButton("Analytics");
        javafx.scene.control.Button leaderboardBtn = Widgets.secondaryButton("Leaderboard");
        javafx.scene.control.Button logoutBtn = Widgets.dangerButton("Logout");

        styleMenuButton(startQuizBtn);
        styleMenuButton(historyBtn);
        styleMenuButton(analyticsBtn);
        styleMenuButton(leaderboardBtn);
        styleMenuButton(logoutBtn);

        buttonBox.getChildren().addAll(startQuizBtn, historyBtn, analyticsBtn, leaderboardBtn, logoutBtn);

        startQuizBtn.setOnAction(e -> chooseQuizFile());
        historyBtn.setOnAction(e -> App.setRoot(new HistoryScreen()));
        analyticsBtn.setOnAction(e -> App.setRoot(new AnalyticsScreen()));
        leaderboardBtn.setOnAction(e -> App.setRoot(new LeaderboardScreen()));
        logoutBtn.setOnAction(e -> {
            UserManager.getInstance().logout();
            App.setRoot(new LoginScreen());
        });

        // Dashboard cards
        int quizzes = current != null ? current.getTotalQuizzes() : 0;
        double avgScore = current != null ? current.getAverageScore() : 0;
        double avgPct = current != null ? current.getAveragePercentage() : 0;

        HBox cards = new HBox(25,
                Widgets.card("Quizzes Taken", String.valueOf(quizzes)),
                Widgets.card("Average Score", String.format("%.1f", avgScore)),
                Widgets.card("Average %", String.format("%.1f%%", avgPct))
        );
        cards.setAlignment(Pos.CENTER);
        cards.setPadding(new Insets(10));

        VBox center = new VBox(25, buttonBox, cards);
        center.setAlignment(Pos.CENTER);

        setCenter(center);
    }

    // ✨ hover animation for buttons
    private void styleMenuButton(javafx.scene.control.Button btn) {
        btn.setStyle(btn.getStyle() +
                "-fx-min-width: 160px; -fx-min-height: 48px; -fx-font-size: 15px;"
        );
        btn.setOnMouseEntered(e -> btn.setScaleX(1.07));
        btn.setOnMouseEntered(e -> btn.setScaleY(1.07));
        btn.setOnMouseExited(e -> btn.setScaleX(1.0));
        btn.setOnMouseExited(e -> btn.setScaleY(1.0));
    }

    private void chooseQuizFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select quiz questions file");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fc.showOpenDialog(App.getPrimaryStage());
        if (file == null) return;

        try {
            Quiz quiz = new Quiz(QuizParser.parseFromFile(file));
            if (quiz.getTotalQuestions() == 0) {
                DialogUtil.showError("Invalid file", "No questions found in the selected file.");
                return;
            }
            App.setRoot(new QuizScreen(quiz));
        } catch (Exception ex) {
            DialogUtil.showError("Error", "Failed to parse quiz file.");
        }
    }
}
