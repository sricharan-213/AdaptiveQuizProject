package ui;

import leaderboard.Leaderboard;
import leaderboard.LeaderboardMode;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.App;
import user.UserManager;

public class LeaderboardScreen extends BorderPane {

    public static class Row {
        private final int rank;
        private final String username;
        private final String subject;
        private final double percentage;
        private final long timeSeconds;
        private final String date;

        public Row(int rank, String username, String subject, double percentage, long timeSeconds, String date) {
            this.rank = rank;
            this.username = username;
            this.subject = subject;
            this.percentage = percentage;
            this.timeSeconds = timeSeconds;
            this.date = date;
        }

        public int getRank() { return rank; }
        public String getUsername() { return username; }
        public String getSubject() { return subject; }
        public double getPercentage() { return percentage; }
        public long getTimeSeconds() { return timeSeconds; }
        public String getDate() { return date; }
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
        TableColumn<Row, String> subjectCol = new TableColumn<>("Subject");
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        TableColumn<Row, Double> pctCol = new TableColumn<>("Percentage");
        pctCol.setCellValueFactory(new PropertyValueFactory<>("percentage"));
        TableColumn<Row, Long> timeCol = new TableColumn<>("Time (s)");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("timeSeconds"));
        TableColumn<Row, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        table.getColumns().addAll(rankCol, userCol, subjectCol, pctCol, timeCol, dateCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(400);

        Leaderboard lb = new Leaderboard();
        // Get all unique subjects
        java.util.Set<String> subjects = lb.getAllSubjects();

        javafx.scene.control.ComboBox<String> subjectBox = new javafx.scene.control.ComboBox<>();
        subjectBox.getItems().addAll(subjects);
        if (!subjects.isEmpty()) subjectBox.getSelectionModel().selectFirst();
        
        // Mode selector: First Attempt or Best Attempt
        javafx.scene.control.ComboBox<LeaderboardMode> modeBox = new javafx.scene.control.ComboBox<>();
        modeBox.getItems().addAll(LeaderboardMode.FIRST_ATTEMPT, LeaderboardMode.BEST_ATTEMPT);
        modeBox.getSelectionModel().select(LeaderboardMode.BEST_ATTEMPT); // Default to Best Attempt
        modeBox.setConverter(new javafx.util.StringConverter<LeaderboardMode>() {
            @Override
            public String toString(LeaderboardMode mode) {
                if (mode == null) return "";
                return mode == LeaderboardMode.FIRST_ATTEMPT ? "First Attempt" : "Best Attempt";
            }
            @Override
            public LeaderboardMode fromString(String string) {
                return "First Attempt".equals(string) ? LeaderboardMode.FIRST_ATTEMPT : LeaderboardMode.BEST_ATTEMPT;
            }
        });
        
        Label rankLbl = new Label();
        rankLbl.setStyle("-fx-text-fill: #D32F2F; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px; " +
                "-fx-background-color: white; " +
                "-fx-background-radius: 8px;");
        
        // Helper method to update table and rank label for a subject and mode
        java.util.function.BiConsumer<String, LeaderboardMode> updateLeaderboard = (String sel, LeaderboardMode mode) -> {
            if (sel == null || mode == null) {
                table.getItems().clear();
                rankLbl.setText("");
                return;
            }
            // Get sorted entries for this subject and mode
            java.util.List<leaderboard.LeaderboardEntry> subjectEntries = lb.getEntriesForSubject(sel, mode);
            table.getItems().clear();
            int r = 1;
            for (leaderboard.LeaderboardEntry e : subjectEntries) {
                table.getItems().add(new Row(r, e.getUsername(), e.getSubject(), e.getPercentage(), e.getTotalTimeSeconds(), e.getDateTimeIso()));
                r++;
            }
            // Update current user rank label
            String currentUserLocal = UserManager.getInstance().getCurrentUser() != null
                    ? UserManager.getInstance().getCurrentUser().getUsername()
                    : null;
            if (currentUserLocal != null) {
                int subjectRank = lb.getRankForUserInSubject(currentUserLocal, sel, mode);
                if (subjectRank > 0) {
                    String modeText = mode == LeaderboardMode.FIRST_ATTEMPT ? "First Attempt" : "Best Attempt";
                    rankLbl.setText("Your rank in " + sel + " (" + modeText + "): " + subjectRank);
                } else {
                    rankLbl.setText("You are not on the " + sel + " leaderboard yet.");
                }
            } else {
                rankLbl.setText("");
            }
        };

        // Update when subject changes
        subjectBox.setOnAction(evt -> {
            String sel = subjectBox.getSelectionModel().getSelectedItem();
            LeaderboardMode mode = modeBox.getSelectionModel().getSelectedItem();
            updateLeaderboard.accept(sel, mode);
        });

        // Update when mode changes
        modeBox.setOnAction(evt -> {
            String sel = subjectBox.getSelectionModel().getSelectedItem();
            LeaderboardMode mode = modeBox.getSelectionModel().getSelectedItem();
            updateLeaderboard.accept(sel, mode);
        });

        // Populate initial
        String initialSubject = subjectBox.getSelectionModel().getSelectedItem();
        LeaderboardMode initialMode = modeBox.getSelectionModel().getSelectedItem();
        updateLeaderboard.accept(initialSubject, initialMode);

        Button backBtn = Widgets.ghostButton("Back");
        backBtn.setOnAction(e -> App.setRoot(new MainMenuScreen()));

        // Create controls row with labels
        Label subjectLabel = new Label("Subject:");
        subjectLabel.setStyle("-fx-text-fill: #424242; -fx-font-size: 14px; -fx-font-weight: bold;");
        Label modeLabel = new Label("Mode:");
        modeLabel.setStyle("-fx-text-fill: #424242; -fx-font-size: 14px; -fx-font-weight: bold;");
        
        HBox controlsBox = new HBox(10);
        controlsBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        controlsBox.getChildren().addAll(subjectLabel, subjectBox, modeLabel, modeBox);

        VBox topBox = new VBox(10, title, controlsBox);
        root.getChildren().addAll(topBox, table, rankLbl, backBtn);
        setCenter(root);
    }
}


