package org.openjfx.Controllers;

import org.openjfx.App;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Loader {
    public static void loadFxml(String fxmlPath, boolean isMaximized) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Controller.class.getResource(fxmlPath));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        App.setUpStage(stage, isMaximized);
        stage.show();
    }

    public static void loadFxmlStartupPage() {
        loadFxml("/start.fxml", false);
    }

    public static void showStage(Scene scene, boolean isMaximized) {
        Stage stage = new Stage();
        App.setUpStage(stage, isMaximized);
        stage.setScene(scene);
        stage.show();
    }
}
