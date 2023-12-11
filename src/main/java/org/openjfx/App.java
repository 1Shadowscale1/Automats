package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

public class App extends Application {
    private static Image mainIcon;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws IOException {
        URL url = App.class.getResource("/start.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();

        setUpStage(primaryStage, false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void setUpStage(Stage stage, boolean isMaximized) {
        stage.setTitle("Automaton");
        stage.setMaximized(isMaximized);
        stage.setResizable(false);

        if (mainIcon != null) {
            stage.getIcons().add(mainIcon);
            return;
        }

        InputStream iconStream = App.class.getResourceAsStream("/steam.png");

        assert iconStream != null;

        mainIcon = new Image(iconStream);
        stage.getIcons().add(mainIcon);
    }
}
