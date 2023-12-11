package org.openjfx.Controllers;

import automaton.Automaton;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.ArrayList;

public class Controller {
    public static final ArrayList<Automaton> automatonList = new ArrayList<>();

    @FXML
    private Button solveTasksMenuButton;

    @FXML
    private Button generationMenuButton;

    @FXML
    private Button authorsButton;

    @FXML
    private Button exitButton;

    @FXML
    private void initialize() {
        setupTaskButton(solveTasksMenuButton, "/solveTasksMenu.fxml");
        setupTaskButton(generationMenuButton, "/generationMenu.fxml");
        initAuthorsButton();
        initExitButton();
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

    @FXML
    private void initExitButton() {
        exitButton.setOnAction(event -> {
            Platform.exit();
        });
    }
}
