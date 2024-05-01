package org.openjfx.Controllers;

import static org.openjfx.Controllers.TasksGenerationInputController.setupButtonAsReturnToPreviousState;

import java.io.IOException;

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

import algorithms.ReverseAutomatonsGenerator;

public class AutomatonReverseGenerationInputController {
    @FXML
    private Button returnToPreviousStateButton;

    @FXML
    private TextField optionsCountField;

    @FXML
    private TextField statesCountField;

    @FXML
    private TextField lettersCountField;

    @FXML
    private Button generateTasksButton;

    @FXML
    private AnchorPane inputWindowMainPane;

    private Text inputCorrectnessText;

    @FXML
    void initialize() {
        setupButtonAsReturnToPreviousState(returnToPreviousStateButton);
        setupCountField(optionsCountField);
        setupCountField(statesCountField);
        setupCountField(lettersCountField);
        setupTaskButton(generateTasksButton);
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

            ReverseAutomatonsGenerator reverseAutomatonsGenerator = new ReverseAutomatonsGenerator(
                    Integer.parseInt(optionsCountField.getText()),
                    Integer.parseInt(statesCountField.getText()),
                    Integer.parseInt(lettersCountField.getText()));
            reverseAutomatonsGenerator.generateReverseAutomatons();
            try {
                reverseAutomatonsGenerator.createTasksPdfFile();
                reverseAutomatonsGenerator.createAnswersPdfFile();
            } catch (IOException e) {
                throw new RuntimeException();
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
        if (optionsCountField.getText().isBlank() || statesCountField.getText().isBlank()
                || lettersCountField.getText().isBlank())
            return false;

        int optionsCount = Integer.parseInt(optionsCountField.getText());
        int statesCount = Integer.parseInt(statesCountField.getText());
        int lettersCount = Integer.parseInt(lettersCountField.getText());

        if (optionsCount < 25 || optionsCount > 30) {
            inputCorrectnessText = new Text("Количество вариантов: от 25 до 30");
            inputCorrectnessText.setFill(Color.RED);
            inputCorrectnessText.setFont(Font.font("System", FontPosture.ITALIC, 14));
            AnchorPane.setBottomAnchor(inputCorrectnessText, 10.0);
            AnchorPane.setLeftAnchor(inputCorrectnessText, 10.0);
            inputWindowMainPane.getChildren().add(inputCorrectnessText);
            return false;
        }

        if (statesCount < 2 || statesCount > 5) {
            inputCorrectnessText = new Text("Количество состояний в автомате: от 2 до 5");
            inputCorrectnessText.setFill(Color.RED);
            inputCorrectnessText.setFont(Font.font("System", FontPosture.ITALIC, 14));
            AnchorPane.setBottomAnchor(inputCorrectnessText, 10.0);
            AnchorPane.setLeftAnchor(inputCorrectnessText, 10.0);
            inputWindowMainPane.getChildren().add(inputCorrectnessText);
            return false;
        }

        if (lettersCount < 1 || lettersCount > 4) {
            inputCorrectnessText = new Text("Количество букв в алфавите: от 1 до 3");
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
