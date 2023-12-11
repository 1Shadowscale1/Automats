package org.openjfx.Controllers;

import static org.openjfx.Controllers.AutomatonInputController.setupButtonAsReturnToStart;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SolveTasksMenuController {
    @FXML
    private Button backButton;

    @FXML
    private Button tableAutomatonInputButton;

    @FXML
    private Button tableAndRegexAutomatonInputButton;

    @FXML
    private Button tableAutomatonInputWithSyncCheckButton;

    @FXML
    private Button solveTaskInputButton;

    @FXML
    private void initialize() {
        setupButtonAsReturnToStart(backButton);
        setupTaskButton(tableAutomatonInputButton, "/automatonInput.fxml");
        setupTaskButton(tableAndRegexAutomatonInputButton, "/automatonAndRegexInput.fxml");
        setupTaskButton(tableAutomatonInputWithSyncCheckButton, "/automatonSyncInput.fxml");
        setupTaskButton(solveTaskInputButton, "/solveTaskInput.fxml");
    }

    @FXML
    private void setupTaskButton(Button button, String fxmlName) {
        button.setOnAction(event -> {
            button.getScene().getWindow().hide();
            Loader.loadFxml(fxmlName, false);
        });
    }
}
