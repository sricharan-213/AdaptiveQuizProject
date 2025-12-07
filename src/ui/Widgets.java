package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Widgets {

    public static Button primaryButton(String text) {
        Button b = new Button(text);
        b.setMinHeight(40);
        b.setPadding(new Insets(8, 18, 8, 18));
        b.setStyle("-fx-background-color: linear-gradient(to bottom, #4CAF50, #45a049); " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand;");
        b.setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.15)));
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: linear-gradient(to bottom, #5CBF60, #4CAF50); " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color: linear-gradient(to bottom, #4CAF50, #45a049); " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand;"));
        return b;
    }
    // Secondary button (outline style)
public static Button secondaryButton(String text) {
    Button b = new Button(text);
    b.setStyle("""
        -fx-background-color: transparent;
        -fx-border-color: #1976D2;
        -fx-border-width: 2;
        -fx-text-fill: #1976D2;
        -fx-font-weight: bold;
        -fx-background-radius: 10;
        -fx-border-radius: 10;
    """);
    b.setOnMouseEntered(e -> b.setStyle("""
        -fx-background-color: #E3F2FD;
        -fx-border-color: #1976D2;
        -fx-border-width: 2;
        -fx-text-fill: #0D47A1;
        -fx-font-weight: bold;
        -fx-background-radius: 10;
        -fx-border-radius: 10;
    """));
    b.setOnMouseExited(e -> b.setStyle("""
        -fx-background-color: transparent;
        -fx-border-color: #1976D2;
        -fx-border-width: 2;
        -fx-text-fill: #1976D2;
        -fx-font-weight: bold;
        -fx-background-radius: 10;
        -fx-border-radius: 10;
    """));
    return b;
}

// Danger button (logout)
public static Button dangerButton(String text) {
    Button b = new Button(text);
    b.setStyle("""
        -fx-background-color: #E53935;
        -fx-text-fill: white;
        -fx-font-weight: bold;
        -fx-background-radius: 10;
    """);
    b.setOnMouseEntered(e -> b.setStyle("""
        -fx-background-color: #B71C1C;
        -fx-text-fill: white;
        -fx-font-weight: bold;
        -fx-background-radius: 10;
    """));
    b.setOnMouseExited(e -> b.setStyle("""
        -fx-background-color: #E53935;
        -fx-text-fill: white;
        -fx-font-weight: bold;
        -fx-background-radius: 10;
    """));
    return b;
}

    public static Button ghostButton(String text) {
        Button b = new Button(text);
        b.setMinHeight(36);
        b.setStyle("-fx-background-color: transparent; " +
                "-fx-text-fill: #2196F3; " +
                "-fx-font-size: 13px; " +
                "-fx-border-color: #2196F3; " +
                "-fx-border-width: 1.5px; " +
                "-fx-border-radius: 6px; " +
                "-fx-background-radius: 6px; " +
                "-fx-cursor: hand;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: #E3F2FD; " +
                "-fx-text-fill: #1976D2; " +
                "-fx-font-size: 13px; " +
                "-fx-border-color: #1976D2; " +
                "-fx-border-width: 1.5px; " +
                "-fx-border-radius: 6px; " +
                "-fx-background-radius: 6px; " +
                "-fx-cursor: hand;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color: transparent; " +
                "-fx-text-fill: #2196F3; " +
                "-fx-font-size: 13px; " +
                "-fx-border-color: #2196F3; " +
                "-fx-border-width: 1.5px; " +
                "-fx-border-radius: 6px; " +
                "-fx-background-radius: 6px; " +
                "-fx-cursor: hand;"));
        return b;
    }

    public static VBox card(String title, String body) {
        VBox box = new VBox(6);
        box.setPadding(new Insets(14));
        box.setStyle("-fx-background-color: linear-gradient(to bottom right, #E8F5E9, #C8E6C9); " +
                "-fx-background-radius: 12px; " +
                "-fx-border-color: #81C784; " +
                "-fx-border-width: 1px; " +
                "-fx-border-radius: 12px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label titleLbl = new Label(title);
        titleLbl.setStyle("-fx-text-fill: #2E7D32; " +
                "-fx-font-size: 12px; " +
                "-fx-font-weight: bold;");
        Label bodyLbl = new Label(body);
        bodyLbl.setStyle("-fx-text-fill: #1B5E20; " +
                "-fx-font-size: 20px; " +
                "-fx-font-weight: bold;");
        bodyLbl.setWrapText(true);

        box.getChildren().addAll(titleLbl, bodyLbl);
        return box;
    }

    public static HBox spacedRow() {
        HBox row = new HBox(10);
        row.setPadding(new Insets(10, 0, 10, 0));
        return row;
    }

    public static Region grow() {
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        return r;
    }
}


