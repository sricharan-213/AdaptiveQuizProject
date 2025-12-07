package ui;

import exceptions.InvalidInputException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.App;
import user.User;
import user.UserManager;
import utils.DialogUtil;
import utils.Validator;

public class SignupScreen extends BorderPane {

    public SignupScreen() {
        buildUi();
    }

    private void buildUi() {
        setPadding(new Insets(40));
        setStyle("-fx-background-color: linear-gradient(to bottom, #FFF3E0, #FFE0B2);");

        VBox center = new VBox(20);
        center.setAlignment(Pos.CENTER);
        center.setMaxWidth(400);
        center.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 16px; " +
                "-fx-padding: 40px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 4);");

        Label title = new Label("Create an account");
        title.setStyle("-fx-text-fill: #F57C00; " +
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
        passwordField.setPromptText("Password (min 4 chars)");
        passwordField.setStyle("-fx-font-size: 14px; " +
                "-fx-padding: 10px; " +
                "-fx-background-radius: 8px; " +
                "-fx-border-color: #BDBDBD; " +
                "-fx-border-radius: 8px;");
        passwordField.setMaxWidth(Double.MAX_VALUE);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setStyle("-fx-font-size: 14px; " +
                "-fx-padding: 10px; " +
                "-fx-background-radius: 8px; " +
                "-fx-border-color: #BDBDBD; " +
                "-fx-border-radius: 8px;");
        emailField.setMaxWidth(Double.MAX_VALUE);

        javafx.scene.control.Button signupBtn = Widgets.primaryButton("Sign up");
        signupBtn.setMaxWidth(Double.MAX_VALUE);

        Hyperlink loginLink = new Hyperlink("Already have an account? Log in");
        loginLink.setStyle("-fx-text-fill: #FF9800; " +
                "-fx-font-size: 13px; " +
                "-fx-cursor: hand;");
        loginLink.setOnMouseEntered(e -> loginLink.setStyle("-fx-text-fill: #F57C00; " +
                "-fx-font-size: 13px; " +
                "-fx-cursor: hand;"));
        loginLink.setOnMouseExited(e -> loginLink.setStyle("-fx-text-fill: #FF9800; " +
                "-fx-font-size: 13px; " +
                "-fx-cursor: hand;"));

        signupBtn.setOnAction(e -> {
            String u = usernameField.getText();
            String p = passwordField.getText();
            String em = emailField.getText();
            try {
                Validator.validateSignup(u, p, em);
                User user = UserManager.getInstance().signup(u, p, em);
                if (user == null) {
                    DialogUtil.showError("Sign up failed", "Username already exists.");
                } else {
                    DialogUtil.showInfo("Account created", "You can now log in.");
                    App.setRoot(new LoginScreen());
                }
            } catch (InvalidInputException ex) {
                DialogUtil.showError("Invalid input", ex.getMessage());
            }
        });

        loginLink.setOnAction(e -> App.setRoot(new LoginScreen()));

        center.getChildren().addAll(title, usernameField, passwordField, emailField, signupBtn, loginLink);
        setCenter(center);
    }
}


