package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.App;
import user.User;
import user.UserManager;
import utils.TimeUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class HistoryScreen extends BorderPane {

    public static class HistoryRow {
        private final String quizName;
        private final String score;
        private final String percentage;
        private final String time;
        private final String date;
        private final User.QuizHistoryEntry historyEntry; // Store reference for click handling

        public HistoryRow(String quizName, String score, String percentage, String time, String date, User.QuizHistoryEntry historyEntry) {
            this.quizName = quizName;
            this.score = score;
            this.percentage = percentage;
            this.time = time;
            this.date = date;
            this.historyEntry = historyEntry;
        }

        public String getQuizName() {
            return quizName;
        }

        public String getScore() {
            return score;
        }

        public String getPercentage() {
            return percentage;
        }

        public String getTime() {
            return time;
        }

        public String getDate() {
            return date;
        }

        public User.QuizHistoryEntry getHistoryEntry() {
            return historyEntry;
        }
    }

    public HistoryScreen() {
        buildUi();
    }

    private void buildUi() {
        setPadding(new Insets(20));
        setStyle("-fx-background-color: linear-gradient(to bottom, #F3E5F5, #E1BEE7);");

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label title = new Label("Quiz History");
        title.setStyle("-fx-text-fill: #7B1FA2; " +
                "-fx-font-size: 32px; " +
                "-fx-font-weight: bold;");

        TableView<HistoryRow> table = new TableView<>();
        table.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 10px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        
        TableColumn<HistoryRow, String> quizCol = new TableColumn<>("Quiz");
        quizCol.setCellValueFactory(new PropertyValueFactory<>("quizName"));
        quizCol.setPrefWidth(200);
        TableColumn<HistoryRow, String> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        TableColumn<HistoryRow, String> pctCol = new TableColumn<>("Percentage");
        pctCol.setCellValueFactory(new PropertyValueFactory<>("percentage"));
        TableColumn<HistoryRow, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        TableColumn<HistoryRow, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setPrefWidth(150);

        table.getColumns().addAll(quizCol, scoreCol, pctCol, timeCol, dateCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(400);

        // Format date helper
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        
        User u = UserManager.getInstance().getCurrentUser();
        if (u != null) {
            for (User.QuizHistoryEntry h : u.getHistory()) {
                String quizName = h.getSubject() != null ? h.getSubject() + " Quiz" : "General Quiz";
                String score = h.getScore() + " / " + h.getTotalQuestions();
                double pct = h.getPercentage(); // Use stored percentage
                String time = TimeUtil.formatSeconds(h.getTotalTimeSeconds());
                
                // Format date
                String dateStr = "Unknown";
                try {
                    Instant instant = Instant.parse(h.getDateTimeIso());
                    LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    dateStr = dateTime.format(dateFormatter);
                } catch (Exception e) {
                    // Fallback to ISO string if parsing fails
                    dateStr = h.getDateTimeIso();
                }
                
                table.getItems().add(
                        new HistoryRow(quizName, score, String.format("%.1f%%", pct), time, dateStr, h)
                );
            }
        }

        // Make table rows clickable
        table.setRowFactory(tv -> {
            javafx.scene.control.TableRow<HistoryRow> row = new javafx.scene.control.TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    HistoryRow clickedRow = row.getItem();
                    if (clickedRow != null && clickedRow.getHistoryEntry() != null) {
                        // Navigate to results screen
                        ResultsScreen.showResultsFromHistory(clickedRow.getHistoryEntry());
                    }
                }
            });
            return row;
        });

        Button backBtn = Widgets.ghostButton("Back");
        backBtn.setOnAction(e -> App.setRoot(new MainMenuScreen()));

        root.getChildren().addAll(title, table, backBtn);
        setCenter(root);
    }
}


