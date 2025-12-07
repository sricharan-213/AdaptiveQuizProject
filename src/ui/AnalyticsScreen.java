package ui;

import javafx.animation.*;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import main.App;
import user.User;
import user.UserManager;
import utils.TimeUtil;

public class AnalyticsScreen extends VBox {

    public AnalyticsScreen() {
        buildUi();
    }

    private void buildUi() {
        setPadding(new Insets(20));
        setStyle("-fx-background-color: linear-gradient(to bottom, #FFF3E0, #FFE0B2);");

        VBox root = this;
        root.setSpacing(20);
        root.setPadding(new Insets(20));

        Label title = new Label("Analytics");
        title.setStyle("-fx-text-fill: #E65100; " +
                "-fx-font-size: 32px; " +
                "-fx-font-weight: bold;");
        title.setOpacity(0);

        // Animated title entrance
        FadeTransition titleFade = new FadeTransition(Duration.millis(600), title);
        titleFade.setFromValue(0);
        titleFade.setToValue(1);
        
        TranslateTransition titleSlide = new TranslateTransition(Duration.millis(600), title);
        titleSlide.setFromY(-20);
        titleSlide.setToY(0);
        
        ParallelTransition titleAnim = new ParallelTransition(titleFade, titleSlide);
        titleAnim.play();

        User u = UserManager.getInstance().getCurrentUser();

        int totalQuizzes = u != null ? u.getTotalQuizzes() : 0;
        double avgScore = u != null ? u.getAverageScore() : 0;
        double avgPct = u != null ? u.getAveragePercentage() : 0;
        long totalTime = u != null ? u.getTotalTimeSeconds() : 0;

        // Stats cards with animations
        HBox statsCards = new HBox(15);
        statsCards.setAlignment(javafx.geometry.Pos.CENTER);

        VBox card1 = createStatCard("Total Quizzes", String.valueOf(totalQuizzes), "#FF9800", 0);
        VBox card2 = createStatCard("Avg Score", String.format("%.1f", avgScore), "#FF5722", 150);
        VBox card3 = createStatCard("Avg %", String.format("%.1f%%", avgPct), "#F57C00", 300);
        VBox card4 = createStatCard("Total Time", TimeUtil.formatSeconds(totalTime), "#E65100", 450);

        // Add interactive hover effects to cards
        addCardInteractivity(card1);
        addCardInteractivity(card2);
        addCardInteractivity(card3);
        addCardInteractivity(card4);

        statsCards.getChildren().addAll(card1, card2, card3, card4);

        // Trend graph: quiz index vs percentage
        Label chartTitle = new Label("Score Trend Over Time");
        chartTitle.setStyle("-fx-text-fill: #E65100; " +
                "-fx-font-size: 20px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px 0;");
        chartTitle.setOpacity(0);

        FadeTransition chartTitleFade = new FadeTransition(Duration.millis(600), chartTitle);
        chartTitleFade.setDelay(Duration.millis(600));
        chartTitleFade.setFromValue(0);
        chartTitleFade.setToValue(1);
        chartTitleFade.play();

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Quiz #");
        NumberAxis yAxis = new NumberAxis(0, 100, 10);
        yAxis.setLabel("Percentage");
        LineChart<Number, Number> trendChart = new LineChart<>(xAxis, yAxis);
        trendChart.setTitle("Score trend");
        trendChart.setLegendVisible(false);
        trendChart.setPrefHeight(400);
        trendChart.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 10px; " +
                "-fx-padding: 15px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        // Animate chart entrance
        trendChart.setOpacity(0);
        trendChart.setScaleX(0.9);
        trendChart.setScaleY(0.9);

        FadeTransition chartFade = new FadeTransition(Duration.millis(800), trendChart);
        chartFade.setDelay(Duration.millis(800));
        chartFade.setFromValue(0);
        chartFade.setToValue(1);

        ScaleTransition chartScale = new ScaleTransition(Duration.millis(800), trendChart);
        chartScale.setDelay(Duration.millis(800));
        chartScale.setFromX(0.9);
        chartScale.setFromY(0.9);
        chartScale.setToX(1.0);
        chartScale.setToY(1.0);
        chartScale.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition chartAnim = new ParallelTransition(chartFade, chartScale);
        chartAnim.play();

        // Add hover effect to chart
        DropShadow chartHoverShadow = new DropShadow();
        chartHoverShadow.setColor(Color.web("#E65100", 0.3));
        chartHoverShadow.setRadius(15);

        trendChart.setOnMouseEntered(e -> {
            trendChart.setEffect(chartHoverShadow);
            ScaleTransition st = new ScaleTransition(Duration.millis(200), trendChart);
            st.setToX(1.02);
            st.setToY(1.02);
            st.play();
        });

        trendChart.setOnMouseExited(e -> {
            DropShadow originalShadow = new DropShadow();
            originalShadow.setColor(Color.web("#000000", 0.1));
            originalShadow.setRadius(8);
            trendChart.setEffect(originalShadow);
            
            ScaleTransition st = new ScaleTransition(Duration.millis(200), trendChart);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        if (u != null) {
            int idx = 1;
            for (User.QuizHistoryEntry h : u.getHistory()) {
                double pct = (h.getTotalQuestions() == 0)
                        ? 0
                        : 100.0 * h.getScore() / h.getTotalQuestions();
                series.getData().add(new XYChart.Data<>(idx, pct));
                idx++;
            }
        }
        trendChart.getData().add(series);

        // Animate chart data points appearing
        if (!series.getData().isEmpty()) {
            Timeline dataAnimation = new Timeline();
            for (int i = 0; i < series.getData().size(); i++) {
                XYChart.Data<Number, Number> data = series.getData().get(i);
                final int index = i;
                
                KeyFrame kf = new KeyFrame(
                    Duration.millis(1000 + index * 100),
                    event -> {
                        if (data.getNode() != null) {
                            data.getNode().setScaleX(0);
                            data.getNode().setScaleY(0);
                            
                            ScaleTransition st = new ScaleTransition(Duration.millis(300), data.getNode());
                            st.setToX(1.2);
                            st.setToY(1.2);
                            st.setAutoReverse(true);
                            st.setCycleCount(2);
                            st.play();
                        }
                    }
                );
                dataAnimation.getKeyFrames().add(kf);
            }
            dataAnimation.play();
        }

        Button backBtn = Widgets.ghostButton("Back");
        backBtn.setOpacity(0);
        
        // Animate back button
        FadeTransition backFade = new FadeTransition(Duration.millis(600), backBtn);
        backFade.setDelay(Duration.millis(1200));
        backFade.setFromValue(0);
        backFade.setToValue(1);
        backFade.play();

        // Add hover effect to back button
        addButtonHoverEffect(backBtn);
        
        backBtn.setOnAction(e -> {
            playClickAnimation(backBtn);
            fadeOutAndNavigate(new MainMenuScreen());
        });

        root.getChildren().addAll(title, statsCards, chartTitle, trendChart, backBtn);
    }

    private VBox createStatCard(String title, String value, String color, int delayMs) {
        VBox card = new VBox(5);
        card.setAlignment(javafx.geometry.Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setMinWidth(140);
        card.setStyle("-fx-background-color: " + color + "; " +
                "-fx-background-radius: 12px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: white; " +
                "-fx-font-size: 13px; " +
                "-fx-opacity: 0.9;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: white; " +
                "-fx-font-size: 22px; " +
                "-fx-font-weight: bold;");

        card.getChildren().addAll(titleLabel, valueLabel);

        // Animate card entrance
        card.setOpacity(0);
        card.setTranslateY(30);
        card.setScaleX(0.8);
        card.setScaleY(0.8);

        FadeTransition fade = new FadeTransition(Duration.millis(500), card);
        fade.setDelay(Duration.millis(200 + delayMs));
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition translate = new TranslateTransition(Duration.millis(500), card);
        translate.setDelay(Duration.millis(200 + delayMs));
        translate.setFromY(30);
        translate.setToY(0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(500), card);
        scale.setDelay(Duration.millis(200 + delayMs));
        scale.setFromX(0.8);
        scale.setFromY(0.8);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition pt = new ParallelTransition(fade, translate, scale);
        pt.play();

        return card;
    }

    private void addCardInteractivity(VBox card) {
        DropShadow hoverShadow = new DropShadow();
        hoverShadow.setColor(Color.web("#E65100", 0.4));
        hoverShadow.setRadius(20);
        hoverShadow.setSpread(0.3);

        card.setOnMouseEntered(e -> {
            card.setEffect(hoverShadow);
            
            ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
            st.setToX(1.1);
            st.setToY(1.1);
            st.play();

            RotateTransition rt = new RotateTransition(Duration.millis(400), card);
            rt.setByAngle(5);
            rt.setAutoReverse(true);
            rt.setCycleCount(2);
            rt.play();
        });

        card.setOnMouseExited(e -> {
            DropShadow originalShadow = new DropShadow();
            originalShadow.setColor(Color.web("#000000", 0.2));
            originalShadow.setRadius(8);
            card.setEffect(originalShadow);
            
            ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();

            card.setRotate(0);
        });

        card.setOnMousePressed(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), card);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });

        card.setOnMouseReleased(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), card);
            st.setToX(1.1);
            st.setToY(1.1);
            st.play();
        });
    }

    private void addButtonHoverEffect(Button btn) {
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#E65100", 0.4));
        shadow.setRadius(15);
        shadow.setSpread(0.3);

        btn.setOnMouseEntered(e -> {
            btn.setEffect(shadow);
            ScaleTransition st = new ScaleTransition(Duration.millis(200), btn);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });

        btn.setOnMouseExited(e -> {
            btn.setEffect(null);
            ScaleTransition st = new ScaleTransition(Duration.millis(200), btn);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }

    private void playClickAnimation(Button btn) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), btn);
        st.setToX(0.95);
        st.setToY(0.95);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();
    }

    private void fadeOutAndNavigate(MainMenuScreen newScreen) {
        FadeTransition fade = new FadeTransition(Duration.millis(300), this);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> App.setRoot(newScreen));
        fade.play();
    }
}