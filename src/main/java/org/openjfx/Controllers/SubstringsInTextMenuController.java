package org.openjfx.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import static org.openjfx.Controllers.AutomatonInputController.setupButtonAsReturnToStart;

public class SubstringsInTextMenuController {

    @FXML
    private Button backButton;

    @FXML
    private Button generateTasksButton;

    @FXML
    private Button solveTaskInputButton;

    @FXML
    void initialize() {
        setupButtonAsReturnToStart(backButton);
        setupTaskButton(solveTaskInputButton, "/solveTaskInput.fxml");
        setupTaskButton(generateTasksButton, "/tasksGenerationInput.fxml");
    }

    @FXML
    private void setupTaskButton(Button button, String fxmlName) {
        button.setOnAction(event -> {
            button.getScene().getWindow().hide();
            Loader.loadFxml(fxmlName, false);
        });
    }
}
