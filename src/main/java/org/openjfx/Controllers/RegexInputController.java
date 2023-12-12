package org.openjfx.Controllers;

import static org.openjfx.Controllers.Controller.automatonList;

import algorithms.GlushkovAlgo;
import automaton.Automaton;
import regexp.RegexpException;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

public class RegexInputController {
    @FXML
    private AnchorPane inputWindowMainPane;

    private Text regexStatusText;

    @FXML
    void initialize() {
        initCreateTableButton();
    }

    @FXML
    private static void setupButtonAsReturnToPreviousState(Button button) {
        button.setOnAction(event -> {
            button.getScene().getWindow().hide();
            automatonList.clear();
            Loader.loadFxml("/solveTasksMenu.fxml", false);
        });
    }

    protected void initCreateTableButton() {
            try {
                TextField regexTextField = AutomatonInputController.getTextFieldWithPrompt("Введите регулярное выражение, '*' - итерация, '+' - объединение, 'λ' - символ пустого слов, две буквы рядом - конкатенация", 715);
                regexTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("[a-z0-1+*λ()]*")) {
                        regexTextField.setText(newValue.replaceAll("[^a-z0-1+*λ()]", ""));
                    }
                });

                var lambdaButton = new Button("λ");
                lambdaButton.setOnAction(ev -> regexTextField.setText(regexTextField.getText() + "λ"));
                Button createAutomatonButton = getCreateAutomatonButton(regexTextField);
                Text regexHintText = AutomatonInputController.getColoredText("Например, a*b*c* или a+b+c", Color.WHITESMOKE, Font.font("System", FontPosture.ITALIC, 12));
                Button checkRegexCorrectnessButton = new Button();
                checkRegexCorrectnessButton = getCheckRegexCorrectnessButton(regexTextField);
                Button returnToPreviousStateButton = new Button("Назад");
                setupButtonAsReturnToPreviousState(returnToPreviousStateButton);
                inputWindowMainPane = getMainPaneForTableAndRegexInputPage(
                        createAutomatonButton,
                        regexTextField,
                        regexHintText,
                        checkRegexCorrectnessButton,
                        lambdaButton,
                        returnToPreviousStateButton);
                Loader.showStage(new Scene(inputWindowMainPane), true);
            } catch (NumberFormatException ignored) {
            }
    }

    private Button getCheckRegexCorrectnessButton(TextField regexTextField) {
        Button checkRegexCorrectnessButton = new Button("Проверить корректность регулярного выражения");
        checkRegexCorrectnessButton.setPrefWidth(300);
        checkRegexCorrectnessButton.setOnAction(event -> {
            String regex = regexTextField.getText();
            inputWindowMainPane.getChildren().remove(regexStatusText);
            regexStatusText = new Text();
            try {
                GlushkovAlgo.doGlushkovAlgo(regex);
                regexStatusText.setText("✓");
                regexStatusText.setFill(Color.GREEN);
                regexStatusText.setFont(Font.font("System", FontPosture.ITALIC, 24));
                AnchorPane.setTopAnchor(regexStatusText, 46.0);
            }
            catch (RegexpException e) {
                regexStatusText.setText("Некорректное регулярное выражение: " + e.getText());
                regexStatusText.setFill(Color.RED);
                regexStatusText.setFont(Font.font("System", FontPosture.ITALIC, 14));
                AnchorPane.setTopAnchor(regexStatusText, 48.0);
            }
            AnchorPane.setLeftAnchor(regexStatusText, Math.max(regexTextField.getPrefWidth(), regexTextField.getMaxWidth()) + 20.0);
            inputWindowMainPane.getChildren().add(regexStatusText);
            inputWindowMainPane.requestLayout();
        });
        return checkRegexCorrectnessButton;
    }

    private AnchorPane getMainPaneForTableAndRegexInputPage(
                                                            Button createAutomatonButton,
                                                            TextField regexTextField,
                                                            Text regexHintText,
                                                            Button checkRegexCorrectnessButton,
                                                            Button lambdaButton,
                                                            Button returnToPreviousStateButton) {
        AnchorPane mainPane = new AnchorPane(createAutomatonButton, regexTextField, regexHintText, checkRegexCorrectnessButton,
                lambdaButton, returnToPreviousStateButton);
        mainPane.setStyle("-fx-background-color: #2e3348;");

        AnchorPane.setBottomAnchor(createAutomatonButton, 10.0);
        AnchorPane.setRightAnchor(createAutomatonButton, 10.0);

        AnchorPane.setTopAnchor(regexTextField, 30.0);
        AnchorPane.setLeftAnchor(regexTextField, 10.0);

        AnchorPane.setTopAnchor(regexHintText, 65.0);
        AnchorPane.setLeftAnchor(regexHintText, 10.0);

        AnchorPane.setTopAnchor(checkRegexCorrectnessButton, 65.0);
        AnchorPane.setLeftAnchor(checkRegexCorrectnessButton, regexTextField.getPrefWidth() + 10 - checkRegexCorrectnessButton.getPrefWidth());

        AnchorPane.setTopAnchor(lambdaButton, 65.0);
        AnchorPane.setLeftAnchor(lambdaButton, regexTextField.getPrefWidth() + 10 - checkRegexCorrectnessButton.getPrefWidth() - 30);

        AnchorPane.setTopAnchor(returnToPreviousStateButton, 10.0);
        AnchorPane.setRightAnchor(returnToPreviousStateButton, 10.0);

        return mainPane;
    }

    private Button getCreateAutomatonButton(TextField regexTextField) {
        Button createAutomatonButton = new Button("Создать автоматы");
        createAutomatonButton.setOnAction(event2 -> {
            Automaton regexBasedAutomaton;
            try {
                regexBasedAutomaton = GlushkovAlgo.doGlushkovAlgo(regexTextField.getText());
            }
            catch (RegexpException e) {
                inputWindowMainPane.getChildren().remove(regexStatusText);
                regexStatusText = new Text("Некорректное регулярное выражение: " + e.getText());
                regexStatusText.setFont(Font.font("System", FontPosture.ITALIC, 14));
                regexStatusText.setFill(Color.RED);
                AnchorPane.setTopAnchor(regexStatusText, 45.0);
                AnchorPane.setLeftAnchor(regexStatusText, Math.max(regexTextField.getPrefWidth(), regexTextField.getMaxWidth()) + 20.0);
                inputWindowMainPane.getChildren().add(regexStatusText);
                return;
            }

            automatonList.add(regexBasedAutomaton);

            createAutomatonButton.getScene().getWindow().hide();
            Loader.loadFxml("/taskSeven.fxml", true);
        });
        return createAutomatonButton;
    }
}
