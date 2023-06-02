package org.openjfx.Controllers;

import automaton.Automaton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.ArrayList;

public class Controller {

    public static final ArrayList<Automaton> automatonList = new ArrayList<>();

    @FXML
    private Button authorsButton;

    @FXML
    private Button generateSyncAutomatonButton;

    @FXML
    private Button tableAndRegexAutomatonInputButton;

    @FXML
    private Button tableAutomatonInputButton;

    @FXML
    private Button tableAutomatonInputWithSyncCheckButton;

    @FXML
    private Button searchSubstringsInTextButton;

    @FXML
    void initialize() {
        initAuthorsButton();
        setupTaskButton(tableAutomatonInputButton, "/automatonInput.fxml");
        setupTaskButton(tableAndRegexAutomatonInputButton, "/automatonAndRegexInput.fxml");
        setupTaskButton(tableAutomatonInputWithSyncCheckButton, "/automatonSyncInput.fxml");
        setupTaskButton(generateSyncAutomatonButton, "/automatonSyncGenerationInput.fxml");
        setupTaskButton(searchSubstringsInTextButton, "/substringsInTextMenu.fxml");
    }

    @FXML
    private void setupTaskButton(Button button, String fxmlName) {
        button.setOnAction(event -> {
            button.getScene().getWindow().hide();
            automatonList.clear();
            Loader.loadFxml(fxmlName, false);
        });
    }

    @FXML
    private void initAuthorsButton() {
        authorsButton.setOnAction(event -> {
            authorsButton.getScene().getWindow().hide();
            Loader.loadFxml("/authors.fxml", false);
        });
    }
}


