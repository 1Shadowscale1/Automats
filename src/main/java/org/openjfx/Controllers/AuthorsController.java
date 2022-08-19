package org.openjfx.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.openjfx.App;

import java.io.IOException;

public class AuthorsController {

    @FXML
    private Button backButton;

    @FXML
    void initialize() {
        backButton.setOnAction(event -> {
            backButton.getScene().getWindow().hide();

            var loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/start.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            App.setUpApplicationWindow(stage);
            stage.show();
        });
    }

}
