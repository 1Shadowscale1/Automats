package org.openjfx.Controllers;

import static org.openjfx.Controllers.Controller.automatonList;

import java.util.Arrays;

import com.google.common.collect.HashBasedTable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import algorithms.Closures;
import automaton.Automaton;

public class AutomatonPrefixClosureInputController extends AutomatonInputController {
    @FXML
    protected void initialize() {
        setupButtonAsReturnToPreviousState(returnToPreviousStateButton);
        initCreateTableButton();
        setupStatesCountField();
        setupAlphabetField();
        inputCorrectnessText = new Text();
    }

    @FXML
    public static void setupButtonAsReturnToPreviousState(Button button) {
        button.setOnAction(event -> {
            button.getScene().getWindow().hide();
            automatonList.clear();
            Loader.loadFxml("/solveClosuresTasksMenu.fxml", false);
        });
    }

    protected Button getCreateAutomatonButton(TextField startVertexTextField, TextField finalVerticesTextField,
            TableView<String[]> automatonTableView, String[] states, String[] alphabet) {
        Button button = new Button("Создать автомат");
        button.setOnAction(event2 -> {
            String[][] automatonJumpTable = new String[automatonTableView.getItems().size()][automatonTableView
                    .getColumns().size()];
            ObservableList<String[]> items = automatonTableView.getItems();
            String startVertex = startVertexTextField.getText().strip();
            String[] finalVertices = finalVerticesTextField.getText().split(",");

            for (int i = 0; i < states.length; i++) {
                states[i] = items.get(i)[0];
            }

            tableWindowMainPane.getChildren().remove(inputCorrectnessText);
            if (!checkInputCorrectness(startVertex, finalVertices, states, automatonTableView)) {
                tableWindowMainPane.getChildren().add(inputCorrectnessText);
                tableWindowMainPane.requestLayout();
                return;
            }

            for (int i = 0; i < items.size(); i++) {
                System.arraycopy(items.get(i), 0, automatonJumpTable[i], 0, automatonTableView.getColumns().size());
            }

            for (int i = 0; i < automatonJumpTable.length; i++) {
                for (int j = 0; j < automatonJumpTable[i].length; j++) {
                    automatonJumpTable[i][j] = automatonJumpTable[i][j].strip();
                }
            }

            for (int i = 0; i < finalVertices.length; i++) {
                finalVertices[i] = finalVertices[i].strip();
            }

            HashBasedTable<String, String, String> jumpTable = HashBasedTable.create();

            for (int i = 0; i < automatonJumpTable.length; i++) {
                for (int j = 1; j < automatonJumpTable[i].length; j++) {
                    jumpTable.put(states[i], alphabet[j - 1], automatonJumpTable[i][j]);
                }
            }

            Automaton resAutomaton = new Automaton(false, jumpTable, startVertex, Arrays.asList(finalVertices));

            tableWindowMainPane.getChildren().remove(inputCorrectnessText);
            if (!resAutomaton.isAutomatonWithoutUnreachableVertices()) {
                setupInputCorrectnessText(inputCorrectnessText,
                        "Автомат должен содержать только достижимые вершины",
                        automatonTableView);
                tableWindowMainPane.getChildren().add(inputCorrectnessText);
                tableWindowMainPane.requestLayout();
                return;
            }

            try {
                automatonList.add(resAutomaton);
                automatonList.add(Closures.prefixClosure(resAutomaton));
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            button.getScene().getWindow().hide();

            Loader.loadFxml("/taskNine.fxml", true);
        });
        return button;
    }

    protected void initCreateTableButton() {
        createTableButton.setOnAction(event -> {
            try {
                String[] states = new String[Integer.parseInt(statesCountField.getText())];
                String[] alphabet = alphabetField.getText().split(",");

                for (int i = 0; i < states.length; i++) {
                    states[i] = Integer.toString(i + 1);
                }

                if (!checkAlphabetAndStatesCorrectness(states, alphabet))
                    return;

                returnToPreviousStateButton.getScene().getWindow().hide();

                String[][] jumpTable = new String[states.length][alphabet.length + 1];

                for (int i = 0; i < states.length; i++) {
                    for (int j = 0; j < alphabet.length + 1; j++) {
                        if (j == 0)
                            jumpTable[i][j] = Integer.toString(i + 1);
                        else {
                            jumpTable[i][j] = "";
                        }
                    }
                }

                ObservableList<String[]> data = FXCollections.observableArrayList(jumpTable);

                TableView<String[]> automatonTableView = getAutomatonTableView(data, alphabet, states);
                TextField startVertexTextField = getTextFieldWithPrompt("Введите начальное состояние", 325);
                TextField finalVerticesTextField = getTextFieldWithPrompt(
                        "Введите через запятую конечные состояния автомата", 325);
                Text automatonInfoText = getColoredText("Введите таблицу переходов автомата", Color.WHITESMOKE,
                        Font.font("System", 20));
                Button createAutomatonButton = getCreateAutomatonButton(
                        startVertexTextField,
                        finalVerticesTextField,
                        automatonTableView,
                        states,
                        alphabet);
                Button returnToStartButton = new Button("Вернуться в начало");
                setupButtonAsReturnToStart(returnToStartButton);
                tableWindowMainPane = setupMainPaneForTableCreationPage(automatonTableView, createAutomatonButton,
                        startVertexTextField, finalVerticesTextField, automatonInfoText, returnToStartButton);
                Loader.showStage(new Scene(tableWindowMainPane), true);
            } catch (NumberFormatException ignored) {
            }
        });
    }
}
