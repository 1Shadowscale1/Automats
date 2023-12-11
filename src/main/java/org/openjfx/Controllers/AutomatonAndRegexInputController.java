package org.openjfx.Controllers;

import algorithms.GlushkovAlgo;
import automaton.Automaton;
import com.google.common.collect.HashBasedTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import regexp.RegexpException;

import java.util.*;

import static org.openjfx.Controllers.Controller.automatonList;

public class AutomatonAndRegexInputController extends AutomatonInputController {

    private AnchorPane mainPane;

    private Text regexStatusText;

    @FXML
    protected void initialize() {
        super.initialize();
    }

    protected void initCreateTableButton() {
        createTableButton.setOnAction(event -> {
            try {
                String[] states = new String[Integer.parseInt(statesCountField.getText())];
                String[] alphabet = alphabetField.getText().split(",");

                for (int i = 0; i < states.length; i++) {
                    states[i] = Integer.toString(i + 1);
                }

                inputWindowMainPane.getChildren().remove(inputCorrectnessText);

                if (states.length == 0) {
                    inputCorrectnessText = new Text("В автомате не может быть 0 состояний");
                    inputCorrectnessText.setFill(Color.RED);
                    inputCorrectnessText.setFont(Font.font("System", FontPosture.ITALIC, 14));
                    AnchorPane.setBottomAnchor(inputCorrectnessText, 10.0);
                    AnchorPane.setLeftAnchor(inputCorrectnessText, 10.0);
                    inputWindowMainPane.getChildren().add(inputCorrectnessText);
                    return;
                }

                if (alphabetField.getText().equals("") || (alphabetField.getText().trim().length() == 0)  || containsDuplicates(alphabet)) {
                    if (containsDuplicates(alphabet))
                        inputCorrectnessText = new Text("Алфавит не может содержать повторяющиеся буквы");
                    else
                        inputCorrectnessText = new Text("Алфавит не может быть пустым");
                    inputCorrectnessText.setFill(Color.RED);
                    inputCorrectnessText.setFont(Font.font("System", FontPosture.ITALIC, 14));
                    AnchorPane.setBottomAnchor(inputCorrectnessText, 10.0);
                    AnchorPane.setLeftAnchor(inputCorrectnessText, 10.0);
                    inputWindowMainPane.getChildren().add(inputCorrectnessText);
                    return;
                }

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
                TextField finalVerticesTextField = getTextFieldWithPrompt("Введите через запятую конечные состояния автомата", 325);
                TextField regexTextField = getTextFieldWithPrompt("Введите регулярное выражение, '*' - итерация, '+' - объединение, 'λ' - символ пустого слов, две буквы рядом - конкатенация", 715);
                regexTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("[a-z0-1+*λ()]*")) {
                        regexTextField.setText(newValue.replaceAll("[^a-z0-1+*λ()]", ""));
                    }
                });

                var lambdaButton = new Button("λ");
                lambdaButton.setOnAction(ev -> regexTextField.setText(regexTextField.getText() + "λ"));
                Button createAutomatonButton = getCreateAutomatonButton(automatonTableView, startVertexTextField, finalVerticesTextField, states, alphabet, regexTextField);
                Text automatonHintText = getColoredText("Введите таблицу переходов автомата", Color.WHITESMOKE, Font.font("System", FontPosture.ITALIC, 18));
                Text regexHintText = getColoredText("Например, a*b*c* или a+b+c", Color.WHITESMOKE, Font.font("System", FontPosture.ITALIC, 12));
                Button checkRegexCorrectnessButton = getCheckRegexCorrectnessButton(regexTextField);
                Button returnToStartButton = new Button("Вернуться в начало");
                setupButtonAsReturnToStart(returnToStartButton);
                mainPane = getMainPaneForTableAndRegexInputPage(automatonTableView,
                        createAutomatonButton,
                        startVertexTextField,
                        finalVerticesTextField,
                        regexTextField,
                        regexHintText,
                        automatonHintText,
                        checkRegexCorrectnessButton,
                        lambdaButton,
                        returnToStartButton);
                Loader.showStage(new Scene(mainPane), true);
            } catch (NumberFormatException ignored) {
            }
        });
    }

    private Button getCheckRegexCorrectnessButton(TextField regexTextField) {
        Button checkRegexCorrectnessButton = new Button("Проверить корректность регулярного выражения");
        checkRegexCorrectnessButton.setPrefWidth(300);
        checkRegexCorrectnessButton.setOnAction(event -> {
            String regex = regexTextField.getText();
            mainPane.getChildren().remove(regexStatusText);
            regexStatusText = new Text();
            try {
                GlushkovAlgo.doGlushkovAlgo(regex);
                regexStatusText.setText("✓");
                regexStatusText.setFill(Color.GREEN);
                regexStatusText.setFont(Font.font("System", FontPosture.ITALIC, 24));
                AnchorPane.setBottomAnchor(regexStatusText, 46.0);
            }
            catch (RegexpException e) {
                regexStatusText.setText("Некорректное регулярное выражение: " + e.getText());
                regexStatusText.setFill(Color.RED);
                regexStatusText.setFont(Font.font("System", FontPosture.ITALIC, 14));
                AnchorPane.setBottomAnchor(regexStatusText, 48.0);
            }
            AnchorPane.setLeftAnchor(regexStatusText, Math.max(regexTextField.getPrefWidth(), regexTextField.getMaxWidth()) + 20.0);
            mainPane.getChildren().add(regexStatusText);
            mainPane.requestLayout();
        });
        return checkRegexCorrectnessButton;
    }

    private AnchorPane getMainPaneForTableAndRegexInputPage(TableView<String[]> automatonTableView,
                                                            Button createAutomatonButton,
                                                            TextField startVertexTextField,
                                                            TextField finalVerticesTextField,
                                                            TextField regexTextField,
                                                            Text regexHintText,
                                                            Text automatonHintText,
                                                            Button checkRegexCorrectnessButton,
                                                            Button lambdaButton,
                                                            Button returnToStartButton) {
        AnchorPane mainPane = new AnchorPane(automatonTableView, createAutomatonButton, startVertexTextField,
                finalVerticesTextField, automatonHintText, regexTextField, regexHintText, checkRegexCorrectnessButton,
                lambdaButton, returnToStartButton);
        mainPane.setStyle("-fx-background-color: #2e3348;");

        AnchorPane.setLeftAnchor(automatonTableView, 10.0);
        AnchorPane.setTopAnchor(automatonTableView, 50.0);

        AnchorPane.setTopAnchor(startVertexTextField, 50.0);
        AnchorPane.setLeftAnchor(startVertexTextField, Math.min(automatonTableView.getPrefWidth(), automatonTableView.getMaxWidth()) + 20.0);

        AnchorPane.setTopAnchor(finalVerticesTextField, 80.0);
        AnchorPane.setLeftAnchor(finalVerticesTextField, Math.min(automatonTableView.getPrefWidth(), automatonTableView.getMaxWidth()) + 20.0);

        AnchorPane.setBottomAnchor(createAutomatonButton, 10.0);
        AnchorPane.setRightAnchor(createAutomatonButton, 10.0);

        AnchorPane.setTopAnchor(automatonHintText, 10.0);
        AnchorPane.setLeftAnchor(automatonHintText, 10.0);

        AnchorPane.setBottomAnchor(regexTextField, 45.0);
        AnchorPane.setLeftAnchor(regexTextField, 10.0);

        AnchorPane.setBottomAnchor(regexHintText, 25.0);
        AnchorPane.setLeftAnchor(regexHintText, 10.0);

        AnchorPane.setBottomAnchor(checkRegexCorrectnessButton, 10.0);
        AnchorPane.setLeftAnchor(checkRegexCorrectnessButton, regexTextField.getPrefWidth() + 10 - checkRegexCorrectnessButton.getPrefWidth());

        AnchorPane.setBottomAnchor(lambdaButton, 10.0);
        AnchorPane.setLeftAnchor(lambdaButton, regexTextField.getPrefWidth() + 10 - checkRegexCorrectnessButton.getPrefWidth() - 30);

        AnchorPane.setTopAnchor(returnToStartButton, 10.0);
        AnchorPane.setRightAnchor(returnToStartButton, 10.0);

        return mainPane;
    }

    private Button getCreateAutomatonButton(TableView<String[]> automatonTableView, TextField startVertexTextField, TextField finalVerticesTextField, String[] states, String[] alphabet, TextField regexTextField) {
        Button createAutomatonButton = new Button("Создать автоматы");
        createAutomatonButton.setOnAction(event2 -> {
            String startVertex = startVertexTextField.getText().strip();
            List<String> finalVertices = Arrays.asList(finalVerticesTextField.getText().split(","));
            String[][] firstAutomatonTable = new String[automatonTableView.getItems().size()][automatonTableView.getColumns().size()];

            ObservableList<String[]> items = automatonTableView.getItems();

            for (int i = 0; i < states.length; i++) {
                states[i] = items.get(i)[0];
            }

            mainPane.getChildren().remove(inputCorrectnessText);
            if (!checkInputCorrectness(startVertex, finalVerticesTextField.getText().split(","), states, automatonTableView)) {
                mainPane.getChildren().add(inputCorrectnessText);
                mainPane.requestLayout();
                return;
            }
            mainPane.getChildren().remove(inputCorrectnessText);

            Automaton regexBasedAutomaton;
            try {
                regexBasedAutomaton = GlushkovAlgo.doGlushkovAlgo(regexTextField.getText());
            }
            catch (RegexpException e) {
                mainPane.getChildren().remove(regexStatusText);
                regexStatusText = new Text("Некорректное регулярное выражение: " + e.getText());
                regexStatusText.setFont(Font.font("System", FontPosture.ITALIC, 14));
                regexStatusText.setFill(Color.RED);
                AnchorPane.setBottomAnchor(regexStatusText, 45.0);
                AnchorPane.setLeftAnchor(regexStatusText, Math.max(regexTextField.getPrefWidth(), regexTextField.getMaxWidth()) + 20.0);
                mainPane.getChildren().add(regexStatusText);
                return;
            }

            for (int i = 0; i < items.size(); i++) {
                System.arraycopy(items.get(i), 0, firstAutomatonTable[i], 0, automatonTableView.getColumns().size());
            }

            for (int i = 0; i < firstAutomatonTable.length; i++) {
                for (int j = 0; j < firstAutomatonTable[i].length; j++) {
                    firstAutomatonTable[i][j] = firstAutomatonTable[i][j].strip();
                }
            }

            for (int i = 0; i < finalVertices.size(); i++) {
                finalVertices.set(i, finalVertices.get(i).strip());
            }

            HashBasedTable<String, String, String> jumpTable = HashBasedTable.create();

            for (int i = 0; i < firstAutomatonTable.length; i++) {
                for (int j = 1; j < firstAutomatonTable[i].length; j++) {
                    if (Objects.equals(firstAutomatonTable[i][j], ""))
                        continue;
                    jumpTable.put(states[i], alphabet[j - 1], firstAutomatonTable[i][j]);
                }
            }

            automatonList.add(new Automaton(false, jumpTable, startVertex, finalVertices));
            automatonList.add(regexBasedAutomaton);


            createAutomatonButton.getScene().getWindow().hide();
            Loader.loadFxml("/taskTwo.fxml", true);
        });
        return createAutomatonButton;
    }
}

