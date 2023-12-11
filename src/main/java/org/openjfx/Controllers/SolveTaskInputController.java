package org.openjfx.Controllers;

import static org.openjfx.Controllers.Controller.automatonList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class SolveTaskInputController {
    public static final List<String> inputData = new ArrayList<>();

    @FXML
    private TextField textField;

    @FXML
    private TextField patternField;

    @FXML
    private Button returnToPreviousStateButton;

    @FXML
    private Button bruteForceSolutionButton;

    @FXML
    private Button kmpSolutionButton;

    @FXML
    private AnchorPane inputWindowMainPane;

    private Text inputCorrectnessText;

    @FXML
    protected void initialize() {
        setupButtonAsReturnToPreviousState(returnToPreviousStateButton);
        setupTextField();
        setupPatternField();
        setupTaskButton(bruteForceSolutionButton, "/taskFive.fxml");
        setupTaskButton(kmpSolutionButton, "/taskSix.fxml");
    }

    @FXML
    private static void setupButtonAsReturnToPreviousState(Button button) {
        button.setOnAction(event -> {
            button.getScene().getWindow().hide();
            automatonList.clear();
            Loader.loadFxml("/solveTasksMenu.fxml", false);
        });
    }

    @FXML
    private void setupTaskButton(Button button, String fxmlName) {
        button.setOnAction(event -> {
            if (!checkInputCorrectness())
                return;
            String text = textField.getText();
            String pattern = patternField.getText();
            inputData.add(pattern);
            inputData.add(text);
            button.getScene().getWindow().hide();
            Loader.loadFxml(fxmlName, true);
        });
    }

    private void setupTextField() {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z ]*")) {
                textField.setText(newValue.replaceAll("[^a-zA-Z ]", ""));
            }
        });
    }

    private void setupPatternField() {
        patternField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z]*")) {
                patternField.setText(newValue.replaceAll("[^a-zA-Z]", ""));
            }
        });
    }

    private boolean checkInputCorrectness() {
        inputWindowMainPane.getChildren().remove(inputCorrectnessText);
        if (textField.getText().equals(" ") || textField.getText().isBlank() || patternField.getText().isBlank())
            return false;

        String[] words = textField.getText().split(" ");
        int wordsCount = words.length;
        int patternSymbolsCount = patternField.getText().length();
        int maxLength = -1;

        if (wordsCount > 3) {
            inputCorrectnessText = new Text("Текст может содержать максимум 3 слова");
            inputCorrectnessText.setFill(Color.RED);
            inputCorrectnessText.setFont(Font.font("System", FontPosture.ITALIC, 14));
            AnchorPane.setBottomAnchor(inputCorrectnessText, 10.0);
            AnchorPane.setLeftAnchor(inputCorrectnessText, 10.0);
            inputWindowMainPane.getChildren().add(inputCorrectnessText);
            return false;
        }

        for (String word : words) {
            if (word.length() > 7) {
                inputCorrectnessText = new Text("Одно слово может содержать максимум 7 символов");
                inputCorrectnessText.setFill(Color.RED);
                inputCorrectnessText.setFont(Font.font("System", FontPosture.ITALIC, 14));
                AnchorPane.setBottomAnchor(inputCorrectnessText, 10.0);
                AnchorPane.setLeftAnchor(inputCorrectnessText, 10.0);
                inputWindowMainPane.getChildren().add(inputCorrectnessText);
                return false;
            }
            if (word.length() > maxLength) {
                maxLength = word.length();
            }
        }

        if (patternSymbolsCount > 7 || patternSymbolsCount > maxLength) {
            if (patternSymbolsCount > 7) {
                inputCorrectnessText = new Text("Одно слово может содержать максимум 7 символов");
            } else {
                inputCorrectnessText = new Text("Длина подстроки не может превосходить длины любого слова в тексте");
            }
            inputCorrectnessText.setFill(Color.RED);
            inputCorrectnessText.setFont(Font.font("System", FontPosture.ITALIC, 14));
            AnchorPane.setBottomAnchor(inputCorrectnessText, 10.0);
            AnchorPane.setLeftAnchor(inputCorrectnessText, 10.0);
            inputWindowMainPane.getChildren().add(inputCorrectnessText);
            return false;
        }

        return true;
    }
}
