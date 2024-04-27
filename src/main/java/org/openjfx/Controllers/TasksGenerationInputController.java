package org.openjfx.Controllers;

import algorithms.GeneratePDFTaskAndAnswer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

import static org.openjfx.Controllers.Controller.automatonList;

import java.io.IOException;

public class TasksGenerationInputController {

    @FXML
    private TextField optionsCountField;

    @FXML
    private TextField wordsCountField;

    @FXML
    private Button returnToPreviousStateButton;

    @FXML
    private Button generateTasksButton;

    @FXML
    private AnchorPane inputWindowMainPane;

    private Text inputCorrectnessText;

    @FXML
    void initialize() {
        setupButtonAsReturnToPreviousState(returnToPreviousStateButton);
        setupCountField(optionsCountField);
        setupCountField(wordsCountField);
        setupTaskButton(generateTasksButton);
    }

    @FXML
    public static void setupButtonAsReturnToPreviousState(Button button) {
        button.setOnAction(event -> {
            button.getScene().getWindow().hide();
            automatonList.clear();
            Loader.loadFxml("/generationMenu.fxml", false);
        });
    }

    private void setupCountField(TextField countField) {
        countField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                countField.setText(newValue.replaceAll("\\D", ""));
            }
        });
    }

    @FXML
    private void setupTaskButton(Button button) {
        button.setOnAction(event -> {
            if (!checkInputCorrectness())
                return;

            button.getScene().getWindow().hide();

            GeneratePDFTaskAndAnswer generatePDFTaskAndAnswer = new GeneratePDFTaskAndAnswer
                (Integer.parseInt(wordsCountField.getText()), Integer.parseInt(optionsCountField.getText()));
            generatePDFTaskAndAnswer.generateStringAndPattern();
            try {
                generatePDFTaskAndAnswer.generatePdfTask();
                generatePDFTaskAndAnswer.generatePdfAnswer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Alert informationDialog = new Alert(AlertType.INFORMATION);
            informationDialog.setTitle("Генерация вариантов");
            informationDialog.setHeaderText(null);
            informationDialog.setContentText("Варианты успешно сгенерированы! Их можно найти в директории tasks");
            informationDialog.showAndWait();

            Loader.loadFxml("/generationMenu.fxml", false);
        });
    }

    private boolean checkInputCorrectness() {
        inputWindowMainPane.getChildren().remove(inputCorrectnessText);
        if (optionsCountField.getText().isBlank() || wordsCountField.getText().isBlank())
            return false;

        int optionsCount = Integer.parseInt(optionsCountField.getText());
        int wordsCount = Integer.parseInt(wordsCountField.getText());

        if (optionsCount < 25 || optionsCount > 30) {
            inputCorrectnessText = new Text("Количество вариантов: от 25 до 30");
            inputCorrectnessText.setFill(Color.RED);
            inputCorrectnessText.setFont(Font.font("System", FontPosture.ITALIC, 14));
            AnchorPane.setBottomAnchor(inputCorrectnessText, 10.0);
            AnchorPane.setLeftAnchor(inputCorrectnessText, 10.0);
            inputWindowMainPane.getChildren().add(inputCorrectnessText);
            return false;
        }

        if (wordsCount < 1 || wordsCount > 3) {
            inputCorrectnessText = new Text("Количество слов в тексте: от 1 до 3");
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
