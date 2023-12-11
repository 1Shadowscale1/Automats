package org.openjfx.Controllers;

import algorithms.SynchronizedAutomatonsGenerator;
import automaton.SynchronizedAutomaton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

import static org.openjfx.Controllers.Controller.automatonList;

public class AutomatonSyncGenerationInputController extends AutomatonInputController {

    @FXML
    private TextField alphabetField;

    @FXML
    private Button generateSyncAutomatonButton;

    @FXML
    protected void initialize() {
        setupButtonAsReturnToPreviousState(returnToPreviousStateButton);
        setupStatesCountField();
        this.setupAlphabetField(this.alphabetField);
        setupGenerateSyncAutomatonButton(generateSyncAutomatonButton);
    }

    @FXML
    public static void setupButtonAsReturnToPreviousState(Button button) {
        button.setOnAction(event -> {
            button.getScene().getWindow().hide();
            automatonList.clear();
            Loader.loadFxml("/generationMenu.fxml", false);
        });
    }

    @FXML
    private void setupGenerateSyncAutomatonButton(Button button) {
        button.setOnAction(event -> {
            if (!checkAlphabetAndStatesCorrectness())
                return;
            SynchronizedAutomaton generatedAutomaton = null;
            int stateCount = Integer.parseInt(statesCountField.getText());
            int letterCount = Integer.parseInt(alphabetField.getText());
            try {
                generatedAutomaton = SynchronizedAutomatonsGenerator.synchronizedAutomatonsGenerator(1, stateCount, letterCount).get(0);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            automatonList.add(generatedAutomaton);

            button.getScene().getWindow().hide();

            Loader.loadFxml("/taskFour.fxml", true);
        });
    }

    protected void setupAlphabetField(TextField alphabetField) {
        alphabetField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]*")) {
                alphabetField.setText(newValue.replaceAll("[^0-9]", ""));
            }
        });
    }

    protected boolean checkAlphabetAndStatesCorrectness() {
        inputWindowMainPane.getChildren().remove(inputCorrectnessText);
        if (statesCountField.getText().isBlank() || alphabetField.getText().isBlank())
            return false;

        int states = Integer.parseInt(statesCountField.getText());
        int lettersCount = Integer.parseInt(alphabetField.getText());

        if (states < 2 || states > 10) {
            if (states < 2) {
                inputCorrectnessText = new Text("В синхронизируемом автомате не может быть меньше 2-х состояний");
            } else {
                inputCorrectnessText = new Text("В сгенерированном автомате не может быть больше 10-ти состояний");
            }
            inputCorrectnessText.setFill(Color.RED);
            inputCorrectnessText.setFont(Font.font("System", FontPosture.ITALIC, 14));
            AnchorPane.setBottomAnchor(inputCorrectnessText, 10.0);
            AnchorPane.setLeftAnchor(inputCorrectnessText, 10.0);
            inputWindowMainPane.getChildren().add(inputCorrectnessText);
            return false;
        }

        if (lettersCount == 0 || lettersCount > 4) {
            if (lettersCount == 0) {
                inputCorrectnessText = new Text("Алфавит не может быть пустым");
            }
            else {
                inputCorrectnessText = new Text("Алфавит не может содержать больше 4-х букв");
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
