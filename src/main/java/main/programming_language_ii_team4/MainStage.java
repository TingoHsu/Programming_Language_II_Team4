package main.programming_language_ii_team4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class MainStage extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        Image icon = new Image(Objects.requireNonNull(getClass().getResource("/images/icon.png")).toExternalForm());
        FXMLLoader loginSceneLoader = new FXMLLoader(this.getClass().getResource("LoginScene.fxml"));
        Scene loginScene = new Scene(loginSceneLoader.load(), 600, 700);
        primaryStage.getIcons().add(icon);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Drezzy");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public static void changeScene(String fxmlFile) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainStage.class.getResource(fxmlFile));
        Scene scene = new Scene(loader.load(), 600, 700);
        if (fxmlFile != "LoginScene.fxml") {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(MainStage.class.getResource("/styles/" + User.getBackground() + ".css").toExternalForm());
        }
        primaryStage.setScene(scene);
    }
}