package ui;

import analytics.Analysis;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.App;
import quiz.Question;
import quiz.Quiz;
import ui.AnalyticsScreen;
import ui.MainMenuScreen;
import utils.TimeUtil;

import java.util.List;
import java.util.Map;

public class ResultsScreen extends BorderPane {

    private final Quiz quiz;
    private final Analysis analysis;

    public ResultsScreen(Quiz quiz, Analysis analysis) {
        this.quiz = quiz;
        this.analysis = analysis;
        buildUi();
    }

    private void buildUi() {
        setPadding(new Insets(20));
        setStyle("-fx-background-color: linear-gradient(to bottom, #E8F5E9, #C8E6C9);");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox center = new VBox(20);
        center.setPadding(new Insets(20));

        Label title = new Label("Quiz Results");
        title.setStyle("-fx-text-fill: #2E7D32; -fx-font-size: 32px; -fx-font-weight: bold;");

        // Score Cards
        HBox summaryCards = new HBox(15);
        summaryCards.setAlignment(Pos.CENTER);

        VBox scoreCard = createSummaryCard("Score", analysis.getScore() + " / " + analysis.getTotalQuestions(), "#4CAF50");
        VBox pctCard = createSummaryCard("Percentage", String.format("%.1f%%", analysis.getPercentage()), "#2196F3");
        VBox timeCard = createSummaryCard("Total Time", TimeUtil.formatSeconds(analysis.getTotalTimeSeconds()), "#FF9800");

        summaryCards.getChildren().addAll(scoreCard, pctCard, timeCard);

        // Per-question results
        Label questionsTitle = new Label("Question Results");
        questionsTitle.setStyle("-fx-text-fill: #1B5E20; -fx-font-size: 22px; -fx-font-weight: bold; -fx-padding: 10px 0 5px 0;");

        VBox questionsBox = new VBox(12);
        questionsBox.setPadding(new Insets(10));

        List<Question> questions = quiz.getQuestions();
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            boolean isCorrect = q.isCorrect();
            String selected = q.getSelectedOption() != null ? q.getSelectedOption() : "Not answered";
            String correct = q.getCorrectOption();

            HBox questionRow = new HBox(15);
            questionRow.setAlignment(Pos.CENTER_LEFT);
            questionRow.setPadding(new Insets(12));
            questionRow.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

            Circle statusCircle = new Circle(12);
            statusCircle.setFill(isCorrect ? Color.web("#4CAF50") : Color.web("#F44336"));

            VBox questionInfo = new VBox(5);
            Label qNumLabel = new Label("Q" + (i + 1) + ": " + q.getText());
            qNumLabel.setStyle("-fx-text-fill: #212121; -fx-font-size: 15px; -fx-font-weight: bold;");
            qNumLabel.setWrapText(true);

            Label resultLabel = new Label(
                    isCorrect ?
                            "✓ Correct - You selected: " + selected :
                            "✗ Wrong - You selected: " + selected + " | Correct answer: " + correct
            );
            resultLabel.setStyle(isCorrect
                    ? "-fx-text-fill: #2E7D32; -fx-font-size: 13px; -fx-font-weight: bold;"
                    : "-fx-text-fill: #C62828; -fx-font-size: 13px; -fx-font-weight: bold;"
            );

            Label topicLabel = new Label("Topic: " + q.getTopic());
            topicLabel.setStyle("-fx-text-fill: #757575; -fx-font-size: 12px;");

            questionInfo.getChildren().addAll(qNumLabel, resultLabel, topicLabel);
            questionRow.getChildren().addAll(statusCircle, questionInfo);
            questionsBox.getChildren().add(questionRow);
        }

        // Bar Chart Section
        Label chartTitle = new Label("Time per Question");
        chartTitle.setStyle("-fx-text-fill: #1B5E20; -fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 15px 0 5px 0;");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Question Number");
        yAxis.setLabel("Time (seconds)");

        // Dark axis text
        xAxis.setTickLabelFill(Color.web("#1A1A1A"));
        yAxis.setTickLabelFill(Color.web("#1A1A1A"));
        xAxis.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        yAxis.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        chart.setTitle("Time per Question (in seconds)");
        chart.setPrefHeight(350);

        // Dark chart title
        chart.lookup(".chart-title").setStyle("-fx-text-fill: #1A1A1A; -fx-font-size: 18px; -fx-font-weight: bold;");

        // Bar color
        chart.setStyle("-fx-bar-fill: #FF7043;");

        // Spacing for label visibility
        chart.setPadding(new Insets(25, 25, 55, 65));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<Integer, Long> e : analysis.getTimePerQuestion().entrySet()) {
            series.getData().add(new XYChart.Data<>("Q" + (e.getKey() + 1), e.getValue()));
        }
        chart.getData().add(series);

        // Weak topics & suggestions
        List<String> weakTopics = analysis.getWeakTopics();
        List<String> suggestions = analysis.getAdaptiveSuggestions();

        VBox weakBox = new VBox(8);
        weakBox.setPadding(new Insets(15));
        weakBox.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        Label weakTitle = new Label("Weak Topics:");
        weakTitle.setStyle("-fx-text-fill: #D32F2F; -fx-font-size: 16px; -fx-font-weight: bold;");
        weakBox.getChildren().add(weakTitle);
        if (weakTopics.isEmpty()) {
            Label noneLabel = new Label("None identified - Great job!");
            noneLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 13px;");
            weakBox.getChildren().add(noneLabel);
        } else {
            for (String t : weakTopics) {
                Label topicLabel = new Label("• " + t);
                topicLabel.setStyle("-fx-text-fill: #424242; -fx-font-size: 13px;");
                weakBox.getChildren().add(topicLabel);
            }
        }

        VBox suggBox = new VBox(8);
        suggBox.setPadding(new Insets(15));
        suggBox.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        Label suggTitle = new Label("Suggestions:");
        suggTitle.setStyle("-fx-text-fill: #1976D2; -fx-font-size: 16px; -fx-font-weight: bold;");
        suggBox.getChildren().add(suggTitle);
        for (String s : suggestions) {
            Label suggLabel = new Label("• " + s);
            suggLabel.setStyle("-fx-text-fill: #424242; -fx-font-size: 13px;");
            suggBox.getChildren().add(suggLabel);
        }

        // Bottom Buttons
        HBox bottomButtons = new HBox(10);
        bottomButtons.setAlignment(Pos.CENTER);
        Button homeBtn = Widgets.ghostButton("Back to Home");
        Button analyticsBtn = Widgets.primaryButton("Full Analytics");
        homeBtn.setOnAction(e -> App.setRoot(new MainMenuScreen()));
        analyticsBtn.setOnAction(e -> App.setRoot(new AnalyticsScreen()));
        bottomButtons.getChildren().addAll(homeBtn, analyticsBtn);

        // Add all to center
        center.getChildren().addAll(
                title, summaryCards, questionsTitle, questionsBox,
                chartTitle, chart, weakBox, suggBox, bottomButtons
        );

        scrollPane.setContent(center);
        setCenter(scrollPane);
    }

    private VBox createSummaryCard(String title, String value, String color) {
        VBox card = new VBox(5);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setMinWidth(150);
        card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 13px; -fx-opacity: 0.9;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }
}
