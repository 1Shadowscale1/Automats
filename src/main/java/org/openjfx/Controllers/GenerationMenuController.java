package org.openjfx.Controllers;

import static org.openjfx.Controllers.AutomatonInputController.setupButtonAsReturnToStart;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GenerationMenuController {
    @FXML
    private Button backButton;

    @FXML
    private Button generateSyncAutomatonButton;

    @FXML
    private Button generateTasksButton;

    @FXML
    private Button generateAutomatonReverseButton;

    @FXML
    private Button generateAutomatonClosureButton;

    @FXML
    private void initialize() {
        setupButtonAsReturnToStart(backButton);
        setupTaskButton(generateSyncAutomatonButton, "/automatonSyncGenerationInput.fxml");
        setupTaskButton(generateTasksButton, "/tasksGenerationInput.fxml");
        setupTaskButton(generateAutomatonReverseButton, "/automatonReverseGenerationInput.fxml");
        setupTaskButton(generateAutomatonClosureButton, "/automatonClosureGenerationInput.fxml");
    }

    @FXML
    private void setupTaskButton(Button button, String fxmlName) {
        button.setOnAction(event -> {
            button.getScene().getWindow().hide();
            Loader.loadFxml(fxmlName, false);
        });
    }
}
