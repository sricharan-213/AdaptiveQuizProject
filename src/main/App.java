package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ui.LoginScreen;
import user.UserManager;
import utils.FileHandler;

public class App extends Application {

    private static Stage primaryStage;
    private static Scene mainScene;
    private static final int WIDTH = 1100;
    private static final int HEIGHT = 700;

    @Override
    public void start(Stage stage) {
        FileHandler.ensureBaseDirectories();
        UserManager.getInstance().loadAllUsers();

        primaryStage = stage;
        LoginScreen loginScreen = new LoginScreen();
        mainScene = new Scene(loginScreen, WIDTH, HEIGHT);

        primaryStage.setTitle("Adaptive Interactive Quiz System");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void setRoot(Pane root) {
        if (mainScene == null) {
            mainScene = new Scene(root, WIDTH, HEIGHT);
        } else {
            mainScene.setRoot(root);
        }
        primaryStage.setScene(mainScene);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}


