package ui;

import leaderboard.Leaderboard;
import leaderboard.LeaderboardEntry;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.App;
import user.UserManager;

public class LeaderboardScreen extends BorderPane {

    public static class Row {
        private final int rank;
        private final String username;
        private final int totalScore;
        private final int quizzesTaken;
        private final double averageScore;

        public Row(int rank, String username, int totalScore, int quizzesTaken, double averageScore) {
            this.rank = rank;
            this.username = username;
            this.totalScore = totalScore;
            this.quizzesTaken = quizzesTaken;
            this.averageScore = averageScore;
        }

        public int getRank() {
            return rank;
        }

        public String getUsername() {
            return username;
        }

        public int getTotalScore() {
            return totalScore;
        }

        public int getQuizzesTaken() {
            return quizzesTaken;
        }

        public double getAverageScore() {
            return averageScore;
        }
    }

    public LeaderboardScreen() {
        buildUi();
    }

    private void buildUi() {
        setPadding(new Insets(20));
        setStyle("-fx-background-color: linear-gradient(to bottom, #FFEBEE, #FFCDD2);");

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label title = new Label("Leaderboard");
        title.setStyle("-fx-text-fill: #C62828; " +
                "-fx-font-size: 32px; " +
                "-fx-font-weight: bold;");

        TableView<Row> table = new TableView<>();
        table.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 10px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        TableColumn<Row, Integer> rankCol = new TableColumn<>("Rank");
        rankCol.setCellValueFactory(new PropertyValueFactory<>("rank"));
        TableColumn<Row, String> userCol = new TableColumn<>("User");
        userCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<Row, Integer> totalCol = new TableColumn<>("Total Score");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalScore"));
        TableColumn<Row, Integer> quizzesCol = new TableColumn<>("Quizzes");
        quizzesCol.setCellValueFactory(new PropertyValueFactory<>("quizzesTaken"));
        TableColumn<Row, Double> avgCol = new TableColumn<>("Avg Score");
        avgCol.setCellValueFactory(new PropertyValueFactory<>("averageScore"));

        table.getColumns().addAll(rankCol, userCol, totalCol, quizzesCol, avgCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(400);

        Leaderboard lb = new Leaderboard();
        int rank = 1;
        for (LeaderboardEntry e : lb.getEntries()) {
            table.getItems().add(new Row(rank, e.getUsername(), e.getTotalScore(),
                    e.getQuizzesTaken(), e.getAverageScore()));
            rank++;
        }

        String currentUser = UserManager.getInstance().getCurrentUser() != null
                ? UserManager.getInstance().getCurrentUser().getUsername()
                : null;
        Label rankLbl = new Label();
        rankLbl.setStyle("-fx-text-fill: #D32F2F; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px; " +
                "-fx-background-color: white; " +
                "-fx-background-radius: 8px;");
        if (currentUser != null) {
            int r = lb.getRankForUser(currentUser);
            double pct = lb.getPercentileForUser(currentUser);
            if (r > 0) {
                rankLbl.setText("Your rank: " + r + " (" + String.format("Top %.1f%%", pct) + ")");
            } else {
                rankLbl.setText("You are not on the leaderboard yet.");
            }
        }

        Button backBtn = Widgets.ghostButton("Back");
        backBtn.setOnAction(e -> App.setRoot(new MainMenuScreen()));

        root.getChildren().addAll(title, table, rankLbl, backBtn);
        setCenter(root);
    }
}


