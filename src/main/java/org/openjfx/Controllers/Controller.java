package org.openjfx.Controllers;

import automat.Automat;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.ArrayList;

public class Controller {

    public static final ArrayList<Automat> automatonList = new ArrayList<>();

    @FXML
    private Button authorsButton;

    @FXML
    private Button taskOneButton;

    @FXML
    private Button taskTwoButton;

    @FXML
    void initialize() {
        initAuthorsButton();
        initTaskOneButton();
        initTaskTwoButton();
    }

    @FXML
    private void initTaskOneButton() {
        taskOneButton.setOnAction(event -> {
            taskOneButton.getScene().getWindow().hide();
            automatonList.clear();
            Loader.loadFxml("/automatonInput.fxml");
        });
    }

    @FXML
    private void initTaskTwoButton() {
        taskTwoButton.setOnAction(event -> {
            taskTwoButton.getScene().getWindow().hide();
            automatonList.clear();
            Loader.loadFxml("/automatonAndRegexInput.fxml");
        });
    }

    @FXML
    private void initAuthorsButton() {
        authorsButton.setOnAction(event -> {
            authorsButton.getScene().getWindow().hide();
            Loader.loadFxml("/authors.fxml");
        });
    }
}

