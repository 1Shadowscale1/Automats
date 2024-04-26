package org.openjfx.Controllers;

import static org.openjfx.Controllers.AutomatonInputController.setupButtonAsReturnToPreviousState;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SolveClosuresTasksMenuController {
    @FXML
    private Button backButton;

    @FXML
    private Button tableAutomatonInputWithPrefixClosureCheckButton;

    @FXML
    private Button tableAutomatonInputWithSuffixClosureCheckButton;

    @FXML
    private Button tableAutomatonInputWithSubwordClosureCheckButton;

    @FXML
    private void initialize() {
        setupButtonAsReturnToPreviousState(backButton);
        setupTaskButton(tableAutomatonInputWithPrefixClosureCheckButton, "/automatonPrefixClosureInput.fxml");
        setupTaskButton(tableAutomatonInputWithSuffixClosureCheckButton, "/automatonSuffixClosureInput.fxml");
        setupTaskButton(tableAutomatonInputWithSubwordClosureCheckButton, "/automatonSubwordClosureInput.fxml");
    }

    @FXML
    private void setupTaskButton(Button button, String fxmlName) {
        button.setOnAction(event -> {
            button.getScene().getWindow().hide();
            Loader.loadFxml(fxmlName, false);
        });
    }
}
