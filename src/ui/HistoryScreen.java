package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.App;
import user.User;
import user.UserManager;
import utils.TimeUtil;

public class HistoryScreen extends BorderPane {

    public static class HistoryRow {
        private final String dateTime;
        private final String score;
        private final String percentage;
        private final String time;

        public HistoryRow(String dateTime, String score, String percentage, String time) {
            this.dateTime = dateTime;
            this.score = score;
            this.percentage = percentage;
            this.time = time;
        }

        public String getDateTime() {
            return dateTime;
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
        TableColumn<HistoryRow, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        dateCol.setStyle("-fx-background-color: #E1BEE7;");
        TableColumn<HistoryRow, String> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        TableColumn<HistoryRow, String> pctCol = new TableColumn<>("Percentage");
        pctCol.setCellValueFactory(new PropertyValueFactory<>("percentage"));
        TableColumn<HistoryRow, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        table.getColumns().addAll(dateCol, scoreCol, pctCol, timeCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(400);

        User u = UserManager.getInstance().getCurrentUser();
        if (u != null) {
            for (User.QuizHistoryEntry h : u.getHistory()) {
                String score = h.getScore() + "/" + h.getTotalQuestions();
                double pct = (h.getTotalQuestions() == 0)
                        ? 0
                        : 100.0 * h.getScore() / h.getTotalQuestions();
                table.getItems().add(
                        new HistoryRow(h.getDateTimeIso(), score, String.format("%.1f%%", pct),
                                TimeUtil.formatSeconds(h.getTotalTimeSeconds()))
                );
            }
        }

        Button backBtn = Widgets.ghostButton("Back");
        backBtn.setOnAction(e -> App.setRoot(new MainMenuScreen()));

        root.getChildren().addAll(title, table, backBtn);
        setCenter(root);
    }
}


