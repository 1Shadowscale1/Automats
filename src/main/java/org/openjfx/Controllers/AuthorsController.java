package org.openjfx.Controllers;

import static org.openjfx.Controllers.AutomatonInputController.setupButtonAsReturnToStart;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AuthorsController {
    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        setupButtonAsReturnToStart(backButton);
    }
}
