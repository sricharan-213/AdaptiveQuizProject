package ui;

import exceptions.UserNotFoundException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.App;
import user.UserManager;
import utils.DialogUtil;

public class LoginScreen extends BorderPane {

    public LoginScreen() {
        buildUi();
    }

    private void buildUi() {
        setPadding(new Insets(40));
        setStyle("-fx-background-color: linear-gradient(to bottom, #E3F2FD, #1844b1ff);");

        VBox center = new VBox(20);
        center.setAlignment(Pos.CENTER);
        center.setMaxWidth(400);
        center.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 16px; " +
                "-fx-padding: 40px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 4);");

        Label title = new Label("Adaptive Interactive Quiz System");
        title.setStyle("-fx-text-fill: #1a3550ff; " +
                "-fx-font-size: 24px; " +
                "-fx-font-weight: bold; " +
                "-fx-alignment: center;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setStyle("-fx-font-size: 14px; " +
                "-fx-padding: 10px; " +
                "-fx-background-radius: 8px; " +
                "-fx-border-color: #BDBDBD; " +
                "-fx-border-radius: 8px;");
        usernameField.setMaxWidth(Double.MAX_VALUE);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-font-size: 14px; " +
                "-fx-padding: 10px; " +
                "-fx-background-radius: 8px; " +
                "-fx-border-color: #BDBDBD; " +
                "-fx-border-radius: 8px;");
        passwordField.setMaxWidth(Double.MAX_VALUE);

        javafx.scene.control.Button loginBtn = Widgets.primaryButton("Login");
        loginBtn.setMaxWidth(Double.MAX_VALUE);

        Hyperlink signupLink = new Hyperlink("Create a new account");
        signupLink.setStyle("-fx-text-fill: #2196F3; " +
                "-fx-font-size: 13px; " +
                "-fx-cursor: hand;");
        signupLink.setOnMouseEntered(e -> signupLink.setStyle("-fx-text-fill: #1976D2; " +
                "-fx-font-size: 13px; " +
                "-fx-cursor: hand;"));
        signupLink.setOnMouseExited(e -> signupLink.setStyle("-fx-text-fill: #2196F3; " +
                "-fx-font-size: 13px; " +
                "-fx-cursor: hand;"));

        loginBtn.setOnAction(e -> {
            String u = usernameField.getText();
            String p = passwordField.getText();
            try {
                UserManager.getInstance().login(u, p);
                App.setRoot(new MainMenuScreen());
            } catch (UserNotFoundException ex) {
                DialogUtil.showError("Login failed", ex.getMessage());
            }
        });

        signupLink.setOnAction(e -> App.setRoot(new SignupScreen()));

        center.getChildren().addAll(title, usernameField, passwordField, loginBtn, signupLink);
        setCenter(center);
    }
}


