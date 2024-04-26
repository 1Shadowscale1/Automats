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
    private Button tableAutomatonInputWithSyncCheckButton;

    @FXML
    private Button solveTaskInputButton;

    @FXML
    private Button regexInputButton;

    @FXML
    private Button tableAutomatonInputWithReverseCheckButton;

    @FXML
    private Button solveClosuresTasksMenuButton;

    @FXML
    private void initialize() {
        setupButtonAsReturnToStart(backButton);
        setupTaskButton(tableAutomatonInputButton, "/automatonInput.fxml");
        setupTaskButton(tableAutomatonInputWithSyncCheckButton, "/automatonSyncInput.fxml");
        setupTaskButton(solveTaskInputButton, "/solveTaskInput.fxml");
        setupStub(regexInputButton);
        setupTaskButton(tableAutomatonInputWithReverseCheckButton, "/automatonReverseInput.fxml");
        setupTaskButton(solveClosuresTasksMenuButton, "/solveClosuresTasksMenu.fxml");
    }

    @FXML
    private void setupTaskButton(Button button, String fxmlName) {
        button.setOnAction(event -> {
            button.getScene().getWindow().hide();
            Loader.loadFxml(fxmlName, false);
        });
    }

    @FXML
    private void setupStub(Button button) {
        button.setOnAction(event -> {
            button.getScene().getWindow().hide();
            RegexInputController stub = new RegexInputController();
            stub.initialize();
        });
    }
}
